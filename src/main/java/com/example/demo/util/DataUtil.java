package com.example.demo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DataUtil {	
	
	final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	final static String DATE_FORMAT_ONTHECALENDAR = "uuuu-MM-dd hh:mm:ss";
	final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 *  
	 * */
	public static boolean isDateValid(String date) {
		boolean dateValid = false;
		if(isDateValidFormat(date)) {
			if(isDateValidOnTheCalendar(date)) {
				dateValid = true;
			}
		}		
		return dateValid;
	}
	
	/**
	 *  
	 * */
	public static boolean isDateValidFormat(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
	}
	
	/**
	 *  Verifica se é uma data valida no Calendario
	 *  
	 *  método não deixa passar datas consideradas 
	 *  inválidas(como 30 de fevereiro)
	 *  
	 * */
	public static boolean isDateValidOnTheCalendar(String strDate) {
	    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_ONTHECALENDAR)
	    															.withResolverStyle(ResolverStyle.STRICT);
	    try {
	        LocalDate date = LocalDate.parse(strDate, dateTimeFormatter);
	        return true;
	    } catch (DateTimeParseException e) {
	       return false;
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