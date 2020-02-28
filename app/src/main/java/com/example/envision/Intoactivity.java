package com.example.envision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Intoactivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout linearLayout;
    SliderAdaptor sliderAdaptor;

    TextView[] mdots;

    Button pbtn,nbtn;

    int mcurrentpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intoactivity);
        getSupportActionBar().hide();


        final SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        pbtn=(Button)findViewById(R.id.btnprev);
        nbtn=(Button)findViewById(R.id.btnnext);


        viewPager=(ViewPager)findViewById(R.id.viewpage);
        linearLayout=(LinearLayout)findViewById(R.id.llayout);

        sliderAdaptor=new SliderAdaptor(this);
        viewPager.setAdapter(sliderAdaptor);

        addDotsIndicators(0);
        viewPager.addOnPageChangeListener(viewListener);

        nbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nbtn.getText().toString()=="FINISH")
                {
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putBoolean("appintro_done", true);
                    editor.commit();

                    Intent intent=new Intent(Intoactivity.this,OptionActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                   viewPager.setCurrentItem(mcurrentpage+1);
            }
        });

        pbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mcurrentpage-1);
            }
        });
    }

    public void addDotsIndicators(int position){
        mdots=new TextView[3];
        linearLayout.removeAllViews();

        for(int i=0;i<mdots.length;i++){
            mdots[i]=new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mdots[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(30);
            mdots[i].setTextColor(getResources().getColor(R.color.colortransperentwhte));

            linearLayout.addView(mdots[i]);
        }

        if(mdots.length>0){
            mdots[position].setTextColor(getResources().getColor(R.color.colorwhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicators(position);
            mcurrentpage=position;

            if(position==0){
                nbtn.setEnabled(true);
                pbtn.setEnabled(false);
                pbtn.setVisibility(View.INVISIBLE);

                nbtn.setText("NEXT");
                pbtn.setText("");

            }
            else if(position==mdots.length-1){

                nbtn.setEnabled(true);
                pbtn.setEnabled(true);
                pbtn.setVisibility(View.VISIBLE);

                nbtn.setText("FINISH");
                pbtn.setText("BACK");
            }
            else {

                nbtn.setEnabled(true);
                pbtn.setEnabled(true);
                pbtn.setVisibility(View.VISIBLE);

                nbtn.setText("NEXT");
                pbtn.setText("BACK");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
