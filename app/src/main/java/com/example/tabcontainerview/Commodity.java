package com.example.tabcontainerview;

/**
 * Created by gyp19 on 17-5-5.
 * 商品类
 */

public class Commodity {
    public int imageId;
    public String bar_code;//二维码
    public String name;//名称
    public String price;//价钱
    public String produced_date;//生产日期
    public String expiration_date;//保质期
    public String weight;//重量

    public Commodity() {
        this.bar_code = "";
        this.name = "";
        this.price = "";
        this.produced_date = "";
        this.expiration_date = "";
        this.weight = "";
    }
    public Commodity(String name, String price) {
        this.name = name;
        this.price = price;
    }
    public Commodity(int imageId,String name,String price,String produced_date,String expiration_date){
        this.name = name;
        this.price = price;
        this.produced_date = produced_date;
        this.expiration_date = expiration_date;
        this.imageId=imageId;
    }

    public Commodity(String bar_code, String name, String price) {
        this.bar_code = bar_code;
        this.name = name;
        this.price = price;
    }

    public String getBar_code() {
        return bar_code;
    }
    public int getImageId(){
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
    public String getProduced_date(){
        return produced_date;
    }
    public String getExpiration_date(){
        return expiration_date;
    }
}
