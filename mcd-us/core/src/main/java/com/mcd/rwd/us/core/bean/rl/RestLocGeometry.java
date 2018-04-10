
package com.mcd.rwd.us.core.bean.rl;
/**
 * Created by srishma yarra 
 */
public class RestLocGeometry {

	private double[] coordinates;

	public double[] getCoordinates() {
		return coordinates.clone();
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates.clone();
	}

}
