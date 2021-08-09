package com.qianshui.steamguardhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ImportActivity extends AppCompatActivity {
    EditText ip_content;
    Button ip_confirm;
    Button ip_clear;
    List<String> codeList;

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
        setContentView(R.layout.activity_import);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("导入备用令牌");


        ip_content = (EditText) findViewById(R.id.ip_content);
        ip_clear = (Button) findViewById(R.id.ip_clear);
        ip_confirm = (Button) findViewById(R.id.ip_confirm);
        codeList = new ArrayList<>();

        ip_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip_content.setText("");
                Context context = getApplicationContext();
                CharSequence text = "已清除编辑框";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                vibrator.vibrate(40);
            }
        });

        ip_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeList.clear();
                String pattern_number = "(\\d+).\\\n$";
                int start = 0;
                int end = 0;
                String text = ip_content.getText().toString();
                for (int i = 0; i < ip_content.getLineCount(); i++) {
                    end = ip_content.getLayout().getLineEnd(i);
                    String line = text.substring(start, end); //指定行的内容
                    start = end;
                    if (!Pattern.matches(pattern_number, line) && line.replaceAll("\\\n", "").length()==7){
                        codeList.add(line);
                    }
                }
                Log.d("Code List", codeList.toString() + "\n\nSize: " + codeList.size());
                // 缺少返回MainActivity的方法
                SharedPreferences.Editor editor = getSharedPreferences("codeList", MODE_PRIVATE).edit();
                editor.putInt("codeNumbers", codeList.size());
                for (int i=0; i<codeList.size();i++){
                    editor.putString("item_"+i, codeList.get(i));
                }
                editor.commit();
                if(codeList.size()==0){
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "导入失败", duration);
                    toast.show();
                }else {
//                    Intent intent = null;
//                    intent = new Intent(ImportActivity.this, MainActivity.class);
//                    startActivity(intent);
                    finish();
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "导入成功", duration);
                    toast.show();
                }
            }
        });
    }
}