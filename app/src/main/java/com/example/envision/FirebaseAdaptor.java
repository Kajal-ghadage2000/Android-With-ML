package com.example.envision;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class FirebaseAdaptor extends RecyclerView.Adapter<FirebaseAdaptor.ImageViewHolder>  {

    Context context;
    List<FirebaseDB> list;
    OnItemClickListener mlistener;

    public FirebaseAdaptor(Context context, List<FirebaseDB> list)
    {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.list_rowcell,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        FirebaseDB firebaseDB=list.get(position);
        holder.mydate.setText(firebaseDB.getMyDate());
        holder.myid.setText(firebaseDB.getId());
        holder.mydesc.setText(firebaseDB.getName());

        String image=firebaseDB.getBitmap();
        byte[] decodeImage = Base64.decode(image,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodeImage,0,decodeImage.length);

        holder.myimg.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        TextView myid;
        TextView mydate;
        TextView mydesc;
        ImageView myimg;
        public ImageViewHolder(@NonNull View listitemView) {
            super(listitemView);

            myid=(TextView)listitemView.findViewById(R.id.tv_regid);
            mydate=(TextView)listitemView.findViewById(R.id.tv_email);
            mydesc=(TextView)listitemView.findViewById(R.id.tv_name);
            myimg=(ImageView)listitemView.findViewById(R.id.imageView2);

            listitemView.setOnClickListener(this);
            listitemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View v) {
            if(mlistener!=null)
            {
                int pos=getAdapterPosition();
                if(pos!=RecyclerView.NO_POSITION){
                    mlistener.onItemClick(pos);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("select action");
            MenuItem dowhatever=menu.add(Menu.NONE,1,1,"Edit text");
            MenuItem delete=menu.add(Menu.NONE,2,2,"Delete");

            dowhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);


        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mlistener!=null)
            {
                int pos=getAdapterPosition();
                if(pos!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mlistener.onWhateverClick(pos);
                            break;
                        case 2:
                            mlistener.onDeleteClick(pos);
                            break;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onWhateverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mlistener=listener;
    }
}
/*
public class FirebaseAdaptor extends ArrayAdapter<FirebaseDB> {

    Activity context;
    List<FirebaseDB> list;

    public FirebaseAdaptor(Activity context, List<FirebaseDB> list)
    {
        super(context,R.layout.list_rowcell,list);
        this.context=context;
        this.list=list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem =inflater.inflate(R.layout.list_rowcell,null,true);

        TextView myid=(TextView)listViewItem.findViewById(R.id.tv_regid);
        TextView mydate=(TextView)listViewItem.findViewById(R.id.tv_email);
        TextView mydesc=(TextView)listViewItem.findViewById(R.id.tv_name);
        ImageView myimg=(ImageView)listViewItem.findViewById(R.id.imageView2);

        FirebaseDB firebaseDB=list.get(position);

        myid.setText(firebaseDB.getId());
        mydesc.setText(firebaseDB.getName());
        mydate.setText(firebaseDB.getMyDate());

//        String image=firebaseDB.getBitmap();
//        byte[] decodeImage = Base64.decode(image,Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(decodeImage,0,decodeImage.length);
//
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Title", null);
//        Uri myuri=Uri.parse(path);
//
//        Picasso.with(context).load(myuri).resize(120, 120).centerInside().into(myimg);
        //myimg.setImageBitmap(bitmap);

        return listViewItem;
    }
}
 */
