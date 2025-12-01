package com.cassinisys.platform.security;

import com.cassinisys.platform.model.core.Login;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SecurityAccessContext {
	private Login subject;
	private Object object;
	private Object action;
	private Object environment;
	public SecurityAccessContext(){}
	public SecurityAccessContext(Login subject, Object object, Object action, Object environment) {
		super();
		this.subject = subject;
		this.object = object;
		this.action = action;
		this.environment = environment;
	}
	public Login getSubject() {
		return subject;
	}
	public void setSubject(Login subject) {
		this.subject = subject;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object resource) {
		this.object = resource;
	}
	public Object getAction() {
		return action;
	}
	public void setAction(Object action) {
		this.action = action;
	}
	public Object getEnvironment() {
		return environment;
	}
	public void setEnvironment(Object environment) {
		this.environment = environment;
	}

	public Boolean compareDates(Date objectDateValue, String method, String cDate) throws ParseException {
		Date criteriaDate = new SimpleDateFormat("dd/MM/yyyy").parse(cDate);
		Date objectDate = new SimpleDateFormat("yyyy-MM-dd").parse(objectDateValue.toString());
		if (method.equals("equals")) {
			return objectDate.equals(criteriaDate);
		} else if (method.equals("before")) {
			return objectDate.before(criteriaDate);
		} else if (method.equals("after")) {
			return objectDate.after(criteriaDate);
		}
		return false;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((environment == null) ? 0 : environment.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityAccessContext other = (SecurityAccessContext) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (environment == null) {
			if (other.environment != null)
				return false;
		} else if (!environment.equals(other.environment))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
}