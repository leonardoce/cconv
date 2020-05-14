package it.interfree.leonardoce.iconv.math;

import java.text.DecimalFormat;

public class GeodesicUtils 
{
	public static double degreeToRadians(double orig)
	{
		return orig * Math.PI / 180;
	}
	
	public static double radiansToDegree(double dest)
	{
		return dest * 180 / Math.PI;
	}
	
	public static Punto3D degreeToRadians(Punto3D orig)
	{
		Punto3D dest = new Punto3D(
			orig.x * Math.PI / 180, 
			orig.y * Math.PI / 180, 
			orig.z * Math.PI / 180
		);
		
		return dest;
	}
	
	public static Punto3D radiansToDegree(Punto3D orig)
	{
		Punto3D dest = new Punto3D(
			orig.x * 180 / Math.PI, 
			orig.y * 180 / Math.PI, 
			orig.z * 180 / Math.PI
		);
		
		return dest;
	}

	public static Punto2D degreeToRadians(Punto2D orig)
	{
		Punto2D dest = new Punto2D(
			orig.x * Math.PI / 180, 
			orig.y * Math.PI / 180
		);
		
		return dest;
	}
	
	public static Punto2D radiansToDegree(Punto2D orig)
	{
		Punto2D dest = new Punto2D(
			orig.x * 180 / Math.PI, 
			orig.y * 180 / Math.PI
		);
		
		return dest;
	}
	
	public static double degreeToDecimal(double prime, double minutes, double seconds)
	{
		return prime + minutes/60 + seconds/3600;
	}
	
	public static double degreeToDecimal(double prime, double minutes)
	{
		return prime + minutes/60;
	}
	
	public static double[] decimalToDegree(double decimal)
	{
		double result[] = new double[3];
		
		result[0] = Math.floor(decimal);
		decimal = decimal - Math.floor(decimal);
		decimal = decimal * 60;
		
		result[1] = Math.floor(decimal);
		decimal = decimal - Math.floor(decimal);
		decimal = decimal * 60;
		
		result[2] = Math.floor(decimal*100)/100;
		
		return result;
	}
	
	public static String formatDegree(double decimal, String segnoPositivo, String segnoNegativo)
	{
		String segno = decimal>=0 ? segnoPositivo : segnoNegativo;
		decimal = Math.abs(decimal);
		
		DecimalFormat dec = new DecimalFormat("#0.00");
		DecimalFormat round = new DecimalFormat("#0");
		
		double[] degree = decimalToDegree(decimal);
		
		String result = round.format(degree[0]); 
		result += "\u00b0 ";
		result += round.format(degree[1]); 
		result += "\' ";
		result += dec.format(degree[2]); 
		result += "\" ";
		result += segno;
		
		return result;
	}
	
	public static String formatMinutes(double decimal, String segnoPositivo, String segnoNegativo)
	{
		String segno = decimal>=0 ? segnoPositivo : segnoNegativo;

		decimal = Math.abs(decimal);
		
		DecimalFormat dec = new DecimalFormat("#0.0000");
		DecimalFormat round = new DecimalFormat("#0");
		
		double[] degree = decimalToDegree(decimal);
		
		String result = round.format(degree[0]); 
		result += "\u00b0 ";
		result += dec.format(degree[1] + degree[2]/60); 
		result += "\' ";
		result += segno;
		
		return result;
	}

    public static String formatQTH(double latitude, double longitude) {

        // http://en.wikipedia.org/wiki/Maidenhead_Locator_System

        double lon = longitude + 180;
        double lat = latitude + 90;

        String grid = "";

        grid += (char)('A' + (int)(lon / 20));
        grid += (char)('A' + (int)(lat / 10));
        grid += (char)('0' + (int)((lon % 20)/2));
        grid += (char)('0' + (int)((lat % 10)/1));
        grid += (char)('a' + (int)(((lon - ((int)(lon/2)*2))) / (5.0/60.0)));
        grid += (char)('a' + (int)(((lat - ((int)(lat/1))*1)) / (2.5/60.0)));

        return grid;
    }
}
