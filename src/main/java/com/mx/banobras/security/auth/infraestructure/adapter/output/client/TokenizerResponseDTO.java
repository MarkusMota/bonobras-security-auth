package com.mx.banobras.security.auth.infraestructure.adapter.output.client;

import lombok.Data;

@Data
public class TokenizerResponseDTO {
	private Integer statusCode;
	private TokenDTO tokenDTO;
	private ErrorMessageDTO errorMessageDTO;
}


