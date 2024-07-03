package com.mx.banobras.security.auth.infraestructure.adapter.output.client;
/**
 * LdapClient.java:
 * 
 * Clase para conectarse en al directorio activo y validar el usuario . 
 *  
 * @author Marcos Gonzalez
 * @version 1.0, 13/06/2024
 * @see documento "MAR - Marco Arquitectonico de Referencia"
 * @since JDK 17
 */

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.mx.banobras.security.auth.application.outputport.ILdapOutPort;
import com.mx.banobras.security.auth.infraestructure.config.dto.SecurityAuthDTO;

import lombok.Data;


@Component
@Data
public class LdapClient implements ILdapOutPort {
	
	/** Variable para las trazas de la clase */
	Logger log = LogManager.getLogger(LdapClient.class);

	/** Variable que contiene la url del ldap */
	@Value("${app.ldap.server}")
	String ldapServer;

	/** Variable que contiene el filtro para la busqueda en ldap */
	@Value("${app.ldap.search.base}")
	String ldapSearchBase;

	/** Variable que contiene el nombre de usuario de conexion en ldap */
	@Value("${app.ldap.username}")
	String ldapUsername;

	/** Variable que contiene el password de conexión de ldap */
	@Value("${app.ldap.password}")
	String ldapPassword;

	/** Variable que contiene el valor para buscar en ldap */
	@Value("${app.ldap.validate}")
	boolean ldapValidate;
	
	/** Variable que contiene el valor del usuario a buscar en ldap */
	@Value("${app.ldap.username.search}")
	String ldapUserNameSearch;

	/**
	 * Metodo para buscar el usuario en LDAP.
	 * 
	 * @param userName - Alias del usuario.
	 * 
	 * @return regresa un valor booleano, si el valor es verdadero si encotro al usario.
	 * @throws NamingException 
	 * 
	 */
	@Override
	public LdapVO autentication(SecurityAuthDTO securityAuthDTO) throws  NamingException{
		/** Objeto para guardar los datos que provienen de LDAP */
		LdapVO dataLdapVO = null;
		String userName = null;
		List<String> listaGrupo = new ArrayList<String>();
		listaGrupo.add("SICOVI/Administrador");
		listaGrupo.add("SICOVI/Operedor");

		/** Condicion para validar en LDAP */
		if (ldapValidate) {
			log.info("Se valida usuario en LDAP");
			if (ldapUserNameSearch != null && !ldapUserNameSearch.isEmpty() ) {
				log.info("La validacion es por usuario de prueba");
				userName = ldapUserNameSearch;
			}else {
				log.info("La validacion es por usuario en credentials.");
				userName = securityAuthDTO.getUserName();
				
			}

			Hashtable env = new Hashtable();
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, ldapUsername);
			env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, ldapServer);

			InitialDirContext ctx = new InitialDirContext(env);

			/** Busca un usuario en especifico */
			String searchFilter = "(samaccountName=" + userName + ")";
			
			
			/** crea los filtros a buscar en LDAP */
			String[] reqAtt = { "uid","cn", "sn", "initials","displayname", 
					            "mail", "department", "company", "sAMAccountName",
					            "userPrincipalName", "title", "mailNickname",
					            "telephoneNumber", "initials", "userAccountControl", "memberOf"};
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			controls.setReturningAttributes(reqAtt);
			
