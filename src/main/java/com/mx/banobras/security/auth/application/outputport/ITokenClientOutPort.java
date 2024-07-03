package com.mx.banobras.security.auth.application.outputport;

import java.io.IOException;
import java.net.http.HttpResponse;
import com.mx.banobras.security.auth.infraestructure.config.dto.SecurityAuthDTO;



public interface ITokenClientOutPort {

	public HttpResponse<String> getTokenAuthorization(SecurityAuthDTO securityAuthDTO) throws IOException, InterruptedException;
	
	
	
}
