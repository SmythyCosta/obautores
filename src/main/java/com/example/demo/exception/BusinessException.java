package com.example.demo.exception;

public class BusinessException extends Exception  {
	
	private static final long serialVersionUID = 2148610487071098173L;

	public BusinessException(String mensagem, Throwable causa) {
		super(mensagem, causa);
		causa.printStackTrace();
	}

	public BusinessException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}

}
