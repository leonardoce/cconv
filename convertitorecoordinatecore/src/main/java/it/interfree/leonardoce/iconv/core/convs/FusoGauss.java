package it.interfree.leonardoce.iconv.core.convs;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum FusoGauss {
	FUSO_OVEST, // Quello nostro
	FUSO_EST;

	private static final Map<Integer, FusoGauss> lookup = new HashMap<Integer, FusoGauss>();

	static {
		for (FusoGauss s : EnumSet.allOf(FusoGauss.class))
			lookup.put(s.ordinal(), s);
	}

	public static FusoGauss get(int code) {
		return lookup.get(code);
	}
	
	public int getId()
	{
		return ordinal();
	}
}
