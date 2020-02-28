package com.example.envision;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView,ballontext;
    ImageView ballon;
    Animation frombottom,fromtop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String my_edittext_preference = mySharedPreferences.getString("edittext_preference", "");
        final boolean introdone=mySharedPreferences.getBoolean("appintro_done", false);

        textView=(TextView)findViewById(R.id.textview);
        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        textView.setAnimation(frombottom);
        if(my_edittext_preference!=null)
            textView.setText("Welcome "+my_edittext_preference);

        ballontext=(TextView)findViewById(R.id.textView2);

        ballon=(ImageView)findViewById(R.id.ballonimage);
        fromtop= AnimationUtils.loadAnimation(this,R.anim.fromtop);
        ballon.setAnimation(fromtop);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                if(introdone)
                {
                    // This method will be executed once the timer is over
                    Intent i = new Intent(MainActivity.this, OptionActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                        // This method will be executed once the timer is over
                        Intent i = new Intent(MainActivity.this, Intoactivity.class);
                        startActivity(i);
                        finish();
                }
            }
        }, 6000);
    }


}
