package com.atechnos.torch;

import java.io.Serializable;

/**
 * Created by user on 5/26/2017.
 */

public class SlidingModel implements Serializable {
    private int product_id;
    private String product_name;
    private String appurl;
    private String image;


    public SlidingModel( ) {



    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
