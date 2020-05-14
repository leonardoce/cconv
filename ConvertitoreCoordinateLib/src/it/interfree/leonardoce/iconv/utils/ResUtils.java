package it.interfree.leonardoce.iconv.utils;

import android.content.Context;

import it.interfree.leonardoce.convertitorecoordinatelib.R;
import it.interfree.leonardoce.iconv.core.convs.FusoGauss;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;

/**
 * Created by leonardo on 1/1/15.
 */
public class ResUtils {
    public static String fuso2string(Context context, FusoGauss fuso)
    {
        if (fuso.equals(FusoGauss.FUSO_EST))
        {
            return context.getString(R.string.est);
        }
        else
        {
            return context.getString(R.string.ovest);
        }
    }

    public static String formatLatitude(Context ctx, double latitude)
    {
        return GeodesicUtils.formatDegree(latitude, ctx.getString(R.string.nord), ctx.getString(R.string.sud));
    }

    public static String formatLongitude(Context ctx, double longitude)
    {
        return GeodesicUtils.formatDegree(longitude, ctx.getString(R.string.est), ctx.getString(R.string.ovest));
    }

    public static String formatMinutesLatitude(Context ctx, double latitude)
    {
        return GeodesicUtils.formatMinutes(latitude, ctx.getString(R.string.nord), ctx.getString(R.string.sud));
    }

    public static String formatMinutesLongitude(Context ctx, double longitude)
    {
        return GeodesicUtils.formatMinutes(longitude, ctx.getString(R.string.est), ctx.getString(R.string.ovest));
    }
    public static String formatLatLong(Context ctx, double latitude, double longitude)
    {
        String formatLatitude = formatLatitude(ctx, latitude);
        String formatLongitude = formatLongitude(ctx, longitude);

        return formatLatitude + " " + formatLongitude;
    }

    public static String formatMinutesLatLong(Context ctx, double latitude, double longitude)
    {
        String formatLatitude = formatMinutesLatitude(ctx, latitude);
        String formatLongitude = formatMinutesLongitude(ctx, longitude);
        return formatLatitude + " " + formatLongitude;
    }

}
