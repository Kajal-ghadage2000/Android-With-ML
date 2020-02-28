package com.example.envision;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//floating context menu

/*
public class HistoryActivity extends AppCompatActivity {

    ListView show_list;
    DataBaseHelper dataBaseHelper;
    ArrayList<RegistrationDataModel> registrationDataModelArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dataBaseHelper=new DataBaseHelper(this);

        registrationDataModelArrayList= dataBaseHelper.getRegistrationData();
        show_list= (ListView) findViewById(R.id.ListViewLundiCatalogueRDV);
//        show_list= (RecyclerView) findViewById(R.id.recycler_view);

        Collections.reverse(registrationDataModelArrayList);
        ListActivityAdapter listActivityAdapter=new ListActivityAdapter(this,registrationDataModelArrayList);
        show_list.setAdapter(listActivityAdapter);

//        show_list.setLayoutManager(new LinearLayoutManager(this));

        //show_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
 //           @Override
  //          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent=new Intent(ListActivity.this,UpdateActivity.class);
//                intent.putExtra("id",registrationDataModelArrayList.get(i).getId());
//                intent.putExtra("name",registrationDataModelArrayList.get(i).getName());
//                intent.putExtra("email",registrationDataModelArrayList.get(i).getEmail());
//                intent.putExtra("address",registrationDataModelArrayList.get(i).getAddress());
//                intent.putExtra("phone",registrationDataModelArrayList.get(i).getPhone());
//                startActivity(intent);
//                finish();


 //           }
 //       });



    }

}
*/
public class HistoryActivity extends AppCompatActivity implements FirebaseAdaptor.OnItemClickListener {

    RecyclerView show_list;
    DatabaseReference textrecog;
    ArrayList<FirebaseDB> firebaseDBList;

    ProgressBar progressBar;

    FirebaseDB firebaseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
//        show_list= (ListView) findViewById(R.id.ListViewLundiCatalogueRDV);

        textrecog= FirebaseDatabase.getInstance().getReference("mytextrecognizer");
        firebaseDBList =new ArrayList<>();



        show_list= (RecyclerView) findViewById(R.id.ListViewLundiCatalogueRDV);
        show_list.setHasFixedSize(true);
        show_list.setLayoutManager(new LinearLayoutManager(this));

        progressBar=(ProgressBar) findViewById(R.id.progress_circle);

    }

    @Override
    protected void onStart() {
        super.onStart();
        textrecog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firebaseDBList.clear();
                for(DataSnapshot artistsnapshot:dataSnapshot.getChildren())
                {
                    firebaseDB=artistsnapshot.getValue(FirebaseDB.class);
                    firebaseDBList.add(firebaseDB);

                }

                Collections.reverse(firebaseDBList);
                FirebaseAdaptor adaptor=new FirebaseAdaptor(HistoryActivity.this,firebaseDBList);
                show_list.setAdapter(adaptor);
                adaptor.setOnItemClickListener(HistoryActivity.this);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"normal click"+position,Toast.LENGTH_LONG).show();
        String image=firebaseDBList.get(position).getBitmap();
        byte[] decodeImage = Base64.decode(image,Base64.DEFAULT);
        GallaryActivity.bitmap = BitmapFactory.decodeByteArray(decodeImage,0,decodeImage.length);
        Intent intent=new Intent(HistoryActivity.this,ImageEditActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onWhateverClick(int position) {
        Toast.makeText(this,"whatever click"+position,Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Text");

        final EditText input = new EditText(this);

        final String item_value = firebaseDBList.get(position).getName();
        final int pos=position;
        input.setText(item_value);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setGravity(Gravity.LEFT | Gravity.TOP);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                textrecog.child(firebaseDBList.get(pos).getId()).child("name").setValue(input.getText().toString());

            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this,"delete click"+position,Toast.LENGTH_LONG).show();
        textrecog.child(firebaseDBList.get(position).getId()).removeValue();
    }
}



