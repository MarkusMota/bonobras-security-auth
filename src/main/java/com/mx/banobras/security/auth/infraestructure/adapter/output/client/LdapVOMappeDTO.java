package com.mx.banobras.security.auth.infraestructure.adapter.output.client;

import com.mx.banobras.security.auth.infraestructure.config.dto.LdapDTO;

public class LdapVOMappeDTO {
	
	
	public LdapDTO mapperVOtoDTO(LdapVO ldapVO) {
		
		LdapDTO ldapDTO = new LdapDTO();
		
		ldapDTO.setUserName(ldapVO.getUserName());
		ldapDTO.setPassword(ldapVO.getPassword());
		ldapDTO.setExpediente(ldapVO.getExpediente());
		ldapDTO.setCuentaControl(ldapVO.getCuentaControl());
		ldapDTO.setNombre(ldapVO.getNombre());
		ldapDTO.setPuesto(ldapVO.getPuesto());
		ldapDTO.setArea(ldapVO.getArea());
		ldapDTO.setExtension(ldapVO.getExtension());
		ldapDTO.setActivo(ldapVO.getActivo());
		ldapDTO.setEmailPrincipal(ldapVO.getEmailPrincipal());
		ldapDTO.setEmail(ldapVO.getEmail());
		ldapDTO.setListaGrupos(ldapVO.getListaGrupos());
		return ldapDTO;
	}

	
	
}
