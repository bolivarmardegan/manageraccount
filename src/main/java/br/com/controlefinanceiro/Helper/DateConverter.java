package br.com.controlefinanceiro.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.primefaces.component.message.Message;

@FacesConverter("dateConverter")
public class DateConverter implements Converter {
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			calendar.setTime(simpleDateFormat.parse(value));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Calendar calendar = (Calendar) value;
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
		String dataString = s.format(calendar.getTime());
		return dataString;
	}

}
