package com.games.ms;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.games.ms.Adapter.UserRedeemHistoryAdapter;
import com.games.ms.Adapter.UserWinnerAdapter;
import com.games.ms.Adapter.WelcomeRewardAdapter;
import com.games.ms.Interface.itemClick;
import com.games.ms.model.MyWinnigmodel;
import com.games.ms.model.RedeemModel;
import com.games.ms.model.WelcomeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Homepage extends AppCompatActivity {

    Animation animBounce, animBlink;
    public static final String MY_PREFS_NAME = "Login_data";
    ImageView imgLogout, imgshare, imaprofile;
    ImageView imgpublicGame/*, imgPrivategame, ImgCustomePage, ImgVariationGane*/;
    private String user_id, name, mobile, profile_pic, referral_code, wallet, game_played;
    private TextView txtName, txtwallet, txtproname;
    Button btnbuychips;
    LinearLayout lnrbuychips, lnrinvite, lnrmail, lnrearnchips, lnrsetting, lnrvideo, lnrlogout, lnrhistory;
    Typeface helvatikaboldround, helvatikanormal;
    public String token = "";
    private String game_for_private, app_version;
    SeekBar sBar;
    SeekBar sBarpri;
    ImageView imgCreatetable, imgCreatetablecustom, imgclosetoppri, imgclosetop;
    int pval = 1;
    int pvalpri = 1;
    Button btnCreatetable;
    Button btnCreatetablepri;
    TextView txtStart, txtLimit, txtwalletchips,
            txtwalletchipspri, txtBootamount, txtPotLimit, txtNumberofBlind,
            txtMaximumbetvalue;
    TextView txtStartpri, txtLimitpri, txtBootamountpri, txtPotLimitpri, txtNumberofBlindpri, txtMaximumbetvaluepri;
    RelativeLayout rltimageptofile;
    int version = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

//        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String datevalue = prefs.getString("cur_date4", "12/06/2020");


        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate1 = df1.format(c);
        int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"), datevalue, formattedDate1);


        if (dateDifference > 0) {
            // catalog_outdated = 1;

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c);

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("cur_date4", formattedDate);
            editor.apply();
            showDailyWinCoins();

        } else {

            System.out.println("");


        }


        //   textView.setText(currentDateTimeString);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = getResources().getDisplayMetrics();


        int densityDpi = (int) (metrics.density * 160f);

        ((ImageView) findViewById(R.id.imgMywining)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyWinningAcitivity.class);
                startActivity(intent);
            }
        });

        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        helvatikaboldround = Typeface.createFromAsset(getAssets(),
                "fonts/helvetica-rounded-bold-5871d05ead8de.otf");

        helvatikanormal = Typeface.createFromAsset(getAssets(),
                "fonts/Helvetica.ttf");

        rltimageptofile = findViewById(R.id.rltimageptofile);

        rltimageptofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUserInfo();
            }
        });

        imgpublicGame = (ImageView) findViewById(R.id.imgpublicGame);
//        imgPrivategame = (ImageView) findViewById(R.id.imgPrivategame);

        txtName = findViewById(R.id.txtName);
        txtName.setTypeface(helvatikaboldround);
        txtwallet = findViewById(R.id.txtwallet);
        txtwallet.setTypeface(helvatikanormal);
        txtproname = findViewById(R.id.txtproname);
        txtproname.setTypeface(helvatikaboldround);
        TextView txtMail = findViewById(R.id.txtMail);
        TextView txtEarn = findViewById(R.id.txtEarn);
        TextView txtInvite = findViewById(R.id.txtInvite);
        TextView txtBuyChips = findViewById(R.id.txtBuyChips);
        TextView txtLogout = findViewById(R.id.txtLogout);
        // txtMail.setVisibility(View.INVISIBLE);
        txtMail.setTypeface(helvatikaboldround);
        txtEarn.setTypeface(helvatikaboldround);
        txtInvite.setTypeface(helvatikaboldround);
        txtBuyChips.setTypeface(helvatikaboldround);
        txtLogout.setTypeface(helvatikaboldround);

        imgLogout = findViewById(R.id.imgLogout);


        lnrbuychips = findViewById(R.id.lnrbuychips);
        lnrmail = findViewById(R.id.lnrmail);
        //lnrmail.setVisibility(View.INVISIBLE);
        lnrearnchips = findViewById(R.id.lnrearnchips);
        lnrinvite = findViewById(R.id.lnrinvite);
        lnrsetting = findViewById(R.id.lnrsetting);
        lnrvideo = findViewById(R.id.lnrvideo);
        lnrlogout = findViewById(R.id.lnrlogout);
        lnrhistory = findViewById(R.id.lnrhistory);


        imaprofile = findViewById(R.id.imaprofile);


        // load the animation
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);

        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        imgpublicGame.startAnimation(animBlink);
        imgpublicGame.startAnimation(animBounce);


