package com.example.envision;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SliderAdaptor extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdaptor(Context context){
        this.context=context;
    }

    public int[] slide_images={
            R.drawable.cameraicon,
            R.drawable.gallaryicon,
            R.drawable.save
    };
    public String heading[]={
            "TEXT DETECTION AND RECOGNIZATION USING CAMERA",
            "TEXT RECOGNIZATION FROM IMAGE TAKEN FROM GALLARY",
            "SAVED HISTORY"
    };
    public String desciption[]={
            "There are two buttons one is DETECT TEXT which allow user to detect text using camera and another is IMAGE EDIT which allow various operations on Recognized  text.",
            "This activity allow user to choose image from gallary and perform various operations on recgnized text.",
            "Here history is shown which allow operation like display,update,delete record."
    };

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideimageview=(ImageView)view.findViewById(R.id.slide_image);
        slideimageview.setImageResource(slide_images[position]);
        TextView slideHeading=(TextView)view.findViewById(R.id.slide_headding);
        slideHeading.setText(heading[position]);
        TextView slideDescription=(TextView)view.findViewById(R.id.slide_desc);
        slideDescription.setText(desciption[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}

