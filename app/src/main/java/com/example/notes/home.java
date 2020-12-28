package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class home extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Model> dataholders;
    Model model;
    Cursor cursor_obj;
    MyAdapter myAdapter;
    ImageView no_record_ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView= findViewById(R.id.recyclerView);
        no_record_ImageView= findViewById(R.id.imageView);

        //Setting up layoutmanager for recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Fetching data. Syntax: Cursor obj= new sample_class(this).Method_Implemented_Inside_"sample_class"_to_Fetch_Data;
        Cursor cursors= new SQLite_DB_Manager(this).fetchRecords();
        dataholders=new ArrayList<>();

        while (cursors.moveToNext())//reading data one by one
        {
//            /*******************
//            Syntax: Sample_Class obj= new Sample_Class(obj_of_Cursor.getString(0),obj_of_Cursor.getString(1),...);
//            getString(0) = 0th index or 1st column of the database
//            getString(1) = 1st index or 2nd column of the database etc.
//             *******************/
            model=new Model(cursors.getInt(0),cursors.getString(1), cursors.getString(2));
            dataholders.add(model);
        }

        //Setting up adapter
        MyAdapter myAdapter=new MyAdapter(dataholders,getApplicationContext());
        recyclerView.setAdapter(myAdapter);

        if (myAdapter.getItemCount() == 0)
        {
            no_record_ImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            no_record_ImageView.setVisibility(View.INVISIBLE);
        }

    }

    /************* MENU ITEMS *************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menus,menu);
        menu.findItem(R.id.home).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search:

                break;

            case R.id.refresh:
                finish();
                startActivity(getIntent());
                break;

            case R.id.addNote:
                startActivity(new Intent(getApplicationContext(),addNotes.class));
                finish();
                break;
            case R.id.about:
                AlertDialog.Builder builder=new AlertDialog.Builder(this); //Pass any view to continue
                builder.setIcon(R.drawable.ic_baseline_info_24);
                builder.setTitle("About");
                builder.setMessage("'Notes' is an Android Application that enables the user to save notes in device.\nDeveloper: Abhijit Dasgupta");
                builder.setNegativeButton("Ok", null);
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}