//        imgPrivategame.startAnimation(animBlink);
//        imgPrivategame.startAnimation(animBounce);
//
//
//        ImgCustomePage.startAnimation(animBlink);
//        ImgCustomePage.startAnimation(animBounce);
//        ImgCustomePage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDialoagonBack();
//            }
//        });
//
//
//        ImgVariationGane.startAnimation(animBlink);
//        ImgVariationGane.startAnimation(animBounce);
        clickTask();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        // Log.d(TAG, msg);
                        // Toast.makeText(Homepage.this, token, Toast.LENGTH_SHORT).show();
                        UserProfile();
                    }
                });


    }

    public void DialogUserInfo() {

        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_user);
        dialog.setTitle("Title...");

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ((View) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView img_diaProfile = dialog.findViewById(R.id.img_diaProfile);
        TextView txt_diaName = dialog.findViewById(R.id.txt_diaName);
        TextView txt_diaPhone = dialog.findViewById(R.id.txt_diaPhone);
        final EditText edtUsername = dialog.findViewById(R.id.edtUsername);

        final LinearLayout lnrUserinfo = dialog.findViewById(R.id.lnr_userinfo);
        final LinearLayout lnr_updateuser = dialog.findViewById(R.id.lnr_updateuser);

        txt_diaName.setText("" + name);
        txt_diaPhone.setText("" + mobile);
        Picasso.with(Homepage.this).load(Const.IMGAE_PATH + profile_pic).into(img_diaProfile);

        ((ImageView) dialog.findViewById(R.id.img_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lnrUserinfo.setVisibility(View.GONE);
                lnr_updateuser.setVisibility(View.VISIBLE);

            }
        });

        ((ImageView) dialog.findViewById(R.id.imgsub)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!edtUsername.getText().toString().trim().equals("")) {
                    lnrUserinfo.setVisibility(View.VISIBLE);
                    lnr_updateuser.setVisibility(View.GONE);

                    UserUpdateProfile(edtUsername.getText().toString().trim());

                    dialog.dismiss();
                } else {
                    Toast.makeText(Homepage.this, "Input field in empty!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        dialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        UserProfile();
        GameLeave();
    }

    public void clickTask() {
        imgpublicGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySaund(R.raw.buttontouchsound);

                Dialog_SelectPlayer();

            }
        });


        lnrearnchips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWinnerDailog();
            }
        });
        lnrhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRedeemDailog();
            }
        });

//        ImgVariationGane.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(Homepage.this, "Coming Soon",
//                        Toast.LENGTH_LONG).show();
//            }
//        });

        lnrmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(getApplicationContext(), MaiUserListingActivity.class);
//                startActivity(intent);
//                Toast.makeText(Homepage.this, "Coming Soon",
//                        Toast.LENGTH_LONG).show();
                showDailyWinCoins();

            }
        });


