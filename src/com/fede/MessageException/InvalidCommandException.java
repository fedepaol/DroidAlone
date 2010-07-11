package com.fede.MessageException;

import com.fede.R;
import com.fede.Utilities.GeneralUtils;

import android.content.Context;

public class InvalidCommandException extends Exception {
	public InvalidCommandException(String r, Context c)
	{
		super(r);
		String error = c.getString(R.string.notify_error);
		GeneralUtils.notifyEvent(error, r, c);
	}
}

