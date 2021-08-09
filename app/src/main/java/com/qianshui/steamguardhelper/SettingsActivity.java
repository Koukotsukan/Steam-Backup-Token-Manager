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


//            SwitchPreferenceCompat progress_move = findPreference("progress_move");
//            progress_move.setOnPreferenceChangeListener((preference, newValue) -> {
//                return true;
//            });
//            progress_move.setOnPreferenceChangeListener((preference, newValue) -> {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("提示");
//                builder.setMessage("更改此选项需要重启应用，请确认是否继续？");
//                builder.setPositiveButton("否", null);
//                builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent mStartActivity = new Intent(getContext(), MainActivity.class);
//                        int mPendingIntentId = 123456;
//                        PendingIntent mPendingIntent = PendingIntent.getActivity(getContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//                        AlarmManager mgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
//                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//                        System.exit(0);
//                    }
//                });
//                builder.show();
//                return false;
//            });


            purge_data.setOnPreferenceClickListener(preference1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
                builder.setTitle("警告");
                builder.setMessage("你即将清除本地存储的所有令牌，但没有使用过的令牌仍然能够正常使用，请确认是否删除？");
                builder.setPositiveButton("否", null);
                builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clear(getContext());
                        CharSequence text = "清除成功";
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



            app_version.setSummary("当前版本号：v" + getAppVersionName(getContext()));


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