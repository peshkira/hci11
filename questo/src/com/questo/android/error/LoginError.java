package com.questo.android.error;

public class LoginError {

	private Boolean emailWrong;
	private Boolean passwordWrong;
	
	/**
	 * Constructs a LoginError. Use null if fact is unknown (e.g. if passwordWrong is unknown, set to null).
	 * @param emailWrong
	 * @param passwordWrong
	 */
	public LoginError(Boolean emailWrong, Boolean passwordWrong) {
		super();
		this.emailWrong = emailWrong;
		this.passwordWrong = passwordWrong;
	}

	public Boolean getEmailWrong() {
		return emailWrong;
	}

	public Boolean getPasswordWrong() {
		return passwordWrong;
	}

}
