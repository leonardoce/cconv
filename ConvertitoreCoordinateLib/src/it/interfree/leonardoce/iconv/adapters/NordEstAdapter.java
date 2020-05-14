package it.interfree.leonardoce.iconv.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NordEstAdapter extends BaseAdapter 
{
	public final static int MODO_LATITUDINE=1;
	public final static int MODO_LONGITUDINE=2;
	private int modoCorrente = 0;
	private Context context;
	
	public NordEstAdapter(Context pContext, int pModo)
	{
		context = pContext;
		modoCorrente = pModo;
	}
	
	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Object getItem(int position) 
	{
		if (modoCorrente==MODO_LATITUDINE)
		{
			switch(position)
			{
			case 0:
				return "Nord";
			case 1:
				return "Sud";
			}
		}
		else
		{
			switch(position)
			{
			case 0:
				return "Est";
			case 1:
				return "Ovest";
			}
		}
		
		return "";
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{		
		TextView result;
		
		if (convertView==null)
		{
			result = new TextView(context);
		}
		else
		{
			result = (TextView) convertView; 
		}
		
		result.setText(getItem(position).toString());
		return result;
	}

	public double imponiSegno(int position, double x)
	{
		if(position!=0)
		{
			return -x;
		}
		else
		{
			return x;
		}
	}
}
