package edu.csulb.android.photonotesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView lv;
    DatabaseHelper sqLiteHelper;
    ArrayList<Data> dgss;
    private static final int PER_REQUEST_CODE1 = 100;
    private static final int PER_REQUEST_CODE2 = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try{
            if(ActivityCompat.checkSelfPermission(ListActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PER_REQUEST_CODE1);
            }
            else if(ActivityCompat.checkSelfPermission(ListActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ListActivity.this, new String[]{Manifest.permission.CAMERA}, PER_REQUEST_CODE2);
            }
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }

        lv = (ListView) findViewById(R.id.lv);
        sqLiteHelper = new DatabaseHelper(getApplicationContext());


        dgss = new ArrayList<Data>();

        showlist();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListActivity.this, ViewPhotoActivity.class);
                i.putExtra("id",dgss.get(position).id);
                startActivity(i);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                sqLiteHelper.deletePhoto(dgss.get(position).id);
                showlist();
                return false;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(ListActivity.this, AddPhotoActivity.class);
                startActivityForResult(i, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showlist();
    }

    private void showlist()
    {
        dgss = sqLiteHelper.getList();

        ArrayList<String> captions = new ArrayList<String>();

        if(dgss.size()==0)
        {
            lv.setAdapter(null);
            Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_LONG);
        }
        else
        {
            for(int i=0;i<dgss.size();i++)
                captions.add(dgss.get(i).caption);
            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,captions);
            lv.setAdapter(adp);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PER_REQUEST_CODE1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Your permission
                ActivityCompat.requestPermissions(ListActivity.this, new String[]{Manifest.permission.CAMERA}, PER_REQUEST_CODE2);
            }
            else
            {
                Toast.makeText(this, "Please provide appropriate permissions to avoid any unexpected behavior.", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == PER_REQUEST_CODE2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
             //Done
            }
            else {
                Toast.makeText(this, "Please provide appropriate permissions to avoid any unexpected behavior.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
