package edu.sc.seis.fissuresUtil.bag;

/**
 * Taper.java
 *
 *
 * Created: Sat Oct 19 21:53:21 2002
 *
 * @author <a href="mailto:www@seis.sc.edu">Philip Crotwell</a>
 * @version $Id: Taper.java 2783 2002-10-21 00:00:16Z crotwell $
 */

public class Taper {
    public Taper (){
	this(0.05f);
    }

    public Taper (float width){
	this(HANNING, width);
    }

    public Taper (int type, float width){
	this.type = type;
	this.width = width;
    }



    public void apply(float[] data) {
	int w = Math.round(data.length*width);

	double[] coeff = getCoefficients(w);
	double omega = coeff[0];
	double f0 = coeff[1];
	double f1 = coeff[2];
	for (int i=0; i < w ; i++) {
	    data[i] = (float)(data[i] * (f0 - f1 * Math.cos(omega*i))); 
	    data[data.length-i] = 
		(float)(data[data.length-i] * (f0 - f1 * Math.cos(omega*i))); 
	} // end of for (int i=0; i<data.length; i++)
    }

    public void apply(int[] data) {
	int w = Math.round(data.length*width);

	double[] coeff = getCoefficients(w);
	double omega = coeff[0];
	double f0 = coeff[1];
	double f1 = coeff[2];
	for (int i=0; i < w ; i++) {
	    data[i] = 
		(int)Math.round(data[i] * (f0 - f1 * Math.cos(omega*i))); 
	    data[data.length-i] = 
		(int)Math.round(data[data.length-i] * (f0 - f1 * Math.cos(omega*i))); 
	} // end of for (int i=0; i<data.length; i++)
    }
    
    /** Calculates the coefficients for tapering, omega, f0,f1
     */
    double[] getCoefficients(int length) {
	double[] out = new double[3];
	if (type == HANNING) {
	    out[0] = Math.PI/length;
	    out[1] = .5f;
	    out[2] = .5f;
	} else if (type == HANNING) {
	    out[0] = Math.PI/length;
	    out[1] = .54f;
	    out[2] = .46f;
	} else {
	    // cosine
	    out[0] = Math.PI/2/length;
	    out[1] = 1;
	    out[2] = 1;
	}
	return out;
    }

    public static int HANNING = 0;

    public static int HAMMING = 1;

    public static int COSINE = 2;

    float width;

    int type;



}// Taper