//        imgPrivategame.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //showDialoag();
//                int gamecount = 0;
//                if (game_played != null && !game_played.equals("")) {
//                    gamecount = Integer.parseInt(game_played);
//                }
//                int game_for_privatetemp = Integer.parseInt(game_for_private);
//                if (gamecount > game_for_privatetemp) {
//
//                    showDialoagPrivettable();
//
//                } else {
//
//                    Toast.makeText(Homepage.this, "To Unblock Private Table you have to Play at least " + game_for_privatetemp +
//                                    " Games.",
//                            Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//        });

        lnrsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showDialogSetting();
            }
        });

        lnrvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, RedeemActivity.class);
                startActivity(intent);
            }
        });

        lnrlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new AlertDialog.Builder(Homepage.this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Logout")
//                        .setMessage("Do you want to Logout?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                                editor.putString("user_id", "");
//                                editor.putString("name", "");
//                                editor.putString("mobile", "");
//                                editor.putString("token", "");
//                                editor.apply();
//                                Intent intent = new Intent(Homepage.this, LoginScreen.class);
//                                startActivity(intent);
//                                finish();
//                            }
//
//                        })
//                        .setNegativeButton("No", null)
//                        .show();


                new SmartDialogBuilder(Homepage.this)
                        .setTitle("Do you want to Logout?")
                        //.setSubTitle("This is the alert dialog to showing alert to user")
                        .setCancalable(true)
                        //.setTitleFont("Do you want to Logout?") //set title font
                        // .setSubTitleFont(subTitleFont) //set sub title font
                        .setNegativeButtonHide(true) //hide cancel button
                        .setPositiveButton("Logout", new SmartDialogClickListener() {
                            @Override
                            public void onClick(SmartDialog smartDialog) {
                                // Toast.makeText(context,"Ok button Click",Toast.LENGTH_SHORT)
                                // .show();
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("user_id", "");
                                editor.putString("name", "");
                                editor.putString("mobile", "");
                                editor.putString("token", "");
                                editor.apply();
                                Intent intent = new Intent(Homepage.this, LoginScreen.class);
                                startActivity(intent);
                                finish();

                                smartDialog.dismiss();
                            }
                        }).setNegativeButton("Cancel", new SmartDialogClickListener() {
                    @Override
                    public void onClick(SmartDialog smartDialog) {
                        // Toast.makeText(context,"Cancel button Click",Toast.LENGTH_SHORT).show();
                        smartDialog.dismiss();

                    }
                }).build().show();

//                new SweetAlertDialog(Homepage.this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Are you sure?")
//                        .setContentText("Won't be able to recover this file!")
//                        .setConfirmText("Yes,delete it!")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                            }
//                        })
//                        .show();
            }
        });

        lnrinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showDialogInvite();

            }
        });
        lnrbuychips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, BuyChipsList.class);
                startActivity(intent);
            }
        });

    }


    private void UserProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.PROFILE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
                                JSONObject jsonObject0 = jsonObject.getJSONArray("user_data").getJSONObject(0);
                                user_id = jsonObject0.getString("id");
                                name = jsonObject0.getString("name");
                                mobile = jsonObject0.getString("mobile");
                                profile_pic = jsonObject0.getString("profile_pic");
                                referral_code = jsonObject0.getString("referral_code");
                                wallet = jsonObject0.getString("wallet");
                                game_played = jsonObject0.getString("game_played");
                                // txtName.setText("Welcome Back "+name);
                                long numberamount = Long.parseLong(wallet);
                                txtwallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
                                txtproname.setText(name);
                                Picasso.with(Homepage.this).load(Const.IMGAE_PATH + profile_pic).into(imaprofile);


                                String setting = jsonObject.getString("setting");
                                JSONObject jsonObjectSetting = new JSONObject(setting);
                                game_for_private = jsonObjectSetting.getString(
                                        "game_for_private");
                                app_version = jsonObjectSetting.getString(
                                        "app_version");

                                int app_versionint = Integer.parseInt(app_version);

                                //if (version > app_versionint){
                                if (app_versionint > version) {

                                    showAppUpdateDialog("Update");

                                } else {


                                }

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("name", name);
                                editor.putString("referal_code", referral_code);
                                editor.putString("img_name", profile_pic);
                                editor.putString("wallet", wallet);
                                editor.putString("game_for_private", game_for_private);
                                editor.putString("app_version", app_version);
                                editor.apply();


                            } else if (code.equals("411")) {

                                Intent intent = new Intent(Homepage.this, LoginScreen.class);
                                startActivity(intent);
                                finishAffinity();
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("name", "");
                                editor.putString("referal_code", "");
                                editor.putString("img_name", "");
                                editor.putString("game_for_private", "");
                                editor.putString("app_version", "");
                                editor.apply();

                                Toast.makeText(Homepage.this, "You are Logged in from another " +
                                                "device.",
                                        Toast.LENGTH_LONG).show();


                            } else {

                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(Homepage.this, message,
                                            Toast.LENGTH_LONG).show();
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Homepage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("id", prefs.getString("user_id", ""));
                params.put("fcm", token);

                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                params.put("app_version", version + "");
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void UserUpdateProfile(final String username) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_UPDATE,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
//                                JSONObject jsonObject0 = jsonObject.getJSONArray("user_data").getJSONObject(0);
//                                user_id = jsonObject0.getString("id");
//                                name = jsonObject0.getString("name");
//                                mobile = jsonObject0.getString("mobile");
//                                profile_pic = jsonObject0.getString("profile_pic");
//                                referral_code = jsonObject0.getString("referral_code");
//                                wallet = jsonObject0.getString("wallet");
//                                // txtName.setText("Welcome Back "+name);
//                                long numberamount = Long.parseLong(wallet);
//                                txtwallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
//                                txtproname.setText(name);
//                                Picasso.with(Homepage.this).load(Const.IMGAE_PATH + profile_pic).into(imaprofile);
//                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                                editor.putString("name", name);
//                                editor.putString("referal_code", referral_code);
//                                editor.apply();

                                UserProfile();

                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(Homepage.this, message,
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Homepage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("name", username);
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    public void showDialoag() {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final EditText edtEnterTableId = (EditText) dialog.findViewById(R.id.edtEnterTableId);

        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        Button btnCreatNewTable = (Button) dialog.findViewById(R.id.btnCreatNewTable);
        // if button is clicked, close the custom dialog
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

//                Intent intent = new Intent(Homepage.this, PrivateTablev2.class);
//                intent.putExtra("table_id", edtEnterTableId.getText().toString());
//                intent.putExtra("type", "1");
//                startActivity(intent);
//                dialog.dismiss();

            }
        });
        btnCreatNewTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Homepage.this, PrivateTablev2.class);
