package com.mx.banobras.security.auth.application.inputport;

import com.mx.banobras.security.auth.infraestructure.config.dto.SecurityAuthDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.CipherResponseDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.DataDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.LdapResponseDTO;



public interface ISecurityAuthInputPort {

	/**
	 * Metodo para obtener el Token, para el consumo de los microservicios.
	 * 
	 * @param securityAuthDTO - DTO que contien los datos para generar el token.
	 * @return regresa el objeto con el token.
	 * @throws Exception Excepción durante el proceso de generar el Token.
	 */
	public LdapResponseDTO autentication(SecurityAuthDTO securityAuthDTO);
	
	/**
	 * Metodo para obtener cifrar una cedena.
	 * 
	 * @param securityAuthDTO - DTO que contien los datos para cifrar.
	 * @return regresa el objeto cifrado.
	 * @throws Exception Excepción durante el proceso de generar el Token.
	 */
	public CipherResponseDTO encode(SecurityAuthDTO securityAuthDTO);
	
	/**
	 * Metodo para obtener descifrar una cedena.
	 * 
	 * @param securityAuthDTO - DTO que contien los datos para cifrar.
	 * @return regresa el objeto descifrado.
	 * @throws Exception Excepción durante el proceso de generar el Token.
	 */
	public CipherResponseDTO dencode(SecurityAuthDTO securityAuthDTO);
	
	
}
