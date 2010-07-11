package com.fede.MessageException;

import android.content.Context;

public class CommandParseException extends InvalidCommandException {
	public CommandParseException(String r, Context c)
	{
		super(r, c);
	}
}

