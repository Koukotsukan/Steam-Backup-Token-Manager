package com.qianshui.steamguardhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TutorialActivity extends AppCompatActivity {
    TextView tutorial_importing;
    TextView tutorial_update;

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
        setContentView(R.layout.activity_tutorial);
        setTitle(this.getString(R.string.tutorial_title));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tutorial_importing = (TextView) findViewById(R.id.tutorial_importing);
        tutorial_update = (TextView) findViewById(R.id.tutorial_update);

        tutorial_importing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://store.steampowered.com/twofactor/manage");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        tutorial_importing.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText("https://store.steampowered.com/twofactor/manage");
                Context context = getApplicationContext();
                CharSequence text = context.getString(R.string.tutorial_copy);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return true;
            }
        });

        tutorial_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getApplicationContext().getString(R.string.update_url).equalsIgnoreCase("none")) {
                    Uri uri = Uri.parse(getApplicationContext().getString(R.string.update_url));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
    }
}