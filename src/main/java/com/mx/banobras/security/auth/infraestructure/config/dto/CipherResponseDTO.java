package com.mx.banobras.security.auth.infraestructure.config.dto;

import lombok.Data;

@Data
public class CipherResponseDTO {
	private Integer statusCode;
	private DataDTO dataDTO;
	private ErrorMessageDTO errorMessageDTO;
}


