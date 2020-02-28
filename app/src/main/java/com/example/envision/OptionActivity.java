package com.example.envision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OptionActivity extends AppCompatActivity implements View.OnClickListener{


    CardView bankcard,ideascard,wificard;
    CheckBox prefCheckBox;
    com.google.android.material.appbar.CollapsingToolbarLayout linearLayout;
    String END_POINT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getSupportActionBar().hide();

        linearLayout=(com.google.android.material.appbar.CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);

        //loadPref();
        bankcard=(CardView)findViewById(R.id.bankcardId);
        ideascard=(CardView)findViewById(R.id.ideas_card);
        wificard=(CardView)findViewById(R.id.wificard);

        bankcard.setOnClickListener(this);
        ideascard.setOnClickListener(this);
        wificard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.bankcardId:
                i=new Intent(this,TextRecog.class);
                startActivity(i);
                break;
            case R.id.ideas_card:
                i=new Intent(this,GallaryActivity.class);
                startActivity(i);
                break;
            case R.id.wificard:
                i=new Intent(this,HistoryActivity.class);//ImageLabeling
                startActivity(i);
                break;

        }
    }

    public void settingMethod(View view) {
        Intent intent = new Intent();
        intent.setClass(OptionActivity.this, SettingPrefrences.class);
        startActivityForResult(intent, 0);
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent();
        intent.setClass(OptionActivity.this, SettingPrefrences.class);
        startActivityForResult(intent, 0);

        return true;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadPref();
    }

//    void alertForClearAbsenceData(){
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(SettingPrefrences.this);
//
//        alert.setTitle("hello title");
//        alert.setMessage("heelo msg");
//
//        alert.setCancelable(false);
//        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//
//            }
//        });
//        alert.show();
//    }
    private void loadPref(){
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        Preference clear =mySharedPreferences.get("absence_data");
//
//        clear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                alertForClearAbsenceData();
//                return true;
//            }
//        });

        END_POINT = mySharedPreferences.getString("list_preference", "");
        String data=END_POINT.toString();

        if(data.equals("0")){
            Toast.makeText(this,END_POINT,Toast.LENGTH_LONG).show();
            linearLayout.setBackgroundResource(R.drawable.red);
        }
        else if(data.equals("1")){
            linearLayout.setBackgroundResource(R.drawable.green);
        }
        else if(data.equals("2")){
            linearLayout.setBackgroundResource(R.drawable.voilate);
        }
        else if(data.equals("3")){
            linearLayout.setBackgroundResource(R.drawable.orange);
        }
        else if(data.equals("4")){
            linearLayout.setBackgroundResource(R.drawable.mendi);
        }
        else if(data.equals("5")){
            linearLayout.setBackgroundResource(R.drawable.colorful);
        }
    }
}
