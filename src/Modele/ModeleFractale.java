	package Modele;

import java.util.Observable;

import Exceptions.NombreSaisiInterdit;

public class ModeleFractale extends Observable {
	
	
	// enum class des différents types de fractales implémentées
	public enum Type {
		MANDELBROT, JULIA, NEWTON,
	}; 

	// Size of the coordinate system for the Mandelbrot set
	private final double DEFAULT_MANDELBROT_RE_MIN = -2.5;
	private final double DEFAULT_MANDELBROT_RE_MAX = 1.5;
	private final double DEFAULT_MANDELBROT_IM_MIN = -1.2;
	private final double DEFAULT_MANDELBROT_IM_MAX = 1.2;
	
	private final double DEFAULT_MANDELBROT_Z = 0;
	private final double DEFAULT_MANDELBROT_Zi = 0;
	
	private double MANDELBROT_RE_MIN ;
	private double MANDELBROT_RE_MAX ;
	private double MANDELBROT_IM_MIN ;
	private double MANDELBROT_IM_MAX ;
	
	// Size of the coordinate system for the Julia set
	private final double DEFAULT_JULIA_RE_MIN = -1.5;
	private final double DEFAULT_JULIA_RE_MAX = 1.5;
	private final double DEFAULT_JULIA_IM_MIN = -1.5;
	private final double DEFAULT_JULIA_IM_MAX = 1.5;
	
	private final double DEFAULT_JULIA_Z = 0.01;
	private final double DEFAULT_JULIA_Zi = 0.285;
	
	private double JULIA_RE_MIN;
	private double JULIA_RE_MAX;
	private double JULIA_IM_MIN;
	private double JULIA_IM_MAX;

	// pour Newton
	private static double EPSILON = 0.000001;
	private double decalage_X = 1;
	private double decalage_Y = 1;
	
	// Nombre d'itérations par défaut
	private static int defaulIterationMax = 50;
	
	private static int imageWidth = 1135;
	private static int imageHeight= 800;

	private int iterationMax;

	private double z;
	private double zi;
	private int nombreZooms;
	private double zoom_x;
	private double zoom_y;
	private Type type;
	
	public ModeleFractale() {
	
		this.type = Type.MANDELBROT;
		iterationMax = 50; 
		z = 0;
		zi=0;		
		nombreZooms = 0;
	
	}


	public static int getDefaulIterationMax() {
		return defaulIterationMax;
	}


	public static void setDefaulIterationMax(int defaulIterationMax) {
		ModeleFractale.defaulIterationMax = defaulIterationMax;
	}

	// Fonction regroupant le setChange et et le notifyObservers afin de notifier à l'observateur d'un changement
	public void notifier() {
		this.setChanged();
		this.notifyObservers();
	}

	public Type getType() {
		return type;
	}

	public double getZoom_x() {
		return zoom_x;
	}


	public double getZoom_y() {
		return zoom_y;
	}


	public static double getEPSILON() {
		return EPSILON;
	}


	public void setEPSILON(double ePSILON) {
		EPSILON = ePSILON;
	}

