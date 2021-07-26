package com.games.ms.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.games.ms.Homepage;
import com.games.ms.R;
import com.games.ms.model.RedeemModel;

import java.util.ArrayList;

public class UserRedeemHistoryAdapter extends RecyclerView.Adapter<UserRedeemHistoryAdapter.myholder> {

    Homepage homepage;
    ArrayList<RedeemModel> winnigmodelArrayList;

    public UserRedeemHistoryAdapter(Homepage homepage, ArrayList<RedeemModel> winnigmodelArrayList) {
        this.homepage = homepage;
        this.winnigmodelArrayList = winnigmodelArrayList;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(homepage).inflate(R.layout.redeem_itemview, parent, false);

        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {

        //Picasso.with(homepage).load(Const.IMGAE_PATH + winnigmodelArrayList.get(position).getUserimage()).into(holder.imgprofile);

        holder.txtname.setText("" + winnigmodelArrayList.get(position).getUser_name());
        holder.txttotalwin.setText("₹" + winnigmodelArrayList.get(position).getCoin());




    }

    @Override
    public int getItemCount() {
        return winnigmodelArrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder {

        ImageView imgprofile, imgreward;
        TextView txtname, txttotalwin;

        public myholder(@NonNull View itemView) {
            super(itemView);

            imgprofile = itemView.findViewById(R.id.imgProfile);
            txtname = itemView.findViewById(R.id.txtusername);
            txttotalwin = itemView.findViewById(R.id.txtammount);
            imgreward = itemView.findViewById(R.id.imgreward);

        }
    }
}
