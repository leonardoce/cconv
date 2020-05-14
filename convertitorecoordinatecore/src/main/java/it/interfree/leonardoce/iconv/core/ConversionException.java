package it.interfree.leonardoce.iconv.core;

public class ConversionException extends Exception 
{
	private static final long serialVersionUID = -8406569549462441539L;

	public ConversionException (String msg)
	{
		super(msg);
	}
	
	public ConversionException (String msg, Throwable derivata)
	{
		super(msg, derivata);
	}
}
