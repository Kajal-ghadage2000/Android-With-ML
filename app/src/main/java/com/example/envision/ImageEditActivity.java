package com.example.envision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import com.example.envision.MainActivity;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageEditActivity extends AppCompatActivity {

    EditText result;
    TextToSpeech mtts;
    String path;
    Bitmap bitmap=null;
    ImageView seeimg,DisplayimageIV,DisplaytextIV;
    TextView textviewor;
    static int textorimage=1;
    DataBaseHelper dataBaseHelper;
    DatabaseReference textrecog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);


        textrecog=FirebaseDatabase.getInstance().getReference("mytextrecognizer");

        //Bundle ex = getIntent().getExtras();
        //bitmap = ex.getParcelable("image");
        dataBaseHelper=new DataBaseHelper(this);
        bitmap=GallaryActivity.bitmap;

        seeimg=(ImageView)findViewById(R.id.SeeImage);
        seeimg.setImageBitmap(bitmap);
        DisplaytextIV=(ImageView)findViewById(R.id.displaytextviewIV);
        DisplayimageIV=(ImageView)findViewById(R.id.displayimageviewIV);

        textviewor=(TextView)findViewById(R.id.textViewOR);
        result=(EditText)findViewById(R.id.editText4);
        runTextRecognition();


        mtts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int result=mtts.setLanguage(Locale.ENGLISH);
                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","language not supported");
                    }
                    else {
                        Toast.makeText(ImageEditActivity.this,"text speech start",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Log.e("TTS","initilization failed");
                }
            }
        });


    }

    private void initSave(){
        String data1 = result.getText().toString();
        if (data1 != "error_not_found" && data1 != null && bitmap != null) {

            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] b = baos.toByteArray();
//            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            String name = result.getText().toString();
            String email = currentDateTimeString;
//            String eimage=encodedImage;

            RegistrationDataModel regDmodel = new RegistrationDataModel();
            regDmodel.setName(name);
            regDmodel.setEmail(email);
 //           regDmodel.setBitmap(b);
 //           regDmodel.setImage(eimage);
            //regDmodel.setImage(bitmap);
            dataBaseHelper.addRegistrationData(regDmodel);
            Toast.makeText(ImageEditActivity.this, "Data Inserted successfully", Toast.LENGTH_SHORT).show();

        }
    }

    private void runTextRecognition() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText texts) {
                processTextRecognitionResult(texts);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void processTextRecognitionResult(FirebaseVisionText firebaseVisionText) {
        //runTextRecognition();
        result.setText(null);
        if (firebaseVisionText.getTextBlocks().size() == 0) {
            result.setText("");
            Toast.makeText(this,"No text found",Toast.LENGTH_LONG).show();
            Toast.makeText(this,"No text found",Toast.LENGTH_LONG).show();
            return;
        }
        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
            //result.append("\n\n");
			for (FirebaseVisionText.Line line: block.getLines()) {
			    result.append("\n");
				for (FirebaseVisionText.Element element: line.getElements()) {
					result.append(element.getText() + " ");
				}
			}

        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.speechTotext:


                String data = result.getText().toString();
                Log.i("TTS", "button clicked: " + data);
                if (data == null || data == "")
                    data = "no text found";
                int speechStatus = mtts.speak(data, TextToSpeech.QUEUE_FLUSH, null);
                if (speechStatus == TextToSpeech.ERROR) {
                    Log.e("TTS", "Error in converting Text to Speech!");
                }

                break;
            case R.id.CopyToClip:

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("textfromImage", result.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this,"Copied to clipboard...",Toast.LENGTH_LONG).show();

                break;
            case R.id.displayimageviewIV:

                if (textorimage == 1) {
                    result.setVisibility(View.GONE);            //above notepad
                    seeimg.setVisibility(View.VISIBLE);
                    DisplaytextIV.setVisibility(View.VISIBLE);
                    DisplayimageIV.setVisibility(View.GONE);
                    textviewor.setText("View Text ");
                    textorimage = 0;
                }

                break;
            case R.id.displaytextviewIV:

                if (textorimage == 0) {
                    result.setVisibility(View.VISIBLE);            //above notepad
                    seeimg.setVisibility(View.GONE);
                    DisplaytextIV.setVisibility(View.GONE);
                    DisplayimageIV.setVisibility(View.VISIBLE);
                    textviewor.setText("View Image");
                    textorimage = 1;
                }
                break;
            case R.id.TranslatorImg:
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, result.getText().toString());
                    intent.putExtra("key_text_input", result.getText().toString());
                    intent.putExtra("key_text_output", "");
                    intent.putExtra("key_language_from", "en");
                    intent.putExtra("key_language_to", "mal");
                    intent.putExtra("key_suggest_translation", "");
                    intent.putExtra("key_from_floating_window", false);
                    intent.setComponent(new ComponentName(
                            "com.google.android.apps.translate",
                            //Change is here
                            //"com.google.android.apps.translate.HomeActivity"));
                            "com.google.android.apps.translate.TranslateActivity"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplication(), "Sorry, No Google Translation Installed",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ShareIV:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = result.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Text Read from image");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                break;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_camera:
                //initSave();
                firebasedb();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    void firebasedb()
    {
       // if(!TextUtils.isEmpty(result.getText().toString()))
        {

            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao); // bmp is bitmap from user image file
            bitmap.recycle();
            byte[] byteArray = bao.toByteArray();
            String imageB64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            
            
            String id=textrecog.push().getKey();
            FirebaseDB firebaseDB=new FirebaseDB(id,currentDateTimeString,result.getText().toString(),imageB64);

            textrecog.child(id).setValue(firebaseDB);

            Toast.makeText(this,"data added",Toast.LENGTH_LONG).show();
        }
    }


}
