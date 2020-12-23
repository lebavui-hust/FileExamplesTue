package com.example.fileexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SQLiteActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_lite);

        try {
            String dbPath = getFilesDir() + "/mydb";
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        findViewById(R.id.btn_create_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.beginTransaction();
                try {
                    // tao bang
                    db.execSQL("create table tblAMIGO(" +
                            "recID integer primary key autoincrement," +
                            "name text," +
                            "phone text);");

                    // them ban ghi
                    db.execSQL("insert into tblAMIGO(name, phone) values ('AAA', '555-1111')");
                    db.execSQL("insert into tblAMIGO(name, phone) values ('BBB', '555-2222')");
                    db.execSQL("insert into tblAMIGO(name, phone) values ('CCC', '555-3333')");

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.beginTransaction();
                try {
                    // xoa ban ghi
                    db.execSQL("delete from tblAMIGO where recID = 1;");

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                cv.put("name", "ABC");
                cv.put("phone", "555-1010");
                long res = db.insert("tblAMIGO", null, cv);
                Log.v("TAG", "Result: " + res);

                cv.put("name", "DEF");
                cv.put("phone", "555-2020");
                res = db.insert("tblAMIGO", null, cv);
                Log.v("TAG", "Result: " + res);

                cv.clear();
                res = db.insert("tblAMIGO", null, cv);
                Log.v("TAG", "Result: " + res);

                res = db.insert("tblAMIGO", "name", cv);
                Log.v("TAG", "Result: " + res);
            }
        });

        findViewById(R.id.btn_raw_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cursor cs = db.rawQuery("select * from tblAMIGO where recID > 2;", null);

                // Simple query
                String[] columns = {"recID", "name", "phone"};
                Cursor cs = db.query("tblAMIGO", columns, "recID > 2",
                        null, null, null, null);

                Log.v("TAG", "Num records: " + cs.getCount());

                cs.moveToPosition(-1);
                while (cs.moveToNext()) {
                    int recID = cs.getInt(0);
                    String name = cs.getString(1);
                    String phone = cs.getString(cs.getColumnIndex("phone"));

                    Log.v("TAG", recID + " - " + name + " - " + phone);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        db.close();
        super.onStop();
    }
}