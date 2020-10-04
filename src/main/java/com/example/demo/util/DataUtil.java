package com.example.demo.util;

import org.apache.commons.validator.routines.DateValidator;

import com.example.demo.exception.BusinessException;

public class DataUtil {

	public static String utilDateToStr(java.util.Date data) {
		java.text.DateFormat formatter = new java.text.SimpleDateFormat(
				"dd/MM/yyyy");
		formatter.setLenient(false);
		String strData = formatter.format(data);
		return strData;
	}

	public static java.util.Date strToUtilDate(String str, String formato)
			throws BusinessException {
		java.text.DateFormat formatter = new java.text.SimpleDateFormat(formato);
		formatter.setLenient(false);
		java.util.Date dataConvertida = null;
		try {
			dataConvertida = (java.util.Date) formatter.parse(str);
			return dataConvertida;
		} catch (java.text.ParseException exc) {
			StringBuffer mensagem = new StringBuffer(
					"N&#227;o foi poss&#237;vel converter a data ou hora.");
			mensagem.append("\n\nMotivo: " + exc.getMessage());
			throw new BusinessException(mensagem.toString(), exc);
		}
	}

	public static java.sql.Date utilDateToSqlDate(java.util.Date data) {
		java.sql.Date dataConvertida = new java.sql.Date(data.getTime());
		return dataConvertida;
	}

	public static java.sql.Time utilDateToSqlTime(java.util.Date hora) {
		java.sql.Time horaConvertida = new java.sql.Time(hora.getTime());
		return horaConvertida;
	}
	

}