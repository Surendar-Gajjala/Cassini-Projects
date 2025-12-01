package com.cassinisys.platform.model.common;

import java.io.Serializable;

/**
 * 
 * @author reddy
 *
 */
public enum ActionType implements Serializable {

	CREATED, UPDATED, DELETED,

	ADDRESS_ADDED, ADDRESS_DELETED,

	TASK_ASSIGNED, TASK_UNASSIGNED, TASK_OBSERVER_ADDED, TASK_OBSERVER_DELETED,

	MEETING_ATTENDEE_ADDED, MEETING_ATTENDEE_UPDATED, MEETING_ATTENDEE_DELETED;

}
