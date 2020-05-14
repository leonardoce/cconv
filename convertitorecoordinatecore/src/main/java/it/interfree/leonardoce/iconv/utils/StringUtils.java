package it.interfree.leonardoce.iconv.utils;


public class StringUtils
{
	public static double string2real(String value)
	{
		value = value.replace(',', '.');
		try
		{
			return Double.parseDouble(value);
		} catch (Exception exc)
		{
			// Non era buono!
			return 0;
		}
	}
	
	public static String null2string (String s)
	{
		return s==null?"":s;
	}
	
	/**
	 * Prepara la stringa per essere inclusa in un file CSV
	 * @param s Stringa da preparare
	 * @return
	 */
	public static String toSafeCsv (String s)
	{
		if (s==null)
		{
			return "";
		}
		
		if (s.indexOf('\"')!=-1)
		{
			s.replace("\"", "\"\"");
		}
		
		if (s.indexOf(',')!=-1)
		{
			s = "\"" + s + "\"";
		}
		
		return s;
	}
	
	/**
	 * Avanza le lettere prendendole da un vettore
	 * @param lettere Lettere da utilizzare
	 * @param lettera Lettera da avanzare
	 * @param step Quantita' di passi
	 * @return
	 */
	public static char avanzaLettera(char[] lettere, char lettera, int step)
	{
		int idx = -1;
		
		for(int i=0; i<lettere.length; i++)
		{
			if (lettere[i]==lettera)
			{
				idx = i;
				break;
			}
		}
		
		if (idx==-1)
		{
			throw new IllegalArgumentException("Lettera non valida");
		}
		
		idx += step;
		idx = idx % lettere.length;
		
		return lettere[idx];
	}
	
	public static int differenzaLettere(char[] lettere, char letteraDue, char letteraUno)
	{
		int idxUno = -1;
		int idxDue = -1;
		
		for(int i=0; i<lettere.length; i++)
		{
			if (lettere[i]==letteraDue)
			{
				idxDue = i;
			}

			if (lettere[i]==letteraUno)
			{
				idxUno = i;
			}
		}
		
		if (idxUno==-1 || idxDue==-1)
		{
			throw new IllegalArgumentException("Lettera non valida");
		}
		
		
		if (idxDue<idxUno)
		{
			idxDue += lettere.length;
		}
			
		return idxDue-idxUno;
	}
	
	/**
	 * Cerca un carattere in un vettore di caratteri. Se lo trova rende l'indice
	 * altrementi -1
	 * @param lettere
	 * @param lettera
	 * @return
	 */
	public static int indiceInVettore(char[] lettere, char lettera)
	{
		int idx = -1;
		
		for(int i=0; i<lettere.length; i++)
		{
			if (lettere[i]==lettera)
			{
				idx = i;
				break;
			}
		}

		return idx;
	}
	
}
