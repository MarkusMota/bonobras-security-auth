package com.mx.banobras.security.auth.application.outputport;

import javax.naming.NamingException;

import com.mx.banobras.security.auth.infraestructure.adapter.output.client.LdapVO;
import com.mx.banobras.security.auth.infraestructure.config.dto.LdapDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.SecurityAuthDTO;

/**
 * ILdapOutPort.java:
 * 
 * Interface de puerto de salida, para buscar el usuario 
 * 
 * @author Marcos Gonzalez
 * @version 1.0, 13/06/2024
 * @see documento "MAR - Marco Arquitectonico de Referencia"
 * @since JDK 17
 */
public interface ILdapOutPort {

	/**
	 * Metodo para buscar el usuario.
	 * 
	 * @param securityAuthDTO componente que conciten el usuario a buscar.
	 * @return LdapVO objeto que contiene los datos de usario en LDAP. 
	 * 
	 * @throws NamingException Excepción durante el proces.
	 */
	public LdapVO autentication(SecurityAuthDTO securityAuthDTO) throws NamingException;
	
}
