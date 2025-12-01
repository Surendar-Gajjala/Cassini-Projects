package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.common.ActionType;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.Constants;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author reddy
 */
public class ActivityCriteria extends Criteria {

	private Integer object;
	private ObjectType objectType;
	private Integer referenceObject;
	private ObjectType referenceObjectType;
	private ActionType action;
	private Integer person;

	@DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
	private Date start;

	@DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
	private Date end;

	public ActivityCriteria() {
	}

	public Integer getObject() {
		return object;
	}

	public void setObject(Integer object) {
		this.object = object;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public Integer getReferenceObject() {
		return referenceObject;
	}

	public void setReferenceObject(Integer referenceObject) {
		this.referenceObject = referenceObject;
	}

	public ObjectType getReferenceObjectType() {
		return referenceObjectType;
	}

	public void setReferenceObjectType(ObjectType referenceObjectType) {
		this.referenceObjectType = referenceObjectType;
	}

	public ActionType getAction() {
		return action;
	}

	public void setAction(ActionType action) {
		this.action = action;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Integer getPerson() {
		return person;
	}

	public void setPerson(Integer person) {
		this.person = person;
	}

}
