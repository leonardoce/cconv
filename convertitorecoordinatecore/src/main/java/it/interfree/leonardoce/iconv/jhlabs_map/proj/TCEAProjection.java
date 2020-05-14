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

/**
 * Added isEqualArea and changed base class to CylindricalProjection
 * by Bernhard Jenny, July 12 2010.
 */
package it.interfree.leonardoce.iconv.jhlabs_map.proj;

import it.interfree.leonardoce.iconv.math.Punto2D;

public class TCEAProjection extends CylindricalProjection {

    private double rk0;

    public TCEAProjection() {
        initialize();
    }

    public Punto2D project(double lplam, double lpphi, Punto2D out) {
        out.x = rk0 * Math.cos(lpphi) * Math.sin(lplam);
        out.y = scaleFactor * (Math.atan2(Math.tan(lpphi), Math.cos(lplam)) - projectionLatitude);
        return out;
    }

    public Punto2D projectInverse(double xyx, double xyy, Punto2D out) {
        double t;

        out.y = xyy * rk0 + projectionLatitude;
        out.x *= scaleFactor;
        t = Math.sqrt(1. - xyx * xyx);
        out.y = Math.asin(t * Math.sin(xyy));
        out.x = Math.atan2(xyx, t * Math.cos(xyy));
        return out;
    }

    public void initialize() { // tcea
        super.initialize();
        rk0 = 1 / scaleFactor;
    }

    public boolean isRectilinear() {
        return false;
    }

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return false;
    }

    public String toString() {
        return "Transverse Cylindrical Equal Area";
    }
}
