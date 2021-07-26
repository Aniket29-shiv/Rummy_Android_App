package com.games.ms.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.games.ms.Const;
import com.games.ms.Homepage;
import com.games.ms.MaiUserListingActivity;
import com.games.ms.R;
import com.games.ms.model.Usermodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class UserListingAdapter extends RecyclerView.Adapter<UserListingAdapter.MyHolder> {

    Context context;
    ArrayList<Usermodel> usermodelArrayList;

    public UserListingAdapter(Context context, ArrayList<Usermodel> usermodelArrayList) {
        this.context = context;
        this.usermodelArrayList = usermodelArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_itemview,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.txtname.setText(""+usermodelArrayList.get(position).userName);

        String userGender = usermodelArrayList.get(position).userGender;

        if(userGender.equals(""))
            holder.txtGender.setText("Gender : N/A");
        else
        holder.txtGender.setText("Gender : "+usermodelArrayList.get(position).userGender);

        Picasso.with(context).load(Const.IMGAE_PATH + usermodelArrayList.get(position).userImage).error(R.drawable.notfound).into(holder.imgProfile);

        final String MY_PREFS_NAME = "Login_data" ;
        final String id = usermodelArrayList.get(position).userid;
        final String name = usermodelArrayList.get(position).userName;
        final String mobile = usermodelArrayList.get(position).userMobile;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("user_id", id);
                editor.putString("name", name);
                editor.putString("mobile", mobile);
                editor.apply();

                Intent intent = new Intent(context, Homepage.class);
                context.startActivity(intent);
                ((MaiUserListingActivity)context).finishAffinity();

            }
        });

    }

    @Override
    public int getItemCount() {
        return usermodelArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView txtname,txtGender;
        ImageView imgProfile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            txtname = itemView.findViewById(R.id.txtname);
            txtGender = itemView.findViewById(R.id.txtGender);
            imgProfile = itemView.findViewById(R.id.imgProfile);

        }
    }

}
