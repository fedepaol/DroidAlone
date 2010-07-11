package com.fede.MessageException;

import android.content.Context;

public class ForwardingDisabledException extends InvalidCommandException {
	public ForwardingDisabledException(String r, Context c)
	{
		super(r, c);
	}
}

