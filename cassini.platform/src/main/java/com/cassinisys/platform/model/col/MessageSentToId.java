package com.cassinisys.platform.model.col;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author reddy
 */
@Embeddable
@Data
@EqualsAndHashCode(callSuper=false)
public class MessageSentToId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "MESSAGE_ID", nullable = false)
	private Integer messageId;

	@Column(name = "PERSON", nullable = false)
	private Integer personId;

	public MessageSentToId() {
	}

	public MessageSentToId(Integer messageId, Integer personId) {
		this.messageId = messageId;
		this.personId = personId;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messageId == null) ? 0 : messageId.hashCode());
		result = prime * result
				+ ((personId == null) ? 0 : personId.hashCode());
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
		MessageSentToId other = (MessageSentToId) obj;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (personId == null) {
			if (other.personId != null)
				return false;
		} else if (!personId.equals(other.personId))
			return false;
		return true;
	}

}
