package com.example.myapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<HashMap<String,String>> listData;
    Uri uri;
    SimpleAdapter simpleAdapter;
    TextView textView3;
    EditText editText;
    String search;
    Button button;
    int iSelectedItem = -1; //현재 선택된 항목이 없음1.

    int iSelectedID = 0;
    int iMaxID = 0; // iMaxID + 1

    ListView listView2;
    ArrayList<HashMap<String,String>> listData2;
    SimpleAdapter simpleAdapter2;
    int iSelectedItem2 = -1; //현재 선택된 항목이 없음2.

    DBHelper dbHelper;
    SQLiteDatabase database;

    ActivityResultContract<Intent, ActivityResult> contract = new ActivityResultContracts.StartActivityForResult();
    ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                int iItem = intent.getIntExtra("item", -1);
                int iID = intent.getIntExtra("id", 0);
                HashMap<String,String> hItem = new HashMap<>();
                hItem.put("gtitle", intent.getStringExtra("gtitle"));
                hItem.put("wtitle", intent.getStringExtra("wtitle"));
                hItem.put("stamp", intent.getStringExtra("stamp"));
                hItem.put("site", intent.getStringExtra("site"));
                hItem.put("swt", intent.getStringExtra("swt"));
                hItem.put("day", intent.getStringExtra("day"));
                hItem.put("special", intent.getStringExtra("special"));
                ContentValues values = new ContentValues();
                values.put("gtitle", intent.getStringExtra("gtitle"));
                values.put("wtitle", intent.getStringExtra("wtitle"));
                values.put("stamp", intent.getStringExtra("stamp"));
                values.put("site", intent.getStringExtra("site"));
                values.put("swt", intent.getStringExtra("swt"));
                values.put("day", intent.getStringExtra("day"));
                values.put("special", intent.getStringExtra("special"));

                if(iItem == -1) {//add(변경x)
                    iMaxID++;
                    hItem.put("id", String.valueOf(iMaxID));
                    values.put("id", String.valueOf(iMaxID));
                    listData.add(hItem);
                    database = dbHelper.getWritableDatabase();
                    database.insert(DBContract.TABLE_NAME, null, values);
                }

                else {//edit(변경x)
                    hItem.put("id", String.valueOf(iID));
                    listData.set(iItem, hItem);
                    database = dbHelper.getWritableDatabase();
                    database.update(DBContract.TABLE_NAME, values, "id=?",
                            new String[] {String.valueOf(iID)});
                }
                simpleAdapter.notifyDataSetChanged();
            } else
                Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
        }
    };
    ActivityResultLauncher<Intent> launcher;

    private void loadTable() {//일반 탭 데이터 불러오기
        listData.clear();
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(DBContract.SQL_LOAD, null);
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
            listData.add(hitem);
            iMaxID = Math.max(iMaxID, nID);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    private void loadTable2() {// 즐찾 탭 데이터 불러오기
        listData2.clear();
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(DBContract.SQL_SELECT3, new String[] {"1"});// 조건
        while(cursor.moveToNext()) {
            int nID2 = cursor.getInt(0);
            HashMap<String, String> hitem2 = new HashMap<>();
            hitem2.put("gtitle", cursor.getString(1));
            hitem2.put("wtitle", cursor.getString(2));
            hitem2.put("stamp", cursor.getString(3));
            hitem2.put("site", cursor.getString(4));
            hitem2.put("swt", cursor.getString(5));
            hitem2.put("day", cursor.getString(6));
            hitem2.put("special", cursor.getString(7));
            hitem2.put("id", String.valueOf(nID2));
            listData2.add(hitem2);
        }
        simpleAdapter2.notifyDataSetChanged();
    }// LOADTABLE2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.listView);
        listView2 = findViewById(R.id.listView2);
        textView3 = findViewById(R.id.textView3);
        editText = findViewById(R.id.editTextSearch);
        button = findViewById(R.id.buttonSearchCancel);
        listData = new ArrayList<>();
        listData2 = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(this, listData, R.layout.simple_list_item_activated_1,
                new String[] {"gtitle", "wtitle"}, new int[] {R.id.text1, R.id.text2});
        listView.setAdapter(simpleAdapter);

        simpleAdapter2 = new SimpleAdapter(this, listData2, R.layout.simple_list_item_activated_1,
                new String[] {"gtitle", "wtitle"}, new int[] {R.id.text1, R.id.text2});
        listView2.setAdapter(simpleAdapter2);

        dbHelper = new DBHelper(this);

        loadTable();
        loadTable2();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                iSelectedItem = position;
                HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
                String swt = hitem.get("swt");
                String site = hitem.get("site");
                String site2;
                if(site.isEmpty()) {
                    site2 = "없음";
                }else {
                    site2 = "있음";
                }
                textView3.setText("사이트 정보: " + site2 +"\n" + "내용: "+swt);
                iSelectedID = Integer.parseInt(hitem.get("id"));
                for (int i = 0; i < listView2.getCount(); i++)
                    listView2.setItemChecked(i, false);
                iSelectedItem2 = -1;//선택초기화
            }
        });// 아이템 클릭시 정보(일반)

        launcher = registerForActivityResult(contract, callback);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                iSelectedItem2 = position;
                HashMap<String, String> hitem2 = (HashMap<String, String>) simpleAdapter2.getItem(iSelectedItem2);
                String swt = hitem2.get("swt");
                String site = hitem2.get("site");
                String site2;
                if(site.isEmpty()) {
                    site2 = "없음";
                }else {
                    site2 = "있음";
                }
                textView3.setText("사이트 정보: " + site2 +"\n" + "내용: "+swt);
                for (int i = 0; i < listView.getCount(); i++)
                    listView.setItemChecked(i, false);
                iSelectedItem = -1;//선택초기화
            }
        });// 아이템 클릭시 정보(즐찾)

        editText.addTextChangedListener(new TextWatcher() {// 실시간으로 검색데이터를 넘겨주고 결과 반환
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iSelectedItem = -1;
                iSelectedItem2 = -1;
                textView3.setText("");
                for (int i = 0; i < listView.getCount(); i++)
                    listView.setItemChecked(i, false);
                for (int i = 0; i < listView2.getCount(); i++)
                    listView2.setItemChecked(i, false);
                search = "%"+editText.getText().toString().trim()+"%";
                listData.clear();
                database = dbHelper.getWritableDatabase();
                Cursor cursor = database.rawQuery(DBContract.SQL_SELECT5, new String[]{search}); //해결
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
                    listData.add(hitem);
                }
                simpleAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }//oncreate

    public void onClickAdd(View view) {//추가버튼
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("item", -1);
        intent.putExtra("id", 0);
        launcher.launch(intent);
        editText.setText("");
    }

    public void onClickEdit(View view) {//수정버튼
        if(iSelectedItem == -1) {
            Toast.makeText(this, "일반에서 선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("item", iSelectedItem);
            intent.putExtra("id", iSelectedID);
            HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
            intent.putExtra("gtitle", hitem.get("gtitle"));
            intent.putExtra("wtitle", hitem.get("wtitle"));
            intent.putExtra("stamp", hitem.get("stamp"));
            intent.putExtra("site", hitem.get("site"));
            intent.putExtra("swt", hitem.get("swt"));
            intent.putExtra("day", hitem.get("day"));
            intent.putExtra("special", hitem.get("special"));
            launcher.launch(intent);
            editText.setText("");
        }
    }

    public void onClickDelete(View view) {//삭제버튼
        if(iSelectedItem == -1) {
            Toast.makeText(this, "일반에서 선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }else {
            database = dbHelper.getWritableDatabase();
            database.delete(DBContract.TABLE_NAME, "id=?",
                    new String[] {String.valueOf(iSelectedID)});
            loadTable();
            loadTable2();
            iSelectedItem = -1;
            iSelectedItem2 = -1;
            textView3.setText("");
            editText.setText("");// 삭제시 검색 초기화
        }
    }//삭제

    public void onClickSt(View view) {//사이트버튼
        if(iSelectedItem == -1 && iSelectedItem2 == -1) {
            Toast.makeText(getApplicationContext(), "선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if(iSelectedItem2 == -1){
            HashMap<String,String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
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
        } else if(iSelectedItem == -1) {
            HashMap<String,String> hitem2 = (HashMap<String, String>) simpleAdapter2.getItem(iSelectedItem2);
            String nSite = hitem2.get("site");
            String nStamp = hitem2.get("stamp");
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
    }


    public void onClickHW(View view) {//일정버튼
        editText.setText("");
        Intent intent = new Intent(getApplicationContext(), Activity2.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onMenuClickBookMark(MenuItem item) {//즐겨찾기 추가/삭제
        if(iSelectedItem == -1 && iSelectedItem2 == -1) {
            Toast.makeText(getApplicationContext(), "항목을 선택해주세요", Toast.LENGTH_SHORT).show();
        } else if(!(iSelectedItem == -1)){
            HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
            iSelectedID = Integer.parseInt(hitem.get("id"));
            String id = Integer.toString(iSelectedID);
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id", hitem.get("id"));
            values.put("gtitle", hitem.get("gtitle"));
            values.put("wtitle", hitem.get("wtitle"));
            values.put("stamp", hitem.get("stamp"));
            values.put("site", hitem.get("site"));
            values.put("swt", hitem.get("swt"));
            values.put("day", hitem.get("day"));
            values.put("special", hitem.get("special"));

            Cursor cursor = database.rawQuery(DBContract.SQL_SELECT4, new String[] {id});
            cursor.moveToNext();

            if(cursor.getInt(0) == 0) {
                values.put("bookmark", "1");
                database.update(DBContract.TABLE_NAME, values, "id=?",
                        new String[] {id});
            } else {
                values.put("bookmark", "0");
                database.update(DBContract.TABLE_NAME, values, "id=?",
                        new String[] {id});
            }
            loadTable2();
            for (int i = 0; i < listView.getCount(); i++)
                listView.setItemChecked(i, false);
            iSelectedItem = -1;
            for (int i = 0; i < listView2.getCount(); i++)
                listView2.setItemChecked(i, false);
            iSelectedItem2 = -1;
        } else if(!(iSelectedItem2 == -1)){
            HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter2.getItem(iSelectedItem2);
            iSelectedID = Integer.parseInt(hitem.get("id"));
            String id = Integer.toString(iSelectedID);
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id", hitem.get("id"));
            values.put("gtitle", hitem.get("gtitle"));
            values.put("wtitle", hitem.get("wtitle"));
            values.put("stamp", hitem.get("stamp"));
            values.put("site", hitem.get("site"));
            values.put("swt", hitem.get("swt"));
            values.put("day", hitem.get("day"));
            values.put("special", hitem.get("special"));

            Cursor cursor = database.rawQuery(DBContract.SQL_SELECT4, new String[] {id});
            cursor.moveToNext();

            if(cursor.getInt(0) == 0) {
                values.put("bookmark", "1");
                database.update(DBContract.TABLE_NAME, values, "id=?",
                        new String[] {id});
            } else {
                values.put("bookmark", "0");
                database.update(DBContract.TABLE_NAME, values, "id=?",
                        new String[] {id});
            }
            loadTable2();
            for (int i = 0; i < listView2.getCount(); i++)
                listView2.setItemChecked(i, false);
            iSelectedItem2 = -1;
            for (int i = 0; i < listView.getCount(); i++)
                listView.setItemChecked(i, false);
            iSelectedItem = -1;
        }
    }

    public void onMenuClickSearch(MenuItem item) {//검색
        editText.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        editText.requestFocus();
    }

    public void onClickSearchCancel(View view) {//X버튼
        iSelectedItem = -1;
        iSelectedItem2 = -1;
        textView3.setText("");
        for (int i = 0; i < listView.getCount(); i++)
            listView.setItemChecked(i, false);
        for (int i = 0; i < listView2.getCount(); i++)
            listView2.setItemChecked(i, false);
        editText.setText("");
        editText.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
    }
}