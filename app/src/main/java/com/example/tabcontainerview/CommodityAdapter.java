package com.example.tabcontainerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nowl on 2017/5/1.
 */

public class CommodityAdapter extends ArrayAdapter<Commodity>{
    private int resourceId;
    public CommodityAdapter(Context context, int textViewResourceId, List<Commodity> objects) {
        super(context, textViewResourceId, objects);
        resourceId=textViewResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Commodity commodityView=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.commodityImage=(ImageView) view.findViewById(R.id.commodity_image);
            viewHolder.commodityName=(TextView) view.findViewById(R.id.commodity_name);
            viewHolder.commodityPrice=(TextView) view.findViewById(R.id.commodity_price);
            viewHolder.produced_date=(TextView) view.findViewById(R.id.commodity_produced_date);
            viewHolder.expiration_date=(TextView) view.findViewById(R.id.commodity_expiration_date);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.commodityImage.setImageResource(commodityView.getImageId());
        viewHolder.commodityName.setText(commodityView.getName());
        viewHolder.commodityPrice.setText(commodityView.getPrice());
        viewHolder.produced_date.setText(commodityView.getProduced_date());
        viewHolder.expiration_date.setText(commodityView.getExpiration_date());
        return view;
    }
    class ViewHolder{
        ImageView commodityImage;
        TextView commodityName;
        TextView commodityPrice;
        TextView produced_date;//生产日期
        TextView expiration_date;
    }
}
