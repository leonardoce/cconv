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
 * Bernhard Jenny, May 07: split Hammer and Eckert-Greifendorff projections.
 */
package it.interfree.leonardoce.iconv.jhlabs_map.proj;

import it.interfree.leonardoce.iconv.math.Punto2D;

public class HammerProjection extends PseudoCylindricalProjection {

    private final double w = 0.5;
    private double m = 1;
    private double rm;

    public HammerProjection() {
    }

    public Punto2D project(double lplam, double lpphi, Punto2D xy) {
        double cosphi, d;

        d = Math.sqrt(2. / (1. + (cosphi = Math.cos(lpphi)) * Math.cos(lplam *= w)));
        xy.x = m * d * cosphi * Math.sin(lplam);
        xy.y = rm * d * Math.sin(lpphi);
        return xy;
    }

    public void initialize() {
        super.initialize();
        if ((m = Math.abs(m)) <= 0.) {
            throw new ProjectionException("-27");
        } else {
            m = 1.;
        }
        rm = 1. / m;
        m /= w;
        es = 0.;
    }

    /**
     * Returns true if this projection is equal area
     */
    public boolean isEqualArea() {
        return true;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getM() {
        return m;
    }

    public String toString() {
        return "Hammer";
    }
}
