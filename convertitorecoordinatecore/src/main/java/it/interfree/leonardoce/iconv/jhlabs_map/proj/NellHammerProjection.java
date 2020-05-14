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
 *
 * Nell-Hammer is a mean of the Cylindical Equal-Area and the Sinusoidal.
 * PROJ.4 contains code for weighted means. Here n = 0.5
 *
 * Bernhard Jenny, July 2007:
 * Changed name from NellHProjection, changed superclass to 
 * PseudoCylindricalProjection.
 *
 * Bernhard Jenny, 19 September 2010: Fixed inverse projection.
 * 
 */
package it.interfree.leonardoce.iconv.jhlabs_map.proj;

import it.interfree.leonardoce.iconv.jhlabs_map.MapMath;
import it.interfree.leonardoce.iconv.math.Punto2D;


public class NellHammerProjection extends PseudoCylindricalProjection {

    private final static int NITER = 9;
    private final static double EPS = 1e-7;

    public Punto2D project(double lplam, double lpphi, Punto2D out) {
        out.x = 0.5 * lplam * (1. + Math.cos(lpphi));
        out.y = 2.0 * (lpphi - Math.tan(0.5 * lpphi));
        return out;
    }

    public Punto2D projectInverse(double xyx, double xyy, Punto2D out) {
        double V, c, p;
        int i;
        
        p = 0.5 * xyy;
        double phi = 0; // PROJ.4 does not implicitly initialize phi!
        for (i = NITER; i > 0; --i) {
            c = Math.cos(0.5 * phi);
            phi -= V = (phi - Math.tan(phi / 2) - p) / (1. - 0.5 / (c * c));
            if (Math.abs(V) < EPS) {
                break;
            }
        }
        if (i == 0) {
            out.y = p < 0. ? -MapMath.HALFPI : MapMath.HALFPI;
            out.x = 2. * xyx;
        } else {
            out.x = 2. * xyx / (1. + Math.cos(phi));
            out.y = phi;
        }
        return out;
    }

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Nell-Hammer";
    }
}
