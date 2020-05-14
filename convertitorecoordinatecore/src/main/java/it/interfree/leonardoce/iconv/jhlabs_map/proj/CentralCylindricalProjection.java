/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/*
 * This file was semi-automatically converted from the public-domain USGS PROJ source.
 */
package it.interfree.leonardoce.iconv.jhlabs_map.proj;

import it.interfree.leonardoce.iconv.jhlabs_map.MapMath;
import it.interfree.leonardoce.iconv.math.Punto2D;


public class CentralCylindricalProjection extends CylindricalProjection {

	private double ap;

	private final static double EPS10 = 1.e-10;

	public CentralCylindricalProjection() {
		minLatitude = Math.toRadians(-80);
		maxLatitude = Math.toRadians(80);
	}
	
	public Punto2D project(double lplam, double lpphi, Punto2D out) {
		if (Math.abs(Math.abs(lpphi) - MapMath.HALFPI) <= EPS10) throw new ProjectionException("F");
		out.x = lplam;
		out.y = Math.tan(lpphi);
		return out;
	}

	public Punto2D projectInverse(double xyx, double xyy, Punto2D out) {
		out.y = Math.atan(xyy);
		out.x = xyx;
		return out;
	}

	public boolean hasInverse() {
		return true;
	}

	public String toString() {
		return "Central Cylindrical";
	}

}