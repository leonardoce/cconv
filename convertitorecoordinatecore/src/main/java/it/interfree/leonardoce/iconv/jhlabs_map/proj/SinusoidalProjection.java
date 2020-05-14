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
 * Added isEqualArea by Bernhard Jenny, October 28 2008.
 */

package it.interfree.leonardoce.iconv.jhlabs_map.proj;

import it.interfree.leonardoce.iconv.jhlabs_map.MapMath;
import it.interfree.leonardoce.iconv.math.Punto2D;


public class SinusoidalProjection extends PseudoCylindricalProjection {

    public Punto2D project(double lam, double phi, Punto2D xy) {
        xy.x = lam * Math.cos(phi);
        xy.y = phi;
        return xy;
    }

    public Punto2D projectInverse(double x, double y, Punto2D lp) {
        lp.x = x / Math.cos(y);
        lp.y = y;
        return lp;
    }

    public double getWidth(double y) {
        return MapMath.normalizeLongitude(Math.PI) * Math.cos(y); // FIXME
    }

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Sinusoidal";
    }
}