	// Fonction pour changer le type de la fractale, et ainsi effectuer les changements nécessaires
	public void setType(Type type) {

		this.type = type;
				
		// Dans chaque cas, on remet toutes les valeurs nécesaires à leur valeur par défaut.
		// On réinitialise également le zoom
		switch (type) {
		case MANDELBROT:
			MANDELBROT_RE_MIN = DEFAULT_MANDELBROT_RE_MIN;
			MANDELBROT_RE_MAX = DEFAULT_MANDELBROT_RE_MAX;
			MANDELBROT_IM_MIN = DEFAULT_MANDELBROT_IM_MIN;
			MANDELBROT_IM_MAX = DEFAULT_MANDELBROT_IM_MAX;
			
		    this.z = DEFAULT_MANDELBROT_Z;
		    this.zi = DEFAULT_MANDELBROT_Zi;
					
			zoom_x = imageWidth/(MANDELBROT_RE_MAX - MANDELBROT_RE_MIN);
			zoom_y = imageHeight/(MANDELBROT_IM_MAX - MANDELBROT_IM_MIN);
			break;
		case JULIA:
			JULIA_RE_MIN = DEFAULT_JULIA_RE_MIN;
			JULIA_RE_MAX = DEFAULT_JULIA_RE_MAX;
			JULIA_IM_MIN = DEFAULT_JULIA_IM_MIN;
		    JULIA_IM_MAX = DEFAULT_JULIA_IM_MAX;
		    
		    this.z = DEFAULT_JULIA_Z;
		    this.zi = DEFAULT_JULIA_Zi;
			
			zoom_x = imageWidth/(JULIA_RE_MAX - JULIA_RE_MIN);
			zoom_y = imageHeight/(JULIA_IM_MAX - JULIA_IM_MIN);
			
			break;
		case NEWTON:
			zoom_x = imageWidth/5;
			zoom_y = imageHeight/5;
			break;
		}
		nombreZooms = 0;
		// On réinitialise également le nombre d'itérations
		this.iterationMax = ModeleFractale.getDefaulIterationMax();
		notifier();
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) throws NombreSaisiInterdit {
		
		if (z < -1 || z > 1 )
		{
			throw new NombreSaisiInterdit();
		}
		else
		{
			this.z = z;
			notifier();
		}

	}

	public double getZi() {
		return zi;
	}

	public void setZi(double zi) throws NombreSaisiInterdit{
		if (zi < -1 || zi > 1 )
		{
			throw new NombreSaisiInterdit();
		}
		else
		{
			this.zi = zi;
			notifier();
		}
	}
	
	public int getNombreZooms() {
		return nombreZooms;
	}

	public void setNombreZooms(int nombreZooms) {
		this.nombreZooms = nombreZooms;
		notifier();
	}

	public int getIterationMax() {
		return iterationMax;
	}

	public void setIterationMax(int iterationMax) throws NombreSaisiInterdit{
		if (iterationMax < 0 )
		{
			throw new NombreSaisiInterdit();
		}
		else 
		{
			this.iterationMax = iterationMax;
			notifier();
		}

	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		ModeleFractale.imageWidth = imageWidth;
		notifier();
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		ModeleFractale.imageHeight = imageHeight;
		notifier();
	}

	public double getMANDELBROT_RE_MIN() {
		return MANDELBROT_RE_MIN;
	}

	public void setMANDELBROT_RE_MIN(double mANDELBROT_RE_MIN) {
		this.MANDELBROT_RE_MIN = mANDELBROT_RE_MIN;
		notifier();
	}

	public double getMANDELBROT_RE_MAX() {
		return MANDELBROT_RE_MAX;
	}

	public void setMANDELBROT_RE_MAX(double mANDELBROT_RE_MAX) {
		this.MANDELBROT_RE_MAX = mANDELBROT_RE_MAX;
		notifier();
	}

	public double getMANDELBROT_IM_MIN() {
		return MANDELBROT_IM_MIN;
	}

	public void setMANDELBROT_IM_MIN(double mANDELBROT_IM_MIN) {
		this.MANDELBROT_IM_MIN = mANDELBROT_IM_MIN;
		notifier();
	}

	public double getMANDELBROT_IM_MAX() {
		return MANDELBROT_IM_MAX;
	}

	public void setMANDELBROT_IM_MAX(double mANDELBROT_IM_MAX) {
		this.MANDELBROT_IM_MAX = mANDELBROT_IM_MAX;
		notifier();
	}

	public double getJULIA_RE_MIN() {
		return JULIA_RE_MIN;
	}

	public void setJULIA_RE_MIN(double jULIA_RE_MIN) {
		this.JULIA_RE_MIN = jULIA_RE_MIN;
		notifier();
	}

	public double getJULIA_RE_MAX() {
		return JULIA_RE_MAX;
	}

	public void setJULIA_RE_MAX(double jULIA_RE_MAX) {
		this.JULIA_RE_MAX = jULIA_RE_MAX;
		notifier();
	}

