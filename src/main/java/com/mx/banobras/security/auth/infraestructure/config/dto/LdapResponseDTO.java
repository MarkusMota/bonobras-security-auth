package com.mx.banobras.security.auth.infraestructure.config.dto;

import lombok.Data;

@Data
public class LdapResponseDTO {
	private Integer statusCode;
	private LdapDTO ldapDTO;
	
	private ErrorMessageDTO errorMessageDTO;
}


