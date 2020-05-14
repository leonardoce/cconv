package it.interfree.leonardoce.convertitorecoordinatelib;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import it.interfree.leonardoce.convertitorecoordinatelib.config.CurrentConfiguration;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;

/**
 * This activity will let the user select the preferred settings
 * for his environment
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getPreferenceManager().setSharedPreferencesName(OriginiCassiniManager.ID_PREFERENZE);
        addPreferencesFromResource(R.xml.cconv_prefs);
    }
}
