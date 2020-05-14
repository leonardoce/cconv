package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.convertitorecoordinatelib.config.CurrentConfiguration;
import it.interfree.leonardoce.iconv.adapters.TipiConversioniAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class ConvertitoreCoordinateActivity extends AppCompatActivity
implements OnItemClickListener{
    private TipiConversioniAdapter adattatore;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.cconv_start);
        adattatore = new TipiConversioniAdapter(this);

        GridView gridview = (GridView) findViewById(R.id.gridViewMenuPrincipale);
        gridview.setAdapter(adattatore);
        gridview.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        if (adattatore.getDestinazione(position)!=null)
        {        
            startActivity(adattatore.getDestinazione(position));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cconv_menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;

        if (item.getItemId() == R.id.menu_help) {
            i = new Intent(this, HelpIconvActivity.class);
            startActivity(i);
            return true;
        } else if(item.getItemId()==R.id.menu_settings ) {
            i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
// vim:tabstop=4:shiftwidth=4:expandtab
