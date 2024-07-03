package com.mx.banobras.security.auth.infraestructure.config.dto;



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
	private String consumerId; 
	private String functionalId;
	private String transactionId;
	
}
