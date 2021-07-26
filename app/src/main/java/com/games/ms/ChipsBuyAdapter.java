package com.games.ms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChipsBuyAdapter extends RecyclerView.Adapter<ChipsBuyAdapter.ViewHolder> {
    Activity context;

    ArrayList<ChipsBuyModel> historyModelArrayList;


    public ChipsBuyAdapter(Activity context, ArrayList<ChipsBuyModel> historyModelArrayList) {
        this.context = context;
        this.historyModelArrayList = historyModelArrayList;
       
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chips_buy_layout,parent,false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChipsBuyModel model = historyModelArrayList.get(position);
        ((ViewHolder) holder).bind(model,position);
        
       
    }
    
    @Override
    public int getItemCount() {
        
        return historyModelArrayList.size();
        
    }
    
    
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_amount,txt_date,
        txtAmount,txtproname;
       ImageView imgbuy,imalucky;
        RelativeLayout rel_layout;
        Typeface helvatikabold, helvatikanormal;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgbuy = itemView.findViewById(R.id.imgbuy);
            imalucky = itemView.findViewById(R.id.imalucky);
            //txtDescription = itemView.findViewById(R.id.txtDescription);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtproname = itemView.findViewById(R.id.txtproname);
            rel_layout = itemView.findViewById(R.id.rel_layout);
            helvatikabold = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Helvetica-Bold.ttf");


            //txt_date = itemView.findViewById(R.id.txt_date);
            
        }
        
        public void bind(final ChipsBuyModel model , int position){
            int val = position%2;



            // " +

            String uri2 = "";
            if (val == 1){
                 uri2 = "@drawable/bulkchipsgreen";  // where myresource

            }else {
                 uri2 = "@drawable/bulkchipsred";  // where myresource
            }

            uri2 = "@drawable/ic_buychips";  // where myresource

            int imageResource2 = context.getResources().getIdentifier(uri2,
                    null,
                    context.getPackageName());

            Picasso.with(context).load(imageResource2).into(imalucky);

            txtproname.setText(model.getProname()+" Chips for Game Play");
            txtAmount.setText("₹"+model.getAmount());
            txtproname.setTypeface(helvatikabold);
            txtAmount.setTypeface(helvatikabold);
            //txtproname.setText(model.getProname());
            rel_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context , BuyChipsDetails.class);
                    intent.putExtra("plan_id",model.getId());
                    intent.putExtra("chips_details",model.getProname());
                    intent.putExtra("amount",model.getAmount());
                    context.startActivity(intent);
                }
            });

            imgbuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, ""+model.getProname(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context , BuyChipsDetails.class);
                    intent.putExtra("plan_id",model.getId());
                    intent.putExtra("chips_details",model.getProname());
                    intent.putExtra("amount",model.getAmount());
                    context.startActivity(intent);
                }
            });

        }
    }
    
}

