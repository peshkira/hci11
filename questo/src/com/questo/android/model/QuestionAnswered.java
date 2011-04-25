package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class QuestionAnswered {

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String uuid;
	@DatabaseField(foreign = true)
	private User user;
	@DatabaseField(foreign = true)
	private Question question;
	@DatabaseField
	private Date answeredAt;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public QuestionAnswered() {
	}

	public QuestionAnswered(String uuid, User user, Question question, Date answeredAt) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.question = question;
		this.answeredAt = answeredAt;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}
	
	public Question getQuestion() {
		return question;
	}

	public Date getAnsweredAt() {
		return answeredAt;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		QuestionAnswered other = (QuestionAnswered) o;
		return other.id.equals(this.id);
	}

}