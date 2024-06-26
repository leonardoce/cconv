/*
Copyright 2010 Bernhard Jenny

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
package it.interfree.leonardoce.iconv.jhlabs_map.proj;

import it.interfree.leonardoce.iconv.math.Punto2D;

/**
 * Apian I Globular projection.
 * Code from proj4.
 * @author Bernhard Jenny, Institute of Cartography, ETH Zurich
 */
public class Apian1Projection extends Projection {

    private static final double HLFPI2 = 2.46740110027233965467;
    private static final double EPS = 1e-10;

    public Apian1Projection() {
    }

    public Punto2D project(double lplam, double lpphi, Punto2D out) {

        double ax = Math.abs(lplam);
        out.y = lpphi;
        if (ax >= EPS) {
            double f = 0.5 * (HLFPI2 / ax + ax);
            out.x = ax - f + Math.sqrt(f * f - lpphi * lpphi);
            if (lplam < 0.) {
                out.x = -out.x;
            }
        } else {
            out.x = 0.;
        }
        return out;
    }

    @Override
    public Punto2D projectInverse(double x, double y, Punto2D out) {
        binarySearchInverse(x, y, out);
        return out;
    }

    @Override
    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Apian Globular I";
    }
}
