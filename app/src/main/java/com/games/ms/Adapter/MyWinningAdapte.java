package com.games.ms.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.games.ms.R;
import com.games.ms.model.MyWinnigmodel;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyWinningAdapte extends RecyclerView.Adapter<MyWinningAdapte.Myholder> {

    Context context;
    ArrayList<MyWinnigmodel> myWinnigmodelArrayList;

    public MyWinningAdapte(Context context, ArrayList<MyWinnigmodel> myWinnigmodelArrayList) {

        this.context = context;
        this.myWinnigmodelArrayList = myWinnigmodelArrayList;

    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.winnig_itemview,parent,false);

        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        SharedPreferences prefs = context.getSharedPreferences("Login_data", MODE_PRIVATE);
        String name = prefs.getString("name", "");

        holder.txtid.setText("Table id : "+myWinnigmodelArrayList.get(position).table_id);
        holder.txtusername.setText(""+name);
        holder.txtammount.setText("amount : ₹"+myWinnigmodelArrayList.get(position).amount);

    }

    @Override
    public int getItemCount() {
        return myWinnigmodelArrayList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{

        TextView txtid,txtusername,txtammount;

        public Myholder(@NonNull View itemView) {
            super(itemView);

            txtid = itemView.findViewById(R.id.txtid);
            txtusername = itemView.findViewById(R.id.txtusername);
            txtammount = itemView.findViewById(R.id.txtammount);

        }
    }

}