//                intent.putExtra("table_id", "");
//                intent.putExtra("type", "2");
//                startActivity(intent);
//                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void PlaySaund(int sound) {

        final MediaPlayer mp = MediaPlayer.create(this,
                sound);
        mp.start();
    }

    public void showDialoagPrivettable() {

        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.custom_dialog_custon_boot);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        sBarpri = (SeekBar) dialog.findViewById(R.id.seekBar1);
        sBarpri.setProgress(0);
        sBarpri.incrementProgressBy(10);
        sBarpri.setMax(100);
        txtStartpri = (TextView) dialog.findViewById(R.id.txtStart);
        txtLimitpri = (TextView) dialog.findViewById(R.id.txtLimit);
        txtwalletchipspri = (TextView) dialog.findViewById(R.id.txtwalletchips);
        long numberamount = Long.parseLong(wallet);
        txtwalletchipspri.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

        // txtwalletchipspri.setText(wallet);
        txtBootamountpri = (TextView) dialog.findViewById(R.id.txtBootamount);
        txtPotLimitpri = (TextView) dialog.findViewById(R.id.txtPotLimit);
        txtNumberofBlindpri = (TextView) dialog.findViewById(R.id.txtNumberofBlind);
        txtMaximumbetvaluepri = (TextView) dialog.findViewById(R.id.txtMaximumbetvalue);
        imgclosetoppri = (ImageView) dialog.findViewById(R.id.imgclosetop);
        imgclosetoppri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imgCreatetable = (ImageView) dialog.findViewById(R.id.imgCreatetable);
        imgCreatetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Homepage.this, "To unlock this feature Please Purchage Extended Licence.",
                        Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Homepage.this, PrivateTablev2.class);
//                intent.putExtra("bootvalue", pvalpri + "");
//                startActivity(intent);
                dialog.dismiss();
            }
        });
        // tView.setText(sBar.getProgress() + "/" + sBar.getMax());
        sBarpri.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                if (progress == 1) {

                    pvalpri = 50;

                } else if (progress == 2) {
                    pvalpri = 100;
                } else if (progress == 3) {

                    pvalpri = 500;
                } else if (progress == 4) {

                    pvalpri = 1000;

                } else if (progress == 5) {

                    pvalpri = 5000;

                } else if (progress == 6) {

                    pvalpri = 10000;
                } else if (progress == 7) {

                    pvalpri = 25000;
                } else if (progress == 8) {

                    pvalpri = 50000;
                } else if (progress == 9) {

                    pvalpri = 100000;
                } else if (progress == 10) {

                    pvalpri = 250000;
                }

                //progress = progress * 10;
                // pvalpri = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtBootamountpri.setText("Boot amount : " + kvalue(pvalpri) + "");

                int valueforpot = pvalpri * 1024;
                int valueformaxi = pvalpri * 128;

                //long valueforpotlong= valueforpot;

                txtPotLimitpri.setText("Pot limit : " + kvalue(valueforpot) + "");
                txtMaximumbetvaluepri.setText("Maximumbet balue : " + kvalue(valueformaxi) + "");
                txtNumberofBlindpri.setText("Number of Blinds : 4");
                //tView.setText(pval + "/" + seekBar.getMax());
            }
        });


        dialog.show();
    }

    LinearLayout lnr_2player,lnr_5player;
    TextView tv_2player,tv_5player;
    int selected_type = 5;
    public void Dialog_SelectPlayer() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_select_palyer);
        lnr_2player = dialog.findViewById(R.id.lnr_2player);
        lnr_5player = dialog.findViewById(R.id.lnr_5player);
        tv_2player =  (TextView) dialog.findViewById(R.id.tv_2player);
        tv_5player =  (TextView) dialog.findViewById(R.id.tv_5player);

        Button btn_player = dialog.findViewById(R.id.btn_play);

        ChangeTextviewColorChange(5);

        lnr_2player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(2);
            }
        });

        lnr_5player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(5);
            }
        });

        btn_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(selected_type == 2)
                {
                    Intent intent = new Intent(Homepage.this, Rummy2Player.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Homepage.this, Rummy5Player.class);
                    startActivity(intent);
                }


            }
        });

        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


    }

    private void ChangeTextviewColorChange(int colortype){

        selected_type = colortype;

        if(colortype == 2)
        {
            tv_2player.setTextColor(getResources().getColor(R.color.white));
            lnr_2player.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            tv_5player.setTextColor(getResources().getColor(R.color.black));
            lnr_5player.setBackgroundColor(getResources().getColor(R.color.white));

        }
        else {
            tv_2player.setTextColor(getResources().getColor(R.color.black));
            lnr_2player.setBackgroundColor(getResources().getColor(R.color.white));

            tv_5player.setTextColor(getResources().getColor(R.color.white));
            lnr_5player.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        }


    }


    public void showDialoagonBack() {

        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.custom_dialog_custon_boot);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        sBar = (SeekBar) dialog.findViewById(R.id.seekBar1);
        sBar.setProgress(0);
        sBar.incrementProgressBy(10);
        sBar.setMax(100);
        txtStart = (TextView) dialog.findViewById(R.id.txtStart);
        txtLimit = (TextView) dialog.findViewById(R.id.txtLimit);
        txtwalletchips = (TextView) dialog.findViewById(R.id.txtwalletchips);
        long numberamount = Long.parseLong(wallet);
        txtwalletchips.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
        txtBootamount = (TextView) dialog.findViewById(R.id.txtBootamount);
        txtPotLimit = (TextView) dialog.findViewById(R.id.txtPotLimit);
        txtNumberofBlind = (TextView) dialog.findViewById(R.id.txtNumberofBlind);
        txtMaximumbetvalue = (TextView) dialog.findViewById(R.id.txtMaximumbetvalue);
        imgclosetop = (ImageView) dialog.findViewById(R.id.imgclosetop);
        imgclosetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imgCreatetablecustom = (ImageView) dialog.findViewById(R.id.imgCreatetable);
        imgCreatetablecustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Homepage.this, CustomisedTablev2.class);
