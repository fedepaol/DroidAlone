package com.fede;

public interface SendInterface {
	void sendSms(String number, String message);
	void sendMail(String mailBody);
}
