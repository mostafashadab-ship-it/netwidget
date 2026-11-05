package com.example.netwidget;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class TapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences(NetWidgetProvider.PREFS, Context.MODE_PRIVATE);
        int mode = sp.getInt(NetWidgetProvider.KEY_MODE, 0);

        // Tell the user which option to pick now, then flip for the next tap.
        Toast.makeText(this,
                mode == 0 ? "Select: NR/LTE (5G/4G)" : "Select: NR/LTE/GSM/WCDMA (5G/4G/3G/2G)",
                Toast.LENGTH_LONG).show();

        sp.edit().putInt(NetWidgetProvider.KEY_MODE, mode == 0 ? 1 : 0).apply();
        NetWidgetProvider.refreshAll(this);

        openNetworkModeScreen();
        finish();
    }

    private void openNetworkModeScreen() {
        // Hidden radio info screen: has the "Set preferred network type" spinner.
        try {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setComponent(new ComponentName("com.android.phone", "com.android.phone.settings.RadioInfo"));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return;
        } catch (Exception ignored) {
        }
        try {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClassName("com.android.settings", "com.android.settings.RadioInfo");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return;
        } catch (Exception ignored) {
        }
        try {
            Intent i = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open network settings", Toast.LENGTH_LONG).show();
        }
    }
}
