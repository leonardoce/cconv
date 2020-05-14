package it.interfree.leonardoce.iconv.db;

import it.interfree.leonardoce.iconv.core.convs.FusoGauss;

import java.util.List;


/**
 * Implementata dal backend che salva su un database SQLite
 * @author leonardo
 *
 */
public interface IOriginiStorage 
{
	// = Gestione lista delle origini Cassini =
	public List<OrigineCassini> getOrigini();
	public void save(OrigineCassini value);
	public OrigineCassini getOrigine(String nome);
	public void close();
	public void deleteOrigineCassini(String nome);
	public boolean isPredefinita(String nome);
	
	// = Gestione dell'origine selezionata =
	public String getIdOrigineSelezionata();
	public void setIdOrigineSelezionata(String id);
	
	// = Gestione del fuso Gauss selezionato =
	public FusoGauss getFusoGaussSelezionato();
	public void setFusoGaussSelezionato(FusoGauss fuso);

    // = Gestione della Zona UTM =
    public String getDatumUTMSelezionato();
    public void setDatumUTMSelezionato(String datum);

    // = Gestione del log delle conversioni =
	public void addToLogConversioni(RisultatoConversione risultato);
	public void updateLogConversioni(RisultatoConversione risultato);
	public void clearLogConversioni();
	public List<RisultatoConversione> getLog();
	public void deleteRigaLog(int id);
}
