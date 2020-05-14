package it.interfree.leonardoce.iconv.math;

/**
 * Rappresenta le informazioni necessarie per la trasformazione da coordinate
 * geocentriche a geodetiche
 * 
 * Codice basato su quello delle librerie Proj
 * 
 * @author leonardo
 */
public class GeocentricInfo {
	double Geocent_a; /* Semi-major axis of ellipsoid in meters */
	double Geocent_b; /* Semi-minor axis of ellipsoid */
	double Geocent_a2; /* Square of semi-major axis */
	double Geocent_b2; /* Square of semi-minor axis */
	double Geocent_e2; /* Eccentricity squared */
	double Geocent_ep2; /* 2nd eccentricity squared */

	private static double PI_OVER_2 = Math.PI / 2.0;
	private static double PI = Math.PI;

	private static double GENAU = 1.E-12;
	private static double GENAU2 = (GENAU*GENAU);
	private static int MAXITER = 30;
	

	public void setGeocentricParameters(double a, double b)
			throws GeocentricException { /* BEGIN Set_Geocentric_Parameters */
		/*
		 * The function Set_Geocentric_Parameters receives the ellipsoid
		 * parameters as inputs and sets the corresponding state variables.
		 * 
		 * a : Semi-major axis, in meters. (input) b : Semi-minor axis, in
		 * meters. (input)
		 */
		if (a <= 0.0) {
			throw new GeocentricException("Errore nel parametro A");
		}

		if (b <= 0.0) {
			throw new GeocentricException("Errore nel parametro B");
		}

		if (a < b) {
			throw new GeocentricException("A deve essere minore di B");
		}

		Geocent_a = a;
		Geocent_b = b;
		Geocent_a2 = a * a;
		Geocent_b2 = b * b;
		Geocent_e2 = (Geocent_a2 - Geocent_b2) / Geocent_a2;
		Geocent_ep2 = (Geocent_a2 - Geocent_b2) / Geocent_b2;
	}

	public double getA() {
		return Geocent_a;
	}

	public double getB() {
		return Geocent_b;
	}

	public Punto3D geodeticToGeocentric(double Latitude, double Longitude,
			double Height) throws GeocentricException {
		/* BEGIN Convert_Geodetic_To_Geocentric */
		/*
		 * The function Convert_Geodetic_To_Geocentric converts geodetic
		 * coordinates (latitude, longitude, and height) to geocentric
		 * coordinates (X, Y, Z), according to the current ellipsoid parameters.
		 * 
		 * Latitude : Geodetic latitude in radians (input) Longitude : Geodetic
		 * longitude in radians (input) Height : Geodetic height, in meters
		 * (input) X : Calculated Geocentric X coordinate, in meters (output) Y
		 * : Calculated Geocentric Y coordinate, in meters (output) Z :
		 * Calculated Geocentric Z coordinate, in meters (output)
		 */
		double Rn; /* Earth radius at location */
		double Sin_Lat; /* sin(Latitude) */
		double Sin2_Lat; /* Square of sin(Latitude) */
		double Cos_Lat; /* cos(Latitude) */

		/*
		 * * Don't blow up if Latitude is just a little out of the value* range
		 * as it may just be a rounding issue. Also removed longitude* test, it
		 * should be wrapped by cos() and sin(). NFW for PROJ.4, Sep/2001.
		 */
		if (Latitude < -PI_OVER_2 && Latitude > -1.001 * PI_OVER_2) {
			Latitude = -PI_OVER_2;
		} else if (Latitude > PI_OVER_2 && Latitude < 1.001 * PI_OVER_2) {
			Latitude = PI_OVER_2;
		} else if ((Latitude < -PI_OVER_2) || (Latitude > PI_OVER_2)) {
			/*
			 * Latitude out of range
			 */
			throw new GeocentricException("Latitude out of range");
		}

		if (Longitude > PI) {
			Longitude -= (2 * PI);
		}
		Sin_Lat = Math.sin(Latitude);
		Cos_Lat = Math.cos(Latitude);
		Sin2_Lat = Sin_Lat * Sin_Lat;
		Rn = Geocent_a / (Math.sqrt(1.0e0 - Geocent_e2 * Sin2_Lat));

		Punto3D p = new Punto3D();

		p.x = (Rn + Height) * Cos_Lat * Math.cos(Longitude);
		p.y = (Rn + Height) * Cos_Lat * Math.sin(Longitude);
		p.z = ((Rn * (1 - Geocent_e2)) + Height) * Sin_Lat;

		return p;

	} /* END OF Convert_Geodetic_To_Geocentric */
	

