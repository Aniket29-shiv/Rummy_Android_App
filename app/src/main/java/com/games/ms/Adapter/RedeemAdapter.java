package com.games.ms.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;
import com.squareup.picasso.Picasso;
import com.games.ms.ChipsBuyModel;
import com.games.ms.Const;
import com.games.ms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RedeemAdapter extends RecyclerView.Adapter<RedeemAdapter.ViewHolder> {
    Activity context;
    private static final String MY_PREFS_NAME = "Login_data";

    ArrayList<ChipsBuyModel> historyModelArrayList;
    ProgressDialog progressDialog;


    public RedeemAdapter(Activity context, ArrayList<ChipsBuyModel> historyModelArrayList) {
        this.context = context;
        this.historyModelArrayList = historyModelArrayList;

    }

    @NonNull
    @Override
    public RedeemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.redeem_buy_layout, parent, false);
        return new RedeemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RedeemAdapter.ViewHolder holder, int position) {
        final ChipsBuyModel model = historyModelArrayList.get(position);
        progressDialog = new ProgressDialog(context);

        ((RedeemAdapter.ViewHolder) holder).bind(model, position);


    }

    @Override
    public int getItemCount() {

        return historyModelArrayList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_amount, txt_date,
                txtAmount, txtproname;
        ImageView imalucky;
        RelativeLayout rel_layout;
        Typeface helvatikabold, helvatikanormal;
        Button imgbuy;

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

        public void bind(final ChipsBuyModel model, int position) {
            int val = position % 2;


            // " +

            String uri2 = "";
            if (val == 1) {
                uri2 = "@drawable/bulkchipsgreen";  // where myresource

            } else {
                uri2 = "@drawable/bulkchipsred";  // where myresource
            }

            uri2 = "@drawable/ic_buychips";  // where myresource

            int imageResource2 = context.getResources().getIdentifier(uri2,
                    null,
                    context.getPackageName());

            Picasso.with(context).load(imageResource2).into(imalucky);

            txtproname.setText(model.getProname() + " Coins for Game Play");
            txtAmount.setText("₹" + model.getAmount());
            txtproname.setTypeface(helvatikabold);
            txtAmount.setTypeface(helvatikabold);
            //txtproname.setText(model.getProname());
            rel_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getChipsList(model.getId());
                }
            });

            imgbuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, ""+model.getProname(), Toast.LENGTH_SHORT).show();
                    getChipsList(model.getId());
                }
            });

        }
    }


    public void getChipsList(final String id) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.SEND_USER_REDEEM_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equals("200")) {
                                progressDialog.dismiss();

                                ShowDialogs();
                               /* Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                context.finish();*/

                            } else {
                                // Toast.makeText(HistoryActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                HashMap<String, String> params = new HashMap<>();
                params.put("token", prefs.getString("token", ""));
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("redeem_id", id);
                params.put("mobile", prefs.getString("mobile", ""));
                //params.put("user_id", SharedPref.getVal(HistoryActivity.this,SharedPref.id));
                Log.d("parameter", "getParams: " + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void ShowDialogs() {


        new SmartDialogBuilder(context)
                .setTitle("Successfully Withdrawn")
                //.setSubTitle("This is the alert dialog to showing alert to user")
                .setCancalable(true)
                //.setTitleFont("Do you want to Logout?") //set title font
                // .setSubTitleFont(subTitleFont) //set sub title font
                .setNegativeButtonHide(true) //hide cancel button
                .setPositiveButton("Ok", new SmartDialogClickListener() {
                    @Override
                    public void onClick(SmartDialog smartDialog) {
                        // Toast.makeText(context,"Ok button Click",Toast.LENGTH_SHORT)

                        context.finish();

                        smartDialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new SmartDialogClickListener() {
            @Override
            public void onClick(SmartDialog smartDialog) {
                // Toast.makeText(context,"Cancel button Click",Toast.LENGTH_SHORT).show();
                smartDialog.dismiss();

            }
        }).build().show();
    }


}

