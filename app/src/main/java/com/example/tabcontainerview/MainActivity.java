package com.example.tabcontainerview;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tabcontainerview.adapter.DefaultAdapter;
import com.example.tabcontainerview.fragment.AppFragment;
import com.example.tabcontainerview.fragment.MainFragment;
import com.example.tabcontainerview.fragment.MineFragment;
import com.example.tabcontainerview.fragment.WorkFragment;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static String URL_ALLCOMMODITY = "http://123.206.23.219/SmartShopping/API/ShowAllcommodity.php";
    private TabContainerView tabContainerView;
    private int[] iconImageArray, selectedIconImageArray;
    private Fragment[] fragments;
    private MainFragment mainFragment=new MainFragment();



    private DatabaseHelper dbHelper;    //数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        initView();
        initToolBar();
        dbHelper = new DatabaseHelper(this, "SmartDB.db", null, DatabaseHelper.VERSION);

        //请求全部商品信息
        LoadCommodity(URL_ALLCOMMODITY);
    }

    private void initView() {
        iconImageArray = new int[]{R.drawable.icon_work, R.drawable.icon_main, R.drawable.icon_app, R.drawable.icon_mine};
        selectedIconImageArray = new int[]{R.drawable.icon_work_selected1, R.drawable.icon_main_selected1, R.drawable.icon_app_selected1, R.drawable.icon_mine_selected1};
        fragments = new Fragment[] {new WorkFragment(), mainFragment, new AppFragment(), new MineFragment()};

        tabContainerView = (TabContainerView) findViewById(R.id.tab_containerview_main);
        tabContainerView.setAdapter(new DefaultAdapter(this, fragments, getSupportFragmentManager(), getResources().getStringArray(R.array.titleArray),
                getResources().getColor(R.color.colorPrimary), iconImageArray, selectedIconImageArray));
    }

    private void initToolBar() {
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    /**
     * 加载全部商品信息，得到返回的json数据
     * 调用函数解析json数据
     */
    private void LoadCommodity(final String url){
        HttpUtils.getInstance().doGet(url, new HttpUtils.HttpResponseListernr() {
            @Override
            public void onResponse(String result) {
                parseJSONWithJSONObject(result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", result);
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * @param jsonData 服务器返回的json数据
     * 使用JSONObject解析json
     * 解析完之后的结果存入数据库
     */
    private void parseJSONWithJSONObject(String jsonData) {
        Commodity cd = new Commodity();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                cd.bar_code = jsonObject.getString("bar_code");
                cd.name = jsonObject.getString("name");
                cd.price = jsonObject.getString("price");
                cd.produced_date = jsonObject.getString("produced_date");
                cd.expiration_date = jsonObject.getString("expiration_date");
                cd.weight = jsonObject.getString("weight");

                //将其同步本地数据库
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("bar_code", cd.bar_code);
                values.put("name", cd.name);
                values.put("price", cd.price);
                values.put("produced_date", cd.produced_date);
                values.put("expiration_date", cd.expiration_date);
                values.put("weight", cd.weight);

                db.insert("commodity", "null", values);
                values.clear();
                db.close();
            }
        } catch (Exception e) {
        }
    }

    //每次程序正常结束时删除本地数据库。
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("commodity", null, null);
        db.delete("inventory", null, null);
        db.close();
    }




/*****************************/

}
