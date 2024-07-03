package com.mx.banobras.security.auth.common.util;

public enum CommonConstant {
	/** Constante para el mensaje de erorr 500 */
	MSG_ERROR_500 ("Error interno, comunicarse con el administrador del sistema."),
	/** Constante para el mensaje de erorr 500 */
	MSG_ERROR_503 ("Error interno, servicio no disponible."),
	/** Constante para el mensaje de erorr 500 */
	MSG_CREDENTIALS_INVALID ("Credenciales incorrectas."),
	/** Constante para el mensaje de erorr 500 */
	MSG_CREDENTIALS_EMPTY ("No hay datos en credenciales."),
	/** Constante para el mensaje de servicio no esta disponible LDAP */
	MSG_NO_SERVICE_LDAP ("Servicio no disponible - LDAP"),
	/** Constante para el mensaje de servicio no esta disponible Tokenizer */
	MSG_NO_SERVICE_TOKENIZER ("Servicio no disponible - Tokenizer"),
	/** COnstante para validar el localhost IPV4 */
	LOCALHOST_IPV4 ("127.0.0.1"),
	/** COnstante para validar el localhost IPV6 */
	LOCALHOST_IPV6 ("0:0:0:0:0:0:0:1"),
	/** Constante para validar la cadena "unknown" */
	UNKNOWN ("unknown"),
	/** Constante para validar el header "X-Forwarded-For" */
	H_X_FORWARDED_FOR ("X-Forwarded-For"),
	/** Constante para validar el header "Proxy-Client-IP" */
	H_PROXY_CLIENT_IP ("Proxy-Client-IP"),
	/** Constante para validar el header "WL-Proxy-Client-IP" */
	H_WL_PROXY_CLIENT_IP ("WL-Proxy-Client-IP"),
	/** Constante para validar el caracter coma "," */
	COMMA (","),
	/** Constante para imprimir en log */
	BUSCA_POR ("Busca por:: ");
	
	private String name;

	private CommonConstant(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