	public double getJULIA_IM_MIN() {
		return JULIA_IM_MIN;
	}

	public void setJULIA_IM_MIN(double jULIA_IM_MIN) {
		this.JULIA_IM_MIN = jULIA_IM_MIN;
		notifier();
	}

	public double getJULIA_IM_MAX() {
		return JULIA_IM_MAX;
	}

	public void setJULIA_IM_MAX(double jULIA_IM_MAX) {
		this.JULIA_IM_MAX = jULIA_IM_MAX;
		notifier();

	}
	
	
	public double distanceAuCentre(double x1, double x2){
		double centre =(x1+x2)/2d; 
		
		return Math.abs(x1 - centre)/10;
		
	}
	
	
	// fonction appel�e par le controleur pour effectuer le zoom
	public void zoomIn() {
		
		// A chaque zoom, on incr�mente le nombre de zooms et le nombre d'it�rations
		this.nombreZooms += 1;
		this.iterationMax += 20;
		
		// Pour les cas Mandelbrot et Julia, on r�duit les bords de la fractale d'une valeur de 1/10 de sa valeur, ce qui zoom en son centre
		// puis on remet � jour la valeur du zoom avec ces nouvelles valeurs
		switch (this.getType()) {
		case MANDELBROT:
			
			double distance_x = distanceAuCentre(MANDELBROT_RE_MIN, MANDELBROT_RE_MAX);
			double distance_y = distanceAuCentre(MANDELBROT_IM_MIN, MANDELBROT_IM_MAX);

			this.MANDELBROT_RE_MIN += distance_x;
			this.MANDELBROT_RE_MAX -= distance_x;
			this.MANDELBROT_IM_MIN += distance_y;
			this.MANDELBROT_IM_MAX -= distance_y;
			
			
			zoom_x = imageWidth/(MANDELBROT_RE_MAX  - MANDELBROT_RE_MIN );
			zoom_y = imageHeight/(MANDELBROT_IM_MAX - MANDELBROT_IM_MIN);
			
			break;
			
		case JULIA:
			
			distance_x = distanceAuCentre(JULIA_RE_MIN, JULIA_RE_MAX);
			distance_y = distanceAuCentre(JULIA_IM_MIN, JULIA_IM_MAX);
			
			this.JULIA_RE_MIN += distance_x ;
			this.JULIA_RE_MAX -= distance_x ;
			this.JULIA_IM_MIN += distance_y;
			this.JULIA_IM_MAX -= distance_y ;

			zoom_x = imageWidth/(JULIA_RE_MAX - JULIA_RE_MIN);
			zoom_y = imageHeight/(JULIA_IM_MAX - JULIA_IM_MIN);
			
			break;
		// dans le cas Newton, on se contente de multiplier la valeur du zoom par 1.5
		case NEWTON:			
			zoom_x = zoom_x *1.5;
			zoom_y = zoom_y * 1.5;
			break;
		default:
			break;
		}
		notifier();
	}

