package com.example.envision;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class FirebaseDB {
    private  String name="";
    private  String mydate="";
    private  String id="";
    private String bitmap;

    FirebaseDB(){}
    FirebaseDB(String id,String mydate,String name,String bitmap)
    {
        this.bitmap=bitmap;
        this.name=name;
        this.mydate=mydate;
        this.id=id;
    }
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

    public String getMyDate() {
        return mydate;
    }

    public void setMyDate(String mydate) {
        this.mydate = mydate;
    }

    public void setBitmap(String bitmap) {
            this.bitmap=bitmap;
    }

    public String getBitmap() {
        return bitmap;
    }

    //    public void setImage(String eimage) {
//        this.eimage=eimage;
//    }
//
//    public String getImage() {
//        return  eimage;
//    }
}
