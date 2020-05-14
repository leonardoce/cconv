package it.interfree.leonardoce.iconv.math;

import java.util.Formatter;

public class PuntoMGRS extends Punto3D {
	private int zonaUtm;
	private char banda;
	private String quadrante;
	
	public int getZonaUtm() {
		return zonaUtm;
	}
	public void setZonaUtm(int zonaUtm) {
		this.zonaUtm = zonaUtm;
	}
	public char getBanda() {
		return banda;
	}
	public void setBanda(char banda) {
		this.banda = banda;
	}
	public String getQuadrante() {
		return quadrante;
	}
	public void setQuadrante(String quadrante) {
		this.quadrante = quadrante;
	}
	
	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer();
		
		Formatter formatter = new Formatter(sb);
		formatter.format("%02d %c %s %05d %05d", zonaUtm, banda, quadrante, (int)x, (int)y);
		formatter.close();
		
		return sb.toString();
	}
	
}
