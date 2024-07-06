package com.mx.banobras.security.auth.infraestructure.adapter.input.controller;

/**
 * SecurityAuthController.java:
 * 
 * Clase controller que expone los servicios para autenticar y cifrar datos
 * 
 * @author Marcos Gonzalez
 * @version 1.0, 13/06/2024
 * @see Documento "MAR - Marco Arquitectonico de Referencia"
 * @since JDK 17
 */

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mx.banobras.security.auth.application.inputport.ISecurityAuthInputPort;
import com.mx.banobras.security.auth.common.util.CommonConstant;
import com.mx.banobras.security.auth.infraestructure.config.dto.CipherResponseDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.ErrorMessageDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.SecurityAuthDTO;
import com.mx.banobras.security.auth.infraestructure.config.dto.LdapResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(originPatterns = { "*" })
@RestController
@RequestMapping("/banobras/v1/security")
public class SecurityAuthController {

	/** Trazas de la aplicación */
	Logger log = LogManager.getLogger(SecurityAuthController.class);

	/** Injection variable del objeto HttpServletRequest */
	private HttpServletRequest httRequest;

	/** Injection variable para la interfaz iTokenizerInputPort */
	private final ISecurityAuthInputPort iSecurityAuthInputPort;

	/** Consturctor de las interfaces que usa el controller */
	public SecurityAuthController(ISecurityAuthInputPort iSecurityAuthInputPort, 
			HttpServletRequest httRequest) {
		this.iSecurityAuthInputPort = iSecurityAuthInputPort;
		this.httRequest = httRequest;
	}