			NamingEnumeration<SearchResult> objs = ctx.search(ldapSearchBase, searchFilter, controls);
			if (objs.hasMoreElements()) {
				
				while (objs.hasMoreElements()) {
					SearchResult match = objs.nextElement();
					Attributes attrs = match.getAttributes();
					
					listaGrupo = findGroups(attrs.get("memberOf").toString());
					
					dataLdapVO = new LdapVO(cleanText(attrs,"sAMAccountName"), null, cleanText(attrs,"initials"), cleanText(attrs,"userAccountControl"),
											cleanText(attrs,"cn"), cleanText(attrs,"title"), cleanText(attrs,"department"), 
											cleanText(attrs,"telephoneNumber"), findDisabled(match, "Disabled Accounts"), cleanText(attrs,"userPrincipalName"), 
											cleanText(attrs,"memberOf") , listaGrupo );
					
				}
				
				log.info(new StringBuilder().append("Si existe el usuario en LDAP: ").append(userName));
				
			} else {
				log.info(new StringBuilder().append("No existe el usuario en LDAP."));
				dataLdapVO = null;
			}

		} else {
			log.info("No!! se valida por LDAP - se usa datos dummy.");
			listaGrupo = findGroups("dummy");
			dataLdapVO = new LdapVO(securityAuthDTO.getUserName(), securityAuthDTO.getPassword(), "123456","1222", "PEREZ PEREZ, MARIO", "EXPERTO TEXNICO", "SUBGERENCIA DE CARRETERAS ESTATALES", "1432",1, " mperez@banobras.gob.mx",  "Mario.perez@banobras.gob.mx", listaGrupo );
		}
		return dataLdapVO;
	}
	
	
	private String cleanText(Attributes attrs, String etiqueta) {
		String cadenaResult = attrs.get(etiqueta).toString();
		cadenaResult = cadenaResult.replace(etiqueta, "");
		cadenaResult = cadenaResult.replace(":", "");
		return cadenaResult.trim();
	}
	
	private Integer findDisabled(SearchResult match, String etiqueta) {
		String cadena = match.toString();
		if(cadena.contains(etiqueta)) {
			return 0;
		}else {
			return 1;
		}
	}
	
	private List<String> findGroups(String attr){
		//String menberOf = "CN=SARAS 1,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Usuarios Teams,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=DGAFID_GNCCC_BIA,OU=DGA Fiduciaria,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Almacenamiento FULL,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Control de Gestion FULL,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=153100-GERENCIA DE NEGOCIOS CARRETEROS\\, CONCERTACIÓN Y COMITÉS,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Direccion Fiduciaria SP,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Act Sist Fid Tlamatini,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=CiscoACS,OU=Websense,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SNegFidInfOperConsulta,OU=D Negocios Infra,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=fideosEspecial,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_SGNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_GNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Ecbcomerusuarios,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Acceso a Negocios Carreteros Concertacion y Comites,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=G Formal Prod Fid,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Gcia Neg Carr Conc Com,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=PYCP,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Fiduciario,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Empleados y mandos medios,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=004 Gerentes,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx";
		//String menberOf = "CN=SARAS 1,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Usuarios Teams,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=DGAFID_GNCCC_BIA,OU=DGA Fiduciaria,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Almacenamiento FULL,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Control de Gestion FULL,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=153100-GERENCIA DE NEGOCIOS CARRETEROS\\, CONCERTACIÓN Y COMITÉS,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Direccion Fiduciaria SP,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Act Sist Fid Tlamatini,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=CiscoACS,OU=Websense,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SNegFidInfOperConsulta,OU=D Negocios Infra,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=fideosEspecial,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_SGNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_GNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Ecbcomerusuarios,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Acceso a Negocios Carreteros Concertacion y Comites,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=G Formal Prod Fid,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Gcia Neg Carr Conc Com,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=PYCP,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Fiduciario,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Empleados y mandos medios,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=004 Gerentes,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx":
		String menberOf = "CN=SARAS 2,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Usuarios Teams,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Empleados con Servicio Medico,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=162120-SUBGERENCIA DE INFORMACION CONTABLE,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=CiscoACS,OU=Websense,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=sigro,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=sicofin,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=IKOSCASH,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Derivados,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Derivados DERIVADOS,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=S Contabilidad,OU=S Contabilidad,OU=D Finanzas,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Empleados y mandos medios,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Subg Reg Conta,OU=S Contabilidad,OU=D Finanzas,OU=Grupos,DC=banobras,DC=gob,DC=mx";
		
		String[] memberOfList = menberOf.split("DC=mx,");
		List<String> listGroup = new ArrayList<>();
		for (String aux : memberOfList) {

			if (aux.contains("OU=Aplicaciones")) {
					int ii = aux.indexOf("CN=");
					String cadena2 = aux.substring(ii + 3);
					int fi = cadena2.indexOf(",");
					listGroup.add(cadena2.substring(0, fi));
					
				}
		}
		
		return listGroup;
	}
	
	
	
	
}