//                Intent intent = new Intent(Homepage.this, CustomsiedTablev3.class);
//                intent.putExtra("bootvalue", pval + "");
//                startActivity(intent);

                Toast.makeText(Homepage.this, "To unlock this feature Please Purchage Extended Licence.",
                        Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        // tView.setText(sBar.getProgress() + "/" + sBar.getMax());
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                progress = progress / 10;
                if (progress == 1) {

                    pval = 50;

                } else if (progress == 2) {
                    pval = 100;
                } else if (progress == 3) {

                    pval = 500;
                } else if (progress == 4) {

                    pval = 1000;

                } else if (progress == 5) {

                    pval = 5000;

                } else if (progress == 6) {

                    pval = 10000;
                } else if (progress == 7) {

                    pval = 25000;
                } else if (progress == 8) {

                    pval = 50000;
                } else if (progress == 9) {

                    pval = 100000;
                } else if (progress == 10) {

                    pval = 250000;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtBootamount.setText("Boot amount : " + kvalue(pval) + "");

                int valueforpot = pval * 1024;
                int valueformaxi = pval * 128;
                txtPotLimit.setText("Pot limit : " + kvalue(valueforpot) + "");
                txtMaximumbetvalue.setText("Maximumbet balue : " + kvalue(valueformaxi) + "");
                txtNumberofBlind.setText("Number of Blinds : 4");
                //tView.setText(pval + "/" + seekBar.getMax());
            }
        });


        dialog.show();
    }

    private void GameLeave() {

//        final ProgressDialog progressDialog = new ProgressDialog(Homepage.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Logging in..");
//        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_TABLE_LEAVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        System.out.println("" + response);
                        // finish();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    public void showDialogInvite() {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.custom_dialog_invite);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclosetop);
        TextView txtshare = (TextView) dialog.findViewById(R.id.txtshare);
        TextView txtReferalcode = (TextView) dialog.findViewById(R.id.txtReferalcode);
        txtReferalcode.setText(referral_code);
        TextView txtAnd = (TextView) dialog.findViewById(R.id.txtAnd);
        ImageView imgfb = (ImageView) dialog.findViewById(R.id.imgfb);
        ImageView imgwhats = (ImageView) dialog.findViewById(R.id.imgwhats);
        ImageView imgmail = (ImageView) dialog.findViewById(R.id.imgmail);
        imgmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();
                String applink = "https://play.google.com/store/apps/details?id=" + appPackageName;

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.google.android.gm");
                String shareMessage = "You have been invited to use 3 Patti " +
                        "app. Use the referral code    " +
                        referral_code + " Download the App now. "
                        + "Link:- " + applink;

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });

        imgwhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();
                String applink = "https://play.google.com/store/apps/details?id=" + appPackageName;

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                String shareMessage = "You have been invited to use 3 Patti " +
                        "app. Use the referral code    " +
                        referral_code + " Download the App now. "
                        + "Link:- " + applink;

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });

        imgfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();
                String applink = "https://play.google.com/store/apps/details?id=" + appPackageName;

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.facebook.katana");
                String shareMessage = "You have been invited to use 3 Patti " +
                        "app. Use the referral code    " +
                        referral_code + " Download the App now. "
                        + "Link:- " + applink;

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });
        TextView txtyourfrind = (TextView) dialog.findViewById(R.id.txtyourfrind);
        TextView txtchipsbelow = (TextView) dialog.findViewById(R.id.txtchipsbelow);
        TextView txtFooter = (TextView) dialog.findViewById(R.id.txtFooter);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void showDialogSetting() {
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.custom_dialog_setting_home);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View imgclose = (View) dialog.findViewById(R.id.imgclosetop);
//
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        ((Button) dialog.findViewById(R.id.btnGameOption)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showGiftDialog();
                Toast.makeText(Homepage.this, "Coming Soon",
                        Toast.LENGTH_LONG).show();

                //dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.btnHelp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Homepage.this, "Coming Soon",
                        Toast.LENGTH_LONG).show();


            }
        });

        ((Button) dialog.findViewById(R.id.btnSaund)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Homepage.this, "Coming Soon",
                        Toast.LENGTH_LONG).show();

            }
        });

        ((Button) dialog.findViewById(R.id.btnPrivacy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTermDialog("Privacy Policy");

            }
        });

        ((Button) dialog.findViewById(R.id.btnHowtoplay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showHelpDialog();

                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.btnTermscond)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTermDialog("Term and Conditions");

            }
        });


        ((Button) dialog.findViewById(R.id.btnLearnvari)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Homepage.this, "Coming Soon",
                        Toast.LENGTH_LONG).show();

            }
        });

        ((Button) dialog.findViewById(R.id.btnHelpandsupport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();

                dialog.dismiss();
            }
        });

        ((View) dialog.findViewById(R.id.lnrlogoutdia)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("user_id", "");
                editor.putString("name", "");
                editor.putString("mobile", "");
                editor.putString("token", "");
                editor.apply();
                Intent intent = new Intent(Homepage.this, LoginScreen.class);
                startActivity(intent);
                finish();

                dialog.dismiss();
            }
        });

        dialog.show();
    }



    TextView txtnotfound;


    public void showDailyWinCoins() {

        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_dailyreward);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RecyclerView Reward_rec;

        txtnotfound = dialog.findViewById(R.id.txtnotfound);
        imgclosetop = dialog.findViewById(R.id.imgclosetop);
        imgclosetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ((View) dialog.findViewById(R.id.btnCollect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectWelcomeBonus(dialog);

            }
        });


        Reward_rec = dialog.findViewById(R.id.reward_rec);

        itemClick itemClick = new itemClick() {
            @Override
            public void OnClick(String id,String path) {

//                CollectWelcomeBonus();
            }
        };

        ArrayList<Integer> CoinsList = new ArrayList<>();
        CoinsList.add(R.drawable.day1);
        CoinsList.add(R.drawable.day2);
        CoinsList.add(R.drawable.day3);
        CoinsList.add(R.drawable.day4);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);

        GetUserWelcomeBonus(Reward_rec, itemClick, dialog, CoinsList);

        Reward_rec.setLayoutManager(new LinearLayoutManager(Homepage.this, RecyclerView.HORIZONTAL, false));


        dialog.show();
    }

