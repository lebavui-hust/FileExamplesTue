package com.example.fileexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
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
                    InputStream is = openFileInput("internal_data.txt");
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
                    OutputStream os = openFileOutput("internal_data.txt", 0);
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