package br.com.mv.microservice.service.exception;

public class EmailExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EmailExistenteException(String message) {
		super(message);
	}
	

}
