package com.games.ms.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.games.ms.Interface.itemClick;
import com.games.ms.R;
import com.games.ms.model.WelcomeModel;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class WelcomeRewardAdapter extends RecyclerView.Adapter<WelcomeRewardAdapter.myholder> {

    Context context;
    RecyclerView recyclerView;
    ArrayList<WelcomeModel> welcomeList ;
    com.games.ms.Interface.itemClick itemClick;


    int height[] = {200,225,250,275,300};

    public WelcomeRewardAdapter(Context context, ArrayList<WelcomeModel> welcomeList, itemClick itemClick) {
        this.context = context;
        this.welcomeList = welcomeList;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.reward_itemview,parent,false);
        return new myholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final myholder holder, final int position) {

//        holder.parentLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                DpToPx((Activity) context, height[position])));

        int numberamount = Integer.parseInt(welcomeList.get(position).getCoins());
        int collecteddays = Integer.parseInt(welcomeList.get(position).getCollected_days());

        holder.txtcoins.setText("Rs " + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
        holder.txtdays.setText("Days " + welcomeList.get(position).getDay());

         int pos1 = position + 1;

         if(pos1 <= collecteddays)
         {
             holder.rlt_collected.setVisibility(View.VISIBLE);
         }
         else {
             holder.rlt_collected.setVisibility(View.GONE);
         }

        Picasso.with(context).load(welcomeList.get(position).getImgcoins()).into(holder.imgreward);



        holder.imgreward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.imgreward.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake_animation));

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//
//                        showWinDialog();
//                    }
//                },1000);

            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.OnClick(welcomeList.get(position).getId(),"");
            }
        });

    }

    @Override
    public int getItemCount() {
        return welcomeList.size();
    }

    class myholder extends RecyclerView.ViewHolder{

        ImageView imgreward;
        TextView txtcoins,txtdays;
        View parentLayout,rlt_collected;

        public myholder(@NonNull View itemView) {
            super(itemView);

            imgreward = itemView.findViewById(R.id.imgreward);
            txtcoins = itemView.findViewById(R.id.txtcoins);
            txtdays = itemView.findViewById(R.id.txtdays);
            parentLayout = itemView.findViewById(R.id.parentlayout);
            rlt_collected = itemView.findViewById(R.id.rlt_collected);

        }
    }

    public int DpToPx(Activity activity, int dp){
        Resources r  = activity.getResources();
        int px    = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;

    }

    public static void showWinDialog(Context context,String testmessage) {
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool  = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {

            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        final int sound =  soundPool.load(context,R.raw.ta_da,1);
        soundPool.play(sound,1,1,0,0,1);

        // custom dialog
        final Dialog dialog = new Dialog(context,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_wincoins);
        dialog.setTitle("Title...");
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclosetop);
        TextView txtwincoins = (TextView) dialog.findViewById(R.id.txtwincoins);

        txtwincoins.setText("Congratulations you win"+ testmessage +" coins!");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },2500);

        dialog.show();
    }



}
