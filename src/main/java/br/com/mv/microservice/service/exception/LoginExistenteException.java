package br.com.mv.microservice.service.exception;

public class LoginExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public LoginExistenteException(String message) {
		super(message);
	}
	

}
