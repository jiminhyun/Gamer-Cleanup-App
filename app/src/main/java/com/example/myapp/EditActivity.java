package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    EditText editTextGame, editTextWalkThrough, editTextYoutube, editTextSite,
            editTextSWT, editTextDay, editTextSpecial;
    int iItem = -1;
    int iID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextGame = findViewById(R.id.editTextGame);
        editTextWalkThrough = findViewById(R.id.editTextWalkThrough);
        editTextYoutube = findViewById(R.id.editTextYoutube);
        editTextSite = findViewById(R.id.editTextSite);
        editTextSWT = findViewById(R.id.editTextSWT);
        editTextDay = findViewById(R.id.editTextDay);
        editTextSpecial = findViewById(R.id.editTextSpecial);

        Intent intentR = getIntent();
        if(intentR != null) {
            iItem = intentR.getIntExtra("item", -1);
            iID = intentR.getIntExtra("id", 0);
            if(iItem != -1) {
                editTextGame.setText(intentR.getStringExtra("gtitle"));
                editTextWalkThrough.setText(intentR.getStringExtra("wtitle"));
                editTextYoutube.setText(intentR.getStringExtra("stamp"));
                editTextSite.setText(intentR.getStringExtra("site"));
                editTextSWT.setText(intentR.getStringExtra("swt"));
                editTextDay.setText(intentR.getStringExtra("day"));
                editTextSpecial.setText(intentR.getStringExtra("special"));
            }
        }

        Button btnCancel = findViewById(R.id.buttonCancel);
        Button btnSave = findViewById(R.id.buttonSave);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gName = editTextGame.getText().toString().trim();
                String wName = editTextWalkThrough.getText().toString().trim();
                String Stamp = editTextYoutube.getText().toString().trim();
                String Site = editTextSite.getText().toString().trim();
                String Swt = editTextSWT.getText().toString().trim();
                String Day = editTextDay.getText().toString().trim();
                String Special = editTextSpecial.getText().toString().trim();

                if(gName.isEmpty() || wName.isEmpty() || Swt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "게임제목, 공략제목, 간이 공략을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("gtitle", gName);
                intent.putExtra("wtitle", wName);
                intent.putExtra("stamp", Stamp);
                intent.putExtra("site", Site);
                intent.putExtra("swt", Swt);
                intent.putExtra("day", Day);
                intent.putExtra("special", Special);
                intent.putExtra("item", iItem);
                intent.putExtra("id", iID);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }   //oncreate
}