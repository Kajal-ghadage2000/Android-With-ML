package com.example.envision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import java.util.List;
import java.util.Locale;
import dmax.dialog.SpotsDialog;

public class ImageLabeling extends AppCompatActivity {

    // static int buttonno;
    static Bitmap bitmap;
    static String readtext;

    CameraView cameraView;
    Button btntext;
    AlertDialog waitingDialog;
    TextView textview;

    TextToSpeech mtts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_labeling);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        waitingDialog=new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage("please wait")
                .setCancelable(false)
                .build();
        cameraView=(CameraView)findViewById(R.id.cameraview);

        btntext=(Button)findViewById(R.id.button5);
        btntext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();
                cameraView.captureImage();
                // detectText();
            }
        });
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                //waitingDialog.show();
                ImageLabeling.bitmap=cameraKitImage.getBitmap();
                ImageLabeling.bitmap=Bitmap.createScaledBitmap(ImageLabeling.bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                runDetector(ImageLabeling.bitmap);


            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        mtts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int result=mtts.setLanguage(Locale.ENGLISH);
                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","language not supported");
                    }
                    else {
                        Toast.makeText(ImageLabeling.this,"text speech start",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Log.e("TTS","initilization failed");
                }
            }
        });

    }

    private void runDetector(Bitmap bitmap) {
        FirebaseVisionOnDeviceImageLabelerOptions options = new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7f)
                .build();
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(ImageLabeling.bitmap);
        FirebaseVisionImageLabeler detector = FirebaseVision.getInstance().getOnDeviceImageLabeler(options);
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                extractLabel(labels);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                readtext=e.getMessage();
                Toast.makeText(ImageLabeling.this,readtext,Toast.LENGTH_LONG).show();

            }
        });

       /* new Helper(new Helper.Consumer() {

            @Override
            public void accept(boolean internet) {
                if(internet) {
                    FirebaseVisionCloudImageLabelerOptions options = new FirebaseVisionCloudImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build();
                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(ImageLabeling.bitmap);
                    FirebaseVisionImageLabeler detector = FirebaseVision.getInstance().getCloudImageLabeler(options);
                    detector.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            //Helper.dismissDialog();
                            extractLabel(labels);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Helper.dismissDialog();
                            readtext=e.getMessage();
                            Toast.makeText(ImageLabeling.this,readtext,Toast.LENGTH_LONG).show();

                        }
                    });
                }
                else
                {
                    FirebaseVisionOnDeviceImageLabelerOptions options = new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                            .setConfidenceThreshold(0.7f)
                            .build();
                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(ImageLabeling.bitmap);
                    FirebaseVisionImageLabeler detector = FirebaseVision.getInstance().getOnDeviceImageLabeler(options);
                    detector.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            extractLabel(labels);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            readtext=e.getMessage();
                            Toast.makeText(ImageLabeling.this,readtext,Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });*/
    }


    private void extractLabel(List<FirebaseVisionImageLabel> labels) {
        readtext="";
        String txt="";
        for (FirebaseVisionImageLabel label : labels) {
            readtext=readtext+ label.getText() + "\n";
            readtext=readtext+label.getConfidence() + "\n\n";
        }
        for (FirebaseVisionImageLabel label : labels) {
            txt=label.getText();
            break;
        }

        Toast.makeText(this,readtext,Toast.LENGTH_LONG).show();

        String data=txt;
        Log.i("TTS", "button clicked: " + data);
        if(data==null || data=="")
            data="no text found";
        int speechStatus = mtts.speak(data, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
