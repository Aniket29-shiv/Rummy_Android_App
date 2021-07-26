package com.games.ms.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.games.ms.Const;
import com.games.ms.R;
import com.games.ms.model.Chats;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.myholder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<Chats> chats;
    String fuser;

    public MessageAdapter(Context context, List<Chats> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == MSG_TYPE_LEFT)
        {
            view  = LayoutInflater.from(context).inflate(R.layout.chat_itemview_left,parent,false);
        }
        else {
            view  = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
        }

        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {

        Chats mchats = chats.get(position);
        holder.show_message.setText(""+mchats.getMessage());

        if(chats.get(position).getSender().equals(fuser))
        Picasso.with(context).load(Const.IMGAE_PATH + mchats.getSenderImage()).into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class myholder extends RecyclerView.ViewHolder{

        TextView show_message;
        ImageView profileImage;

        public myholder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.txtMessage);
            profileImage = itemView.findViewById(R.id.img_profile);
        }
    }

    @Override
    public int getItemViewType(int position) {

        SharedPreferences prefs = context.getSharedPreferences("Login_data", MODE_PRIVATE);
        fuser = prefs.getString("user_id", "");

        if(chats.get(position).getSender().equals(fuser))
        {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }

    }
}
