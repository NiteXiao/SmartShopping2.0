package com.example.tabcontainerview.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tabcontainerview.MyBluetoothIO;
import com.example.tabcontainerview.R;
import com.example.tabcontainerview.ShowAllCommodity;
import com.example.tabcontainerview.lianjielanya;
import com.uuzuche.lib_zxing.activity.CaptureActivity;


/**
 * Created by chenpengfei on 2017/3/21.
 */
public class MineFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mine, container, false);
        view.findViewById(R.id.quanbu).setOnClickListener(this);
        view.findViewById(R.id.button_lianjie).setOnClickListener(this);
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
            case R.id.quanbu:
                intent = new Intent(getActivity(), ShowAllCommodity.class);
                startActivity(intent);
                break;
            case R.id.button_lianjie:
                intent = new Intent (getActivity(), lianjielanya.class);
                startActivity(intent);
        }

    }

}
