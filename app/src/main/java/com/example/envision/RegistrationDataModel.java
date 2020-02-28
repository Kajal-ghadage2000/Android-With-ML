package com.example.envision;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by sanjeevksaroj on 4/4/17.
 */

public class RegistrationDataModel {
    private  String name="";
    private  String email="";
    private  String eimage="";
    private  String id="";
    private Bitmap bitmap;
    private byte[] b;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public void setBitmap(byte[] b) {
//            this.b=b;
//    }
//
//    public byte[] getBitmap() {
//        return b;
//    }

    //    public void setImage(String eimage) {
//        this.eimage=eimage;
//    }
//
//    public String getImage() {
//        return  eimage;
//    }
}
