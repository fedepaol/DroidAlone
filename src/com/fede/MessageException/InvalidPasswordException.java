package com.fede.MessageException;

import android.content.Context;

public class InvalidPasswordException extends InvalidCommandException {
	public InvalidPasswordException(String r, Context c)
	{
		super(r, c);
	}
}
