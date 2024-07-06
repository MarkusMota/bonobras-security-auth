package com.mx.banobras.security.auth.application.inputport;

/**
 * ISecurityAuthInputPort.java:
 * 
 * Interfaz que contiene los metodos del caso de uso de la Autenticacion.
 * 
 * @author Marcos Gonzalez
 * @version 1.0, 13/06/2024
 * @see Documento "MAR - Marco Arquitectonico de Referencia"
 * @since JDK 17
 */


import com.mx.banobras.security.auth.infraestructure.config.dto.SecurityAuthDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.CipherResponseDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.LdapResponseDTO;



public interface ISecurityAuthInputPort {

	/**
	 * Metodo para obtener validar el token y autenticación con LDAP.
	 * 
	 * @param securityAuthDTO - DTO que contien los datos para validar en LDAP y Tokenizer.
	 * @return regresa el objeto con los datos de LDAP.
	 * @throws Exception Excepción durante el proceso de generar el Token.
	 */
	public LdapResponseDTO authenticationTokenLdap(SecurityAuthDTO securityAuthDTO);
	
	/**
	 * Metodo para obtener la autenticación con LDAP.
	 * 
	 * @param securityAuthDTO - DTO que contien los datos para validar en LDAP.
	 * @return regresa el objeto con los datos de LDAP.
	 * @throws Exception Excepción durante el proceso.
	 */
	public LdapResponseDTO authenticationLdap(SecurityAuthDTO securityAuthDTO);
	
	/**
	 * Metodo para obtener encirptar una cedena.
	 * 
	 * @param securityAuthDTO - DTO que contien los datos para encirptar.
	 * @return regresa el objeto encirptado.
	 * @throws Exception Excepción durante el proceso.
	 */
	public CipherResponseDTO encode(SecurityAuthDTO securityAuthDTO);
	
	/**
	 * Metodo para obtener desencirptar una cedena.
	 * 
	 * @param securityAuthDTO - DTO que contien los datos para desencirptar.
	 * @return regresa el objeto desencirptado.
	 * @throws Exception Excepción durante el proceso.
	 */
	public CipherResponseDTO decode(SecurityAuthDTO securityAuthDTO);
	
	
}
