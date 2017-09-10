package com.example.tabcontainerview.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tabcontainerview.DatabaseHelper;
import com.example.tabcontainerview.MainActivity;
import com.example.tabcontainerview.MyBluetoothIO;
import com.example.tabcontainerview.R;
import com.example.tabcontainerview.ShowAllCommodity;
import com.example.tabcontainerview.ShowCommodity;
import com.example.tabcontainerview.lianjielanya;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


/**
 * Created by chenpengfei on 2017/3/21.
 */
public class WorkFragment extends Fragment implements View.OnClickListener {
    private Double weightD; //商品本身重量
    private Double weight;  //当前测量重量
    private String result;  //存储扫描出来的二维码
    private int coumt = 0;  //接收数据的次数
    private String number = "";
    private DatabaseHelper dbHelper;    //数据库


    private final int PANDUANFANWEI = 150;   //商品重量的误差值
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_work, container, false);
        view.findViewById(R.id.saomiao).setOnClickListener(this);
        return view;
    }
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            case R.id.saomiao:
                MyBluetoothIO.SendClean();
                intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    result = bundle.getString(CodeUtils.RESULT_STRING);
                    Intent intent = new Intent(getActivity(), ShowCommodity.class);
                    intent.putExtra("result_data", result); //向下显示商品界面活动传递二维码
                    Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, 2);
                }
            }
        }

        if (requestCode == 2) {  //加入购物车，开始监听蓝牙数据
            if (resultCode == RESULT_OK) {
                String weightS = data.getStringExtra("Weight");
                Log.d("asd",weightS);
                weightD = Double.valueOf(weightS);
                Toast.makeText(getActivity(), "正在放入商品请等候", Toast.LENGTH_SHORT).show();

                //开启监听线程
                new MyTestIS().start();
            }
        }
    }

    private class MyTestIS extends Thread {
        InputStream is;

        MyTestIS() {
            is = MyBluetoothIO.is_c;
        }

        @Override
        public void run() {
            super.run();
            try {
                for (int i = 0; i < 4; i++) {
                    byte[] buffer = new byte[128];
                    int count = is.read(buffer);    //如果蓝牙客户端没有发送数据则会被组赛
                    Message msg = new Message();
                    msg.obj = new String(buffer, 0, count, "utf-8");
                    handler.sendMessage(msg);
                    Log.d("sousuoshangping", "这是线程跑的第" + i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ++coumt;
            number += String.valueOf(msg.obj);
            Log.d("sousuoshangping", "当前所接收的数据：" + number);
            if (coumt >= 4) {
                weight = Double.valueOf(number);
                Log.d("sousuoshangping", "实际重量：" + weight.toString() + "  标准重量：" + weightD.toString());
                if ((weight > (weightD - PANDUANFANWEI)) && (weight < (weightD + PANDUANFANWEI))) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("commodity", null, "bar_code=?", new String[]{result}, null, null, null);
                    cursor.moveToFirst();
                    String code = cursor.getString(cursor.getColumnIndex("bar_code"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    cursor.close();

                    ContentValues values = new ContentValues();
                    values.put("bar_code", code);
                    values.put("name", name);
                    values.put("price", price);
                    db.insert("inventory", "null", values);
                    values.clear();
                    dbHelper.close();
                    Toast.makeText(getActivity(), "商品添加成功", Toast.LENGTH_SHORT).show();
                    MyBluetoothIO.SendAddC();
                    number = "";
                    coumt = 0;
                } else {
                    Toast.makeText(getActivity(), "商品添加失败", Toast.LENGTH_SHORT).show();
                    MyBluetoothIO.SendAddD();
                    number = "";
                    coumt = 0;
                }
            }
        }
    };
}
