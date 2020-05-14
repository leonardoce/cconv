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
/**
 * Removed isRectilinear (duplicate of super class), changed class name from 
 * PlateCarreeProjection to EquidistantCylindricalProjection, changed 
 * name to "Equidistant Cylindrical (Plate Carr\u00e9e)", added support for
 * latitude of true scale.
 * by Bernhard Jenny, June 26, 2008 and July 13, 2010.
 */
package it.interfree.leonardoce.iconv.jhlabs_map.proj;

import it.interfree.leonardoce.iconv.math.Punto2D;

public class EquidistantCylindricalProjection extends CylindricalProjection {

    private double cosTrueScaleLatitude;

    public Punto2D project(double x, double y, Punto2D dst) {
        dst.x = x * cosTrueScaleLatitude;
        dst.y = y;
        return dst;
    }

    public Punto2D projectInverse(double x, double y, Punto2D lp) {
        lp.y = y;
        lp.x = x / cosTrueScaleLatitude;
        return lp;
    }

    public void initialize() {
        super.initialize();
        cosTrueScaleLatitude = Math.cos(trueScaleLatitude);
        es = 0.;
    }

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Equidistant Cylindrical (Plate Carr\u00e9e)";
    }
}