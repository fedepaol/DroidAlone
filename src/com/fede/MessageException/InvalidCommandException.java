package com.fede.MessageException;

import android.content.Context;

public class InvalidCommandException extends Exception {
	public InvalidCommandException(String r, Context c)
	{
		super(r);
	}
}

