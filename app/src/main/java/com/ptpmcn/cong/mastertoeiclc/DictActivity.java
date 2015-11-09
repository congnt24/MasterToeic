package com.ptpmcn.cong.mastertoeiclc;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.ptpmcn.cong.dictionary.Dictionary;
import com.ptpmcn.cong.dictionary.IDictionaryHandler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;

public class DictActivity extends Activity implements IDictionaryHandler{
    private static final int REQUEST_IMAGE_CAPTURE = 10;
    private static final int REQUEST_SELECT_PHOTO= 11;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static TessBaseAPI baseApi;
    FrameLayout dict_container;
    private Dictionary dict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);
        initAPI();
        dict_container = (FrameLayout) findViewById(R.id.dict_container);
        dict = Dictionary.getInstance().init(this).setDefaultUi(dict_container, getLayoutInflater());
    }

    private void initAPI() {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                baseApi = new TessBaseAPI();
                baseApi.init(getFilesDir().getPath(), "eng");
                return null;
            }
        }.execute();

    }

    @Override
    public void onClickCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onClickGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT PHOTO"), REQUEST_SELECT_PHOTO);
    }

    @Override
    public void onClickSpeechToText() {
        promptSpeechInput();
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say something!!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn\'t support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            baseApi.setImage((Bitmap) extras.get("data"));
            String text = baseApi.getUTF8Text();
            Dictionary.getInstance().setSearchView(text);
        }else
        if (requestCode == REQUEST_SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                baseApi.setImage(bm);
                String text = baseApi.getUTF8Text();
                Dictionary.getInstance().setSearchView(text);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }else
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK){
            if (null != data) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                dict.setSearchView(result.get(0));
            }
        }
    }

    @Override
    protected void onStop() {
        dict.close();
        super.onStop();
    }
}

