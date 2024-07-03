package com.mx.banobras.security.auth.config;

/**
 * BeanConfiguration.java:
 * 
 * Clase para la configuracion de los servicios 
 *  
 * @author Marcos Gonzalez
 * @version 1.0, 13/06/2024
 * @see documento "MAR - Marco Arquitectonico de Referencia"
 * @since JDK 17
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mx.banobras.security.auth.application.outputport.ILdapOutPort;
import com.mx.banobras.security.auth.application.outputport.ITokenClientOutPort;
import com.mx.banobras.security.auth.application.service.SecurityAuthUseCaseService;
import com.mx.banobras.security.auth.common.util.CipherAESCommon;


@Configuration
public class BeanConfiguration {

	/**
	 * Metodo para obtener crear la inyección del servicio del Token.
	 * 
	 * @param tokenizer - Objeto Toenizer para crear el token.
	 * @param iLdapOutPort - Interfaz para validar con LDAP el usuario.
	 * 
	 * @throws none.
	 */
	@Bean
	SecurityAuthUseCaseService securityAuthUseCaseService(
			ILdapOutPort iLdapOutPort, 
			ITokenClientOutPort iTokenClientOutPort,
			CipherAESCommon cipherAESCommon) {
		return new SecurityAuthUseCaseService(iLdapOutPort, iTokenClientOutPort, cipherAESCommon);
	}
	

}
