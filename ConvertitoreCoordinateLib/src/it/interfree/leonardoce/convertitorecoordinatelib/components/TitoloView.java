package it.interfree.leonardoce.convertitorecoordinatelib.components;

import it.interfree.leonardoce.convertitorecoordinatelib.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitoloView extends LinearLayout {
	public TitoloView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		TypedArray t = getContext().obtainStyledAttributes(attrs,R.styleable.TitoloView);
		
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View child = vi.inflate(R.layout.sublayout_titolo, null);
		if (!this.isInEditMode()) {
			addView(child, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
			String titolo = t.getString(R.styleable.TitoloView_titolo);
			if (titolo!=null)
			{
				TextView tv = (TextView)child.findViewById(R.id.textViewTitolo);
				tv.setText(titolo);
			}
		}
	}
}
