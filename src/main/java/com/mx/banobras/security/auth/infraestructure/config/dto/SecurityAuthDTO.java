package com.mx.banobras.security.auth.infraestructure.config.dto;


/**
 * SecurityAuthDTO.java:
 * 
 * Objeto que contiene los datos para la autenticacion en LDAP. 
 *  
 * @author Marcos Gonzalez
 * @version 1.0, 13/06/2024
 * @see Documento "MAR - Marco Arquitectonico de Referencia"
 * @since JDK 17
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityAuthDTO {
	private String credentials;
	private String userName;
	private String password;
	private String jwtToken;
	private String application;
	private String consumerId; 
	private String functionalId;
	private String transactionId;
	
}
