package it.interfree.leonardoce.iconv.db;

import it.interfree.leonardoce.iconv.math.Punto3D;


public class OrigineCassini 
{
	private String descrizioneString;
	private Punto3D fix;
	
	public String getDescrizioneString() {
		return descrizioneString;
	}
	public void setDescrizioneString(String descrizioneString) {
		this.descrizioneString = descrizioneString;
	}
	public Punto3D getFix() {
		return fix;
	}
	public void setFix(Punto3D fix) {
		this.fix = fix;
	}
	public OrigineCassini(String descrizioneString, Punto3D fix) {
		super();
		this.descrizioneString = descrizioneString;
		this.fix = fix;
	}
	
	
}
