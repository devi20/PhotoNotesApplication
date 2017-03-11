package edu.csulb.android.photonotesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPhotoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);

        Intent i = getIntent();
        String id = i.getStringExtra("id");

        DatabaseHelper sqLiteHelper = new DatabaseHelper(getApplicationContext());
        Data dgs = new Data();

        dgs = sqLiteHelper.getPhoto(id);

        Bitmap myBitmap = BitmapFactory.decodeFile(dgs.location);

        imageView.setImageBitmap(myBitmap);

        textView.setText(dgs.caption);

        dgs = sqLiteHelper.getPhoto(id);



    }
}
