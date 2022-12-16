package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Activity2 extends AppCompatActivity {

    ListView listView2;
    ArrayList<HashMap<String,String>> listData2;
    ListView listView3;
    ArrayList<HashMap<String,String>> listData3;
    DBHelper dbHelper;
    SQLiteDatabase database;
    SimpleAdapter simpleAdapter;
    SimpleAdapter simpleAdapter2;
    Uri uri;
    String today = "";
    Calendar now = Calendar.getInstance();
    int MONTH = now.get(Calendar.MONTH)+1;
    String specialMonth = now.get(Calendar.YEAR)+"/"+MONTH+"%";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView2 = findViewById(R.id.todaylist);
        listData2 = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(this, listData2, R.layout.simple_list_item_activated_2,
                new String[] {"gtitle", "wtitle", "swt"}, new int[] {R.id.text1, R.id.text2, R.id.text3});
        listView2.setAdapter(simpleAdapter);

        listView3 = findViewById(R.id.SpecialList);
        listData3 = new ArrayList<>();
        simpleAdapter2 = new SimpleAdapter(this, listData3, R.layout.simple_list_item_activated_3,
                new String[] {"gtitle", "wtitle", "swt", "special"}, new int[] {R.id.text1, R.id.text2, R.id.text3, R.id.text4});
        listView3.setAdapter(simpleAdapter2);

        dbHelper = new DBHelper(this);
        loadTable();
        loadTable2();
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                specialMonth = year + "/" + month + "%";
                loadTable2();
            }
        });
        //
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                HashMap<String,String> hitem = (HashMap<String, String>) simpleAdapter.getItem(position);
                String nSite = hitem.get("site");
                String nStamp = hitem.get("stamp");
                String[] nStamp2;
                if (nSite.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "사이트 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                } else if (nStamp.isEmpty()) {
                    uri = Uri.parse(nSite);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    nStamp2 = nStamp.split("/");
                    int time = Integer.parseInt(nStamp2[0]) * 60 + Integer.parseInt(nStamp2[1]);
                    String suburi = "&t=" + time + "s";
                    String mainuri = nSite + suburi;
                    uri = Uri.parse(mainuri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                HashMap<String,String> hitem = (HashMap<String, String>) simpleAdapter2.getItem(position);
                String nSite = hitem.get("site");
                String nStamp = hitem.get("stamp");
                String[] nStamp2;
                if (nSite.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "사이트 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                } else if (nStamp.isEmpty()) {
                    uri = Uri.parse(nSite);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    nStamp2 = nStamp.split("/");
                    int time = Integer.parseInt(nStamp2[0]) * 60 + Integer.parseInt(nStamp2[1]);
                    String suburi = "&t=" + time + "s";
                    String mainuri = nSite + suburi;
                    uri = Uri.parse(mainuri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
        //
    }
    private void loadTable() {
        int day = now.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1:
                today = "일";
                break;
            case 2:
                today = "월";
                break;
            case 3:
                today = "화";
                break;
            case 4:
                today = "수";
                break;
            case 5:
                today = "목";
                break;
            case 6:
                today = "금";
                break;
            case 7:
                today = "토";
                break;
        }// 오늘 요일 알아내기
        listData2.clear();
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(DBContract.SQL_SELECT, new String[] {today});
        while(cursor.moveToNext()) {
            int nID = cursor.getInt(0);
            HashMap<String, String> hitem = new HashMap<>();
            hitem.put("gtitle", cursor.getString(1));
            hitem.put("wtitle", cursor.getString(2));
            hitem.put("stamp", cursor.getString(3));
            hitem.put("site", cursor.getString(4));
            hitem.put("swt", cursor.getString(5));
            hitem.put("day", cursor.getString(6));
            hitem.put("special", cursor.getString(7));
            hitem.put("id", String.valueOf(nID));
            listData2.add(hitem);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    private void loadTable2() {
        listData3.clear();
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(DBContract.SQL_SELECT2, new String[]{specialMonth}); //해결
        while(cursor.moveToNext()) {
            int nID = cursor.getInt(0);
            HashMap<String, String> hitem = new HashMap<>();
            hitem.put("gtitle", cursor.getString(1));
            hitem.put("wtitle", cursor.getString(2));
            hitem.put("stamp", cursor.getString(3));
            hitem.put("site", cursor.getString(4));
            hitem.put("swt", cursor.getString(5));
            hitem.put("day", cursor.getString(6));
            hitem.put("special", cursor.getString(7));
            hitem.put("id", String.valueOf(nID));
            listData3.add(hitem);
        }
        simpleAdapter2.notifyDataSetChanged();
    }

    public void onClickWT(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}