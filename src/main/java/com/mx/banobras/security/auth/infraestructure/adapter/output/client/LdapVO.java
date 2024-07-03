package com.mx.banobras.security.auth.infraestructure.adapter.output.client;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LdapVO {
	private String userName;
	private String password;
	private String expediente;
	private String cuentaControl;
	private String nombre;
	private String puesto;
	private String area;
	private String extension;
	private Integer activo;
	private String emailPrincipal;
	private String email;
	private List<String> listaGrupos;
	
}
