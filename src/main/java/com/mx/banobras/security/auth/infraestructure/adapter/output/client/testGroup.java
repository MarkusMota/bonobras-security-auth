package com.mx.banobras.security.auth.infraestructure.adapter.output.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class testGroup {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String cadena = "CN=SARAS 1,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Usuarios Teams,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=DGAFID_GNCCC_BIA,OU=DGA Fiduciaria,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Almacenamiento FULL,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Control de Gestion FULL,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=153100-GERENCIA DE NEGOCIOS CARRETEROS\\, CONCERTACIÓN Y COMITÉS,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Direccion Fiduciaria SP,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Act Sist Fid Tlamatini,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=CiscoACS,OU=Websense,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SNegFidInfOperConsulta,OU=D Negocios Infra,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=fideosEspecial,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_SGNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_GNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Ecbcomerusuarios,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Acceso a Negocios Carreteros Concertacion y Comites,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=G Formal Prod Fid,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Gcia Neg Carr Conc Com,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=PYCP,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Fiduciario,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Empleados y mandos medios,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=004 Gerentes,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx";
		while (cadena.length() > 0) {

			int ii = cadena.indexOf("CN=");
			String cadena2 = cadena.substring(ii + 3);
			int fi = cadena2.indexOf(",");
			System.out.println(cadena2.substring(0, fi));

			cadena = cadena2;
			if (!cadena.contains("CN=")) {
				break;
			}
		}

		
		System.out.println("***********************************");
		//String menberOf = "CN=SARAS 1,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Usuarios Teams,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=DGAFID_GNCCC_BIA,OU=DGA Fiduciaria,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Almacenamiento FULL,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Control de Gestion FULL,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=153100-GERENCIA DE NEGOCIOS CARRETEROS\\, CONCERTACIÓN Y COMITÉS,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Direccion Fiduciaria SP,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Act Sist Fid Tlamatini,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=CiscoACS,OU=Websense,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SNegFidInfOperConsulta,OU=D Negocios Infra,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=fideosEspecial,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_SGNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_GNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Ecbcomerusuarios,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Acceso a Negocios Carreteros Concertacion y Comites,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=G Formal Prod Fid,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Gcia Neg Carr Conc Com,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=PYCP,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Fiduciario,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Empleados y mandos medios,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=004 Gerentes,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx";
		//String menberOf = "CN=SARAS 1,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Usuarios Teams,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=DGAFID_GNCCC_BIA,OU=DGA Fiduciaria,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Almacenamiento FULL,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Control de Gestion FULL,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=153100-GERENCIA DE NEGOCIOS CARRETEROS\\, CONCERTACIÓN Y COMITÉS,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Direccion Fiduciaria SP,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Act Sist Fid Tlamatini,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=CiscoACS,OU=Websense,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SNegFidInfOperConsulta,OU=D Negocios Infra,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=fideosEspecial,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_SGNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=SAF_GNCCC,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Ecbcomerusuarios,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Acceso a Negocios Carreteros Concertacion y Comites,OU=S Fiduciaria Apoyo,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=G Formal Prod Fid,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Gcia Neg Carr Conc Com,OU=S Fiduciaria Admon,OU=D Jur Fid,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=PYCP,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Fiduciario,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Empleados y mandos medios,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=004 Gerentes,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx":
		String menberOf = "CN=SARAS 2,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Usuarios Teams,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Empleados con Servicio Medico,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=162120-SUBGERENCIA DE INFORMACION CONTABLE,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=CiscoACS,OU=Websense,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=sigro,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=sicofin,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=IKOSCASH,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Derivados,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Derivados DERIVADOS,OU=Aplicaciones,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=S Contabilidad,OU=S Contabilidad,OU=D Finanzas,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Empleados y mandos medios,OU=Listas de Distribucion de Correo,OU=Grupos,DC=banobras,DC=gob,DC=mx, CN=Subg Reg Conta,OU=S Contabilidad,OU=D Finanzas,OU=Grupos,DC=banobras,DC=gob,DC=mx";

		String[] memberOfList = menberOf.split("DC=mx,");
		List<String> listGroup = new ArrayList<String>();
		for (String aux : memberOfList) {

			if (aux.contains("OU=Aplicaciones")) {
					int ii = aux.indexOf("CN=");
					String cadena2 = aux.substring(ii + 3);
					int fi = cadena2.indexOf(",");
					listGroup.add(cadena2.substring(0, fi));
					
				}
		}
		
		Iterator<String> iter = listGroup.iterator();
		
		while(iter.hasNext()) {
			String ss = iter.next();
			System.out.println(ss);
		}
		

	}

}
