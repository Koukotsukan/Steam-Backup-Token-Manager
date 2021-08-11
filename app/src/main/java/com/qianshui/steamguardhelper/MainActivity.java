package com.qianshui.steamguardhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String current_code;
    public List<String> codeList;
    TextView import_code;
    TextView settings;
    TextView tutorial;
    TextView about;
    Button copy;
    Button share;
    Button delete;
    Button extend;
    ProgressBar progressBar;
    TextView code;
    TextView remain;
    boolean codeStatus;
    boolean deny_progressBar;
    boolean deny_screenshot;
    boolean allow_copy;
    CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        codeList = new ArrayList<>();
        import_code = (TextView) findViewById(R.id.import_code);
        settings = (TextView) findViewById(R.id.settings);
        tutorial = (TextView) findViewById(R.id.tutorial);
        about = (TextView) findViewById(R.id.about);
        copy = (Button) findViewById(R.id.copy);
        share = (Button) findViewById(R.id.share);
        delete = (Button) findViewById(R.id.delete);
        extend = (Button) findViewById(R.id.extend);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        current_code = "";
        code = (TextView) findViewById(R.id.code);
        remain = (TextView) findViewById(R.id.remain);
        codeStatus = false;

        mCountDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int currentProgress = progressBar.getProgress();
                if (currentProgress <= 10) {
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                    code.setTextColor(Color.parseColor("#ff0000"));
                }
                currentProgress--;
                progressBar.setProgress(currentProgress);
            }

            @Override
            public void onFinish() {
                if (!deny_progressBar) {
                    code.setText(getApplicationContext().getString(R.string.unlock_to_get));
                    current_code = "";
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#3289BE")));
                    code.setTextColor(Color.parseColor("#ffffff"));
                    progressBar.setProgress(60);
                    codeStatus = false;
                }
            }
        };

        import_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(MainActivity.this, ImportActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(MainActivity.this, TutorialActivity.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!current_code.equals("")) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setText(current_code);
                    Context context = getApplicationContext();
                    CharSequence text = context.getString(R.string.copy_done);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                    vibrator.vibrate(40);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = context.getString(R.string.null_result);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!current_code.equals("")) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getApplicationContext().getString(R.string.share_content_part_1) + current_code.substring(0, 7) + getApplicationContext().getString(R.string.share_content_part_2));
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent, getApplicationContext().getString(R.string.share_to)));
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = context.getString(R.string.null_result);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (codeList.size()!=0) {
//                    if (codeStatus) {
//                        progressBar.setProgress(60);
//                        current_code = codeList.get(0);
//                        codeList.remove(0);
//                        SharedPreferences.Editor editor = getSharedPreferences("codeList", MODE_PRIVATE).edit();
//                        editor.putInt("codeNumbers", codeList.size());
//                        for (int i = 0; i < codeList.size(); i++) {
//                            editor.putString("item_" + i, codeList.get(i));
//                        }
//                        editor.commit();
//
//                        code.setText(current_code);
//                        remain.setText("剩余容量: " + codeList.size());
//                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#3289BE")));
//                        code.setTextColor(Color.parseColor("#ffffff"));
//                        mCountDownTimer.cancel();
//                        mCountDownTimer.start();
//                    }
//                }else{
                code.setText(getApplicationContext().getString(R.string.unlock_to_get));
                current_code = "";
                codeStatus = false;
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#3289BE")));
                code.setTextColor(Color.parseColor("#ffffff"));
                progressBar.setProgress(60);
//                Context context = getApplicationContext();
//                CharSequence text = "当前没有数据";
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(context, text, duration);
////                    toast.show();
                mCountDownTimer.cancel();
//                }
            }
        });

        extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeStatus) {
                    progressBar.setProgress(60);
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#3289BE")));
                    code.setTextColor(Color.parseColor("#ffffff"));
                    mCountDownTimer.cancel();
                    mCountDownTimer.start();
                }
            }
        });

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeList.size() != 0) {
                    if (!codeStatus) {
                        progressBar.setProgress(60);
                        codeStatus = true;
                        current_code = codeList.get(0);
                        codeList.remove(0);
                        SharedPreferences.Editor editor = getSharedPreferences("codeList", MODE_PRIVATE).edit();
                        editor.putInt("codeNumbers", codeList.size());
                        for (int i = 0; i < codeList.size(); i++) {
                            editor.putString("item_" + i, codeList.get(i));
                        }
                        editor.commit();
                        if (allow_copy) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            clipboard.setText(current_code);
                            Context context = getApplicationContext();
                            CharSequence text = context.getString(R.string.copy_done);
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        code.setText(current_code);
                        remain.setText(getApplicationContext().getString(R.string.remained_code) + codeList.size());
                        mCountDownTimer.start();

                    }
                } else {
                    if (!codeStatus) {
                        Context context = getApplicationContext();
                        CharSequence text = context.getString(R.string.no_data);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            }
        });

        code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (codeStatus) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setText(current_code);
                    Context context = getApplicationContext();
                    CharSequence text = context.getString(R.string.copy_done);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                    vibrator.vibrate(40);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        codeList.clear();
        SharedPreferences preferencesDataList = getSharedPreferences("codeList", MODE_PRIVATE);
        int codeNumbers = preferencesDataList.getInt("codeNumbers", 0);
        for (int i = 0; i < codeNumbers; i++) {
            String code = preferencesDataList.getString("item_" + i, null);
            codeList.add(code);
        }
        remain.setText(getApplicationContext().getString(R.string.remained_code) + codeList.size());


        SharedPreferences progress_move = PreferenceManager.getDefaultSharedPreferences(this);
        deny_progressBar = progress_move.getBoolean("progress_move", false);
        if (deny_progressBar) {
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }


        SharedPreferences no_screenshot = PreferenceManager.getDefaultSharedPreferences(this);
        deny_screenshot = no_screenshot.getBoolean("no_screenshot", false);
        if (deny_screenshot) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }

        SharedPreferences auto_copy = PreferenceManager.getDefaultSharedPreferences(this);
        allow_copy = auto_copy.getBoolean("auto_copy", false);
    }
}