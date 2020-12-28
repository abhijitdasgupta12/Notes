package com.example.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    ArrayList<Model> data_holder;
//    ArrayList<Model> backups;

    private Context context;

    public MyAdapter(ArrayList<Model> data_holder, Context context)
    {
        this.data_holder = data_holder;
//        backups=new ArrayList<>(data_holder);
        this.context= context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)
    {
        holder.title.setText(data_holder.get(position).getTitle());
        holder.desc.setText(data_holder.get(position).getDescription());

        /*********************************** UPDATE ***********************************/
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.edit.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialog_elements))
                        .setExpanded(true,800)
                        .create();

                /* Runtime Views */
                View view=dialogPlus.getHolderView();

                //TypeCasting from the "dialog_elements.xml"
                final EditText update_title=view.findViewById(R.id.titleEt1);
                final EditText update_desc=view.findViewById(R.id.descEt1);
                final Button update=view.findViewById(R.id.updateBtn);

                final int col_id= data_holder.get(position).getId();
                final Cursor cursors= new SQLite_DB_Manager(context.getApplicationContext()).fetchRecordsFromSelectedRow(col_id);

                while (cursors.moveToNext())
                {
                    update_title.setText(cursors.getString(1));
                    update_desc.setText(cursors.getString(2));
                }

                dialogPlus.show();

                /* Update Operation */
                update.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(cursors!=null &&  cursors.moveToFirst())
                        {
                            int res= new SQLite_DB_Manager(context).updateSpecificRow(String.valueOf(col_id), cursors.getString(1), cursors.getString(2));

                            Toast.makeText(context.getApplicationContext(), "res="+res, Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        /*********************************** DELETE ***********************************/
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.delete.getContext()); //Pass any view to continue
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete Note");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String res= new SQLite_DB_Manager(context.getApplicationContext()).deleteData(data_holder.get(position).getId());
                        Toast.makeText(context.getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data_holder.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, desc;
        ImageButton edit, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title= itemView.findViewById(R.id.titleTv);
            desc= itemView.findViewById(R.id.descTv);
            edit= itemView.findViewById(R.id.editBtn);
            delete= itemView.findViewById(R.id.deleteBtn);
        }
    }
}
