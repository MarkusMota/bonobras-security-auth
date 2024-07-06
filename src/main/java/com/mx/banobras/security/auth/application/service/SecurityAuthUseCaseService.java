package com.mx.banobras.security.auth.application.service;

import java.io.IOException;
import java.net.http.HttpResponse;
/**
 * TokenizerUseCaseService.java:
 * 
 * Clase de tipo @Service que contiene las funciones del caso de uso del Api TOkenizer
 *  
 * @author Marcos Gonzalez
 * @version 1.0, 13/06/2024
 * @see documento "MAR - Marco Arquitectonico de Referencia"
 * @since JDK 17
 */
import java.util.Date;

import javax.naming.CommunicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mx.banobras.security.auth.application.inputport.ISecurityAuthInputPort;
import com.mx.banobras.security.auth.application.outputport.ILdapOutPort;
import com.mx.banobras.security.auth.application.outputport.ITokenClientOutPort;
import com.mx.banobras.security.auth.common.util.CipherAESCommon;
import com.mx.banobras.security.auth.common.util.CommonConstant;
import com.mx.banobras.security.auth.infraestructure.adapter.output.client.LdapVO;
import com.mx.banobras.security.auth.infraestructure.adapter.output.client.LdapVOMappeDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.CipherResponseDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.DataDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.ErrorMessageDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.LdapDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.SecurityAuthDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.TokenizerResponseDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.LdapResponseDTO;

@Service
public class SecurityAuthUseCaseService implements ISecurityAuthInputPort {

	/** Trazas de la aplicación */
	Logger log = LogManager.getLogger(SecurityAuthUseCaseService.class);

	/** Variable para inejctar la clase Tokenizer */
	private final ITokenClientOutPort iTokenClientOutPort;
	/** Variable para inejctar la clase ILdapOutPort, para conexión a LDAP */
	private final ILdapOutPort iLdapOutPort;
	/** Injection variable para la clase CipherAESCommon */
	private final CipherAESCommon cipherAESCommon;

	/**
	 * Constructor para inyectar los objetos Tokenizer, ILdapOutPort y CipherAESCommon
	 * 
	 * @param tokenizer    Objeto de dominio el Api Tokenizer.
	 * @param iLdapOutPort Interface de puerto de salida para conectarse al LDAP.
	 * @param CipherAESCommon componente para desencriptar datos.
	 * 
	 */
	public SecurityAuthUseCaseService(ILdapOutPort iLdapOutPort, ITokenClientOutPort iTokenClientOutPort,
			CipherAESCommon cipherAESCommon) {
		this.iLdapOutPort = iLdapOutPort;
		this.iTokenClientOutPort = iTokenClientOutPort;
		this.cipherAESCommon = cipherAESCommon;
	}

