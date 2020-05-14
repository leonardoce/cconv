package it.interfree.leonardoce.convertitorecoordinatelib.config;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class CurrentConfiguration {
	private static String metadata = null;
	
	private static void controllaMetadata(Context ctx) {
		if ( metadata==null ) {
			String iconvMode;
			try {
				Bundle metaData = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA).metaData;
				if ( metaData!=null ) {
					iconvMode = metaData.getString("iconv_mode");
				} else {
					iconvMode = "";
				}
			} catch (NameNotFoundException e) {
				iconvMode = "";
			}
			
			metadata = iconvMode;
		}
	}
}
