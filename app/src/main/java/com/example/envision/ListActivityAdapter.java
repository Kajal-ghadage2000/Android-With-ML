package com.example.envision;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListActivityAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<RegistrationDataModel> arrayList = new ArrayList<RegistrationDataModel>();
    LayoutInflater layoutInflater = null;

    public  ListActivityAdapter(Activity activity,ArrayList<RegistrationDataModel> dataModelArrayList){
        this.activity=activity;
        this.arrayList=dataModelArrayList;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        TextView tv_name,tv_email,tv_regid;
        ImageView tv_image;
    }
    ViewHolder viewHolder = null;

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View vi=view;
        final int pos = position;
        if(vi == null){

            // create  viewholder object for list_rowcell View.
            viewHolder = new ViewHolder();
            // inflate list_rowcell for each row

            vi = layoutInflater.inflate(R.layout.list_rowcell,null);
            viewHolder.tv_name = (TextView) vi.findViewById(R.id.tv_name);
            viewHolder.tv_email = (TextView) vi.findViewById(R.id.tv_email);
            viewHolder.tv_regid = (TextView) vi.findViewById(R.id.tv_regid);
//            viewHolder.tv_image = (ImageView) vi.findViewById(R.id.imageView2);
            //viewHolder.imageView=(ImageView)vi.findViewById(R.id.imageView2);

            vi.setTag(viewHolder);
        }else {

            viewHolder= (ViewHolder) vi.getTag();
        }


        viewHolder.tv_name.setText(arrayList.get(pos).getName());
        viewHolder.tv_email.setText(arrayList.get(pos).getEmail());
        viewHolder.tv_regid.setText("Scan_"+arrayList.get(pos).getId());

//        byte[] b = Base64.decode(arrayList.get(pos).getBitmap(), Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//        viewHolder.tv_image.setImageBitmap(bitmap);
//        viewHolder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(arrayList.get(pos).getImage(), 0,arrayList.get(pos).getImage().length));
        return vi;

    }
}
/*

public class ListActivityAdapter extends RecyclerView.Adapter<ListActivityAdapter.ViewHolder> {
    Context context;
    ArrayList<RegistrationDataModel> arrayList = new ArrayList<RegistrationDataModel>();
    LayoutInflater layoutInflater = null;

    public ListActivityAdapter(Context context, ArrayList<RegistrationDataModel> dataModelArrayList) {
        this.context=context;
        this.arrayList = dataModelArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                        LayoutInflater
                        .from(context)
                        .inflate(R.layout.list_rowcell, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nameTextView.setText(arrayList.get(position).getName());
        holder.mobileTextView.setText(arrayList.get(position).getEmail());
        holder.landlineTextView.setText(arrayList.get(position).getId());
//
//        holder.parentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, DetailsActivity.class);
//                intent.putExtra(DetailsActivity.EXTRA_CONTACT, contact);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTextView;
        private TextView mobileTextView;
        private TextView landlineTextView;
     //   Context context;

        public ViewHolder(@NonNull View view) {
            super(view);
        //    this.context = context;
            this.nameTextView = (TextView)view
                    .findViewById(R.id.tv_name);
            this.mobileTextView = (TextView)view
                    .findViewById(R.id.tv_email);
            this.landlineTextView = (TextView)view
                    .findViewById(R.id.tv_regid);

        }


    }

}*/
/*
<androidx.recyclerview.widget.RecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/list"
        android:layout_alignParentStart="true"
        android:layout_alignParentTop="true"
        android:layout_alignParentEnd="true"
        android:layout_marginStart="10dp"
        android:layout_marginTop="10dp"
        android:layout_marginEnd="10dp"
        android:layout_marginBottom="10dp"
        android:divider="@color/colorAccent"
        android:background="#F7E5E4"/>
*/