	/**
	 * Metodo para validar el Token y autenticar el usuario.
	 * 
	 * @param securityAuthDTO Objeto que contien los datos para la validación y autenticacion.
	 * @return LdapResponseDTO objeto que contiene los datos del usuario en LDAP.
	 * 
	 */
	@Override
	public LdapResponseDTO authenticationTokenLdap(SecurityAuthDTO securityAuthDTO) {

		/** Variable que contiene el objeto de respuesta del token */
		LdapResponseDTO ldapResponseDTO = null;
		LdapVO ldapVO = null;

		HttpResponse<String> responseToken = null;

		try {
			if (securityAuthDTO.getCredentials().isEmpty()) {
				throw new IllegalArgumentException(CommonConstant.MSG_CREDENTIALS_EMPTY.getName());
			}
			/** Descripta las credenciales */
			securityAuthDTO = cipherAESCommon.getDataCredentials(securityAuthDTO);
			
			log.info("Valida el token");
			responseToken = iTokenClientOutPort.getTokenAuthorization(securityAuthDTO);
			if (responseToken.statusCode() == 200) {
				log.info(new StringBuilder().append("Valida si existe el usuario en LDAP: ")
						.append(securityAuthDTO.getUserName()));
				ldapVO = iLdapOutPort.autentication(securityAuthDTO);
				if (ldapVO != null) {
					LdapVOMappeDTO mapperLdapDTO = new LdapVOMappeDTO();
					LdapDTO ldapDTO = mapperLdapDTO.mapperVOtoDTO(ldapVO);
					ldapResponseDTO = new LdapResponseDTO();
					ldapResponseDTO.setStatusCode(HttpStatus.OK.value());
					ldapResponseDTO.setLdapDTO(ldapDTO);
				}
			} else {
				if (responseToken.statusCode() == 404) {
					log.info("URL token no encontrada.");
					ldapResponseDTO = new LdapResponseDTO();
					ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO();
					errorMessageDTO.setMessage("URL no exsite");
					errorMessageDTO.setStatusCode(responseToken.statusCode());
					errorMessageDTO.setTimestamp(new Date());
					ldapResponseDTO.setErrorMessageDTO(errorMessageDTO);
					ldapResponseDTO.setStatusCode(responseToken.statusCode());

				} else {
					log.info("Token incorrecto");
					Gson gson = new Gson();
					TokenizerResponseDTO tokenizerResponseDTO = gson.fromJson(responseToken.body(),
							TokenizerResponseDTO.class);
					log.info(tokenizerResponseDTO.getStatusCode());
					log.info(tokenizerResponseDTO.getErrorMessageDTO().getMessage());
					ldapResponseDTO = new LdapResponseDTO();
					ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO();
					errorMessageDTO.setMessage(tokenizerResponseDTO.getErrorMessageDTO().getMessage());
					errorMessageDTO.setStatusCode(tokenizerResponseDTO.getErrorMessageDTO().getStatusCode());
					errorMessageDTO.setTimestamp(tokenizerResponseDTO.getErrorMessageDTO().getTimestamp());
					ldapResponseDTO.setErrorMessageDTO(errorMessageDTO);
					ldapResponseDTO.setStatusCode(tokenizerResponseDTO.getStatusCode());
				}
			}

		} catch (IOException | InterruptedException ioex) {
			log.error("IOException | InterruptedException");
			log.error(CommonConstant.MSG_NO_SERVICE_TOKENIZER.getName(), ioex);
			ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), new Date(),
					CommonConstant.MSG_NO_SERVICE_TOKENIZER.getName());
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
			ldapResponseDTO.setErrorMessageDTO(message);
			Thread.currentThread().interrupt();
		} catch (CommunicationException ex0) {
			log.error(CommonConstant.COMMUNICATION_EXCEPTION_LDAP.getName());
			log.error(CommonConstant.MSG_NO_SERVICE_LDAP.getName(), ex0);
			ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), new Date(),
					CommonConstant.MSG_NO_SERVICE_LDAP.getName());
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
			ldapResponseDTO.setErrorMessageDTO(message);
		} catch (IllegalArgumentException eil) {
			log.error(CommonConstant.ILLEGAL_ARG_EXCEPTION.getName(), eil);
			ErrorMessageDTO errorMessage = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
					eil.getMessage());
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			ldapResponseDTO.setErrorMessageDTO(errorMessage);

		} catch (Exception ex1) {
			log.error(CommonConstant.EXCEPTION.getName(), ex1);
			ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
					ex1.getMessage());
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			ldapResponseDTO.setErrorMessageDTO(message);
		}
		return ldapResponseDTO;
	}

	/**
	 * Metodo para crear el Token
	 * 
	 * @param tokenizerDTO Objeto que contien los datos para generar el toekn.
	 * @return TokenizerResponseDTO regresa el objeto TokenizerResponseDTO que
	 *         contiene los datos del toekn.
	 * 
	 */
	@Override
	public LdapResponseDTO authenticationLdap(SecurityAuthDTO securityAuthDTO) {

		/** Variable que contiene el objeto de respuesta de LDAP */
		LdapResponseDTO ldapResponseDTO = null;
		LdapVO ldapVO = null;

		try {
			if (securityAuthDTO.getCredentials().isEmpty()) {
				throw new IllegalArgumentException(CommonConstant.MSG_CREDENTIALS_EMPTY.getName());
			}
			/** Descripta las credenciales */
			securityAuthDTO = cipherAESCommon.getDataCredentials(securityAuthDTO);

			log.info(new StringBuilder().append("Valida si existe el usuario en LDAP: ")
					.append(securityAuthDTO.getUserName()));
			ldapVO = iLdapOutPort.autentication(securityAuthDTO);
			if (ldapVO != null) {
				LdapVOMappeDTO mapperLdapDTO = new LdapVOMappeDTO();
				LdapDTO ldapDTO = mapperLdapDTO.mapperVOtoDTO(ldapVO);
				ldapResponseDTO = new LdapResponseDTO();
				ldapResponseDTO.setStatusCode(HttpStatus.OK.value());
				ldapResponseDTO.setLdapDTO(ldapDTO);
			}
		
		} catch (CommunicationException ex0) {
			log.error(CommonConstant.COMMUNICATION_EXCEPTION_LDAP.getName());
			log.error(CommonConstant.MSG_NO_SERVICE_LDAP.getName(), ex0);
			ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), new Date(),
					CommonConstant.MSG_NO_SERVICE_LDAP.getName());
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
			ldapResponseDTO.setErrorMessageDTO(message);
		} catch (IllegalArgumentException eil) {
			log.error(CommonConstant.ILLEGAL_ARG_EXCEPTION.getName(), eil);
			ErrorMessageDTO errorMessage = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
					eil.getMessage());
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			ldapResponseDTO.setErrorMessageDTO(errorMessage);

		} catch (Exception ex1) {
			log.error(CommonConstant.EXCEPTION.getName(), ex1);
			ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
					ex1.getMessage());
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			ldapResponseDTO.setErrorMessageDTO(message);
		}
		return ldapResponseDTO;
	}

	/**
	 * Metodo para obtener encirptar una cedena.
	 * 
	 * @param securityAuthDTO - DTO que contien los datos para encirptar.
	 * @return regresa el objeto encirptado.
	 * @throws Exception Excepción durante el proceso.
	 */
	@Override
	public CipherResponseDTO encode(SecurityAuthDTO securityAuthDTO) {

		/** Variable que contiene el objeto de respuesta del token */
		CipherResponseDTO cipherResponseDTO = null;
		String data = null;
		log.info("Inicia encode service");
		try {
			if (securityAuthDTO.getCredentials().isEmpty()) {
				throw new IllegalArgumentException(CommonConstant.MSG_CREDENTIALS_EMPTY.getName());
			}

			log.info("Encriptar datos");
			data = cipherAESCommon.encryptStirngToAesHex(securityAuthDTO.getCredentials());
			cipherResponseDTO = new CipherResponseDTO();
			cipherResponseDTO.setDataDTO(new DataDTO(data.toUpperCase()));
			cipherResponseDTO.setStatusCode(HttpStatus.OK.value());

		} catch (IllegalArgumentException eil) {
			log.error(CommonConstant.ILLEGAL_ARG_EXCEPTION.getName(), eil);
			ErrorMessageDTO errorMessage = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
					eil.getMessage());
			cipherResponseDTO = new CipherResponseDTO();
			cipherResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			cipherResponseDTO.setErrorMessageDTO(errorMessage);

		} catch (Exception ex1) {
			log.error(CommonConstant.EXCEPTION.getName(), ex1);
			ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
					ex1.getMessage());
			cipherResponseDTO = new CipherResponseDTO();
			cipherResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			cipherResponseDTO.setErrorMessageDTO(message);
		}
		log.info("Termina encode service");
		return cipherResponseDTO;
	}

	/**
	 * Metodo para obtener desencirptar una cedena.
	 * 
	 * @param securityAuthDTO - DTO que contien los datos para desencirptar.
	 * @return regresa el objeto desencirptado.
	 * @throws Exception Excepción durante el proceso.
	 */
	@Override
	public CipherResponseDTO decode(SecurityAuthDTO securityAuthDTO) {

		/** Variable que contiene el objeto de respuesta del token */
		CipherResponseDTO cipherResponseDTO = null;
		String data = null;
		log.info("Inicia decode service");
		try {
			if (securityAuthDTO.getCredentials().isEmpty()) {
				throw new IllegalArgumentException(CommonConstant.MSG_CREDENTIALS_EMPTY.getName());
			}

			log.info("Encriptar datos");
			data = cipherAESCommon.decryptAesHexToString(securityAuthDTO.getCredentials());
			cipherResponseDTO = new CipherResponseDTO();
			cipherResponseDTO.setDataDTO(new DataDTO(data));
			cipherResponseDTO.setStatusCode(HttpStatus.OK.value());

		} catch (IllegalArgumentException eil) {
			log.error(CommonConstant.ILLEGAL_ARG_EXCEPTION.getName(), eil);
			ErrorMessageDTO errorMessage = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
					eil.getMessage());
			cipherResponseDTO = new CipherResponseDTO();
			cipherResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			cipherResponseDTO.setErrorMessageDTO(errorMessage);

		} catch (Exception ex1) {
			log.error(CommonConstant.EXCEPTION.getName(), ex1);
			ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
					ex1.getMessage());
			cipherResponseDTO = new CipherResponseDTO();
			cipherResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			cipherResponseDTO.setErrorMessageDTO(message);
		}
		log.info("Termina decode service");
		return cipherResponseDTO;
	}

}