	public Punto3D geocentricToGeodetic (double X,
	                                        double Y, 
	                                        double Z)
	{ 
		
	/* BEGIN Convert_Geocentric_To_Geodetic */
	/*
	* Reference...
	* ============
	* Wenzel, H.-G.(1985): Hochauflösende Kugelfunktionsmodelle für
	* das Gravitationspotential der Erde. Wiss. Arb. Univ. Hannover
	* Nr. 137, p. 130-131.

	* Programmed by GGA- Leibniz-Institue of Applied Geophysics
	*               Stilleweg 2
	*               D-30655 Hannover
	*               Federal Republic of Germany
	*               Internet: www.gga-hannover.de
	*
	*               Hannover, March 1999, April 2004.
	*               see also: comments in statements
	* remarks:
	* Mathematically exact and because of symmetry of rotation-ellipsoid,
	* each point (X,Y,Z) has at least two solutions (Latitude1,Longitude1,Height1) and
	* (Latitude2,Longitude2,Height2). Is point=(0.,0.,Z) (P=0.), so you get even
	* four solutions,	every two symmetrical to the semi-minor axis.
	* Here Height1 and Height2 have at least a difference in order of
	* radius of curvature (e.g. (0,0,b)=> (90.,0.,0.) or (-90.,0.,-2b);
	* (a+100.)*(sqrt(2.)/2.,sqrt(2.)/2.,0.) => (0.,45.,100.) or
	* (0.,225.,-(2a+100.))).
	* The algorithm always computes (Latitude,Longitude) with smallest |Height|.
	* For normal computations, that means |Height|<10000.m, algorithm normally
	* converges after to 2-3 steps!!!
	* But if |Height| has the amount of length of ellipsoid's axis
	* (e.g. -6300000.m),	algorithm needs about 15 steps.
	*/

	/* local defintions and variables */
	/* end-criterium of loop, accuracy of sin(Latitude) */
	    double P;        /* distance between semi-minor axis and location */
	    double RR;       /* distance between center and location */
	    double CT;       /* sin of geocentric latitude */
	    double ST;       /* cos of geocentric latitude */
	    double RX;
	    double RK;
	    double RN;       /* Earth radius at location */
	    double CPHI0;    /* cos of start or old geodetic latitude in iterations */
	    double SPHI0;    /* sin of start or old geodetic latitude in iterations */
	    double CPHI;     /* cos of searched geodetic latitude */
	    double SPHI;     /* sin of searched geodetic latitude */
	    double SDPHI;    /* end-criterium: addition-theorem of sin(Latitude(iter)-Latitude(iter-1)) */
	    //boolean At_Pole;     /* indicates location is in polar region */
	    int iter;        /* # of continous iteration, max. 30 is always enough (s.a.) */

	    //At_Pole = false;
	    P = Math.sqrt(X*X+Y*Y);
	    RR = Math.sqrt(X*X+Y*Y+Z*Z);
	    
	    double Longitude; 
	    double Latitude; 
	    double Height; 

	/*	special cases for latitude and longitude */
	    if (P/Geocent_a < GENAU) {

	/*  special case, if P=0. (X=0., Y=0.) */
	        //At_Pole = true;	        
		Longitude = 0.;

	/*  if (X,Y,Z)=(0.,0.,0.) then Height becomes semi-minor axis
	 *  of ellipsoid (=center of mass), Latitude becomes PI/2 */
	        if (RR/Geocent_a < GENAU) {
	            Latitude = PI_OVER_2;
	            Height   = -Geocent_b;
	            return new Punto3D(Longitude, Latitude, Height);

	        }
	    }
	    else {
	/*  ellipsoidal (geodetic) longitude
	 *  interval: -PI < Longitude <= +PI */
	        Longitude=Math.atan2(Y,X);
	    }

	/* --------------------------------------------------------------
	 * Following iterative algorithm was developped by
	 * "Institut für Erdmessung", University of Hannover, July 1988.
	 * Internet: www.ife.uni-hannover.de
	 * Iterative computation of CPHI,SPHI and Height.
	 * Iteration of CPHI and SPHI to 10**-12 radian resp.
	 * 2*10**-7 arcsec.
	 * --------------------------------------------------------------
	 */
	    CT = Z/RR;
	    ST = P/RR;
	    RX = 1.0/Math.sqrt(1.0-Geocent_e2*(2.0-Geocent_e2)*ST*ST);
	    CPHI0 = ST*(1.0-Geocent_e2)*RX;
	    SPHI0 = CT*RX;
	    iter = 0;

	/* loop to find sin(Latitude) resp. Latitude
	 * until |sin(Latitude(iter)-Latitude(iter-1))| < genau */
	    do
	    {
	        iter++;
	        RN = Geocent_a/Math.sqrt(1.0-Geocent_e2*SPHI0*SPHI0);

	/*  ellipsoidal (geodetic) height */
	        Height = P*CPHI0+Z*SPHI0-RN*(1.0-Geocent_e2*SPHI0*SPHI0);

	        RK = Geocent_e2*RN/(RN+Height);
	        RX = 1.0/Math.sqrt(1.0-RK*(2.0-RK)*ST*ST);
	        CPHI = ST*(1.0-RK)*RX;
	        SPHI = CT*RX;
	        SDPHI = SPHI*CPHI0-CPHI*SPHI0;
	        CPHI0 = CPHI;
	        SPHI0 = SPHI;
	    }
	    while (SDPHI*SDPHI > GENAU2 && iter < MAXITER);

	/*	ellipsoidal (geodetic) latitude */
	    Latitude=Math.atan(SPHI/Math.abs(CPHI));

	    return new Punto3D(Longitude, Latitude, Height);
	} /* END OF Convert_Geocentric_To_Geodetic */	
}
