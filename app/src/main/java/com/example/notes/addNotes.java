package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addNotes extends AppCompatActivity
{
    EditText titleEditText, descEditText;
    Button addButton;
    AlertDialog.Builder builder;//Pass any view to continue

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        setTitle("Description");

        titleEditText= findViewById(R.id.titleEt);
        descEditText= findViewById(R.id.descEt);
        addButton= findViewById(R.id.addBtn);

        builder=new AlertDialog.Builder(this); //Pass any view to continue

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEditText.getText().toString().isEmpty() || descEditText.getText().toString().isEmpty())
                {
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setTitle("No Data");
                    builder.setMessage("Note adding failed! Title or Description can not be empty!");
                    builder.setNegativeButton("Ok", null);
                    builder.show();
                }
                else
                {
                    addRecord(titleEditText.getText().toString(), descEditText.getText().toString());
                    startActivity(new Intent(getApplicationContext(), home.class));
                    finish();
                }
            }
        });


    }

//    if (new Intent().getIntExtra("index",1) > 0)
//    {
//        col_id= intent.getIntExtra("index",0);
//
//        final Cursor cursors= new SQLite_DB_Manager(this).fetchRecordsFromSelectedRow(col_id);
//
//        while (cursors.moveToNext())
//        {
//            titleEditText.setText(cursors.getString(1));
//            descEditText.setText(cursors.getString(2));
//        }
//
//        addButton.setEnabled(false);
//        updateButton.setEnabled(true);
//
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SQLite_DB_Manager(getApplicationContext()).updateSpecificRow(col_id, cursors.getString(1), cursors.getString(2));
//                Toast.makeText(addNotes.this, "", Toast.LENGTH_SHORT).show();
//                updateButton.setEnabled(false);
//                startActivity(new Intent(getApplicationContext(), home.class));
//                finish();
//            }
//        });
//
//    }

    private void addRecord(String title, String desc)
    {
        String result= new SQLite_DB_Manager(this).addRecord(title,desc);

        titleEditText.setText("");
        descEditText.setText("");

        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    /************* MENU ITEMS *************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menus,menu);
        menu.findItem(R.id.search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.addNote:
                startActivity(new Intent(getApplicationContext(),addNotes.class));
                finish();
                break;
            case R.id.about:
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