//    public void showGiftDialog() {
//        // custom dialog
//        final Dialog dialog = new Dialog(Homepage.this,
//                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
//        dialog.setContentView(R.layout.dialog_gift);
//        dialog.setTitle("Title...");
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        ((ImageView)dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        txtnotfound = dialog.findViewById(R.id.txtnotfound);
//
//        RecyclerView recyclerView_gifts = dialog.findViewById(R.id.recylerview_gifts);
//        recyclerView_gifts.setLayoutManager(new GridLayoutManager(Homepage.this,5));
//
////        itemClick onGitsClick = new itemClick() {
////            @Override
////            public void OnDailyClick(String id) {
////                SendGits(id);
////            }
////        };
//
//      //  GetGiftList(recyclerView_gifts,dialog,onGitsClick);
//
//        dialog.show();
//    }

    public void showTermDialog(String tag) {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_termandcondition);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtnotfound = dialog.findViewById(R.id.txtnotfound);
        TextView txtheader = dialog.findViewById(R.id.txtheader);
        txtheader.setText("" + tag);

        WebView webView = dialog.findViewById(R.id.webview);

        webView.setBackgroundColor(Color.TRANSPARENT);


        UserTermsAndCondition(webView, dialog, tag);

        dialog.show();
    }

    public void showAppUpdateDialog(String tag) {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_app_update);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
