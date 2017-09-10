package com.example.tabcontainerview.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabcontainerview.Commodity;
import com.example.tabcontainerview.CommodityAdapter;
import com.example.tabcontainerview.MainActivity;
import com.example.tabcontainerview.*;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chenpengfei on 2017/3/21.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private List<Commodity> commodityViewList;
    private ListView listView;
    private CommodityAdapter adapter;
    private Button button1;
    private Button button2;
    private EditText et_text;   //输入商品名称
    private TextView tv_text;   //显示结果
    private String result = "";   //存扫码结果
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main, container, false);

        et_text= (EditText) view.findViewById(R.id.sousuo);
        button1 = (Button)view.findViewById(R.id.weizhi);
        button1.setOnClickListener(this);
        button2 = (Button)view.findViewById(R.id.button_sousuo);
        button2.setOnClickListener(this);
        listView=(ListView)view.findViewById(R.id.list_view);
        tv_text = (TextView) view.findViewById(R.id.textView_1);
        setList();
        return view;
    }
    private void setList(){
        commodityViewList = new ArrayList<>();
        //Log.d("file",file.getName());
        for(int i=0;i<10;i++)
            commodityViewList.add(new Commodity(R.mipmap.ic_launcher,"矿泉水","2.0元","生产日期:2017/9/10","保质期:12个月"));
        adapter=new CommodityAdapter(getActivity(),R.layout.commodity_item,commodityViewList);

        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.weizhi:
                intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.button_sousuo: //确定按钮
                tv_text.setText("");
                String com_name = et_text.getText().toString();
                if (result.equals("")) {
                    tv_text.append("未扫描位置二维码\n");
                    Toast.makeText(getActivity(), tv_text.getText(), Toast.LENGTH_LONG).show();
                }
                if (com_name.equals("")) {
                    tv_text.append("未输入商品名称\n");
                    Toast.makeText(getActivity(), tv_text.getText(), Toast.LENGTH_LONG).show();
                }

                if (!result.equals("") && !com_name.equals("")) {
                    intent = new Intent(getActivity(), LuJing.class);
                    intent.putExtra("commodity_name",com_name);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //获取扫码的结果
        if (requestCode == 1) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void setListView(String str){
        commodityViewList = new ArrayList<>();
        //Log.d("file",file.getName());
        for(int i=0;i<10;i++)
            commodityViewList.add(new Commodity(R.mipmap.ic_launcher,str,"2.0元","生产日期:2017/9/10","保质期:12个月"));
        adapter=new CommodityAdapter(getActivity(),R.layout.commodity_item,commodityViewList);
        listView.setAdapter(adapter);
    }

}
