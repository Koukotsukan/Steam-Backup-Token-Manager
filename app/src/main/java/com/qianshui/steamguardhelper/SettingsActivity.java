package com.qianshui.steamguardhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTheme(R.style.SettingsFragmentStyle);


    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference purge_data = findPreference("purge_data");
            Preference app_version = findPreference("app_version");
            Preference rate_app = findPreference("rate_app");

            purge_data.setOnPreferenceClickListener(preference1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
                builder.setTitle(this.getString(R.string.warn));
                builder.setMessage(this.getString(R.string.warn_content));
                builder.setPositiveButton(this.getString(R.string.no), null);
                builder.setNegativeButton(this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clear(getContext());
                        CharSequence text = getContext().getString(R.string.purge_done);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getActivity(), text, duration);
                        toast.show();
                    }
                });
                builder.show();
                return true;
            });

            rate_app.setOnPreferenceClickListener(preference -> {
                Uri uri = Uri.parse("https://www.coolapk.com/apk/286904");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            });



            app_version.setSummary(this.getString(R.string.current_version) + getAppVersionName(getContext()));


        }
    }

    public static void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("codeList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}