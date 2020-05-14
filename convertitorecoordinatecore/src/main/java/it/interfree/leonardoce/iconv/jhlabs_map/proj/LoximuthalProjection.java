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
 * Bernhard Jenny, 19 September 2010:
 * Fixed forward and inverse projections, removed unused constants, added validity
 * test for parameter phi1.
 */
package it.interfree.leonardoce.iconv.jhlabs_map.proj;

import it.interfree.leonardoce.iconv.jhlabs_map.MapMath;
import it.interfree.leonardoce.iconv.math.Punto2D;


public class LoximuthalProjection extends PseudoCylindricalProjection {

    private final static double EPS = 1e-8;
    private double phi1;
    private double cosphi1;
    private double tanphi1;

    public LoximuthalProjection() {
        phi1 = Math.toRadians(40.0);//FIXME - param
        cosphi1 = Math.cos(phi1);
        if (cosphi1 < EPS) {
            throw new ProjectionException("-22");
        }
        tanphi1 = Math.tan(MapMath.QUARTERPI + 0.5 * phi1);
    }

    public Punto2D project(double lplam, double lpphi, Punto2D out) {
        double x;
        double y = lpphi - phi1;
        if (Math.abs(y) < EPS) {
            x = lplam * cosphi1;
        } else {
            x = MapMath.QUARTERPI + 0.5 * lpphi;
            if (Math.abs(x) < EPS || Math.abs(Math.abs(x) - MapMath.HALFPI) < EPS) {
                x = 0.;
            } else {
                x = lplam * y / Math.log(Math.tan(x) / tanphi1);
            }
        }
        out.x = x;
        out.y = y;
        return out;
    }

    public Punto2D projectInverse(double xyx, double xyy, Punto2D out) {
        double latitude = xyy + phi1;
        double longitude;
        if (Math.abs(xyy) < EPS) {
            longitude = xyx / cosphi1;
        } else if (Math.abs(longitude = MapMath.QUARTERPI + 0.5 * latitude) < EPS
                || Math.abs(Math.abs(xyx) - MapMath.HALFPI) < EPS) {
            longitude = 0.;
        } else {
            longitude = xyx * Math.log(Math.tan(longitude) / tanphi1) / xyy;
        }

        out.x = longitude;
        out.y = latitude;
        return out;
    }

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Loximuthal";
    }
}