	// fonction appel�e par le controleur pour effectuer le dezoom
	public void zoomOut() {
		
		// Son fonctionnement est similaire � zoomIn, sauf cela se fait dans l'autre sens
		
		this.nombreZooms -= 1;
		this.iterationMax -=20;
		double distance_x = 0d;
		double distance_y = 0d;
		switch (this.getType()) {
		case MANDELBROT:
			
			distance_x = distanceAuCentre(MANDELBROT_RE_MIN, MANDELBROT_RE_MAX);
			distance_y = distanceAuCentre(MANDELBROT_IM_MIN, MANDELBROT_IM_MAX);
			
			this.MANDELBROT_RE_MIN -= distance_x;
			this.MANDELBROT_RE_MAX += distance_x;
			this.MANDELBROT_IM_MIN -= distance_y;
			this.MANDELBROT_IM_MAX += distance_y;

			zoom_x = imageWidth/(MANDELBROT_RE_MAX - MANDELBROT_RE_MIN );
			zoom_y = imageHeight/(MANDELBROT_IM_MAX - MANDELBROT_IM_MIN);
			
			break;
			
		case JULIA:
	
			distance_x = distanceAuCentre(JULIA_RE_MIN, JULIA_RE_MAX);
			distance_y = distanceAuCentre(JULIA_IM_MIN, JULIA_IM_MAX);
			
			this.JULIA_RE_MIN -= distance_x ;
			this.JULIA_RE_MAX += distance_x ;
			this.JULIA_IM_MIN -= distance_y;
			this.JULIA_IM_MAX += distance_y ;

			zoom_x = imageWidth/(JULIA_RE_MAX - JULIA_RE_MIN);
			zoom_y = imageHeight/(JULIA_IM_MAX - JULIA_IM_MIN);		
			break;
			
		case NEWTON:			
			zoom_x = zoom_x /1.5;
			zoom_y = zoom_y / 1.5;
			break;
		default:
			break;
		}
		notifier();	
	}

	
	// Fonction pour d�caler l'image � l'�cran
	public void decaler(double newMouseX, double newMouseY) {
		
		// Dans le cas de Julia et Mandelbrot, on soustrait aux bords de la fractale la valeur de la distance parcourue par 
		// la souris en X et en Y divis� respectivement par la valeur de zoom_x et zoom_y
		switch (this.getType()) {
		case MANDELBROT:
			MANDELBROT_RE_MIN -= (newMouseX/zoom_x);
			MANDELBROT_RE_MAX -= (newMouseX/zoom_x);
			MANDELBROT_IM_MIN -= (newMouseY/zoom_y);
			MANDELBROT_IM_MAX -= (newMouseY/zoom_y);
			
			break;
			
		case JULIA:
			JULIA_RE_MIN-= newMouseX/zoom_x ;
			JULIA_RE_MAX-= newMouseX/zoom_x ;
			JULIA_IM_MIN-= newMouseY/zoom_y ;
			JULIA_IM_MAX-= newMouseY/zoom_y ;
			break;
		// Dans le cas de Newton, on met � jour les variables decalage X et Y qui servent dans le calcul de la fractale	
		case NEWTON:
			decalage_X -= (newMouseX/zoom_x)/8;
			decalage_Y -= (newMouseY/zoom_y)/8;
			break;
			
		default:
			break;
		}
		notifier();

	}

	// Cette fonction remet la fractale � son �tat initial
	public void restart() {
	
		this.iterationMax = ModeleFractale.getDefaulIterationMax();
		this.nombreZooms = 0;
		
		switch (type) {
		case MANDELBROT:		
			this.MANDELBROT_RE_MIN = DEFAULT_MANDELBROT_RE_MIN;
			this.MANDELBROT_RE_MAX = DEFAULT_MANDELBROT_RE_MAX;
			this.MANDELBROT_IM_MIN = DEFAULT_MANDELBROT_IM_MIN;
			this.MANDELBROT_IM_MAX = DEFAULT_MANDELBROT_IM_MAX;
			zoom_x = imageWidth/(MANDELBROT_RE_MAX - MANDELBROT_RE_MIN);
			zoom_y = imageHeight/(MANDELBROT_IM_MAX - MANDELBROT_IM_MIN);
			break;
		case JULIA:		
			this.JULIA_RE_MIN = DEFAULT_JULIA_RE_MIN ;
			this.JULIA_RE_MAX = DEFAULT_JULIA_RE_MAX ;
			this.JULIA_IM_MIN = DEFAULT_JULIA_IM_MIN ;
			this.JULIA_IM_MAX = DEFAULT_JULIA_IM_MAX ;
			zoom_x = imageWidth/(JULIA_RE_MAX - JULIA_RE_MIN);
			zoom_y = imageHeight/(JULIA_IM_MAX - JULIA_IM_MIN);	
			break;
		case NEWTON:
			zoom_x = imageWidth/5;
			zoom_y = imageHeight/5;
			decalage_X = 1;
			decalage_Y = 1;
			break;
		}
		notifier();
		
	}


	public double getDecalage_X() {
		
		return decalage_X;
	}
	
	public double getDecalage_Y() {
		
		return decalage_Y;
	}




}
