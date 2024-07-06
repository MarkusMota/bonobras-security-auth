package com.mx.banobras.security.auth.infraestructure.config.dto;


/**
 * CipherResponseDTO.java:
 * 
 * Objeto que contiene los datos de respuesta de encritpado y descriptado. 
 *  
 * @author Marcos Gonzalez
 * @version 1.0, 13/06/2024
 * @see Documento "MAR - Marco Arquitectonico de Referencia"
 * @since JDK 17
 */
import lombok.Data;

@Data
public class CipherResponseDTO {
	private Integer statusCode;
	private DataDTO dataDTO;
	private ErrorMessageDTO errorMessageDTO;
}


