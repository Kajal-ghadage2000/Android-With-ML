package com.example.envision;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.envision.GraphicUtils.GraphicOverlay;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.envision.GraphicUtils.GraphicOverlay;
import com.example.envision.GraphicUtils.TextGraphic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class GallaryActivity extends AppCompatActivity {
    ImageView viewImage;
    static  Bitmap bitmap;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);

        viewImage=(ImageView)findViewById(R.id.imageView1);
        viewImage.setImageResource(R.drawable.bg3);
        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        Toast.makeText(this,"Click on image to choose picture from gallary...",Toast.LENGTH_LONG).show();
        Toast.makeText(this,"Click on image to choose picture from gallary...",Toast.LENGTH_LONG).show();
    }

    private void selectImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 2) {
             if (data != null) {
                 Uri contentURI = data.getData();
                 try {
                     Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                     String path = saveImage(bitmap);
                     Toast.makeText(GallaryActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                     viewImage.setImageBitmap(bitmap);
                     GallaryActivity.bitmap=bitmap;
                     Intent intent=new Intent(GallaryActivity.this,ImageEditActivity.class);
                     //intent.putExtra("image",GallaryActivity.bitmap);
                     startActivity(intent);
                     finish();
                 } catch (IOException e) {
                     e.printStackTrace();
                     Toast.makeText(GallaryActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                 }
             }

         }

    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
