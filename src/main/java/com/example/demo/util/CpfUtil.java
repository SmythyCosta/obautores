package com.example.demo.util;

import java.util.List;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;

public class CpfUtil {
	
	public static boolean valida(String cpf){ 
		boolean retorno = false;
		CPFValidator cpfValidator = new CPFValidator(); 
		List<ValidationMessage> erros = cpfValidator.invalidMessagesFor(cpf); 
		
		retorno = (erros.size() > 0) ? false : true;
	    return retorno;
	}
}
