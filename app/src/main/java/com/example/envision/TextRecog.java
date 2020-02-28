package com.example.envision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.example.envision.GraphicUtils.GraphicOverlay;
import com.example.envision.GraphicUtils.TextGraphic;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import com.example.envision.GraphicUtils.GraphicOverlay.Graphic;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TextRecog extends AppCompatActivity {

    Bitmap bitmap;
    private static String readtext;
    @BindView(R.id.camView) CameraView mCameraView;
    @BindView(R.id.cameraBtn) Button mCameraButton;
    @BindView(R.id.graphic_overlay) GraphicOverlay mGraphicOverlay;
    TextToSpeech mtts;
    int current=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recog);

        ButterKnife.bind(TextRecog.this);

        mCameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, mCameraView.getWidth(), mCameraView.getHeight(), false);
                mCameraView.stop();
                if(current==1)
                runTextRecognition(bitmap);
                else {
                    GallaryActivity.bitmap=bitmap;
                    Intent intent=new Intent(TextRecog.this,ImageEditActivity.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current=1;
                mGraphicOverlay.clear();
                mCameraView.start();
                mCameraView.captureImage();
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
                        Toast.makeText(TextRecog.this,"text speech start",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Log.e("TTS","initilization failed");
                }
            }
        });
    }

    private void runTextRecognition(Bitmap bitmap) {
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
//
//        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
//        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
//        detector.detectInImage(image)
//                .addOnSuccessListener(
//                        new OnSuccessListener<FirebaseVisionText>() {
//                            @Override
//                            public void onSuccess(FirebaseVisionText texts) {
//                                processTextRecognitionResult(texts);
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Task failed with an exception
//                                e.printStackTrace();
//                            }
//                        });
    }

    private void processTextRecognitionResult(FirebaseVisionText firebaseVisionText) {
        String txt="";
        if (firebaseVisionText.getTextBlocks().size() == 0) {
            Log.d("TAG", "No text found");
            txt="No text found";
        }
        mGraphicOverlay.clear();
        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
			for (FirebaseVisionText.Line line: block.getLines()) {
				for (FirebaseVisionText.Element element: line.getElements()) {
                    GraphicOverlay.Graphic textGraphic = new TextGraphic(mGraphicOverlay, element);
                    mGraphicOverlay.add(textGraphic);

                }
			}

        }
//
//        List<FirebaseVisionText.Block> blocks = texts.getBlocks();
//        if (blocks.size() == 0) {
//            Log.d("TAG", "No text found");
//            txt="No text found";
//            //return;
//        }
//        mGraphicOverlay.clear();
//        for (int i = 0; i < blocks.size(); i++) {
//            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
//            for (int j = 0; j < lines.size(); j++) {
//                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
//                for (int k = 0; k < elements.size(); k++) {
//                    GraphicOverlay.Graphic textGraphic = new TextGraphic(mGraphicOverlay, elements.get(k));
//                    mGraphicOverlay.add(textGraphic);
//
//                }
//            }
//        }
//
        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()){
            txt=txt+block.getText();
        }

        TextRecog.readtext=txt;
        String data =TextRecog.readtext;
        Log.i("TTS", "button clicked: " + data);
        if(data==null || data=="")
            data="no text found";
        int speechStatus = mtts.speak(data, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    public void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    public void callImageEdit(View view) {
        current=0;
        mGraphicOverlay.clear();
        mCameraView.start();
        mCameraView.captureImage();
    }
}
