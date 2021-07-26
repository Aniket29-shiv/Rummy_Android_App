package com.games.ms.model;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.games.ms.Adapter.DeclareAdapter;
import com.games.ms.Adapter.GiftsAdapter;
import com.games.ms.Const;
import com.games.ms.Dealer;
import com.games.ms.Interface.ApiRequest;
import com.games.ms.Interface.Callback;
import com.games.ms.Interface.itemClick;
import com.games.ms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class Funtions {

    private static final String MY_PREFS_NAME = "Login_data";

    private static final boolean isDebug = true;

    public static final int ANIMATION_SPEED = 1500;
    public static final int Home_Page_Animation = 500;

    public static CountDownTimer onUserCountDownTimer(Context context, int MaxTime, int Interval, final Callback callback){

        CountDownTimer countDownTimer = new CountDownTimer(MaxTime,Interval) {
            @Override
            public void onTick(long millisUntilFinished) {

                callback.Responce("onTick","",null);

            }

            @Override
            public void onFinish() {

                callback.Responce("onFinish","",null);

            }
        };

        return countDownTimer;
    }


    public static Animation AnimationListner(Context context, int url_animation , final Callback callback){

        Animation animation =  AnimationUtils.loadAnimation(context,
                url_animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.Responce("end","",null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return animation;
    }

    public static int GetResourcePath(String imagename,Context context){

        String uri1 = "@drawable/" + imagename.toLowerCase();  // where myresource " +
        int imageResource = context.getResources().getIdentifier(uri1, null,
                context.getPackageName());

        return imageResource;
    }

    public static void SetBackgroundImageAsDisplaySize(Activity context, RelativeLayout relativeLayout,int drawable){

        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(),drawable),size.x,size.y,true);

        ImageView imageview = new ImageView(context);
        RelativeLayout relativelayout = relativeLayout;
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Add image path from drawable folder.
        imageview.setImageBitmap(bmp);
        imageview.setLayoutParams(params);

        if(relativeLayout != null)
            relativelayout.addView(imageview);

    }

    public static AnimatorSet getViewToViewScalingAnimator(final RelativeLayout parentView,
                                                           final View viewToAnimate,
                                                           final Rect fromViewRect,
                                                           final Rect toViewRect,
                                                           final long duration,
                                                           final long startDelay) {
        // get all coordinates at once
        final Rect parentViewRect = new Rect(), viewToAnimateRect = new Rect();
        parentView.getGlobalVisibleRect(parentViewRect);
        viewToAnimate.getGlobalVisibleRect(viewToAnimateRect);

        viewToAnimate.setScaleX(1f);
        viewToAnimate.setScaleY(1f);

        // rescaling of the object on X-axis
        final ValueAnimator valueAnimatorWidth = ValueAnimator.ofInt(fromViewRect.width(), toViewRect.width());
        valueAnimatorWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get animated width value update
                int newWidth = (int) valueAnimatorWidth.getAnimatedValue();

                // Get and update LayoutParams of the animated view
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();

                lp.width = newWidth;
                viewToAnimate.setLayoutParams(lp);
            }
        });

        // rescaling of the object on Y-axis
        final ValueAnimator valueAnimatorHeight = ValueAnimator.ofInt(fromViewRect.height(), toViewRect.height());
        valueAnimatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get animated width value update
                int newHeight = (int) valueAnimatorHeight.getAnimatedValue();

                // Get and update LayoutParams of the animated view
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();
                lp.height = newHeight;
                viewToAnimate.setLayoutParams(lp);
            }
        });

        // moving of the object on X-axis
        ObjectAnimator translateAnimatorX = ObjectAnimator.ofFloat(viewToAnimate, "X", fromViewRect.left - parentViewRect.left, toViewRect.left - parentViewRect.left);

        // moving of the object on Y-axis
        ObjectAnimator translateAnimatorY = ObjectAnimator.ofFloat(viewToAnimate, "Y", fromViewRect.top - parentViewRect.top, toViewRect.top - parentViewRect.top);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator(1f));
        animatorSet.setDuration(duration); // can be decoupled for each animator separately
        animatorSet.setStartDelay(startDelay); // can be decoupled for each animator separately
        animatorSet.playTogether(valueAnimatorWidth, valueAnimatorHeight, translateAnimatorX, translateAnimatorY);

        return animatorSet;
    }

    public static void LOGE(String Class, String Message){
        if(isDebug)
        {
            if (Message != null) {
                if (!isDebug) {
                    return;
                }

                Log.e(""+Class,""+Message);
            }

        }
    }

    public static void showTipsDialog(final Context context, final Dealer dealer, final ImageView imgampire, final Callback callback) {
        // custom dialog
        final int[] tips = {100};

        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_sendtips);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        final TextView txtTips = dialog.findViewById(R.id.txtTips);

        txtTips.setText("TIPS: RS "+dealer.tips+" CHIPS");

        final TextView txttime = dialog.findViewById(R.id.txttime);

        txttime.setText("Dealer since "+ TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - dealer.timeStamp)+" min ago");
        final ImageView imgperson = dialog.findViewById(R.id.imgperson);
        imgperson.setImageDrawable(context.getDrawable(Dealer.dealerImages[dealer.currentDealerPos]));


        dialog.findViewById(R.id.btn_change_dealer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dealer.showDialog(context, 3, new Dealer.CallBack() {
                    @Override
                    public void onDealerChanged(int drawable) {
                        imgperson.setImageDrawable(context.getDrawable(drawable));
                        imgampire.setImageDrawable(context.getDrawable(drawable));
                        txttime.setText("Dealer Changed Just Now");
                        txtTips.setText("TIPS: RS "+dealer.tips+" CHIPS");
//                        Toast.makeText(context, "Dealer Selected", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ImageView imgpl1minus = dialog.findViewById(R.id.imgpl1minus);
        final Button btnpl1number = dialog.findViewById(R.id.btnpl1number);
        ImageView imgpl1plus = dialog.findViewById(R.id.imgpl1plus);

        btnpl1number.setText("" + tips[0]);

        imgpl1plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                tips[0] = tips[0] + 100;
                btnpl1number.setText("" + tips[0]);
            }
        });


        imgpl1minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tips[0] > 100) {
                    tips[0] = tips[0] - 100;
                    btnpl1number.setText("" + tips[0]);
                }


            }
        });

        ((Button) dialog.findViewById(R.id.btnTips)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendTips(tips[0],context,callback);
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public static void showGiftDialog(final Context context, final String player, final Callback callback) {
        // custom dialog
        final Dialog dialog = new Dialog(context,
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

        TextView txtnotfound = dialog.findViewById(R.id.txtnotfound);

        RecyclerView recyclerView_gifts = dialog.findViewById(R.id.recylerview_gifts);
        recyclerView_gifts.setLayoutManager(new GridLayoutManager(context, 5));

        itemClick OnDailyClick = new itemClick() {
            @Override
            public void OnClick(String id, String url) {
                dialog.dismiss();
                SendGits(id, url, player,context,callback);
            }
        };

        GetGiftList(recyclerView_gifts, dialog, OnDailyClick,txtnotfound,context);

        dialog.show();
    }

    public static void GetGiftList(final RecyclerView recyclerView, Dialog dialog,
                                   final itemClick onGitsClick ,
                                   final TextView txtnotfound,
                                   final Context context) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        final ArrayList<GiftModel> giftModelArrayList = new ArrayList();

        HashMap params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("token", prefs.getString("token", ""));

        ApiRequest.Call_Api(context, Const.GIFTS_LIST, params, new Callback() {
            @Override
            public void Responce(String resp,String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    if (code.equalsIgnoreCase("200")) {

                        txtnotfound.setVisibility(View.GONE);

                        JSONArray welcome_bonusArray = jsonObject.getJSONArray("Gift");

                        for (int i = 0; i < welcome_bonusArray.length(); i++) {
                            JSONObject welcome_bonusObject = welcome_bonusArray.getJSONObject(i);

                            GiftModel model = new GiftModel();
                            model.setId(welcome_bonusObject.getString("id"));
                            model.setName(welcome_bonusObject.getString("name"));
                            model.setImage(welcome_bonusObject.getString("image"));
                            model.setCoin(welcome_bonusObject.getString("coin"));

                            giftModelArrayList.add(model);
                        }

                        GiftsAdapter adapter = new GiftsAdapter(context, giftModelArrayList, onGitsClick);
                        recyclerView.setAdapter(adapter);

                    } else {
                        if (jsonObject.has("message")) {
                            String message = jsonObject.getString("message");
//                                    Toast.makeText(PublicTable.this, message,
//                                            Toast.LENGTH_LONG).show();
                        }

                        txtnotfound.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    txtnotfound.setVisibility(View.VISIBLE);

                }

                rlt_progress.setVisibility(View.GONE);


            }
        });
    }

    public static void SendGits(final String gifts,
                                final String gifturl,
                                final String playerno,
                                final Context context,
                                final Callback requestback) {


        HashMap params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("token", prefs.getString("token", ""));
        params.put("tip", "" + gifts);

        ApiRequest.Call_Api(context, Const.GAME_TIPS, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    Bundle bundle1 = new Bundle();
                    bundle1.putString("gifturl",gifturl);

                    requestback.Responce(resp,playerno,bundle1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public static void SendTips(final int tips, final Context context, final Callback backresponse) {

        HashMap params = new HashMap();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("token", prefs.getString("token", ""));
        params.put("tip", "" + tips);

        ApiRequest.Call_Api(context, Const.GAME_TIPS, params, new Callback() {
            @Override
            public void Responce(String resp,String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    backresponse.Responce(""+tips,"",null);

//                    dealer.tips = dealer.tips + tips;

                    String coins = "0";
                    if (jsonObject.has("coin"))
                        coins = jsonObject.getString("coin");

                    if (code.equalsIgnoreCase("200")) {

                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                    } else {
                        if (jsonObject.has("message")) {

                            Toast.makeText(context, message,
                                    Toast.LENGTH_LONG).show();


                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public static void showDeclareDailog(final Context context, ArrayList<CardModel> game_users_cards_list , final Callback callback) {
        // custom dialog
        final Dialog dialog = new Dialog(context,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_declare_result);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ((TextView) dialog.findViewById(R.id.txt_title)).setText("Result | Game ID : "+game_users_cards_list.get(0).game_id);

        int imageResource = Funtions.GetResourcePath(game_users_cards_list.get(0).joker_card,context);
        Picasso.with(context).load(imageResource).into(((ImageView) dialog.findViewById(R.id.imgjokercard)));


        RecyclerView recyclerView = dialog.findViewById(R.id.rec_declareresult);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DeclareAdapter declareAdapter = new DeclareAdapter(context,game_users_cards_list);
        recyclerView.setAdapter(declareAdapter);

        new CountDownTimer(8000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                ((TextView) dialog.findViewById(R.id.txt_timer)).setText(
                        "Get Ready - Next game start in "+(millisUntilFinished/1000)+" second(s)");

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                callback.Responce("startgame","",null);

            }
        }.start();


        if(!dialog.isShowing())
            dialog.show();
    }

    public static void showDialoagonBack(Context context, final Callback callback) {
        // custom dialog
        final Dialog dialog = new Dialog(context,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //Dialog dialog=new Dialog(this,android.R.style.Theme.Black.NoTitleBar.Fullscreen)

        dialog.setContentView(R.layout.custom_dialog_close);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView btnclose = (ImageView) dialog.findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ImageView btnexitgame = (ImageView) dialog.findViewById(R.id.btnexitgame);
        ImageView btnexitloby = (ImageView) dialog.findViewById(R.id.btnexitloby);
        btnexitgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.Responce("","exit",null);

            }
        });

        btnexitloby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.Responce("","next",null);


            }
        });

        ImageView btnswitchtabel = (ImageView) dialog.findViewById(R.id.btnswitchtabel);
        btnswitchtabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.Responce("","switch",null);
            }
        });


        dialog.show();
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

//        Funtions.LOGE("MainActivity","DP : "+dp+" = "+px);

        return px;
    }
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
