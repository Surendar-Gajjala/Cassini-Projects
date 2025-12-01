package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author reddy
 */
@Entity
@Data
@Table(name = "MESSAGE")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "MESSAGE_ID_GEN",
			sequenceName = "MESSAGE_ID_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "MESSAGE_ID_GEN")
	@Column(name = "MESSAGE_ID", nullable = false)
	private Integer id;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "MESSAGE", nullable = false)
	private String message;

	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SENT_DATE", nullable = false)
	private Date sentDate;

	@Column(name = "SENT_BY", nullable = false)
	private Integer sentBy;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "IS_MESSAGESENTTO",
			joinColumns = @JoinColumn(name = "MESSAGE_ID"),
			inverseJoinColumns = @JoinColumn(name = "PERSON"))
	private Set<Person> sentTo;

	public Message() {
		sentTo = new HashSet<Person>();
	}

	public Message(Set<Person> sentTo) {
		this.sentTo = sentTo;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Message other = (Message) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
