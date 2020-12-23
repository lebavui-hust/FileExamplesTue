package com.example.fileexamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private final String DATA_FILE = "my_data";
    private final String DATA_USERNAME = "username";
    private final String DATA_PASSWORD = "password";

    EditText editUsername, editPassword;
    EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);

        editContent = findViewById(R.id.edit_content);

        SharedPreferences prefs = getSharedPreferences(DATA_FILE, MODE_PRIVATE);

        if (!prefs.contains(DATA_USERNAME) || !prefs.contains(DATA_PASSWORD))
            Toast.makeText(MainActivity.this, "Data not available", Toast.LENGTH_LONG).show();

        editUsername.setText(prefs.getString(DATA_USERNAME, ""));
        editPassword.setText(prefs.getString(DATA_PASSWORD, ""));

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gia su dang nhap thanh cong
                // Luu username va password
                SharedPreferences prefs = getSharedPreferences(DATA_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(DATA_USERNAME, editUsername.getText().toString());
                editor.putString(DATA_PASSWORD, editPassword.getText().toString());
                editor.apply();
            }
        });

        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // InputStream is = openFileInput("internal_data.txt");

                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/savedata.txt";
                    File file = new File(filePath);
                    InputStream is = new FileInputStream(file);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String str = "";
                    String line = "";
                    while ((line = reader.readLine()) != null)
                        str = str + line + "\n";
                    reader.close();
                    is.close();

                    editContent.setText(str);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // OutputStream os = openFileOutput("internal_data.txt", 0);

                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/savedata.txt";
                    File file = new File(filePath);
                    OutputStream os = new FileOutputStream(file);

                    OutputStreamWriter writer = new OutputStreamWriter(os);
                    writer.write(editContent.getText().toString());
                    writer.flush();
                    writer.close();
                    os.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        ReadTextFromRawFile();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission denied");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            } else
                Log.v("TAG", "Permission granted");
        }

        File file = Environment.getExternalStorageDirectory();
        String[] subFiles = file.list();
        for (int i = 0; i < subFiles.length; i++)
            Log.v("TAG", subFiles[i]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Log.v("TAG", "Permission granted");
            else
                Log.v("TAG", "Permission denied");
    }

    private void ReadTextFromRawFile() {
        try {
            InputStream is = getResources().openRawResource(R.raw.test);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String str = "";
            String line = "";
            while ((line = reader.readLine()) != null)
                str = str + line + "\n";
            reader.close();
            is.close();

            Log.v("TAG", str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}