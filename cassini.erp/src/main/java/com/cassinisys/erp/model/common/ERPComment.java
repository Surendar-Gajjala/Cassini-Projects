package com.cassinisys.erp.model.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ERP_COMMENT")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "COMMON")
public class ERPComment implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private Date commentedDate;
	@ApiObjectField(required = true)
	private Integer commentedBy;
	@ApiObjectField(required = true)
	private String comment;
	@ApiObjectField(required = true)
	private ObjectType objectType;
	@ApiObjectField(required = true)
	private Integer objectId;
	@ApiObjectField
	private Integer replyTo;

	@Id
	@SequenceGenerator(name = "COMMENT_ID_GEN", sequenceName = "COMMENT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_ID_GEN")
	@Column(name = "COMMENT_ID", nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COMMENTED_DATE", nullable = false)
	public Date getCommentedDate() {
		return commentedDate;
	}

	public void setCommentedDate(Date commentedDate) {
		this.commentedDate = commentedDate;
	}

	@Column(name = "COMMENTED_BY", nullable = false)
	public Integer getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(Integer commentedBy) {
		this.commentedBy = commentedBy;
	}

	@Column(name = "COMMENT", nullable = false)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.core.ObjectType") })
	@Column(name = "OBJECT_TYPE", nullable = false)
	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	@Column(name = "OBJECT_ID", nullable = false)
	public Integer getObjectId() {
		return objectId;
	}

	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}

	@Column(name = "REPLY_TO")
	public Integer getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Integer replyTo) {
		this.replyTo = replyTo;
	}

}
