package com.fede.MessageException;

import android.content.Context;

public class LocationNotFoundException extends InvalidCommandException {
	public LocationNotFoundException(String r, Context c)
	{
		super(r, c);
	}
}
