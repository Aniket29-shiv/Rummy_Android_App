package com.games.ms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class BuyChipsDetails extends AppCompatActivity implements PaymentResultListener {
    private static final String MY_PREFS_NAME = "Login_data" ;
Button btnPaynow;
TextView txtChipsdetails;
String plan_id = "";
String chips_details = "";
String amount = "";
String RazorPay_ID = "";
private String order_id;
ImageView imgback,imgPaynow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chips_details);
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");
        chips_details = intent.getStringExtra("chips_details");
        amount = intent.getStringExtra("amount");

        imgPaynow = findViewById(R.id.imgPaynow);
        txtChipsdetails = findViewById(R.id.txtChipsdetails);
        txtChipsdetails.setText("Buy "+chips_details+" Pay now Rs."+amount);

        imgPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  place_order();
            }
        });
        imgback = findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void place_order(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Const.PLCE_ORDER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code=jsonObject.getString("code");
                            String message=jsonObject.getString("message");

                            if (code.equals("200")){

                                order_id=jsonObject.getString("order_id");
                                String Total_Amount=jsonObject.getString("Total_Amount");
                                RazorPay_ID=jsonObject.getString("RazorPay_ID");
                                startPayment(order_id,Total_Amount,RazorPay_ID);
                            }
                            else  if (code.equals("404")) {
                                Toast.makeText(BuyChipsDetails.this, ""+message, Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header= new HashMap<>();
                header.put("token",Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params= new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("plan_id", plan_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    public void startPayment( String ticket_id, String total_Amount, String razorPay_ID) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

            JSONObject options = new JSONObject();
            options.put("name",  prefs.getString("name", ""));
            options.put("description", "chips payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", total_Amount);
            options.put("order_id", razorPay_ID);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "care@orsoot.com");
            preFill.put("contact",  prefs.getString("mobile", ""));
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }



    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            payNow(razorpayPaymentID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            //Toast.makeText(this, "Payment failed: " + code + " " + response, Toast
            // .LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e(TAG, "Exception in onPaymentError", e);
        }
    }


    public void payNow(final String payment_id){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Const.PY_NOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code=jsonObject.getString("code");
                            String message=jsonObject.getString("message");

                            if (code.equals("200")){
                                Toast.makeText(BuyChipsDetails.this, ""+message, Toast.LENGTH_SHORT).show();
                                dialog_payment_success();
                            }
                            else  if (code.equals("404")) {
                                Toast.makeText(BuyChipsDetails.this, ""+message, Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header= new HashMap<>();
                header.put("token",Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params= new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("order_id", order_id);
                params.put("payment_id", payment_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    private void dialog_payment_success(){

        new SmartDialogBuilder(BuyChipsDetails.this)
                .setTitle("Your Payment has been done Successfully!")
                .setSubTitle("Your Payment has been done Successfully!")
                .setCancalable(false)

                //.setTitleFont("Do you want to Logout?") //set title font
                // .setSubTitleFont(subTitleFont) //set sub title font
                .setNegativeButtonHide(true) //hide cancel button
                .setPositiveButton("Ok", new SmartDialogClickListener() {
                    @Override
                    public void onClick(SmartDialog smartDialog) {
                       smartDialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("Cancel", new SmartDialogClickListener() {
            @Override
            public void onClick(SmartDialog smartDialog) {
                // Toast.makeText(context,"Cancel button Click",Toast.LENGTH_SHORT).show();
                smartDialog.dismiss();

            }
        }).build().show();



//        final Dialog dialog = new Dialog(BuyChipsDetails.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setTitle("");
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.dialog_payment_successful);
//        final Button btn_payNow = (Button) dialog.findViewById(R.id.btn_payNow);
//
//        btn_payNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                finish();
//                //Ticketlist();
//            }
//        });
//
//        dialog.setCancelable(false);
//        dialog.show();

    }

}
