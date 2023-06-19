package local.hal.an91.android.saigoku33db;

import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import local.hal.an91.android.saigoku33db.R.id;

public class TempleEditActivity extends AppCompatActivity {

    /**
     * データベースヘルパーオブジェクト
     */
    private DatabaseHelper _helper;

    private int _templeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_edit);

        //インテントオブジェクトを取得。
        Intent intent = getIntent();

        String number = intent.getStringExtra("templeNumber");
        String name = intent.getStringExtra("templeName");
        _templeId = intent.getIntExtra("ID", 0);


        //DBヘルパーオブジェクトを生成
        _helper = new DatabaseHelper(TempleEditActivity.this);
        SQLiteDatabase db = _helper.getWritableDatabase();

        String sql = "SELECT * FROM temples WHERE _id = " + _templeId;
        Cursor cursor = db.rawQuery(sql, null);

        String honzon = "";
        String shushi = "";
        String address = "";
        String url = "";
        String note = "";
        while (cursor.moveToNext()) {
            int intName = cursor.getColumnIndex("name");
            int intHonzon = cursor.getColumnIndex("honzon");
            int intShushi = cursor.getColumnIndex("shushi");
            int intAddress = cursor.getColumnIndex("address");
            int intUrl = cursor.getColumnIndex("url");
            int intNote = cursor.getColumnIndex("note");

            name = cursor.getString(intName);
            honzon = cursor.getString(intHonzon);
            shushi = cursor.getString(intShushi);
            address = cursor.getString(intAddress);
            url = cursor.getString(intUrl);
            note = cursor.getString(intNote);
        }


        EditText ethonzon = findViewById(R.id.ethonzon);
        ethonzon.setText(honzon);

        EditText etshushi = findViewById(R.id.etshushi);
        etshushi.setText(shushi);

        EditText etaddress = findViewById(R.id.etaddress);
        etaddress.setText(address);

        EditText eturl = findViewById(R.id.etURL);
        eturl.setText(url);

        EditText etnote = findViewById(R.id.etnote);
        etnote.setText(note);


        ActionBar actionBar = getSupportActionBar();
        //アクションバーの［戻る］メニューを有効に設定。
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        //DBヘルパーオブジェクトの解放
        _helper.close();
        super.onDestroy();
    }


    /**
     * 保存ボタンを表示。
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //メニューインフレーターの取得。
        MenuInflater inflater = getMenuInflater();
        //オプションメニュー用。ｘｍｌをインフレーと。
        inflater.inflate(R.menu.menu_options_temple_list, menu);


        //親クラスの同名メソッドを呼び出し、その戻り値を返却。
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 戻るボタンの処理。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //選択されたメニューのIDを取得。
        int itemId = item.getItemId();
        //選択されたメニューが戻るの場合。アクティビティを終了。
        if (itemId == android.R.id.home){
            Log.d("back", "戻るボタンが押された");
            finish();
        }
        // オプションメニューの選択時処理
        switch (itemId) {
            case id.opSave:
                Log.d("push", "保存ボタンが押された");


                //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
                SQLiteDatabase db = _helper.getWritableDatabase();


                //インテントオブジェクトを取得。
                Intent intent = getIntent();

                //リスト画面から渡されたデータを取得。
                String number = intent.getStringExtra("templeNumber");
                String name = intent.getStringExtra("templeName");
                _templeId = intent.getIntExtra("ID", 0);

                //本尊名を取得。
                EditText etHonzon = findViewById(R.id.ethonzon);
                String strHonzon = etHonzon.getText().toString();

                //宗旨を取得。
                EditText etShyushi = findViewById(R.id.etshushi);
                String strShushi = etShyushi.getText().toString();

                //所在地を取得。
                EditText etAddress = findViewById(R.id.etaddress);
                String strAddress = etAddress.getText().toString();

                //URLを取得。
                EditText etUrl = findViewById(R.id.etURL);
                String strUrl = etUrl.getText().toString();

                //感想欄を取得。
                EditText etNote = findViewById(R.id.etnote);
                String strNote = etNote.getText().toString();

                //削除用SQL文字列を用意。
                String sqlDelete = "DELETE FROM temples WHERE _id = ?";
                //SQL文字列をもとにプリペアードステートメントを取得。
                SQLiteStatement stmt = db.compileStatement(sqlDelete);
                //変数のバインド。
                stmt.bindLong(1, _templeId);
                //削除SQLの実行。
                stmt.executeUpdateDelete();
                //インサート用SQL文字列の用意。
                String sqlInsert = "INSERT INTO temples (_id,name,honzon,shushi,address,url,note) VALUES (?,?,?,?,?,?,?)";
                //SQL文字列をもとにプリペアードステートメントを取得。
                stmt = db.compileStatement(sqlInsert);
                //変数のバインド。
                stmt.bindLong(1, _templeId);
                stmt.bindString(2, name);
                stmt.bindString(3, strHonzon);
                stmt.bindString(4, strShushi);
                stmt.bindString(5, strAddress);
                stmt.bindString(6, strUrl);
                stmt.bindString(7, strNote);
                //インサートSQLの実行。
                stmt.executeInsert();

                Log.d("save", "Insert完了");

                finish();
                break;
        }
        //親クラスの同名メソッドを呼び出し、その戻り値を返却。
        return super.onOptionsItemSelected(item);
    }

}