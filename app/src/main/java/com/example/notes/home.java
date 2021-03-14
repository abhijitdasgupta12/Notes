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
import android.widget.SearchView;
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
        myAdapter=new MyAdapter(dataholders,getApplicationContext());
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

        MenuItem searchs, refresh, addnote, about1;

        searchs= menu.findItem(R.id.search);
        refresh= menu.findItem(R.id.refresh);
        addnote= menu.findItem(R.id.addNote);
        about1= menu.findItem(R.id.about);
        
        SearchView searchView= (SearchView)searchs.getActionView(); //This will create a textbox when search option is clicked
        //This part of code will perform action when something is typed in the search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });

        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                startActivity(getIntent());
                return false;
            }
        });

        addnote.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                 startActivity(new Intent(getApplicationContext(),addNotes.class));
                 finish();
                 return false;
            }
        });

        about1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder=new AlertDialog.Builder(home.this); //Pass any view to continue
                builder.setIcon(R.drawable.ic_baseline_info_24);
                builder.setTitle("About");
                builder.setMessage("'Notes' is an Android Application that enables the user to save notes in device.\nThis application was developed during my learning process & for non-profit and personal use only. Some of the layout design idea was taken from Internet.");
                builder.setNegativeButton("Ok", null);
                builder.show();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

//    private void showAlert()
//    {
//        AlertDialog.Builder builder=new AlertDialog.Builder(this); //Pass any view to continue
//        builder.setIcon(R.drawable.ic_baseline_info_24);
//        builder.setTitle("About");
//        builder.setMessage("'Notes' is an Android Application that enables the user to save notes in device.\nThis application was developed during my learning process & for non-profit and personal use only. Some of the layout design idea was taken from Internet.");
//        builder.setNegativeButton("Ok", null);
//        builder.show();
//    }

}
