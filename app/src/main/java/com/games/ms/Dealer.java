package com.games.ms;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Dealer {

    public interface DealerInterface{

        void onClick(int pos);

    }

    @DrawableRes
    public static final int[] dealerImages = new int[]{
            R.drawable.poker_man,
            R.drawable.poker1,
            R.drawable.poker2,
            R.drawable.poker3,
            R.drawable.poker4,
            R.drawable.poker5,
            R.drawable.poker6,
            R.drawable.poker7,
            R.drawable.poker8,
            R.drawable.poker9,
            R.drawable.poker10,
            R.drawable.poker11,
            R.drawable.poker12
     };

   public int currentDealerPos = 0;
   public int tips =0;
   public long timeStamp = System.currentTimeMillis();

    public void showDialog(final Context context, int rowCount, final CallBack callBack) {
        LinearLayout lnrHorizontal = null;

        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dealers_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        RecyclerView rec_deler = dialog.findViewById(R.id.rec_deler);
        rec_deler.setLayoutManager(new GridLayoutManager(context,3));
        rec_deler.setAdapter(new DealerAdapter(context, new DealerInterface() {
            @Override
            public void onClick(int position) {
                int pos = (int) position;
                currentDealerPos = pos;
                timeStamp = System.currentTimeMillis();
                tips = 0;
                dialog.dismiss();
                callBack.onDealerChanged(dealerImages[currentDealerPos]);
            }
        }));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        LinearLayout lnrVertical = dialog.findViewById(R.id.lnrVertical);

        for (int i = 0; i < dealerImages.length; i++) {

            if (i == 0 || i % rowCount == 0) {

                if (lnrHorizontal != null) {
                    lnrVertical.addView(lnrHorizontal);
                }

                lnrHorizontal = new LinearLayout(context);
                lnrHorizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                lnrHorizontal.setOrientation(LinearLayout.HORIZONTAL);
            }

            View view = LayoutInflater.from(context).inflate(R.layout.dealer_imageview, null);
            view.setTag(i);

            ImageView dealerImageView = view.findViewById(R.id.dealerImageView);
            dealerImageView.setImageDrawable(context.getDrawable(dealerImages[i]));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    currentDealerPos = pos;
                    timeStamp = System.currentTimeMillis();
                    tips = 0;
                    dialog.dismiss();
                    callBack.onDealerChanged(dealerImages[currentDealerPos]);
                }
            });
            lnrHorizontal.addView(view);
        }

        if (lnrHorizontal != null) {
            lnrVertical.addView(lnrHorizontal);
        }

    }

   public interface CallBack {
        public void onDealerChanged(int drawable);
    }



    public class DealerAdapter extends RecyclerView.Adapter<DealerAdapter.myholder>{

        Context context;
        DealerInterface dealerInterface;
        public DealerAdapter(Context context,DealerInterface Interface) {
            this.context = context;
            dealerInterface = Interface;
        }

        @NonNull
        @Override
        public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.dealer_imageview,parent,false);
            return new myholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull myholder holder, final int position) {

            ImageView dealerImageView = holder.itemView.findViewById(R.id.dealerImageView);
            dealerImageView.setImageDrawable(context.getDrawable(dealerImages[position]));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dealerInterface.onClick(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return dealerImages.length;
        }

        class  myholder extends RecyclerView.ViewHolder{

            public myholder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

}
