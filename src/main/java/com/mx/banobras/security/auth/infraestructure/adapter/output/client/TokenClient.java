package com.mx.banobras.security.auth.infraestructure.adapter.output.client;
/**
 * TokenClient.java:
 * 
 * Clase para conectarse al tokenizer y validar el token. 
 *  
 * @author Marcos Gonzalez
 * @version 1.0, 13/06/2024
 * @see documento "MAR - Marco Arquitectonico de Referencia"
 * @since JDK 17
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.mx.banobras.security.auth.application.outputport.ITokenClientOutPort;
import com.mx.banobras.security.auth.infraestructure.config.dto.SecurityAuthDTO;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.naming.NamingException;

@Component
public class TokenClient implements ITokenClientOutPort {

	/** Trazas de la aplicación */
	Logger log = LogManager.getLogger(TokenClient.class);

	/** Variable que contiene el nombre de usuario de conexion en ldap */
	@Value("${app.url.token.valid}")
	String urlTokenAuth;

	/**
	 * Metodo para validar el token.
	 * 
	 * @param securityAuthInDTO componente que contiene los datos del token.
	 * 
	 * @return HttpResponse<String> regresa un objeto con los datos del token validado
	 * @throws IOException, InterruptedException
	 * 
	 */
	@Override
	public HttpResponse<String> getTokenAuthorization(SecurityAuthDTO securityAuthInDTO)
			throws IOException, InterruptedException {

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = null;
		log.info("Inicia cliente validar token");
		
		log.info("Inicia cliente validar token");
		
		HttpRequest request = HttpRequest.newBuilder()
				.setHeader("credentials", securityAuthInDTO.getCredentials())
				.setHeader("auth-token", securityAuthInDTO.getJwtToken())
				.setHeader("consumer-api-id", securityAuthInDTO.getConsumerId())
				.setHeader("functional-id", securityAuthInDTO.getFunctionalId())
				.setHeader("transaction-id", securityAuthInDTO.getTransactionId())
				.uri(URI.create(urlTokenAuth))
				.POST(HttpRequest.BodyPublishers.noBody()).build();

		response = client.send(request, HttpResponse.BodyHandlers.ofString());
		log.info(new StringBuilder().append("StatusCode: ").append(response.statusCode()));
		log.info("Finaliza cliente validar token");
		return response;
	}
}