	/**
	 * Metodo para obtener el Token, para el consumo de los microservicios.
	 * 
	 * @param userName - Alias del usuario.
	 * @param x-api-id - Id de servicio que se va a consumir.
	 * @param consumer-api-id - Nombre de la intefaz que lo va a consumir el microservicio.
	 * @param functional-id - Acronimo de la funcionalidad.
	 * @param transaction-id - Identificador de la transacción generado por UUDI.
	 * @param refresh-token -Tiempo de duracion del refresh Token, si el valor es Cero, no se genera.
	 * 
	 * @return regresa el obejto TokenizerResponseDTO con los datos del Token
	 * @throws Exception Excepción durante el proceso de generar el Token.
	 * 
	 */
	@PostMapping("/auth-token-ldap")
	public ResponseEntity<LdapResponseDTO> ldapTokenAuth(
			@RequestHeader(value = "credentials") String credentials,
			@RequestHeader(value = "auth-token") String jwtToken,
			@RequestHeader(value = "application") String application,
			@RequestHeader(value = "consumer-api-id") String consumerApiId,
			@RequestHeader(value = "functional-id") String functionalId,
			@RequestHeader(value = "transaction-id") String transactionId) {

		LdapResponseDTO ldapResponseDTO = null;
		SecurityAuthDTO securityAuthDTO = null;
		ErrorMessageDTO errorMessage = null;

		try {
			/** String remoteHost = request.getRemoteHost(); */
			String remoteHost = getIpRemoteAdress();
			// Agrega parametros para que se muestren en el Log
			ThreadContext.put("transaction-id", transactionId);
			ThreadContext.put("ip", remoteHost);

			log.info("Inicia autenticacion Token-LDAP");
			securityAuthDTO = new SecurityAuthDTO(credentials, null,null, jwtToken, application, consumerApiId, functionalId, transactionId);
			ldapResponseDTO = iSecurityAuthInputPort.authenticationTokenLdap(securityAuthDTO);
			
			// Valida el resultado en la generacio del token
			if (ldapResponseDTO.getStatusCode() == 200) {
				log.info("Datos correctos");
				return new ResponseEntity<>(ldapResponseDTO, HttpStatus.OK);
			} else {
				log.info("Datos incorrectos");
				return new ResponseEntity<>(ldapResponseDTO, HttpStatus.valueOf(ldapResponseDTO.getStatusCode()) );
			}
		} catch (IllegalArgumentException eil) {
			log.error(CommonConstant.ILLEGAL_ARG_EXCEPTION.getName(), eil);
			errorMessage = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), eil.getMessage());
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			ldapResponseDTO.setErrorMessageDTO(errorMessage);
			return new ResponseEntity<>(ldapResponseDTO, HttpStatus.valueOf(ldapResponseDTO.getStatusCode()));
		}  catch (Exception e) {
			log.error(CommonConstant.COMMUNICATION_EXCEPTION_LDAP.getName());
			log.error(CommonConstant.EXCEPTION.getName(), e);
			errorMessage = new ErrorMessageDTO(500, new Date(), "No hay conexión, Directorio Activo");
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
			ldapResponseDTO.setErrorMessageDTO(errorMessage);
			return new ResponseEntity<>(ldapResponseDTO,  HttpStatus.valueOf(ldapResponseDTO.getStatusCode()));
		} finally {
			ThreadContext.clearStack();
			log.info("Finaliza autenticacion Token-LDAP");
		}
	}

	/**
	 * Metodo para obtener el Token, para el consumo de los microservicios.
	 * 
	 * @param userName - Alias del usuario.
	 * @param x-api-id - Id de servicio que se va a consumir.
	 * @param consumer-api-id - Nombre de la intefaz que lo va a consumir el microservicio.
	 * @param functional-id - Acronimo de la funcionalidad.
	 * @param transaction-id - Identificador de la transacción generado por UUDI.
	 * @param refresh-token -Tiempo de duracion del refresh Token, si el valor es Cero, no se genera.
	 * 
	 * @return regresa el obejto TokenizerResponseDTO con los datos del Token
	 * @throws Exception Excepción durante el proceso de generar el Token.
	 * 
	 */
	@PostMapping("/auth-ldap")
	public ResponseEntity<LdapResponseDTO> ldapAuth(
			@RequestHeader(value = "credentials") String credentials,
			@RequestHeader(value = "application") String application,
			@RequestHeader(value = "consumer-api-id") String consumerApiId,
			@RequestHeader(value = "functional-id") String functionalId,
			@RequestHeader(value = "transaction-id") String transactionId) {

		LdapResponseDTO ldapResponseDTO = null;
		SecurityAuthDTO securityAuthDTO = null;
		ErrorMessageDTO errorMessage = null;

		try {
			/** String remoteHost = request.getRemoteHost(); */
			String remoteHost = getIpRemoteAdress();
			// Agrega parametros para que se muestren en el Log
			ThreadContext.put("transaction-id", transactionId);
			ThreadContext.put("ip", remoteHost);

			log.info("Inicia autenticacion LDAP");
			securityAuthDTO = new SecurityAuthDTO(credentials, null,null, null, application, consumerApiId, functionalId, transactionId);
			ldapResponseDTO = iSecurityAuthInputPort.authenticationLdap(securityAuthDTO);
			
			// Valida el resultado 
			if (ldapResponseDTO.getStatusCode() == 200) {
				log.info("Datos correctos");
				return new ResponseEntity<>(ldapResponseDTO, HttpStatus.OK);
			} else {
				log.info("No tiene los permisos para generar el token");
				return new ResponseEntity<>(ldapResponseDTO, HttpStatus.valueOf(ldapResponseDTO.getStatusCode()) );
			}
		} catch (IllegalArgumentException eil) {
			log.error(CommonConstant.ILLEGAL_ARG_EXCEPTION.getName(), eil);
			errorMessage = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), eil.getMessage());
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			ldapResponseDTO.setErrorMessageDTO(errorMessage);
			return new ResponseEntity<>(ldapResponseDTO, HttpStatus.valueOf(ldapResponseDTO.getStatusCode()));
		}  catch (Exception e) {
			e.printStackTrace();
			log.error(CommonConstant.COMMUNICATION_EXCEPTION_LDAP.getName());
			log.error(CommonConstant.EXCEPTION.getName(), e);
			errorMessage = new ErrorMessageDTO(500, new Date(), "No hay conexión, Directorio Activo");
			ldapResponseDTO = new LdapResponseDTO();
			ldapResponseDTO.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
			ldapResponseDTO.setErrorMessageDTO(errorMessage);
			return new ResponseEntity<>(ldapResponseDTO,  HttpStatus.valueOf(ldapResponseDTO.getStatusCode()));
		} finally {
			ThreadContext.clearStack();
			log.info("Finaliza autenticacion LDAP");
		}
	}
	
	
	/**
	 * Metodo para obtener encriptar una cadena.
	 * 
	 * @param data datos a desecriptar.
	 * 
 	 * @return CipherResponseDTO objeto que contien los dato encriptados.
	 * @throws Exception Excepción durante el proceso.
	 * 
	 */
	@PostMapping("/encode")
	public ResponseEntity<CipherResponseDTO> encode(
			@RequestHeader(value = "data") String data) {

		CipherResponseDTO cipherResponseDTO = null;
		SecurityAuthDTO securityAuthDTO = null;
		ErrorMessageDTO errorMessage = null;

		try {
			log.info("Inicia controller encode");
			securityAuthDTO = new SecurityAuthDTO(data, null,null, null, null, null, null,null);

			cipherResponseDTO = iSecurityAuthInputPort.encode(securityAuthDTO);
			
			// Valida el resultado
			if (cipherResponseDTO.getStatusCode() == 200) {
				log.info("Se genera el token correctamente");
				return new ResponseEntity<>(cipherResponseDTO, HttpStatus.OK);
			} else {
				log.info("No tiene los permisos para generar el token");
				return new ResponseEntity<>(cipherResponseDTO, HttpStatus.valueOf(cipherResponseDTO.getStatusCode()) );
			}
		}  catch (Exception e) {
			log.error(CommonConstant.EXCEPTION.getName(), e);
			errorMessage = new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), e.getMessage());
			cipherResponseDTO = new CipherResponseDTO();
			cipherResponseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			cipherResponseDTO.setErrorMessageDTO(errorMessage);
			return new ResponseEntity<>(cipherResponseDTO,  HttpStatus.valueOf(cipherResponseDTO.getStatusCode()));
		} finally {
			log.info("Finaliza controller encode");
		}
	}
	
	/**
	 * Metodo para obtener desencriptar una cadena.
	 * 
	 * @param data datos a desecriptar.
	 * 
 	 * @return regresa la cadena desencriptada.
	 * @throws Exception Excepción durante el proceso.
	 * 
	 */
	@PostMapping("/decode")
	public ResponseEntity<CipherResponseDTO> dencode(
			@RequestHeader(value = "data") String data) {

		CipherResponseDTO cipherResponseDTO = null;
		SecurityAuthDTO securityAuthDTO = null;
		ErrorMessageDTO errorMessage = null;

		try {
			log.info("Inicia controller encode");
			securityAuthDTO = new SecurityAuthDTO(data, null,null, null, null, null, null,null);

			cipherResponseDTO = iSecurityAuthInputPort.decode(securityAuthDTO);
			
			// Valida el resultado 
			if (cipherResponseDTO.getStatusCode() == 200) {
				log.info("Se genera el decode correctamente");
				return new ResponseEntity<>(cipherResponseDTO, HttpStatus.OK);
			} else {
				log.info("No se genera el decode");
				return new ResponseEntity<>(cipherResponseDTO, HttpStatus.valueOf(cipherResponseDTO.getStatusCode()) );
			}
		} catch (Exception e) {
			log.error(CommonConstant.EXCEPTION.getName(), e);
			errorMessage = new ErrorMessageDTO(500, new Date(), e.getMessage());
			cipherResponseDTO = new CipherResponseDTO();
			cipherResponseDTO.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
			cipherResponseDTO.setErrorMessageDTO(errorMessage);
			return new ResponseEntity<>(cipherResponseDTO,  HttpStatus.valueOf(cipherResponseDTO.getStatusCode()));
		} finally {
			log.info("Finaliza controller encode");
		}
	}
	
	
	/**
	 * Metodo para obtener la IP del srevicio que invoca al controller.
	 * @throws UnknownHostException 
	 * 
	 * @throws Exception Excepción durante el proceso de generar el Token.
	 */
	private String getIpRemoteAdress() throws UnknownHostException {
		log.info(new StringBuilder().append("Busca IP :: ").append(httRequest.getRemoteHost()));
		log.info(new StringBuilder().append(CommonConstant.BUSCA_POR.getName()).append(CommonConstant.H_X_FORWARDED_FOR.getName()));
		String ipAddress = "";
		if( buscaEnHeaderPor(CommonConstant.H_X_FORWARDED_FOR.getName())) {
			ipAddress = httRequest.getHeader(CommonConstant.H_X_FORWARDED_FOR.getName());
		}else {
			if(buscaEnHeaderPor( CommonConstant.H_PROXY_CLIENT_IP.getName())){
				ipAddress = httRequest.getHeader(CommonConstant.H_PROXY_CLIENT_IP.getName());
			}else {
				if(buscaEnHeaderPor( CommonConstant.H_WL_PROXY_CLIENT_IP.getName())){
					ipAddress = httRequest.getHeader(CommonConstant.H_WL_PROXY_CLIENT_IP.getName());
				}else {
					if(buscaPorRemoteAddr()) {
						ipAddress = obtenValorPorRemoteAddr();
					}
				}
			}
		}
		log.info(new StringBuilder().append("La ip remota es :: ").append(ipAddress));
		return ipAddress;
	}
	
	
	private boolean buscaEnHeaderPor(String cadenaABuscar) {
		String ipAddress = httRequest.getHeader(cadenaABuscar);
		boolean bandera = true;
		if(ipAddress == null || ipAddress.isEmpty() || CommonConstant.UNKNOWN.getName().equalsIgnoreCase(ipAddress)) {
			bandera = false;
		}
		return bandera;
	}

	private boolean buscaPorRemoteAddr() throws UnknownHostException {
		String ipAddress = httRequest.getRemoteAddr();
		boolean bandera = false;
		if (CommonConstant.LOCALHOST_IPV4.getName().equals(ipAddress) || CommonConstant.LOCALHOST_IPV6.getName().equals(ipAddress)) {
			log.info("Busca por getLocalHost()");
			InetAddress inetAddress = InetAddress.getLocalHost();
			ipAddress = inetAddress.getHostAddress();
			bandera = true;
		}
		if (ipAddress != null && ipAddress.length() > 15 && ipAddress.contains(CommonConstant.COMMA.getName())) {
			bandera = true;
		}
		return bandera;
	}
	
	private String obtenValorPorRemoteAddr() throws UnknownHostException {
		String ipAddress = httRequest.getRemoteAddr();
		if (CommonConstant.LOCALHOST_IPV4.getName().equals(ipAddress) || CommonConstant.LOCALHOST_IPV6.getName().equals(ipAddress)) {
			log.info("Busca por getLocalHost()");
			InetAddress inetAddress = InetAddress.getLocalHost();
			ipAddress = inetAddress.getHostAddress();
		}
		if (ipAddress != null && ipAddress.length() > 15 && ipAddress.contains(CommonConstant.COMMA.getName())) {
			ipAddress = ipAddress.substring(0, ipAddress.indexOf(CommonConstant.COMMA.getName()));
		}
		return ipAddress;
	}
	
	
}
