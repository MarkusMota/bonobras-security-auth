package com.mx.banobras.security.auth.infraestructure.adapter.output.client;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
	public boolean valid;
	public String token;
	public String refreshToken;
	
	
}
