package edu.csulb.android.photonotesapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class AddPhotoActivity extends AppCompatActivity {

    String location="";
    Button button,save;
    ImageView imageView;
    EditText editText;
    private static final int CAMERA_REQUEST = 1888;
    int flag=0;
    Bitmap finalBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

        final DatabaseHelper sqLiteHelper = new DatabaseHelper(getApplicationContext());

        button = (Button)findViewById(R.id.button);
        save = (Button)findViewById(R.id.button2);
        editText = (EditText)findViewById(R.id.edittext);

        imageView = (ImageView)findViewById(R.id.imageView2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = editText.getText().toString();
                if(flag==1 && caption.length()!=0)
                {
                    saveImage();
                    Data dataGetSet = new Data();
                    dataGetSet.location = location;
                    dataGetSet.caption = editText.getText().toString();
                    sqLiteHelper.addtoList(dataGetSet);
                    Toast.makeText(getApplicationContext(), "Photo Note Added!", Toast.LENGTH_LONG).show();
                    onBackPressed();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please take image and add caption to save.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            finalBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(finalBitmap);
            button.setText("Retake");
            flag=1;
        }
    }

    private void saveImage() {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/PhotoNotes");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            location = file.getAbsolutePath();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
