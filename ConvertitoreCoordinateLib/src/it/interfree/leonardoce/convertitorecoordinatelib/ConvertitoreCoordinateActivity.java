package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.convertitorecoordinatelib.config.CurrentConfiguration;
import it.interfree.leonardoce.iconv.adapters.TipiConversioniAdapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ConvertitoreCoordinateActivity extends AppCompatActivity
implements OnItemClickListener{
    private TipiConversioniAdapter adattatore;
    private int lastChoosenPosition;

    // This are the permissions we require to run correctly
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    private ActivityResultLauncher<String[]> permissionsLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.cconv_start);
        adattatore = new TipiConversioniAdapter(this);

        GridView gridview = (GridView) findViewById(R.id.gridViewMenuPrincipale);
        gridview.setAdapter(adattatore);
        gridview.setOnItemClickListener(this);

        permissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Log.d("PERMISSIONS", "arrivo 1");
                    Boolean fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    Log.d("PERMISSIONS", result.toString());
                    if (fineLocationGranted == null || !fineLocationGranted) {
                        Log.d("PERMISSIONS", "The user denied the permission request ACCESS_FINE_LOCATION");
                        finish();
                        return;
                    }

                    if (coarseLocationGranted == null || !coarseLocationGranted) {
                        Log.d("PERMISSIONS", "The user denied the permission request ACCESS_COARSE_LOCATION");
                        finish();
                        return;
                    }

                    startActivity(adattatore.getDestinazione(lastChoosenPosition));
                }
        );
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Se non ci siamo, non ci siamo
        if (adattatore.getDestinazione(position) == null) {
            return;
        }

        if (!adattatore.getRichiedeGPS(position) ) {
            startActivity(adattatore.getDestinazione(position));
            return;
        }

        // Se ho i permessi, e' tutto a posto
        if(hasPermissions()) {
            startActivity(adattatore.getDestinazione(position));
            return;
        }

        // Non ho i permessi, allora li chiedo
        // Request for permissions
        lastChoosenPosition = position;
        permissionsLauncher.launch(PERMISSIONS);
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

    private boolean hasPermissions() {
        for (String permission : PERMISSIONS) {
            var result = ActivityCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }
}
// vim:tabstop=4:shiftwidth=4:expandtab
