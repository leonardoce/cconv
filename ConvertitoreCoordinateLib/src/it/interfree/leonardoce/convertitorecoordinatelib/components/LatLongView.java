package it.interfree.leonardoce.convertitorecoordinatelib.components;

import it.interfree.leonardoce.convertitorecoordinatelib.R;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.utils.StringUtils;

import java.text.DecimalFormat;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class LatLongView extends LinearLayout 
{
	private EditText editLatitudine1;
	private EditText editLatitudine2;
	private EditText editLatitudine3;
	private Spinner spinnerNord;
	private ArrayAdapter<String> adapterNord;
	
	private EditText editLongitudine1;
	private EditText editLongitudine2;
	private EditText editLongitudine3;
	private Spinner spinnerEst;
	private ArrayAdapter<String> adapterEst;
	
	private RadioButton rbSistemaGradi;
	private RadioButton rbSistemaGradiDecimali;
	private RadioButton rbSistemaMinutiDecimali;
    private RadioButton rbSistemaQTH;

    private TextView textViewLatitude;

	public LatLongView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View child = vi.inflate(R.layout.sublayout_latlong, null);
		addView(child, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		// -------------------
		// Prende i componenti
		// -------------------
		
		editLatitudine1 = (EditText) findViewById(R.id.editLatitudine1);
		editLatitudine2 = (EditText) findViewById(R.id.editLatitudine2);
		editLatitudine3 = (EditText) findViewById(R.id.editLatitudine3);
		spinnerNord = (Spinner) findViewById(R.id.spinnerNord);
		adapterNord = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, 
			new String [] {context.getString(R.string.nord), context.getString(R.string.sud)}
		);
		adapterNord.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		editLongitudine1 = (EditText) findViewById(R.id.editLongitudine1);
		editLongitudine2 = (EditText) findViewById(R.id.editLongitudine2);
		editLongitudine3 = (EditText) findViewById(R.id.editLongitudine3);
		spinnerEst = (Spinner) findViewById(R.id.spinnerEst);
		adapterEst = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, 
				new String [] {context.getString(R.string.est), context.getString(R.string.ovest)}
			);
		adapterEst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		rbSistemaGradi = (RadioButton) findViewById(R.id.rbSistemaGradi);
		rbSistemaGradiDecimali = (RadioButton) findViewById(R.id.rbSistemaGradiDecimali);
		rbSistemaMinutiDecimali = (RadioButton) findViewById(R.id.rbSistemaMinutiDecimali);
        rbSistemaQTH = (RadioButton) findViewById(R.id.rbSistemaQTH);

        textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);

		rbSistemaGradi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1)
				{
					impostaSistemaGradi();
				}
			}
		});
		
		rbSistemaGradiDecimali.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1)
				{
					impostaSistemaGradiDecimali();
				}
			}
		});

		rbSistemaMinutiDecimali.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1)
				{
					impostaSistemaMinutiDecimali();
				}
			}
		});

        rbSistemaQTH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1)
                {
                    impostaSistemaQTH();
                }
            }
        });

		// -------------------------------
		// Inizializzazione dei componenti
		// -------------------------------

		rbSistemaGradi.setChecked(true);
		impostaSistemaGradi();
		
		spinnerNord.setAdapter(adapterNord);
		spinnerEst.setAdapter(adapterEst);
	}
	
	public Punto3D getPunto() 
	{	
		// Prende i dati dalla maschera
		double longitude;
		double latitude;
		
		if (rbSistemaGradi.isChecked())
		{
			longitude = GeodesicUtils.degreeToDecimal(
				StringUtils.string2real(editLongitudine1.getText().toString()),
				StringUtils.string2real(editLongitudine2.getText().toString()),
				StringUtils.string2real(editLongitudine3.getText().toString())
			);
			
			latitude = GeodesicUtils.degreeToDecimal(
				StringUtils.string2real(editLatitudine1.getText().toString()),
				StringUtils.string2real(editLatitudine2.getText().toString()),
				StringUtils.string2real(editLatitudine3.getText().toString())
			);			
		}
		else if (rbSistemaMinutiDecimali.isChecked())
		{
			longitude = GeodesicUtils.degreeToDecimal(
				StringUtils.string2real(editLongitudine1.getText().toString()),
				StringUtils.string2real(editLongitudine2.getText().toString())
			);
			
			latitude = GeodesicUtils.degreeToDecimal(
				StringUtils.string2real(editLatitudine1.getText().toString()),
				StringUtils.string2real(editLatitudine2.getText().toString())
			);
		}
        else if (rbSistemaQTH.isChecked() )
        {


            /**
             This code is based on a Perl code that I found in Wikipedia in the
             article for the Maidenhead Locator System
             */
            String grid = editLatitudine1.getText().toString().toUpperCase();

            if ( grid.length()>=4 ) {
                longitude = (grid.charAt(0) - 'A') * 20 - 180;
                latitude = (grid.charAt(1) - 'A') * 10 - 90;
                longitude += (grid.charAt(2)  - '0') * 2;
                latitude += (grid.charAt(3) - '0') * 1;

                if (grid.length() >= 6) {
                    // have subsquares
                    longitude += (grid.charAt(4) - 'A') * 5/60.0;
                    latitude += (grid.charAt(5) - 'A') * 2.5/60.0;
                    // move to center of subsquare
                    longitude += 2.5/60;
                    latitude += 1.25/60;
                } else {
                    // move to center of square
                    longitude += 1;
                    latitude += 0.5;
                }
            } else {
                latitude = 0;
                longitude = 0;
            }
        }
        else
        {
			latitude = StringUtils.string2real(editLatitudine1.getText().toString());
			longitude = StringUtils.string2real(editLongitudine1.getText().toString());
		}
		
		if (!rbSistemaQTH.isChecked() && spinnerNord.getSelectedItemPosition()==1)
		{
			latitude*=-1;
		}
		if (!rbSistemaQTH.isChecked() && spinnerEst.getSelectedItemPosition()==1)
		{
			longitude*=-1;
		}

		return new Punto3D(longitude, latitude, 0);		
	}
	
	public void setPunto(Punto3D value)
	{
		if (value==null)
		{
			return;
		}
		
		double lat = value.y;
		double longi = value.x;
		
		double aLat[] = GeodesicUtils.decimalToDegree(Math.abs(lat));
		double aLong[] = GeodesicUtils.decimalToDegree(Math.abs(longi));
		
		DecimalFormat dec = new DecimalFormat("#0.00");
		DecimalFormat round = new DecimalFormat("#0");
		
		editLatitudine1.setText(round.format(aLat[0]));
		editLatitudine2.setText(round.format(aLat[1]));
		editLatitudine3.setText(dec.format(aLat[2]));
		spinnerNord.setSelection(lat>0?0:1);
		
		editLongitudine1.setText(round.format(aLong[0]));
		editLongitudine2.setText(round.format(aLong[1]));
		editLongitudine3.setText(dec.format(aLong[2]));
		spinnerEst.setSelection(longi>0?0:1);
	}
	
	public void reset()
	{
		editLatitudine1.setText("");
		editLatitudine2.setText("");
		editLatitudine3.setText("");
		spinnerNord.setSelection(0);
		
		editLongitudine1.setText("");
		editLongitudine2.setText("");
		editLongitudine3.setText("");
		spinnerEst.setSelection(0);
		
		rbSistemaGradi.setChecked(true);
		impostaSistemaGradi();
	}
	
	public void impostaSistemaGradi()
	{
		editLatitudine1.setInputType(InputType.TYPE_CLASS_NUMBER);
		editLatitudine2.setInputType(InputType.TYPE_CLASS_NUMBER);
		editLatitudine3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editLatitudine1.setVisibility(VISIBLE);
		editLatitudine2.setVisibility(VISIBLE);
		editLatitudine3.setVisibility(VISIBLE);
		editLatitudine1.setHint(R.string.input_latlong_dd);
		editLatitudine2.setHint(R.string.input_latlong_mm);
		editLatitudine3.setHint(R.string.input_latlong_ss);
	
		editLongitudine1.setInputType(InputType.TYPE_CLASS_NUMBER);
		editLongitudine2.setInputType(InputType.TYPE_CLASS_NUMBER);
		editLongitudine3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editLongitudine1.setVisibility(VISIBLE);
		editLongitudine2.setVisibility(VISIBLE);
		editLongitudine3.setVisibility(VISIBLE);
		editLongitudine1.setHint(R.string.input_latlong_dd);
		editLongitudine2.setHint(R.string.input_latlong_mm);
		editLongitudine3.setHint(R.string.input_latlong_ss);
		
		editLatitudine1.setNextFocusDownId(R.id.editLatitudine2);
		editLatitudine2.setNextFocusDownId(R.id.editLatitudine3);
		editLatitudine2.setNextFocusUpId(R.id.editLatitudine1);
		editLatitudine3.setNextFocusDownId(R.id.editLongitudine1);
		editLatitudine3.setNextFocusUpId(R.id.editLatitudine2);
		editLongitudine1.setNextFocusDownId(R.id.editLongitudine2);
		editLongitudine1.setNextFocusUpId(R.id.editLongitudine1);
		editLongitudine2.setNextFocusDownId(R.id.editLongitudine3);
		editLongitudine2.setNextFocusUpId(R.id.editLongitudine1);
		editLongitudine3.setNextFocusDownId(R.id.spinnerNord);
		editLongitudine3.setNextFocusUpId(R.id.editLongitudine2);

        spinnerNord.setVisibility(VISIBLE);
        spinnerEst.setVisibility(VISIBLE);

        textViewLatitude.setText(R.string.lay_latitudine);
	}
	
	public void impostaSistemaGradiDecimali()
	{
		
		editLatitudine1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editLatitudine1.setVisibility(VISIBLE);
		editLatitudine2.setVisibility(GONE);
		editLatitudine3.setVisibility(GONE);
		editLatitudine1.setHint(R.string.sistema_gradi_decimali);
		
		editLongitudine1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editLongitudine1.setVisibility(VISIBLE);
		editLongitudine2.setVisibility(GONE);
		editLongitudine3.setVisibility(GONE);
		editLongitudine1.setHint(R.string.sistema_gradi_decimali);
		
		editLatitudine1.setNextFocusDownId(R.id.editLongitudine1);
		editLongitudine1.setNextFocusDownId(R.id.spinnerNord);
		editLongitudine1.setNextFocusUpId(R.id.editLatitudine1);

        spinnerNord.setVisibility(VISIBLE);
        spinnerEst.setVisibility(VISIBLE);

        textViewLatitude.setText(R.string.lay_latitudine);
	}

	public void impostaSistemaMinutiDecimali() 
	{
		editLatitudine1.setInputType(InputType.TYPE_CLASS_NUMBER);
		editLatitudine2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editLatitudine1.setVisibility(VISIBLE);
		editLatitudine2.setVisibility(VISIBLE);
		editLatitudine3.setVisibility(GONE);
		editLatitudine1.setHint(R.string.input_latlong_dd);
		editLatitudine2.setHint(R.string.sistema_minuti_decimali);
		editLatitudine3.setHint(R.string.input_latlong_ss);

		editLongitudine1.setInputType(InputType.TYPE_CLASS_NUMBER);
		editLongitudine2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editLongitudine1.setVisibility(VISIBLE);
		editLongitudine2.setVisibility(VISIBLE);
		editLongitudine3.setVisibility(GONE);
		editLongitudine1.setHint(R.string.input_latlong_dd);
		editLongitudine2.setHint(R.string.sistema_minuti_decimali);
		editLongitudine3.setHint(R.string.input_latlong_ss);
	
		editLatitudine1.setNextFocusDownId(R.id.editLatitudine2);
		editLatitudine2.setNextFocusDownId(R.id.editLongitudine1);
		editLongitudine1.setNextFocusDownId(R.id.editLongitudine2);
		editLongitudine2.setNextFocusDownId(R.id.spinnerNord);

		editLatitudine2.setNextFocusUpId(R.id.editLatitudine1);
		editLongitudine1.setNextFocusUpId(R.id.editLatitudine2);
		editLongitudine2.setNextFocusUpId(R.id.editLongitudine1);

        spinnerNord.setVisibility(VISIBLE);
        spinnerEst.setVisibility(VISIBLE);

        textViewLatitude.setText(R.string.lay_latitudine);
	}

    public void impostaSistemaQTH()
    {
        editLatitudine1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editLatitudine1.setVisibility(VISIBLE);
        editLatitudine2.setVisibility(GONE);
        editLatitudine3.setVisibility(GONE);
        editLatitudine1.setHint(R.string.sistema_qth);

        editLongitudine1.setVisibility(GONE);
        editLongitudine2.setVisibility(GONE);
        editLongitudine3.setVisibility(GONE);

        spinnerNord.setVisibility(GONE);
        spinnerEst.setVisibility(GONE);

        textViewLatitude.setText(R.string.lay_grid);
    }

    public boolean isCorretto() {
        if ( !rbSistemaQTH.isChecked() ) {
            return  true;
        }

        String val = editLatitudine1.getText().toString();

        if ( val.length()==4 || val.length()==6 ) {
            return  true;
        } else {
            return  false;
        }
    }
}