//        ((ImageView)dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });


        TextView txtheader = dialog.findViewById(R.id.txtheader);
        RelativeLayout rltUpdate = dialog.findViewById(R.id.rltUpdate);
        txtheader.setText("" + tag);
        rltUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    int tips = 100;

    public void showTipsDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_sendtips);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView imgpl1minus = dialog.findViewById(R.id.imgpl1minus);
        final Button btnpl1number = dialog.findViewById(R.id.btnpl1number);
        ImageView imgpl1plus = dialog.findViewById(R.id.imgpl1plus);

        tips = 100;
        btnpl1number.setText("" + tips);

        imgpl1plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                tips = tips + 100;
                btnpl1number.setText("" + tips);
            }
        });


        imgpl1minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tips > 100) {
                    tips = tips - 100;
                    btnpl1number.setText("" + tips);
                }


            }
        });

        ((Button) dialog.findViewById(R.id.btnTips)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendTips(tips);
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void showHelpDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_rulesplay);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void showWinnerDailog() {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_gift);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtnotfound = dialog.findViewById(R.id.txtnotfound);
        TextView txttitle = dialog.findViewById(R.id.txttitle);

        txttitle.setText("Leader Board");

        RecyclerView recyclerView_gifts = dialog.findViewById(R.id.recylerview_gifts);
        recyclerView_gifts.setLayoutManager(new LinearLayoutManager(Homepage.this));

        UsersWinningList(recyclerView_gifts, dialog);

        dialog.show();
    }

    public void showRedeemDailog() {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_gift);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtnotfound = dialog.findViewById(R.id.txtnotfound);
        TextView txttitle = dialog.findViewById(R.id.txttitle);

        txttitle.setText("Redeem History");

        RecyclerView recyclerView_gifts = dialog.findViewById(R.id.recylerview_gifts);
        recyclerView_gifts.setLayoutManager(new LinearLayoutManager(Homepage.this));

        UsersRedeemList(recyclerView_gifts, dialog);

        dialog.show();
    }

    private void UsersWinningList(final RecyclerView recyclerView, final Dialog dialog) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        final ArrayList<MyWinnigmodel> winnigmodelArrayList = new ArrayList();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_WINNING_LIST,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");


                            if (code.equalsIgnoreCase("200")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("leaderboard");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    MyWinnigmodel model = new MyWinnigmodel();
                                    model.setId(jsonObject1.getString("winner_id"));
                                    model.setUserimage(jsonObject1.getString("profile_pic"));
                                    model.setWinner_id(jsonObject1.getString("winner_id"));
                                    model.setTotalwin(jsonObject1.getString("Total_Win"));
                                    model.setName(jsonObject1.getString("name"));


                                    winnigmodelArrayList.add(model);
                                }

                                UserWinnerAdapter userWinnerAdapter = new UserWinnerAdapter(Homepage.this, winnigmodelArrayList);
                                recyclerView.setAdapter(userWinnerAdapter);
                            } else {
                                if (jsonObject.has("message")) {

                                    Toast.makeText(Homepage.this, message,
                                            Toast.LENGTH_LONG).show();
                                    txtnotfound.setVisibility(View.VISIBLE);

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Homepage.this, "Something went wrong", Toast.LENGTH_LONG).show();
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void UsersRedeemList(final RecyclerView recyclerView, final Dialog dialog) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        final ArrayList<RedeemModel> redeemModelArrayList = new ArrayList();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_Redeem_History_LIST,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            Log.d("response", "onResponse: "+response);

                            if (code.equalsIgnoreCase("200")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("List");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    RedeemModel model = new RedeemModel();
                                    model.setId(jsonObject1.getString("id"));
                                    model.setCoin(jsonObject1.getString("coin"));
                                    model.setMobile(jsonObject1.getString("mobile"));
                                    model.setUser_name(jsonObject1.getString("user_name"));
                                    model.setUser_mobile(jsonObject1.getString("user_mobile"));
                                    model.setStatus(jsonObject1.getString("status"));


                                    redeemModelArrayList.add(model);
                                }

                                UserRedeemHistoryAdapter userWinnerAdapter = new UserRedeemHistoryAdapter(Homepage.this, redeemModelArrayList);
                                recyclerView.setAdapter(userWinnerAdapter);
                            } else {
                                if (jsonObject.has("message")) {

                                    Toast.makeText(Homepage.this, message,
                                            Toast.LENGTH_LONG).show();
                                    txtnotfound.setVisibility(View.VISIBLE);

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Homepage.this, "Something went wrong", Toast.LENGTH_LONG).show();
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private void GetUserWelcomeBonus(final RecyclerView reward_rec, final itemClick itemClick, Dialog dialog, final ArrayList<Integer> coinsList) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        final ArrayList<WelcomeModel> welcomeList = new ArrayList();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.WELCOME_BONUS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {

                                txtnotfound.setVisibility(View.GONE);

                                JSONArray welcome_bonusArray = jsonObject.getJSONArray("welcome_bonus");

                                for (int i = 0; i < welcome_bonusArray.length(); i++) {
                                    JSONObject welcome_bonusObject = welcome_bonusArray.getJSONObject(i);

                                    WelcomeModel model = new WelcomeModel();
                                    model.setCoins(welcome_bonusObject.getString("coin"));
                                    model.setId(welcome_bonusObject.getString("id"));
                                    model.setGame_played(welcome_bonusObject.getString("game_played"));
                                    model.setCollected_days(jsonObject.getString("collected_days"));
                                    model.setDay(welcome_bonusObject.getString("id"));
                                    model.setImgcoins(coinsList.get(i));

                                    welcomeList.add(model);
                                }

                                WelcomeRewardAdapter welcomeRewardAdapter = new WelcomeRewardAdapter(Homepage.this, welcomeList, itemClick);
                                reward_rec.setAdapter(welcomeRewardAdapter);

                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(Homepage.this, message,
                                            Toast.LENGTH_LONG).show();
                                }

                                txtnotfound.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Homepage.this, "Something went wrong", Toast.LENGTH_LONG).show();
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }



    private void CollectWelcomeBonus(Dialog dialog) {

        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.COLLECT_BONUS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            String coins = "0";
                            if (jsonObject.has("coin"))
                                coins = jsonObject.getString("coin");

                            if (code.equalsIgnoreCase("200")) {

                                WelcomeRewardAdapter.showWinDialog(Homepage.this, coins);

                            } else {
                                if (jsonObject.has("message")) {

                                    Toast.makeText(Homepage.this, message,
                                            Toast.LENGTH_LONG).show();


                                }


                            }

                            rlt_progress.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rlt_progress.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Homepage.this, "Something went wrong", Toast.LENGTH_LONG).show();
                rlt_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void UserTermsAndCondition(final WebView webview, final Dialog dialog, final String tag) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.TERMS_CONDITION,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");


                            if (code.equalsIgnoreCase("200")) {


                                JSONObject jsonObject1 = jsonObject.getJSONObject("setting");

                                String data = "";

                                if (tag.equals("Privacy Policy")) {
                                    data = jsonObject1.getString("privacy_policy");
                                } else {
                                    data = jsonObject1.getString("terms");
                                }


                                if (data.equals("")) {
                                    txtnotfound.setVisibility(View.VISIBLE);
                                } else {
                                    txtnotfound.setVisibility(View.GONE);
                                }


                                data = data.replaceAll("&#39;", "'");

                                String szMessage = "<font face= \"trebuchet\" size=3 color=\"#fff\"><b>"
                                        + data
                                        + "</b></font>";


                                webview.getSettings().setJavaScriptEnabled(true);
                                webview.loadDataWithBaseURL("", szMessage, "text/html", "UTF-8", "");


                            } else {
                                if (jsonObject.has("message")) {

                                    Toast.makeText(Homepage.this, message,
                                            Toast.LENGTH_LONG).show();
                                    txtnotfound.setVisibility(View.VISIBLE);

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Homepage.this, "Something went wrong", Toast.LENGTH_LONG).show();
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void SendTips(final int tips) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_TIPS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            String coins = "0";
                            if (jsonObject.has("coin"))
                                coins = jsonObject.getString("coin");

                            if (code.equalsIgnoreCase("200")) {

                                Toast.makeText(Homepage.this, "" + message, Toast.LENGTH_SHORT).show();

                            } else {
                                if (jsonObject.has("message")) {

                                    Toast.makeText(Homepage.this, message,
                                            Toast.LENGTH_LONG).show();


                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Homepage.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("tip", "" + tips);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void SendGits(final String gifts) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_TIPS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");


                            if (code.equalsIgnoreCase("200")) {

                                Toast.makeText(Homepage.this, "" + message, Toast.LENGTH_SHORT).show();

                            } else {
                                if (jsonObject.has("message")) {

                                    Toast.makeText(Homepage.this, message,
                                            Toast.LENGTH_LONG).show();


                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Homepage.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("tip", "" + gifts);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public String kvalue(int number) {

        String numberString = "";
        if (Math.abs(number / 1000000) > 1) {
            numberString = (number / 1000000) + "m";

        } else if (Math.abs(number / 1000) > 1) {
            numberString = (number / 1000) + "k";

        } else {
            numberString = number + "";

        }
        return numberString;
    }

}


