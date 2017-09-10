package com.example.tabcontainerview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tabcontainerview.Commodity;
import com.example.tabcontainerview.CommodityAdapter;
import com.example.tabcontainerview.MainActivity;
import com.example.tabcontainerview.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by chenpengfei on 2017/3/21.
 */
public class AppFragment extends Fragment {
    private List<Commodity> commodityViewList;
    private ListView listView;
    private CommodityAdapter adapter;
    int n=1;
    private View view;
    //private List<Commodity> commodityList=new ArrayList<>();
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_app , container, false);
        this.view=view;
        return view;

    }

    @Override
    public void onResume() {
        setList(view);
        super.onResume();
    }

    private void setList(View view){
        commodityViewList = new ArrayList<>();
        //Log.d("file",file.getName());
        for(int i=0;i<n;i++){
            commodityViewList.add(new Commodity(R.mipmap.ic_launcher,"农夫山泉",n+".0元","生产日期:2017/9/10","保质期:12个月"));
        }
           n++;
        adapter=new CommodityAdapter(getActivity(),R.layout.commodity_item,commodityViewList);
        listView=(ListView)view.findViewById(R.id.list_view1);
        listView.setAdapter(adapter);
    }
}
