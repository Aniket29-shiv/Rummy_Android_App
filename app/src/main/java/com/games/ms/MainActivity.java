package com.games.ms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.games.ms.Interface.ApiRequest;
import com.games.ms.Interface.Callback;
import com.games.ms.model.Animations;
import com.games.ms.model.CardModel;
import com.games.ms.model.Funtions;
import com.games.ms.model.Usermodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.games.ms.Homepage.MY_PREFS_NAME;
import static com.games.ms.model.Funtions.ANIMATION_SPEED;
import static com.games.ms.model.Funtions.convertDpToPixel;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener , View.OnClickListener {

    String INVALID = "Invalid";
    String SET = "set";
    String IMPURE_SEQUENCE = "Impure sequence";
    String PURE_SEQUENCE = "Pure Sequence";
    String DECLARE_BACK = "";

    LinearLayout rlt_addcardview;
    RelativeLayout rltwinnersymble1,rltwinnersymble2;

    ImageView iv1,iv2,iv3,iv4,iv5,iv6,iv7,iv8,iv9,iv10,iv11,iv12,iv13;
    FrameLayout fl13;
    Animation animFadein,animMove;
    int height,width;
    ArrayList<CardModel> cardModelArrayList = new ArrayList<>();
    ArrayList<Usermodel> userModelArrayList = new ArrayList<>();
//    ArrayList<CardModel> cardImageList = new ArrayList<>();

    Animation animMoveCardsPlayerwinner1, animMoveCardsPlayerwinner2;

    ImageView imgpl1circle,imgpl2circle;

    TextView txtPlay1wallet,txtPlay2,txtPlay2wallet;

    Context context = this;
    String user_id_player1 = "";
    String user_id_player2 = "";
    String user_id_player3 = "";
    String user_id_player4 = "";
    String user_id_player5 = "";


    ArrayList<CardModel> rs_cardlist_group1 ;
    ArrayList<CardModel> rp_cardlist_group2 ;
    ArrayList<CardModel> bl_cardlist_group3 ;
    ArrayList<CardModel> bp_cardlist_group4 ;
    ArrayList<CardModel> joker_cardlist_group5 ;

    ArrayList<CardModel> ext_group1 ;
    ArrayList<CardModel> ext_group2 ;
    ArrayList<CardModel> ext_group3 ;
    ArrayList<CardModel> ext_group4 ;
    ArrayList<CardModel> ext_group5 ;

    ArrayList<CardModel> selectedcardvalue ;
    ArrayList<ArrayList<CardModel>> grouplist ;
    ImageView /*ivallcard,*/ivpickcard,iv_jokercard,imgpl1glow,imgpl2glow;

    RelativeLayout ivallcard;
    float centreX , centreY;

    CountDownTimer mCountDownTimer1;
    int timmersectlarge = 60000;
    int timmersectsmall = 1000;

    Button bt_creategroup,bt_sliptcard,bt_discard,bt_startgame,bt_drop,bt_declare;
    boolean isSplit = false;
    String selectedpatti = "";
    TextView txtwinner1,txtwinner2;
    int pStatus = 100;
    int pStatusprogress = 0;

    boolean isProgressrun1 = true;
    boolean isProgressrun2 = true;

    ProgressBar mProgress1, mProgress2, mProgress3, mProgress4, mProgress5;
    String joker_card = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        findViewById(R.id.rlt_highlighted_pick).setVisibility(View.INVISIBLE);
        findViewById(R.id.rlt_highlighted_gadhi).setVisibility(View.INVISIBLE);

        DECLARE_BACK = getString(R.string.declare_back);

        Initialization();


        InitCoutDown();


        UserProgressCount();

    }

    private void UserProgressCount() {

        //Progress -
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        mProgress1 = (ProgressBar) findViewById(R.id.circularProgressbar1);
        mProgress1.setProgressDrawable(drawable);
        mProgress1.setProgress(pStatusprogress);
        mCountDownTimer1 = new CountDownTimer(timmersectlarge, timmersectsmall) {

            @Override
            public void onTick(long millisUntilFinished) {
                imgpl1glow.setVisibility(View.VISIBLE);
                isProgressrun1 = false;
                pStatus--;
                pStatusprogress++;
                mProgress1.setProgress((int) pStatusprogress * 1);
                // txtCounttimer1.setVisibility(View.VISIBLE);
                //txtCounttimer1.setText(pStatus+"");

                if (pStatus < 25) {
                    PlaySaund(R.raw.teenpattitick);
                }

            }

            @Override
            public void onFinish() {
                pStatusprogress = 0;
                mProgress1.setProgress(100);
                mProgress1.setProgress(0);
                imgpl1glow.setVisibility(View.GONE);
                isProgressrun1 = true;
                //GamePack("1");

                API_CALL_leave_table("0");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();

                    }
                },1000);
            }

        };


    }

    int count = 8;
    TextView txtGameFinish;
    CountDownTimer counttimerforstartgame,mCountDownTimer2;
    Timer timerstatus;
    int timertime = 6000;
    SharedPreferences prefs;
    private void InitCoutDown() {

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        API_CALL_get_table();


        timerstatus = new Timer();

        timerstatus.scheduleAtFixedRate(new TimerTask() {

                                            @Override
                                            public void run() {

                                                API_CALL_status();



                                            }

                                        },
//Set how long before to start calling the TimerTask (in milliseconds)
                timertime,
//Set the amount of time between each execution (in milliseconds)
                timertime);



        counttimerforstartgame = new CountDownTimer(8000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                centreX=ivallcard.getX() + ivallcard.getWidth()  / 2;
                centreY=ivallcard.getY() + ivallcard.getHeight() / 2;

//                Funtions.LOGE("MainActivity","centreX : "+centreX+" / "+"centreY :"+centreY);

                count--;
                txtGameFinish.setVisibility(View.VISIBLE);
                txtGameFinish.setText("" + count);

            }

            @Override
            public void onFinish() {
                txtGameFinish.setVisibility(View.GONE);
                API_CALL_start_game();

            }


        }.start();


        Resources res2 = getResources();
        Drawable drawable2 = res2.getDrawable(R.drawable.circular);
        mProgress2 = (ProgressBar) findViewById(R.id.circularProgressbar2);
        mProgress2.setProgressDrawable(drawable2);
        mProgress2.setProgress(pStatusprogress);

        mCountDownTimer2 = new CountDownTimer(timmersectlarge, timmersectsmall) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
//                pStatus++;
//                mProgress.setProgress((int)pStatus*1);
                imgpl2glow.setVisibility(View.VISIBLE);
                isProgressrun2 = false;
                pStatusprogress++;
                mProgress2.setProgress((int) pStatusprogress * 1);
                pStatus--;

            }

            @Override
            public void onFinish() {
                isProgressrun2 = true;
                pStatusprogress = 0;
//                pStatus++;
                mProgress2.setProgress(100);
                mProgress2.setProgress(0);
                imgpl2glow.setVisibility(View.GONE);
                //GamePack("1");

                //  }

            }


        };


    }


    RelativeLayout rl;
    private void Initialization() {

        rs_cardlist_group1 = new ArrayList<>();
        rp_cardlist_group2 = new ArrayList<>();
        bl_cardlist_group3 = new ArrayList<>();
        bp_cardlist_group4 = new ArrayList<>();
        joker_cardlist_group5 = new ArrayList<>();
        selectedcardvalue = new ArrayList<>();

        rl = ((RelativeLayout)findViewById(R.id.sticker_animation_layout));


        ext_group1 = new ArrayList<>();
        ext_group2 = new ArrayList<>();
        ext_group3 = new ArrayList<>();
        ext_group4 = new ArrayList<>();
        ext_group5 = new ArrayList<>();


        grouplist = new ArrayList<>();


        rlt_addcardview=findViewById(R.id.rlt_addcardview);

        rltwinnersymble1=findViewById(R.id.rltwinnersymble1);
        rltwinnersymble2=findViewById(R.id.rltwinnersymble2);

        txtwinner1=findViewById(R.id.txtwinner1);
        txtwinner2=findViewById(R.id.txtwinner2);

        animMoveCardsPlayerwinner1 = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.movetoanotherwinner);
        animMoveCardsPlayerwinner2 = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.movetoanotherleftcornerwinner);



        bt_creategroup=findViewById(R.id.iv_creategroup);
        bt_declare=findViewById(R.id.bt_declare);
        bt_drop=findViewById(R.id.bt_drop);
        bt_startgame=findViewById(R.id.bt_startgame);
        bt_discard=findViewById(R.id.iv_discard);
        bt_sliptcard=findViewById(R.id.iv_sliptcard);
        ivallcard=findViewById(R.id.ivallcard);
        imgpl1glow=findViewById(R.id.imgpl1glow);
        imgpl2glow=findViewById(R.id.imgpl2glow);


        ivpickcard=findViewById(R.id.ivpickcard);
        iv_jokercard=findViewById(R.id.iv_jokercard);

        imgpl1circle = findViewById(R.id.imgpl1circle);
        imgpl2circle = findViewById(R.id.imgpl2circle);

        txtPlay1wallet = findViewById(R.id.txtPlay1wallet);
        txtPlay2wallet = findViewById(R.id.txtPlay2wallet);
        txtPlay2 = findViewById(R.id.txtPlay2);



        iv_jokercard=findViewById(R.id.iv_jokercard);



        txtGameFinish=findViewById(R.id.txtGameFinish);


        iv1=(ImageView)findViewById(R.id.iv1);
        iv2=(ImageView)findViewById(R.id.iv2);
        iv3=(ImageView)findViewById(R.id.iv3);
        iv4=(ImageView)findViewById(R.id.iv4);
        iv5=(ImageView)findViewById(R.id.iv5);
        iv6=(ImageView)findViewById(R.id.iv6);
        iv7=(ImageView)findViewById(R.id.iv7);
        iv8=(ImageView)findViewById(R.id.iv8);
        iv9=(ImageView)findViewById(R.id.iv9);
        iv10=(ImageView)findViewById(R.id.iv10);
        iv11=(ImageView)findViewById(R.id.iv11);
        iv12=(ImageView)findViewById(R.id.iv12);
        iv13=(ImageView)findViewById(R.id.iv13);

        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        iv5.setOnClickListener(this);
        iv6.setOnClickListener(this);
        iv7.setOnClickListener(this);
        iv8.setOnClickListener(this);
        iv9.setOnClickListener(this);
        iv10.setOnClickListener(this);
        iv11.setOnClickListener(this);
        iv12.setOnClickListener(this);
        iv13.setOnClickListener(this);


//        fl13=(FrameLayout) findViewById(R.id.fl13);

        OnClickListener();

//        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        String img = prefs.getString("img_name", "");
//        String wallet = prefs.getString("wallet", "");
//        long numberwallet = Long.parseLong(wallet);
//        ((TextView)findViewById(R.id.txtPlay1wallet)).setText(""
//                + NumberFormat.getNumberInstance(Locale.US).format(numberwallet));
//        Picasso.with(MainActivity.this).load(Const.IMGAE_PATH + img).into(((ImageView)findViewById(R.id.imgpl1circle)));


    }

    private void OnClickListener(){

        findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivallcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isMyChaal)
                {
                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(opponent_game_declare)
                    return;

                if(isSplit)
                {
                    animation_type = "pick";
                    API_CALL_get_card();
                }else {
                    Toast.makeText(MainActivity.this, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
                }

            }
        });

        ivpickcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isMyChaal)
                {
                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(opponent_game_declare)
                    return;

                if(isSplit)
                {
                    animation_type = "drop_pick";
                    API_CALL_get_drop_card();
                }else {
                    Toast.makeText(MainActivity.this, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
                }

            }
        });

        bt_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(!isMyChaal)
//                {
//                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
//                    return;
//                }

//                if(isSplit)
//                {
//                    Toast.makeText(MainActivity.this, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
//                    return;
//                }

                API_CALL_pack_game();

            }
        });

        bt_declare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isMyChaal)
                {
                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isSplit)
                {
                    API_CALL_declare();
                }else {
                    Toast.makeText(MainActivity.this, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
                }

//                GetCardFromLayout();

            }
        });

        bt_startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                API_CALL_start_game();

            }
        });

        bt_creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animation_type = "group";
//                API_CALL_card_value();
                CreateGroupFromSelect();
                bt_sliptcard.setVisibility(View.GONE);

            }
        });

        bt_sliptcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animation_type = "normal";

                SplitCardtoGroup();

                bt_declare.setVisibility(View.VISIBLE);
            }
        });

        bt_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isMyChaal)
                {
                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedcardvalue.size() == 1)
                    selectedpatti = selectedcardvalue.get(0).Image;

                if (selectedpatti.length()>0){

                    if(isSplit)
                    {
                        animation_type = "drop";
                        API_CALL_drop_card(null,0);
                    }
                    else {
                        Toast.makeText(MainActivity.this, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
                    }

                }else {

                    Toast.makeText(MainActivity.this, ""+getString(R.string.select_card_error_message), Toast.LENGTH_SHORT).show();

                }



            }
        });

    }

    @Override
    public void onBackPressed() {
        Funtions.showDialoagonBack(MainActivity.this, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(type.equals("exit"))
                {
                    StopSound();

                    API_CALL_leave_table("1");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finishAffinity();

                        }
                    },500);

                }
                else if(type.equals("next"))
                {
                    API_CALL_leave_table("0");
                    StopSound();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();
                        }
                    },500);
                }

            }
        });
    }

    private void TranslateLayout(View imageView, int position){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PlaySaund(R.raw.teenpatticardflip_android);
            }
        },position);


//        Funtions.LOGE("MainActivity","width : "+width+" / "+"height :"+height);
//        Funtions.LOGE("MainActivity","centreX : "+centreX+" / "+"centreY :"+centreY);

        final TranslateAnimation animationt = new TranslateAnimation(width,
                0, height, 0);
        animationt.setDuration(position);
        animationt.setFillAfter(true);
        animationt.setAnimationListener(this);

        imageView.startAnimation(animationt);


    }

    boolean animationon = false;
    private void DropTranslationAnimation(){

        animationon = true;

        final View fromView, toView, shuttleView;

        fromView = imgpl1circle;
        toView = ivpickcard;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        rl.setVisibility(View.VISIBLE);
        rl.removeAllViews();
        final ImageView sticker = new ImageView(this);


        String uriuser2 = "@drawable/teenpatti_backcard";  // where myresource
        // " +
        int stickerId = getResources().getIdentifier(uriuser2,
                null,
                getPackageName());

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        Picasso.with(context).load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_hieght, card_hieght));
        rl.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.setVisibility(View.GONE);
            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);

        PlaySaund(R.raw.teenpatticardflip_android);


//        AnimatorSet animatorSet =
//                getViewToViewScalingAnimator(rootView, shuttleView, fromRect, toRect, ANIMATION_SPEED, 0);
//
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                shuttleView.setVisibility(View.VISIBLE);
////                fromView.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                shuttleView.setVisibility(View.GONE);
//                fromView.setVisibility(View.VISIBLE);
//                animationon = false;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        animatorSet.start();

    }

    private void PickCardTranslationAnimation(){

        animationon = true;

        View animationview = findViewById(R.id.animationview);

        final View fromView, toView, shuttleView;

        fromView = ivallcard;
        toView = animationview;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        rl.setVisibility(View.VISIBLE);
        rl.removeAllViews();
        final ImageView sticker = new ImageView(this);


        String uriuser2 = "@drawable/teenpatti_backcard";  // where myresource
        // " +
        int stickerId = getResources().getIdentifier(uriuser2,
                null,
                getPackageName());

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        Picasso.with(context).load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_hieght, card_hieght));
        rl.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.setVisibility(View.GONE);
            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);

        Log.e("MainActivity","FromView : "+fromRect);
        Log.e("MainActivity","toView : "+toRect);

        PlaySaund(R.raw.teenpatticardflip_android);


//        AnimatorSet animatorSet = getViewToViewScalingAnimator(rootView, shuttleView, fromRect, toRect, ANIMATION_SPEED, 0);
//
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                shuttleView.setVisibility(View.VISIBLE);
////                fromView.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                shuttleView.setVisibility(View.GONE);
//                fromView.setVisibility(View.VISIBLE);
//                animationon = false;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        animatorSet.start();

    }

    private void DropPickTranslationAnimation(){

        animationon = true;


        View rootView = findViewById(R.id.ivpickcard);
        final View fromView, toView, shuttleView;

        fromView = ivpickcard;
        toView = imgpl1circle;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        rl.setVisibility(View.VISIBLE);
        rl.removeAllViews();
        final ImageView sticker = new ImageView(this);


        String uriuser2 = "@drawable/teenpatti_backcard";  // where myresource
        // " +
        int stickerId = getResources().getIdentifier(uriuser2,
                null,
                getPackageName());

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        Picasso.with(context).load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_hieght, card_hieght));
        rl.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.setVisibility(View.GONE);
            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);

        PlaySaund(R.raw.teenpatticardflip_android);


//        AnimatorSet animatorSet =
//                getViewToViewScalingAnimator(rootView, shuttleView, fromRect, toRect, ANIMATION_SPEED, 0);
//
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                shuttleView.setVisibility(View.VISIBLE);
////                fromView.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                shuttleView.setVisibility(View.GONE);
//                fromView.setVisibility(View.VISIBLE);
//                animationon = false;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        animatorSet.start();

    }


    String table_id = "1",game_id = "",Main_Game_ID = "";
    private void API_CALL_get_table() {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(this, Const.get_table, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {

                    Log.v("get_table" , "working -   "+resp);

                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {
                        table_id = jsonObject
                                .getJSONArray("table_data")
                                .getJSONObject(0)
                                .optString("table_id");
                    }
                    else {
                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    private void API_CALL_pack_game() {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();

        params.put("json",""+Submitcardslist);


//        Log.v("userinfo","user_id "+prefs.getString("user_id", "")+"game_id" +game_id);

        ApiRequest.Call_Api(this, Const.pack_game, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                Log.v("pack_game",resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {

                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                        bt_drop.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    private void API_CALL_leave_table(final String value) {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();

        params.put("json",""+Submitcardslist);


//        Log.v("userinfo","user_id "+prefs.getString("user_id", "")+"game_id" +game_id);

        ApiRequest.Call_Api(this, Const.leave_table, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                Log.v("pack_game",resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {

//                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    String Submitcardslist = "";
    private void API_CALL_declare() {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();

        params.put("json",""+Submitcardslist);

        String url = "";

        if(bt_declare.getText().toString().trim().equalsIgnoreCase(DECLARE_BACK))
            url = Const.declare_back;
        else
            url = Const.declare;

        ApiRequest.Call_Api(this, url, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {


                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {
                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                        bt_declare.setVisibility(View.GONE);
                        bt_drop.setVisibility(View.GONE);
                        game_declare = true;
                    }
                    else {
                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    private String GetCardFromLayout(){

        JSONArray jsonArray = new JSONArray();

        try {


            if(!isSplit)
            {

                for (int k = 0; k < 1 ; k++) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("card_group",""+0);

                    JSONArray jsonArray1 = new JSONArray();

                    for (int i = 0; i < rlt_addcardview.getChildCount() ; i++) {

                        View view = rlt_addcardview.getChildAt(i);

                        jsonArray1.put(""+view.getTag());

                        jsonObject.put("cards",jsonArray1);
                    }

                    jsonArray.put(jsonObject);

                }



            }
            else {


                for (int i = 0; i < rlt_addcardview.getChildCount() ; i++) {

                    JSONObject jsonObject = new JSONObject();

                    JSONArray jsonArray1 = new JSONArray();

                    View view = rlt_addcardview.getChildAt(i);
                    LinearLayout lnr_group_card = view.findViewById(R.id.lnr_group_card);
                    jsonObject.put("card_group",""+lnr_group_card.getTag());

                    for (int j = 0; j < lnr_group_card.getChildCount() ; j++) {

                        View view2 = lnr_group_card.getChildAt(j);
                        jsonArray1.put(""+view2.getTag());

//                    Log.e("MainActivity","Layout Tags : "+view2.getTag());

                        jsonObject.put("cards",jsonArray1);
                    }


                    jsonArray.put(jsonObject);

                }


//            Log.e("MainActivity","Layout Tags : "+jsonArray.toString());


            }

        } catch (JSONException e) {
            e.printStackTrace();

            Log.e("MainActivity","Layout Tags : "+e.getMessage());


        }

        return jsonArray.toString();
    }

    private void API_CALL_start_game() {

        RestartGameActivity();

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(this, Const.start_game, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                Log.v("start_game",resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {
                        game_id = jsonObject.optString("game_id");
                        Main_Game_ID = jsonObject.optString("game_id");
                        bt_startgame.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    private void API_CALL_status() {


        userModelArrayList.clear();


        HashMap params = new HashMap();
        params.put("game_id",""+game_id);
        params.put("table_id",""+table_id);
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));
//        Log.v("userinfo","user_id "+prefs.getString("user_id", "")+"game_id" +game_id);

        ApiRequest.Call_Api(this, Const.status, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    Parse_responseStatus(jsonObject);
//                    Log.v("status" , "working -   "+jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    private void API_CALL_getCardList() {

//        GetNextCartValue();
        RestartGameActivity();

        HashMap params = new HashMap();
        params.put("game_id",""+game_id);
//        params.put("table_id","1");
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(this, Const.my_card, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                Log.v("my_card" , resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    Parse_response(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


//        cardListAdapter.notifyDataSetChanged();

    }

    private void RestartGameActivity(){
        isSplit = false;
        rs_cardlist_group1.clear();
        rp_cardlist_group2.clear();
        bl_cardlist_group3.clear();
        bp_cardlist_group4.clear();
        joker_cardlist_group5.clear();
        ext_group1.clear();
        ext_group2.clear();
        ext_group3.clear();
        ext_group4.clear();
        ext_group5.clear();
        selectedcardvalue.clear();
        grouplist.clear();
        rlt_addcardview.removeAllViews();
        bt_sliptcard.setVisibility(View.GONE);
        bt_creategroup.setVisibility(View.GONE);
        bt_declare.setVisibility(View.VISIBLE);
        bt_drop.setVisibility(View.VISIBLE);
        bt_declare.setText(getString(R.string.declare));
        game_declare = false;
        opponent_game_declare = false;
        cardModelArrayList.clear();
        userModelArrayList.clear();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        timerstatus.cancel();
        StopSound();
    }

    boolean isgamestarted = false;
    boolean game_declare = false;
    boolean opponent_game_declare = false;
    private void Parse_responseStatus(JSONObject jsonObject) throws JSONException {

        String code = jsonObject.optString("code");
        String message = jsonObject.optString("message");
        JSONArray table_users = jsonObject.optJSONArray("table_users");



        if(table_users != null)
        {

            //---------------------------------------------User arrange ----------------

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            //JSONArray jsonArrayuser = jsonObject.getJSONArray("table_users");

            int pp1 = 0;

            for (int i = 0; i < table_users.length(); i++) {
                String use_temp = prefs.getString("user_id", "");

                if (table_users.getJSONObject(i).getString("user_id").equals(use_temp)) {
                    pp1 = i;
                }
            }

            for (int i = 0; i < pp1; i++) {

                JSONObject temp = table_users.getJSONObject(0);

                for (int j = 0; j < table_users.length() - 1; j++) {

                    table_users.put(j, table_users.get(j + 1));//=jsonArrayuser

                }
                table_users.put(table_users.length() - 1,
                        temp);
            }


            if (table_users.length() > 1) {

//                    txtGameFinish.setVisibility(View.VISIBLE);
//                    rltGameFinish.setVisibility(View.VISIBLE);

            } else {

//                    txtGameFinish.setVisibility(View.GONE);
//                    rltGameFinish.setVisibility(View.GONE);
            }

            user_id_player1 = "";
            user_id_player2 = "";
            user_id_player3 = "";
            user_id_player4 = "";
            user_id_player5 = "";

//            Log.v("table_user" , "tbl_user "+table_users);


            for (int k = 0; k < table_users.length(); k++) {
                if (k == 0) {

                    String name = table_users.getJSONObject(0).getString("name");
                    user_id_player1 = table_users.getJSONObject(0).getString(
                            "user_id");
                    String profile_pic = table_users.getJSONObject(0).getString("profile_pic");
                    String walletplayer1 = table_users.getJSONObject(0).getString("wallet");
                    //  txtPlay1.setText(name);
                    long numberamount = Long.parseLong(walletplayer1);
                    txtPlay1wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
                    // txtPlay1wallet.setText("₹" + walletplayer1);
                    Picasso.with(MainActivity.this).load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);

                    if (user_id_player1.equals(prefs.getString("user_id", ""))) {


                    } else {

                        Toast.makeText(MainActivity.this, "Your are timeout from " +
                                        "this table Join again.",
                                Toast.LENGTH_LONG).show();


                        finish();
                    }
                    // imgchipuser1.setVisibility(View.VISIBLE);
//first player
                } else if (k == 1) {

                    user_id_player2 = table_users.getJSONObject(1).getString("user_id");
                    String table_id1 = table_users.getJSONObject(1).getString(
                            "table_id");
                    final String name1 = table_users.getJSONObject(1).getString(
                            "name");
                    user_id_player2 = table_users.getJSONObject(1).getString(
                            "user_id");
                    String profile_pic1 =
                            table_users.getJSONObject(1).getString("profile_pic");
                    String walletplayer2 = table_users.getJSONObject(1).getString(
                            "wallet");


                    if (user_id_player2.equals("0")) {

                        txtPlay2.setText("");
                        txtPlay2wallet.setVisibility(View.INVISIBLE);
                        //lnrPlay2wallet.setVisibility(View.INVISIBLE);

                        String uriuser2 = "@drawable/avatar";  // where myresource
                        // " +
                        int imageResourceuser2 = getResources().getIdentifier(uriuser2,
                                null,
                                getPackageName());

                        Picasso.with(context).load(imageResourceuser2).into(imgpl2circle);
//                            rltSee2.setVisibility(View.GONE);
//                            imgpack2.clearAnimation();
//                            imgpack2.setVisibility(View.GONE);
//                            mProgress2.setProgress(0);
//                            mCountDownTimer2.cancel();
//                            imgpl2glow.setVisibility(View.GONE);
//                            imginvite2.setVisibility(View.VISIBLE);
//                            imggift2.setVisibility(View.GONE);
//                            imgchipuser2.setVisibility(View.GONE);
//                            imgwaiting2.setVisibility(View.GONE);

                    } else {

                        txtPlay2.setText(name1);
                        txtPlay2wallet.setVisibility(View.VISIBLE);
                        //lnrPlay2wallet.setVisibility(View.VISIBLE);
                        long numberamount = Long.parseLong(walletplayer2);
                        txtPlay2wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

                        //txtPlay2wallet.setText("" + walletplayer2);
                        Picasso.with(MainActivity.this).load(Const.IMGAE_PATH + profile_pic1).into(imgpl2circle);
                        // imginvite2.setVisibility(View.GONE);
                        // imggift2.setVisibility(View.VISIBLE);
                        // imgchipuser2.setVisibility(View.VISIBLE);
                        // imgwaiting2.setVisibility(View.VISIBLE);


                    }
                }
            }
        }

        if(code.equalsIgnoreCase("200"))
        {


            JSONArray drop_card = jsonObject.optJSONArray("drop_card");
            JSONArray game_users_cards = jsonObject.optJSONArray("game_users_cards");

            joker_card = jsonObject.optString("joker");
            String game_status = jsonObject.optString("game_status");
            String declare_user_id = jsonObject.optString("declare_user_id");
            boolean declare = jsonObject.optBoolean("declare");

            if(declare_user_id != null &&
                    !declare_user_id.equals(prefs.getString("user_id", "")) && declare)
            {
                opponent_game_declare = true;
                Toast.makeText(context, ""+getString(R.string.declare_game), Toast.LENGTH_SHORT).show();
                bt_declare.setText(DECLARE_BACK);
            }
            else {

                if(!declare_user_id.equalsIgnoreCase("0"))
                {
                    if(declare)
                    {
                        game_declare = declare;
                        bt_declare.setVisibility(View.GONE);
                    }
                }

            }

            String winner_user_id = jsonObject.optString("winner_user_id");

            if(game_status.equalsIgnoreCase("2") || game_status.equalsIgnoreCase("0"))
            {
//                bt_startgame.setVisibility(View.VISIBLE);
                isgamestarted = false;
                complategameUIChange();
                makeWinnertoPlayer(winner_user_id);
                game_id = jsonObject.optString("active_game_id");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count = 8;
                        counttimerforstartgame.start();
                    }
                },3000);


            }
            else if(game_status.equalsIgnoreCase("1"))
            {
                bt_startgame.setVisibility(View.GONE);
                rltwinnersymble1.setVisibility(View.GONE);
                rltwinnersymble2.setVisibility(View.GONE);

                if(!isgamestarted)
                    API_CALL_getCardList();


                String chaal = jsonObject.getString("chaal");
                makeHightLightForChaal(chaal);
            }

            String urijokar = "@drawable/" + joker_card.toLowerCase();  // where myresource " +
//
            int imageResource2 = getResources().getIdentifier(urijokar, null,
                    getPackageName());
            Picasso.with(this).load(imageResource2).into(iv_jokercard);

            //---------------------------------------------User Aarange------------------



            for (int i = 0; i < table_users.length() ; i++) {

                JSONObject tables_Object = table_users.getJSONObject(i);
                Usermodel model = new Usermodel();
                model.userid = tables_Object.optString("user_id");


                model.seat_position = tables_Object.optString("seat_position");


                model.userName = tables_Object.optString("name");
                model.userMobile = tables_Object.optString("mobile");
                model.userImage = tables_Object.optString("profile_pic");
                model.userWallet = tables_Object.optString("wallet");

                userModelArrayList.add(model);
            }

            if(drop_card != null)
            {

                for (int i = 0; i < drop_card.length() ; i++) {

                    JSONObject drop_Object = drop_card.getJSONObject(i);
                    CardModel model = new CardModel();
                    model.Image = drop_Object.optString("card");
                    String uri1 = "@drawable/" + model.Image.toLowerCase();  // where myresource " +

                    int imageResource1 = getResources().getIdentifier(uri1, null,
                            getPackageName());
                    Picasso.with(this).load(imageResource1).into(ivpickcard);

                    String src_joker_cards = "";
                    src_joker_cards = joker_card.substring(joker_card.length() - 1);

                    if(src_joker_cards != null && !src_joker_cards.equals(""))
                    {
                        if(src_joker_cards.contains(model.Image.substring(model.Image.length() - 1)))
                        {

                            ((ImageView)findViewById(R.id.iv_jokercard2)).setVisibility(View.VISIBLE);

                        }
                        else {
                            ((ImageView)findViewById(R.id.iv_jokercard2)).setVisibility(View.GONE);
                        }
                    }



                }


            }

            if(game_users_cards != null)
            {

                ArrayList<CardModel> game_users_cards_list = new ArrayList<>();

                for (int i = 0; i < game_users_cards.length() ; i++) {


                    JSONObject game_object = game_users_cards.getJSONObject(i);
                    JSONObject json_user = game_object.getJSONObject("user");
                    CardModel model = new CardModel();
                    String user_id = json_user.getString("user_id");
                    model.user_id = user_id;

                    for (Usermodel usermodel: userModelArrayList) {
                        if(user_id.equals(usermodel.userid))
                        {
                            model.user_name = usermodel.userName;
                        }
                    }

                    model.score = json_user.optInt("score",0);
                    model.won = json_user.optInt("win",0);
                    model.winner_user_id = winner_user_id;
                    model.joker_card = joker_card;
                    model.game_id = Main_Game_ID;

                    ArrayList<CardModel> groups_cardlist = new ArrayList<>();
                    if(model.user_id.equalsIgnoreCase(prefs.getString("user_id", "")))
                    {


                        String str_jsonArray = Submitcardslist;

                        JSONArray jsonArray = new JSONArray(str_jsonArray);

                        for (int k = 0; k < jsonArray.length() ; k++) {
                            JSONObject json_cardlist = jsonArray.getJSONObject(k);
                            ArrayList<CardModel> user_cards = new ArrayList<>();
                            CardModel group_model = new CardModel();

                            JSONArray json_cards = json_cardlist.getJSONArray("cards");
                            for (int j = 0; j < json_cards.length() ; j++) {

                                CardModel model_cards = new CardModel();
                                model_cards.Image = json_cards.getString(j);
                                user_cards.add(model_cards);

                                group_model.groups_cards = user_cards;

                            }
//                            JSONArray cards_array = json_user.getJSONArray("cards");
                            groups_cardlist.add(group_model);
                            model.groups_cards = groups_cardlist;

                        }
                    }
                    else {

                        JSONArray group_array = json_user.optJSONArray("cards");

                        if(group_array == null)
                            return;

                        for (int k = 0; k < group_array.length() ; k++) {
                            CardModel group_model = new CardModel();
                            ArrayList<CardModel> user_cards = new ArrayList<>();
                            JSONObject cards_object = group_array.getJSONObject(k);

                            String card_group = cards_object.optString("card_group");

                            JSONArray cards_array = cards_object.getJSONArray("cards");

                            for (int j = 0; j < cards_array.length() ; j++) {
//                                JSONObject card_object = cards_array.getJSONObject(j);

                                CardModel model_cards = new CardModel();
                                model_cards.card_group = card_group;
                                model_cards.Image = cards_array.getString(j);
                                user_cards.add(model_cards);

                                group_model.groups_cards = user_cards;
                            }

                            groups_cardlist.add(group_model);
                            model.groups_cards = groups_cardlist;

                        }
                    }

                    game_users_cards_list.add(model);

                }

                Funtions.showDeclareDailog(this, game_users_cards_list, new Callback() {
                    @Override
                    public void Responce(String resp, String type, Bundle bundle) {

                        if(resp.equalsIgnoreCase("startgame"))
                        {
                            API_CALL_start_game();
                        }

                    }
                });

            }


        }
        else {
            game_id = jsonObject.optString("active_game_id");
//            Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
        }

    }

    private void Parse_response(JSONObject jsonObject) throws JSONException {

        String code = jsonObject.optString("code");
        String message = jsonObject.optString("message");

        if(code.equalsIgnoreCase("200"))
        {

            isgamestarted = true;

            JSONArray cardsArray = jsonObject.optJSONArray("cards");

            if(cardsArray != null)
            {


                for (int i = 0; i < cardsArray.length() ; i++) {

                    JSONObject cardObject = cardsArray.getJSONObject(i);
                    CardModel model = new CardModel();
                    model.Image = cardObject.optString("card");

                    addCardsBahar(""+ model.Image,i);


                    model.card_group = cardObject.optString("card_group");

                    cardModelArrayList.add(model);
                }

                bt_sliptcard.setVisibility(View.VISIBLE);

            }

        }
        else {
            Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
        }

    }

    private void API_CALL_card_value() {


        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();
        params.put("json",""+Submitcardslist);

        int count = 0;

//        RemoveCardFromArray();

        for (int i = 0; i < selectedcardvalue.size() ; i++) {
            Funtions.LOGE("MainActvity","\n"+selectedcardvalue.get(i).toString());


            CardModel model = selectedcardvalue.get(i);
            if(model.isSelected)
            {
                model.isSelected = !model.isSelected;
                count++;
                String card_params = "card_"+count;
                params.put(card_params,model.Image);
            }

        }


        if(count < 3)
        {
            Toast.makeText(this, ""+getString(R.string.minimum_grouping), Toast.LENGTH_SHORT).show();
            return;
        }


        ApiRequest.Call_Api(this, Const.card_value, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {
                        bt_creategroup.setVisibility(View.GONE);
                        rlt_addcardview.removeAllViews();

                        for (int i = 0; i < selectedcardvalue.size() ; i++) {

                            CardModel model = selectedcardvalue.get(i);

                            int card_value = (int) jsonObject.getJSONArray("card_value").get(0);

                            if(card_value == 4)
                                model.group_value = IMPURE_SEQUENCE;
                            else
                            if(card_value == 5)
                                model.group_value = PURE_SEQUENCE;
                            else if(card_value == 6)
                                model.group_value = SET;
                            else
                                model.group_value = INVALID;

                            model.value_grp = card_value;

                            if(ext_group1.size() > 0)
                            {
                                RemoveCard(model.Image,ext_group1);
                            }

                            if(ext_group2.size() > 0)
                            {
                                RemoveCard(model.Image,ext_group2);
                            }

                            if(ext_group3.size() > 0)
                            {
                                RemoveCard(model.Image,ext_group3);
                            }

                            if(ext_group4.size() > 0)
                            {
                                RemoveCard(model.Image,ext_group4);
                            }

                            if(ext_group5.size() > 0)
                            {
                                RemoveCard(model.Image,ext_group5);
                            }


                            if(rs_cardlist_group1.size() > 0)
                            {
                                RemoveCard(model.Image,rs_cardlist_group1);
                            }

                            if(rp_cardlist_group2.size() > 0)
                            {
                                RemoveCard(model.Image,rp_cardlist_group2);
                            }

                            if(bl_cardlist_group3.size() > 0)
                            {
                                RemoveCard(model.Image,bl_cardlist_group3);
                            }

                            if(bp_cardlist_group4.size() > 0)
                            {
                                RemoveCard(model.Image,bp_cardlist_group4);
                            }

                            if(joker_cardlist_group5.size() > 0)
                            {
                                RemoveCard(model.Image,joker_cardlist_group5);
                            }


                        }



                        if(ext_group1.size() == 0){
                            ext_group1.addAll(selectedcardvalue);
                        }
                        else if(ext_group2.size() == 0){
                            ext_group2.addAll(selectedcardvalue);
                        }
                        else if(ext_group3.size() == 0){
                            ext_group3.addAll(selectedcardvalue);
                        }
                        else if(ext_group4.size() == 0){
                            ext_group4.addAll(selectedcardvalue);
                        }
                        else if(ext_group5.size() == 0){
                            ext_group5.addAll(selectedcardvalue);
                        }

                        else if(rs_cardlist_group1.size() == 0)
                        {
                            rs_cardlist_group1.addAll(selectedcardvalue);
                        }
                        else if(rp_cardlist_group2.size() == 0)
                        {
                            rp_cardlist_group2.addAll(selectedcardvalue);
                        }
                        else if(bl_cardlist_group3.size() == 0)
                        {
                            bl_cardlist_group3.addAll(selectedcardvalue);
                        }
                        else if(bp_cardlist_group4.size() == 0)
                        {
                            bp_cardlist_group4.addAll(selectedcardvalue);
                        }
                        else if(joker_cardlist_group5.size() == 0)
                        {
                            joker_cardlist_group5.addAll(selectedcardvalue);
                        }


//                        addGrouptoLayout();

                        AddSplit_to_layout();
                    }
                    else {
                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


//        cardListAdapter.notifyDataSetChanged();

    }

    private void CreateGroupFromSelect(){

        int count = 0;

        for (int i = 0; i < selectedcardvalue.size() ; i++) {
            Funtions.LOGE("MainActvity","\n"+selectedcardvalue.get(i).toString());
            CardModel model = selectedcardvalue.get(i);
            if(model.isSelected)
            {
                count++;
            }

        }

        if(count < 3)
        {
            Toast.makeText(this, ""+getString(R.string.minimum_grouping), Toast.LENGTH_SHORT).show();
            return;
        }


        for (int i = 0; i < selectedcardvalue.size() ; i++) {

            CardModel model = selectedcardvalue.get(i);

            if(ext_group1.size() > 0)
            {
                RemoveCard(model.Image,ext_group1);
            }

            if(ext_group2.size() > 0)
            {
                RemoveCard(model.Image,ext_group2);
            }

            if(ext_group3.size() > 0)
            {
                RemoveCard(model.Image,ext_group3);
            }

            if(ext_group4.size() > 0)
            {
                RemoveCard(model.Image,ext_group4);
            }

            if(ext_group5.size() > 0)
            {
                RemoveCard(model.Image,ext_group5);
            }


            if(rs_cardlist_group1.size() > 0)
            {
                RemoveCard(model.Image,rs_cardlist_group1);
            }

            if(rp_cardlist_group2.size() > 0)
            {
                RemoveCard(model.Image,rp_cardlist_group2);
            }

            if(bl_cardlist_group3.size() > 0)
            {
                RemoveCard(model.Image,bl_cardlist_group3);
            }

            if(bp_cardlist_group4.size() > 0)
            {
                RemoveCard(model.Image,bp_cardlist_group4);
            }

            if(joker_cardlist_group5.size() > 0)
            {
                RemoveCard(model.Image,joker_cardlist_group5);
            }


        }

        if(ext_group1.size() == 0){
            ext_group1.addAll(selectedcardvalue);
        }
        else if(ext_group2.size() == 0){
            ext_group2.addAll(selectedcardvalue);
        }
        else if(ext_group3.size() == 0){
            ext_group3.addAll(selectedcardvalue);
        }
        else if(ext_group4.size() == 0){
            ext_group4.addAll(selectedcardvalue);
        }
        else if(ext_group5.size() == 0){
            ext_group5.addAll(selectedcardvalue);
        }

        else if(rs_cardlist_group1.size() == 0)
        {
            rs_cardlist_group1.addAll(selectedcardvalue);
        }
        else if(rp_cardlist_group2.size() == 0)
        {
            rp_cardlist_group2.addAll(selectedcardvalue);
        }
        else if(bl_cardlist_group3.size() == 0)
        {
            bl_cardlist_group3.addAll(selectedcardvalue);
        }
        else if(bp_cardlist_group4.size() == 0)
        {
            bp_cardlist_group4.addAll(selectedcardvalue);
        }
        else if(joker_cardlist_group5.size() == 0)
        {
            joker_cardlist_group5.addAll(selectedcardvalue);
        }

        ArrayList<ArrayList<CardModel>> templist = new ArrayList<>();

        if(rs_cardlist_group1.size() > 0)
        {
            templist.add(rs_cardlist_group1);
        }

        if(rp_cardlist_group2.size() > 0)
        {
            templist.add(rp_cardlist_group2);
        }

        if(bl_cardlist_group3.size() > 0)
        {
            templist.add(bl_cardlist_group3);
        }

        if(bp_cardlist_group4.size() > 0)
        {
            templist.add(bp_cardlist_group4);
        }

        if(joker_cardlist_group5.size() > 0)
        {
            templist.add(joker_cardlist_group5);
        }

        if(ext_group1.size() > 0)
        {
            templist.add(ext_group1);
        }

        if(ext_group2.size() > 0)
        {
            templist.add(ext_group2);
        }

        if(ext_group3.size() > 0)
        {
            templist.add(ext_group3);
        }

        if(ext_group4.size() > 0)
        {
            templist.add(ext_group4);
        }

        if(ext_group5.size() > 0)
        {
            templist.add(ext_group5);
        }

        for (int i = 0; i < templist.size() ; i++) {


            API_CALL_Sort_card_value(templist.get(i),templist.size(),i);

        }

    }

    private void API_CALL_Sort_card_value(final ArrayList<CardModel> arrayList, final int size, final int position) {


        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();
        params.put("json",""+Submitcardslist);

        int count = 0;


        for (int i = 0; i < arrayList.size(); i++) {

            CardModel model = arrayList.get(i);
            count++;
            String card_params = "card_" + count;
            params.put(card_params, model.Image);

        }

        ApiRequest.Call_Api(this, Const.card_value, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {

                        for (int i = 0; i < arrayList.size() ; i++) {

                            CardModel model = arrayList.get(i);

                            int card_value = (int) jsonObject.getJSONArray("card_value").get(0);

                            if(card_value == 4)
                                model.group_value = IMPURE_SEQUENCE;
                            else
                            if(card_value == 5)
                                model.group_value = PURE_SEQUENCE;
                            else if(card_value == 6)
                                model.group_value = SET;
                            else
                                model.group_value = INVALID;

                            model.value_grp = card_value;

                        }


                    }
                    else if(code.equals("406"))
                    {

                        InvalidGroup(arrayList);

                    }
                    else {

                        InvalidGroup(arrayList);

                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();

                    InvalidGroup(arrayList);

                }

                if(position == (size - 1))
                    AddSplit_to_layout();


            }
        });

    }

    private void InvalidGroup(ArrayList<CardModel> arrayList){
        for (int i = 0; i < arrayList.size() ; i++) {

            CardModel model = arrayList.get(i);

            int card_value = 0;

            model.group_value = INVALID;

            model.value_grp = card_value;

        }

    }

    private void API_CALL_drop_card(final ArrayList<CardModel> arrayList, final int countnumber) {


        HashMap params = new HashMap();
        params.put("card",""+selectedpatti);
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();
        params.put("json",""+Submitcardslist);

//        RemoveCardFromArray();
//
//        for (int i = 0; i < selectedcardvalue.size() ; i++) {
//            Funtions.LOGE("MainActvity","\n"+selectedcardvalue.get(i).toString());
//
//
//                    CardModel model = selectedcardvalue.get(i);
//                    if(model.isSelected)
//                    {
//                        model.isSelected = !model.isSelected;
//                        count++;
//                        String card_params = "card_"+count;
//                        params.put(card_params,model.Image);
//                    }
//
//        }


        ApiRequest.Call_Api(this, Const.drop_card, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {
                        isViewonTouch = false;
                        bt_discard.setVisibility(View.GONE);
                        rlt_addcardview.removeAllViews();


                        if(ext_group1.size() > 0)
                        {
                            RemoveCard(selectedpatti,ext_group1);
                        }

                        if(ext_group2.size() > 0)
                        {
                            RemoveCard(selectedpatti,ext_group2);
                        }

                        if(ext_group3.size() > 0)
                        {
                            RemoveCard(selectedpatti,ext_group3);
                        }

                        if(ext_group4.size() > 0)
                        {
                            RemoveCard(selectedpatti,ext_group4);
                        }

                        if(ext_group5.size() > 0)
                        {
                            RemoveCard(selectedpatti,ext_group5);
                        }


                        if(rs_cardlist_group1.size() > 0)
                        {
                            RemoveCard(selectedpatti,rs_cardlist_group1);
                        }

                        if(rp_cardlist_group2.size() > 0)
                        {
                            RemoveCard(selectedpatti,rp_cardlist_group2);
                        }

                        if(bl_cardlist_group3.size() > 0)
                        {
                            RemoveCard(selectedpatti,bl_cardlist_group3);
                        }

                        if(bp_cardlist_group4.size() > 0)
                        {
                            RemoveCard(selectedpatti,bp_cardlist_group4);
                        }

                        if(joker_cardlist_group5.size() > 0)
                        {
                            RemoveCard(selectedpatti,joker_cardlist_group5);
                        }

//                        addGrouptoLayout();
                        AddSplit_to_layout();
                    }
                    else {

                        if(isViewonTouch)
                        {
                            _view.setVisibility(View.VISIBLE);

                            ResetCardtoPosition(arrayList,countnumber);
                        }


                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(isViewonTouch)
                    {
                        _view.setVisibility(View.VISIBLE);
                        isViewonTouch = false;

                        ResetCardtoPosition(arrayList,countnumber);
                    }
                }

            }
        });


//        cardListAdapter.notifyDataSetChanged();

    }

    private void API_CALL_get_card() {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(this, Const.get_card, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                Log.v("get_card" , " "+resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {
                        rlt_addcardview.removeAllViews();
                        JSONArray drop_card = jsonObject.optJSONArray("card");
                        JSONObject cardObject = drop_card.getJSONObject(0);
                        String card = cardObject.getString("cards");


                        CardModel model = new CardModel();
                        model.Image = card;


                        if (ext_group1.size() == 0) {
                            ext_group1.add(model);
                        } else if (ext_group2.size() == 0) {
                            ext_group2.add(model);
                        } else if (ext_group3.size() == 0) {
                            ext_group3.add(model);
                        } else if (ext_group4.size() == 0) {
                            ext_group4.add(model);
                        } else if (ext_group5.size() == 0) {
                            ext_group5.add(model);
                        }

                        else if (rs_cardlist_group1.size() == 0) {
                            rs_cardlist_group1.add(model);
                        } else if (rp_cardlist_group2.size() == 0) {
                            rp_cardlist_group2.add(model);
                        } else if (bl_cardlist_group3.size() == 0) {
                            bl_cardlist_group3.add(model);
                        } else if (bp_cardlist_group4.size() == 0) {
                            bp_cardlist_group4.add(model);
                        } else if (joker_cardlist_group5.size() == 0) {
                            joker_cardlist_group5.add(model);
                        }


                        AddSplit_to_layout();


                    }
                    else {
                    }
                    Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


//        cardListAdapter.notifyDataSetChanged();

    }

    private void API_CALL_get_drop_card() {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();

        params.put("json",""+Submitcardslist);


        ApiRequest.Call_Api(this, Const.get_drop_card, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                Log.v("get_card" , " "+resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {
                        rlt_addcardview.removeAllViews();
                        JSONArray drop_card = jsonObject.optJSONArray("card");
                        JSONObject cardObject = drop_card.getJSONObject(0);
                        String card = cardObject.getString("card");

                        CardModel model = new CardModel();
                        model.Image = card;


                        if (ext_group1.size() == 0) {
                            ext_group1.add(model);
                        } else if (ext_group2.size() == 0) {
                            ext_group2.add(model);
                        } else if (ext_group3.size() == 0) {
                            ext_group3.add(model);
                        } else if (ext_group4.size() == 0) {
                            ext_group4.add(model);
                        } else if (ext_group5.size() == 0) {
                            ext_group5.add(model);
                        }

                        else if (rs_cardlist_group1.size() == 0) {
                            rs_cardlist_group1.add(model);
                        } else if (rp_cardlist_group2.size() == 0) {
                            rp_cardlist_group2.add(model);
                        } else if (bl_cardlist_group3.size() == 0) {
                            bl_cardlist_group3.add(model);
                        } else if (bp_cardlist_group4.size() == 0) {
                            bp_cardlist_group4.add(model);
                        } else if (joker_cardlist_group5.size() == 0) {
                            joker_cardlist_group5.add(model);
                        }


                        AddSplit_to_layout();

                    }
                    else {
                    }
                    Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


//        cardListAdapter.notifyDataSetChanged();

    }

    private void RemoveCardFromArray() {

        if(isSplit)
        {

            if(rs_cardlist_group1.size() > 0)
            {

                for (Iterator<CardModel> it = rs_cardlist_group1.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

            if(rp_cardlist_group2.size() > 0)
            {
                for (Iterator<CardModel> it = rp_cardlist_group2.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

            if(bl_cardlist_group3.size() > 0)
            {
                for (Iterator<CardModel> it = bl_cardlist_group3.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

            if(bp_cardlist_group4.size() > 0)
            {
                for (Iterator<CardModel> it = bp_cardlist_group4.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

            if(joker_cardlist_group5.size() > 0)
            {
                for (Iterator<CardModel> it = joker_cardlist_group5.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

        }
        else {


            for (Iterator<CardModel> it = cardModelArrayList.iterator(); it.hasNext();) {
                if (it.next().isSelected) {
                    it.remove();
                }
            }

//            for (int i = 0; i < arraysize ; i++) {
//                CardModel model = cardModelArrayList.get(i);
//                if(model.isSelected)
//                {
//                    cardModelArrayList.remove(model);
//                    removearray = removearray - 1;
////                    count++;
//                    String card_params = "card_"+count;
////                    params.put(card_params,model.Image);
//                }
//
//            }
        }

    }

    private void RemoveCard(String card_value,ArrayList<CardModel> modelArray){

        for (int i = 0; i < modelArray.size() ; i++) {
            CardModel model = modelArray.get(i);
            if (model.Image.equalsIgnoreCase(card_value))
            {
                modelArray.remove(model);
            }

        }

    }


    public void complategameUIChange(){

        Submitcardslist = GetCardFromLayout();

        rs_cardlist_group1.clear();
        rp_cardlist_group2.clear();
        bl_cardlist_group3.clear();
        bp_cardlist_group4.clear();
        joker_cardlist_group5.clear();
        ext_group1.clear();
        ext_group2.clear();
        ext_group3.clear();
        ext_group4.clear();
        ext_group5.clear();
        selectedcardvalue.clear();
        grouplist.clear();
        rlt_addcardview.removeAllViews();
        bt_sliptcard.setVisibility(View.GONE);

    }

    boolean isMyChaal = false;
    public void makeHightLightForChaal(String chaal_user_id) {

        if(chaal_user_id.equals(prefs.getString("user_id","")))
        {
            isMyChaal = true;

            findViewById(R.id.rlt_highlighted_pick).setVisibility(View.VISIBLE);
            findViewById(R.id.rlt_highlighted_gadhi).setVisibility(View.VISIBLE);

        }
        else {
            isMyChaal = false;

            findViewById(R.id.rlt_highlighted_pick).setVisibility(View.INVISIBLE);
            findViewById(R.id.rlt_highlighted_gadhi).setVisibility(View.INVISIBLE);

        }

        if (chaal_user_id.equals(user_id_player1)) {

            if (isProgressrun1) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer1.start();
                isProgressrun1 = false;
            }

            isProgressrun2 = true;
//            isProgressrun3 = true;
//            isProgressrun4 = true;
//            isProgressrun5 = true;

            mCountDownTimer2.cancel();
//            mCountDownTimer3.cancel();
//            mCountDownTimer4.cancel();
//            mCountDownTimer5.cancel();

            mProgress2.setProgress(0);
//            mProgress3.setProgress(0);
//            mProgress4.setProgress(0);
//            mProgress5.setProgress(0);

            imgpl2glow.setVisibility(View.GONE);
//            imgpl3glow.setVisibility(View.GONE);
//            imgpl4glow.setVisibility(View.GONE);
//            imgpl5glow.setVisibility(View.GONE);

//            txtCounttimer2.setVisibility(View.GONE);
//            txtCounttimer3.setVisibility(View.GONE);
//            txtCounttimer4.setVisibility(View.GONE);
//            txtCounttimer5.setVisibility(View.GONE);


        } else if (chaal_user_id.equals(user_id_player2)) {
//            lnrGameButton.setVisibility(View.GONE);
//            rltGameButton.setVisibility(View.GONE);
//            //SlideDown(lnrGameButton,context);
            if (isProgressrun2) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer2.start();
                isProgressrun2 = false;
            }


            isProgressrun1 = true;
//            isProgressrun3 = true;
//            isProgressrun4 = true;
//            isProgressrun5 = true;

            mProgress1.setProgress(0);
//            mProgress3.setProgress(0);
//            mProgress4.setProgress(0);
//            mProgress5.setProgress(0);

            imgpl1glow.setVisibility(View.GONE);
//            imgpl3glow.setVisibility(View.GONE);
//            imgpl4glow.setVisibility(View.GONE);
//            imgpl5glow.setVisibility(View.GONE);

            mCountDownTimer1.cancel();
//            mCountDownTimer3.cancel();
//            mCountDownTimer4.cancel();
//            mCountDownTimer5.cancel();
//
//            txtCounttimer1.setVisibility(View.GONE);
//            txtCounttimer3.setVisibility(View.GONE);
//            txtCounttimer4.setVisibility(View.GONE);
//            txtCounttimer5.setVisibility(View.GONE);

        }
    }

    public void makeWinnertoPlayer(String chaal_user_id) {
        mProgress1.setProgress(0);
        mProgress2.setProgress(0);
        mCountDownTimer1.cancel();
        mCountDownTimer2.cancel();

        isProgressrun1 = true;
        isProgressrun2 = true;


        PlaySaund(R.raw.tpb_battle_won);

        if (chaal_user_id.equals(user_id_player1)) {

            rltwinnersymble1.setVisibility(View.VISIBLE);

        } else if (chaal_user_id.equals(user_id_player2)) {


            rltwinnersymble2.setVisibility(View.VISIBLE);
        }

    }

    public void StopSound(){

        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer = null;
        }

        if(mCountDownTimer1 != null)
        {
            mCountDownTimer1.cancel();
        }

        if(mCountDownTimer2 != null)
        {
            mCountDownTimer2.cancel();
        }

    }

    MediaPlayer mediaPlayer ;
    public void PlaySaund(int saund) {

        String value = prefs.getString("issoundon", "1");

        if (value.equals("1")) {
            mediaPlayer = MediaPlayer.create(this,
                    saund);
            mediaPlayer.start();

        } else {


        }

    }

    private void SplitCardtoGroup(){
        rs_cardlist_group1.clear();
        rp_cardlist_group2.clear();
        bl_cardlist_group3.clear();
        bp_cardlist_group4.clear();
        joker_cardlist_group5.clear();
        ext_group1.clear();
        ext_group2.clear();
        ext_group3.clear();
        ext_group4.clear();
        ext_group5.clear();
        selectedcardvalue.clear();
        grouplist.clear();
        rlt_addcardview.removeAllViews();
        bt_sliptcard.setVisibility(View.GONE);
        isSplit = true;


        for (int i = 0; i < cardModelArrayList.size() ; i++) {

            String card_tag = cardModelArrayList.get(i).card_group;

            if(card_tag.equalsIgnoreCase("rs"))
            {
                rs_cardlist_group1.add(cardModelArrayList.get(i));
            }
            else if(card_tag.equalsIgnoreCase("rp"))
            {
                rp_cardlist_group2.add(cardModelArrayList.get(i));
            }
            else if(card_tag.equalsIgnoreCase("bl"))
            {
                bl_cardlist_group3.add(cardModelArrayList.get(i));
            }
            else if(card_tag.equalsIgnoreCase("bp"))
            {
                bp_cardlist_group4.add(cardModelArrayList.get(i));
            }
            else if(card_tag.equalsIgnoreCase("jk"))
            {
                joker_cardlist_group5.add(cardModelArrayList.get(i));
            }

        }

        ArrayList<ArrayList<CardModel>> temp_grouplist = new ArrayList<>() ;


        if(rs_cardlist_group1.size() > 0)
        {
            temp_grouplist.add(rs_cardlist_group1);
        }

        if(rp_cardlist_group2.size() > 0)
        {
            temp_grouplist.add(rp_cardlist_group2);
        }

        if(bl_cardlist_group3.size() > 0)
        {
            temp_grouplist.add(bl_cardlist_group3);
        }

        if(bp_cardlist_group4.size() > 0)
        {
            temp_grouplist.add(bp_cardlist_group4);
        }

        if(joker_cardlist_group5.size() > 0)
        {
            temp_grouplist.add(joker_cardlist_group5);
        }

        for (int i = 0; i < temp_grouplist.size() ; i++) {


            API_CALL_Sort_card_value(temp_grouplist.get(i),temp_grouplist.size(),i);

        }

    }


    int grouplist_size  = 0;
    private void AddSplit_to_layout(){

        rlt_addcardview.removeAllViews();
        selectedcardvalue.clear();
        grouplist.clear();

        if(rs_cardlist_group1.size() > 0)
        {
            grouplist.add(rs_cardlist_group1);

//            CreateGroups(rs_cardlist_group1);
        }

        if(rp_cardlist_group2.size() > 0)
        {
            grouplist.add(rp_cardlist_group2);

//            CreateGroups(rp_cardlist_group2);
        }

        if(bl_cardlist_group3.size() > 0)
        {
            grouplist.add(bl_cardlist_group3);

//            CreateGroups(bl_cardlist_group3);
        }

        if(bp_cardlist_group4.size() > 0)
        {
            grouplist.add(bp_cardlist_group4);

//            CreateGroups(bp_cardlist_group4);
        }

        if(joker_cardlist_group5.size() > 0)
        {
            grouplist.add(joker_cardlist_group5);

//            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group1.size() > 0)
        {
            grouplist.add(ext_group1);

//            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group2.size() > 0)
        {
            grouplist.add(ext_group2);

//            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group3.size() > 0)
        {
            grouplist.add(ext_group3);

//            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group4.size() > 0)
        {
            grouplist.add(ext_group4);

//            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group5.size() > 0)
        {
            grouplist.add(ext_group5);

//            CreateGroups(bp_cardlist_group4);
        }


        grouplist_size = grouplist.size();
        for (int i = 0; i < grouplist.size() ; i++) {

            CreateGroups(grouplist.get(i),i);

        }

    }

    private void CreateGroups(ArrayList<CardModel> cardImageList,int count) {

        View view = LayoutInflater.from(this).inflate(R.layout.item_grouplayout,  null);
        TextView tv_status = view.findViewById(R.id.tv_status);
        ImageView iv_status = view.findViewById(R.id.iv_status);
        LinearLayout lnr_group_card = view.findViewById(R.id.lnr_group_card);
        RelativeLayout rlt_icons = view.findViewById(R.id.rlt_icons);

        tv_status.setText(""+cardImageList.get(0).group_value);
        lnr_group_card.setTag(""+cardImageList.get(0).value_grp);

        int Imageresours = 0;

        if(cardImageList.get(0).group_value.equals(INVALID))
        {
            Imageresours = Funtions.GetResourcePath("invalid_circlebg",this);
        }
        else {
            Imageresours = Funtions.GetResourcePath("valid_circlebg",this);
        }

        iv_status.setImageResource(Imageresours);


        for (int i = 0; i < cardImageList.size() ; i++) {

            AddCardtoGroup(cardImageList.get(i).Image,i,lnr_group_card,cardImageList);

        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String) view.getTag();
//                Toast.makeText(MainActivity.this , tag, Toast.LENGTH_LONG).show();

            }
        });


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int valueInPixels = (int) getResources().getDimension(R.dimen.margin_left_group);

        if(grouplist_size > 5)
        {
            valueInPixels = 20 * grouplist_size;
        }

        int leftmargin = 0;
        if(count == 0)
        {
            leftmargin = (int) convertDpToPixel(valueInPixels,this);
        }

        layoutParams.setMargins((int) leftmargin, (int) convertDpToPixel(0,this), 0, 0);


        ViewGroup.LayoutParams params = rlt_addcardview.getLayoutParams();

        setMargins(rlt_icons,0,(int) convertDpToPixel(0,this),
                (int) convertDpToPixel(0,this),0);



        rlt_addcardview.setLayoutParams(params);

        rlt_addcardview.addView(view,layoutParams);

    }

    private void addCardsBahar(String image_card , final int countnumber) {

        View view = LayoutInflater.from(this).inflate(R.layout.item_card,  null);
        ImageView imgcards = view.findViewById(R.id.imgcard);
        view.setTag(image_card+"");

        final ImageView iv_jokercard = view.findViewById(R.id.iv_jokercard);
        String src_joker_cards = "";
        src_joker_cards = joker_card.substring(joker_card.length() - 1);

        if(src_joker_cards != null && !src_joker_cards.equals(""))
        {
            if(src_joker_cards.contains(image_card.substring(image_card.length() - 1)))
            {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_jokercard.setVisibility(View.VISIBLE);
                    }
                },4000);

            }
            else {
                iv_jokercard.setVisibility(View.GONE);
            }
        }


        int valueInPixels = (int) getResources().getDimension(R.dimen.margin_left);

        int left_margin = 0;
        if(countnumber == 0)
        {
            left_margin = (int) convertDpToPixel(valueInPixels,this);
        }

//        Toast.makeText(this, ""+valueInPixels, Toast.LENGTH_SHORT).show();


        final int finalLeft_margin = left_margin;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String) view.getTag();
//                Toast.makeText(MainActivity.this , tag, Toast.LENGTH_LONG).show();

                float CardMargin = 15;

                CardModel model = cardModelArrayList.get(countnumber);
                Funtions.LOGE("MainActivity","Card Position : "+countnumber);

                model.isSelected = !model.isSelected;


                if (model.isSelected)
                {
                    CardMargin = 0;

                    if(selectedcardvalue.size() == 0) {
                        selectedcardvalue.add(model);

                        selectedpatti = model.Image;
                    }

                    for (int i = 0; i < selectedcardvalue.size() ; i++) {

                        CardModel selectmodel = selectedcardvalue.get(i);

                        if(!selectmodel.Image.contains(model.Image))
                        {
                            selectedcardvalue.add(model);
                            selectedpatti = model.Image;
                            break;
                        }

                    }

                }
                else {

                    for (int i = 0; i < selectedcardvalue.size() ; i++) {

                        if(selectedcardvalue.get(i).Image.contains(model.Image))
                        {
                            selectedcardvalue.remove(model);
                            selectedpatti = "";
                        }

                    }

                }


                setMargins(view, finalLeft_margin,(int) convertDpToPixel(CardMargin,MainActivity.this)
                        ,(int) convertDpToPixel(-40,MainActivity.this), (int) convertDpToPixel(10,MainActivity.this));

//                bt_creategroup.setVisibility(View.VISIBLE);

                if(selectedcardvalue.size() == 1)
                {
                    bt_sliptcard.setVisibility(View.GONE);
                    bt_discard.setVisibility(View.VISIBLE);
                }
                else
                {
                    bt_sliptcard.setVisibility(View.VISIBLE);
                    bt_discard.setVisibility(View.GONE);
                }

            }
        });


        String imagename = image_card;
        if(image_card.contains("id")) {
            imagename = image_card.substring(11);
        }

        Funtions.LOGE("MainActivity","imagename : "+imagename);

        int imageResource = Funtions.GetResourcePath(imagename,this);
        Picasso.with(this).load(imageResource).into(imgcards);

        TranslateLayout( imgcards, countnumber*200);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.
                setMargins(left_margin, (int) convertDpToPixel(15,this),
                        (int) convertDpToPixel(-40,this), (int) convertDpToPixel(10,this));
        ViewGroup.LayoutParams params = rlt_addcardview.getLayoutParams();


        if (countnumber==0){
            params.width = (int) convertDpToPixel(85,this);
        }else {
            params.width = (int) convertDpToPixel(85,this) * countnumber;
        }

        rlt_addcardview.setLayoutParams(params);
        rlt_addcardview.requestLayout();
        rlt_addcardview.addView(view,layoutParams);


    }


    String animation_type = "normal";
    private void AddCardtoGroup(String image_card , final int countnumber, LinearLayout addlayout,
                                final ArrayList<CardModel> arrayList) {

        View view = LayoutInflater.from(this).inflate(R.layout.item_card,  null);
        ImageView imgcards = view.findViewById(R.id.imgcard);
        view.setTag(image_card+"");

        final ImageView iv_jokercard = view.findViewById(R.id.iv_jokercard);

        String src_joker_cards = "";
        src_joker_cards = joker_card.substring(joker_card.length() - 1);

        if(src_joker_cards != null && !src_joker_cards.equals(""))
        {
            if(src_joker_cards.contains(image_card.substring(image_card.length() - 1)))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_jokercard.setVisibility(View.VISIBLE);
                    }
                },4000);

            }
            else {
                iv_jokercard.setVisibility(View.GONE);
            }
        }

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (gestureDetector.onTouchEvent(event)) {
                    // single tap

                    onGroupsCardClick(v,arrayList,countnumber);

                    return true;
                } else {
                    // your code for move and drag
                    return onTouchMainCard(v,event,arrayList,countnumber);
                }

            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onGroupsCardClick(view,arrayList,countnumber);

            }
        });


        String imagename = image_card;
        if(image_card.contains("id")) {
            imagename = image_card.substring(11);
        }

        Funtions.LOGE("MainActivity","imagename : "+imagename);
        int imageResource = Funtions.GetResourcePath(imagename,this);
        Picasso.with(this).load(imageResource).into(imgcards);

//        imgcards.setImageResource(Integer.parseInt(image_card));


        if(animation_type.equals("normal"))
            TranslateLayout(imgcards, countnumber*200);
        else if(animation_type.equals("drop") && !animationon)
            DropTranslationAnimation();
        else if(animation_type.equals("pick") && !animationon)
            PickCardTranslationAnimation();
        else if(animation_type.equals("drop_pick") && !animationon)
            DropPickTranslationAnimation();


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        if(countnumber != 0)
//        {
        layoutParams.setMargins(0, (int) convertDpToPixel(15,this), (int) convertDpToPixel(-45,this), 0);
//        }

        ViewGroup.LayoutParams params = addlayout.getLayoutParams();

//        float layout_widith = convertDpToPixel(50,this) * arrayList.size();
//
//        params.width = (int) layout_widith;


        if (countnumber==0){
            params.width = (int) convertDpToPixel(80,this);
        }
        else if(countnumber == 1)
        {
            params.width = (int) convertDpToPixel(100,this) * countnumber;
        }
        else if(countnumber == 2)
        {
            params.width = (int) convertDpToPixel(65,this) * countnumber;
        }
        else if(countnumber == 3)
        {
            params.width = (int) convertDpToPixel(55,this) * countnumber;
        }else if(countnumber == 4)
        {
            params.width = (int) convertDpToPixel(48,this) * countnumber;
        }
        else if(countnumber == 5)
        {
            params.width = (int) convertDpToPixel(45,this) * countnumber;
        }
        else if(countnumber == 6)
        {
            params.width = (int) convertDpToPixel(42,this) * countnumber;
        }
        else {
            params.width = (int) convertDpToPixel(45,this) * countnumber;
        }



        addlayout.setLayoutParams(params);
        addlayout.requestLayout();

        addlayout.addView(view,layoutParams);

    }

    private void onGroupsCardClick(View view, ArrayList<CardModel> arrayList, int countnumber) {

        if(game_declare)
        {
            return;
        }


        String tag = (String) view.getTag();
//                Toast.makeText(MainActivity.this , tag, Toast.LENGTH_LONG).show();

        float CardMargin = 15;
        float ViewAlpha = 255;
        CardModel model = arrayList.get(countnumber);
        Funtions.LOGE("MainActivity","Card Position : "+countnumber);

        model.isSelected = !model.isSelected;

        if (model.isSelected)
        {
            CardMargin = 0;
            ViewAlpha = 128;
            if(selectedcardvalue.size() == 0)
            {
                selectedpatti = model.Image;
                selectedcardvalue.add(model);
            }

            for (int i = 0; i < selectedcardvalue.size() ; i++) {

                CardModel selectmodel = selectedcardvalue.get(i);

                if(!selectmodel.Image.contains(model.Image))
                {
                    selectedcardvalue.add(model);

                    selectedpatti = model.Image;

                    break;
                }

            }

        }
        else {

            for (int i = 0; i < selectedcardvalue.size() ; i++) {

//                        if(selectedcardvalue.get(i).Image.contains(model.Image))
//                        {
//                            selectedcardvalue.remove(model);
                selectedpatti = "";
//                        }
                RemoveCard(model.Image,selectedcardvalue);

            }

        }


        setMargins(view,0,(int) convertDpToPixel(CardMargin,MainActivity.this)
                ,(int) convertDpToPixel(-45,MainActivity.this),0);

//        view.setAlpha(ViewAlpha);

        if(selectedcardvalue.size() == 1 && !opponent_game_declare)
            bt_discard.setVisibility(View.VISIBLE);
        else
            bt_discard.setVisibility(View.GONE);

        if(selectedcardvalue.size() >= 2)
            bt_creategroup.setVisibility(View.VISIBLE);
        else
            bt_creategroup.setVisibility(View.GONE);

    }

    private GestureDetector gestureDetector;

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }


    View _view;
    ViewGroup _root,remove_viewgroup;
    LinearLayout.LayoutParams view_linearparams ;
    private int _xDelta;
    private int _yDelta;
    boolean isCardDrop = false;
    public void InitilizeRootView(final ArrayList<CardModel> arrayList, final int countnumber){

        _root = (ViewGroup)findViewById(R.id.root);
        _root.setVisibility(View.VISIBLE);

        ivpickcard.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        _view.setVisibility(View.GONE);
                        isCardDrop = false;
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:

                        selectedpatti = String.valueOf(_view.getTag());
                        animation_type = "swap_animation";
                        API_CALL_drop_card(arrayList,countnumber);
//                        Toast.makeText(context, "Card Drop"+selectedpatti, Toast.LENGTH_SHORT).show();
                        isCardDrop = true;

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:

                        if(isCardDrop)
                            return false;

                        _view.setVisibility(View.VISIBLE);
                        isViewonTouch = false;

                        ResetCardtoPosition(arrayList,countnumber);

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    boolean isViewonTouch = false;
    private boolean onTouchMainCard(View cardview, MotionEvent event, final ArrayList<CardModel> arrayList, final int countnumber){

        InitilizeRootView(arrayList,countnumber);

        isCardDrop = false;

        if(!isMyChaal)
        {
//            Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!isSplit)
        {
//            Toast.makeText(MainActivity.this, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(isViewonTouch)
            return false;


        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                if(cardview.getLayoutParams() instanceof RelativeLayout.LayoutParams)
                {
                    RelativeLayout.LayoutParams  lParams = (RelativeLayout.LayoutParams) cardview.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                }
                else
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) cardview.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                }


                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:


                _view = cardview;
                view_linearparams = (LinearLayout.LayoutParams) _view.getLayoutParams();
                if(_view.getParent() != null) {
                    remove_viewgroup = ((ViewGroup)_view.getParent());
                    ((ViewGroup)_view.getParent()).removeView(_view); // <- fix
                }

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                _view.setLayoutParams(layoutParams);

                _view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return MainActivity.this.onTouch(v,event,arrayList,countnumber);
                    }
                });

                _view.setVisibility(View.GONE);

                _root.addView(_view);
                isViewonTouch = true;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(isViewonTouch)
                        {
                            ResetCardtoPosition(arrayList,countnumber);
                        }

                    }
                },5000);

                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(_view);
                _view.startDrag(null, shadowBuilder, _view, 0);


                break;
        }
        return true;

    }


    public boolean onTouch(View view, MotionEvent event, final ArrayList<CardModel> arrayList, final int countnumber) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:


                if(view.getLayoutParams() instanceof RelativeLayout.LayoutParams)
                {
                    RelativeLayout.LayoutParams  lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                }
                else
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                }


                break;
            case MotionEvent.ACTION_UP:

                Toast.makeText(context, "View release", Toast.LENGTH_SHORT).show();

                ResetCardtoPosition(arrayList,countnumber);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:


                if(view.getLayoutParams() instanceof RelativeLayout.LayoutParams)
                {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                }
                else {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);

                }

                break;
        }
        _root.invalidate();
        return true;
    }

    private void ResetCardtoPosition(final ArrayList<CardModel> arrayList, final int countnumber){
        _view.setVisibility(View.VISIBLE);
        if(_view.getParent() != null) {
            _root = ((ViewGroup)_view.getParent());
            ((ViewGroup)_view.getParent()).removeView(_view); // <- fix
        }

        if(remove_viewgroup != null) {
            _view.setLayoutParams(view_linearparams);
            remove_viewgroup.addView(_view);
        }

        isViewonTouch = false;
        isCardDrop = false;

        _view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (gestureDetector.onTouchEvent(event)) {
                    // single tap

                    onGroupsCardClick(v,arrayList,countnumber);

                    return true;
                } else {
                    // your code for move and drag
                    return onTouchMainCard(v,event,arrayList,countnumber);
                }

            }
        });


    }
    ArrayList<Float> GetNextCardvalueList = new ArrayList<>();
    private void GetNextCartValue(){

        GetNextCardvalueList.clear();

        float NewValue = 200;
        int MargeValue = 400;
        GetNextCardvalueList.add(NewValue);
        GetNextCardvalueListInt.add(MargeValue);

        for (int i = 0; i < 12 ; i++) {

            NewValue =  NewValue - 35;

            MargeValue =  MargeValue - 35;

            GetNextCardvalueList.add(NewValue);
            GetNextCardvalueListInt.add(MargeValue);
        }

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        animationon = false;
        // check for fade in animation
        if (animation == animFadein) {
//            Toast.makeText(getApplicationContext(), "Animation Stopped",
//                    Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv1.clearAnimation();
                    iv2.clearAnimation();
                    iv3.clearAnimation();
                    iv4.clearAnimation();
                    iv5.clearAnimation();
                    iv6.clearAnimation();
                    iv7.clearAnimation();
                    iv8.clearAnimation();
                    iv9.clearAnimation();
                    iv10.clearAnimation();
                    iv11.clearAnimation();
                    iv12.clearAnimation();
                    iv13.clearAnimation();

                }
            },1000);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    InitMarginView();
                }
            },1000);


//            iv1.setRotation(0f);
            // iv2.startAnimation(animMove);


            //    iv2.startAnimation(animationt);
        }
   /* if(animation==animMove)
    {
        iv2.clearAnimation();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(iv2.getWidth(), iv2.getHeight());
        lp.setMargins(con, 300, 0, 0);
        iv2.setLayoutParams(lp);
    }
*/
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    float CardSel_Margin = 120;
    @Override
    public void onClick(View v) {



        switch(v.getId())
        {


            case R.id.iv1: {
                cardModelArrayList.get(0).isSelected = !cardModelArrayList.get(0).isSelected;
                float CardMargin = MarginTop;
                if (cardModelArrayList.get(0).isSelected)
                    CardMargin = CardSel_Margin;

                setMargins(iv1,
                        (int) convertDpToPixel(GetNextCardvalueListInt.get(0), this),
                        (int) CardMargin,
                        0,
                        0);
                break;
            }
            case R.id.iv2: {
                cardModelArrayList.get(1).isSelected = !cardModelArrayList.get(1).isSelected;
                float CardMargin = MarginTop;
                if (cardModelArrayList.get(1).isSelected)
                    CardMargin = CardSel_Margin;

                setMargins(iv2,
                        (int) convertDpToPixel(GetNextCardvalueListInt.get(1), this),
                        (int) CardMargin,
                        0,
                        0);
                break;
            }
            case R.id.iv3: {
                cardModelArrayList.get(2).isSelected = !cardModelArrayList.get(2).isSelected;
                float CardMargin = MarginTop;
                if (cardModelArrayList.get(2).isSelected)
                    CardMargin = CardSel_Margin;

                setMargins(iv3,
                        (int) convertDpToPixel(GetNextCardvalueListInt.get(2), this),
                        (int) CardMargin,
                        0,
                        0);
                break;
            }case R.id.iv4: {
            cardModelArrayList.get(3).isSelected = !cardModelArrayList.get(3).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(3).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv4,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(3), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }case R.id.iv5: {
            cardModelArrayList.get(4).isSelected = !cardModelArrayList.get(4).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(4).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv5,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(4), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }case R.id.iv6: {
            cardModelArrayList.get(5).isSelected = !cardModelArrayList.get(5).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(5).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv6,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(5), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }case R.id.iv7: {
            cardModelArrayList.get(6).isSelected = !cardModelArrayList.get(6).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(6).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv7,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(6), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }case R.id.iv8: {
            cardModelArrayList.get(7).isSelected = !cardModelArrayList.get(7).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(7).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv8,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(7), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }case R.id.iv9: {
            cardModelArrayList.get(8).isSelected = !cardModelArrayList.get(8).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(8).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv9,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(8), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }case R.id.iv10: {
            cardModelArrayList.get(9).isSelected = !cardModelArrayList.get(9).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(9).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv10,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(9), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }case R.id.iv11: {
            cardModelArrayList.get(10).isSelected = !cardModelArrayList.get(10).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(10).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv11,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(10), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }case R.id.iv12: {
            cardModelArrayList.get(11).isSelected = !cardModelArrayList.get(11).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(11).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv12,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(11), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }case R.id.iv13: {
            cardModelArrayList.get(12).isSelected = !cardModelArrayList.get(12).isSelected;
            float CardMargin = MarginTop;
            if (cardModelArrayList.get(12).isSelected)
                CardMargin = CardSel_Margin;

            setMargins(iv13,
                    (int) convertDpToPixel(GetNextCardvalueListInt.get(12), this),
                    (int) CardMargin,
                    0,
                    0);
            break;
        }


        }

    }


    private void TranslateXYView() {

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.sequential);
//        animMove= AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.move);

        // set animation listener
        animFadein.setAnimationListener(this);

        float toYDelta = convertDpToPixel(75,this);

        final TranslateAnimation animationt = new TranslateAnimation(0,
                convertDpToPixel(GetNextCardvalueList.get(12),this), 0, toYDelta);
        animationt.setDuration(300);
        animationt.setFillAfter(true);
        animationt.setAnimationListener(this);
        final TranslateAnimation animationt2 = new TranslateAnimation(0,
                convertDpToPixel(GetNextCardvalueList.get(11),this), 0, toYDelta);
        animationt2.setDuration(300);
        animationt2.setFillAfter(true);
        animationt2.setAnimationListener(this);
        final TranslateAnimation animationt3 = new TranslateAnimation(0,
                convertDpToPixel(GetNextCardvalueList.get(10),this), 0, toYDelta);
        animationt3.setDuration(400);
        animationt3.setFillAfter(true);
        animationt3.setAnimationListener(this);
        final TranslateAnimation animationt4 = new TranslateAnimation(0,
                convertDpToPixel(GetNextCardvalueList.get(9),this), 0, toYDelta);
        animationt4.setDuration(500);
        animationt4.setFillAfter(true);
        animationt4.setAnimationListener(this);
        final TranslateAnimation animationt5 = new TranslateAnimation(0,
                convertDpToPixel(GetNextCardvalueList.get(8),this), 0, toYDelta);
        animationt5.setDuration(600);
        animationt5.setFillAfter(true);
        animationt5.setAnimationListener(this);
        final TranslateAnimation animationt6 = new TranslateAnimation(0, convertDpToPixel(GetNextCardvalueList.get(7),
                this), 0, toYDelta);
        animationt6.setDuration(700);
        animationt6.setFillAfter(true);
        animationt6.setAnimationListener(this);
        final TranslateAnimation animationt7 = new TranslateAnimation(0, convertDpToPixel(GetNextCardvalueList.get(6),
                this), 0, toYDelta);
        animationt7.setDuration(800);
        animationt7.setFillAfter(true);
        animationt7.setAnimationListener(this);
        final TranslateAnimation animationt8 = new TranslateAnimation(0, convertDpToPixel(GetNextCardvalueList.get(5),
                this), 0, toYDelta);
        animationt8.setDuration(1000);
        animationt8.setFillAfter(true);
        animationt8.setAnimationListener(this);
        final TranslateAnimation animationt9 = new TranslateAnimation(0, convertDpToPixel(GetNextCardvalueList.get(4),
                this), 0, toYDelta);
        animationt9.setDuration(1000);
        animationt9.setFillAfter(true);
        animationt9.setAnimationListener(this);
        final TranslateAnimation animationt10 = new TranslateAnimation(0, convertDpToPixel(GetNextCardvalueList.get(3),
                this), 0, toYDelta);
        animationt10.setDuration(1000);
        animationt10.setFillAfter(true);
        animationt10.setAnimationListener(this);
        final TranslateAnimation animationt11 = new TranslateAnimation(0, convertDpToPixel(GetNextCardvalueList.get(2),
                this), 0, toYDelta);
        animationt11.setDuration(1000);
        animationt11.setFillAfter(true);
        animationt11.setAnimationListener(this);
        final TranslateAnimation animationt12 = new TranslateAnimation(0, convertDpToPixel(GetNextCardvalueList.get(1),this),
                0, toYDelta);
        animationt12.setDuration(1000);
        animationt12.setFillAfter(true);
        animationt12.setAnimationListener(this);
        final TranslateAnimation animationt13 = new TranslateAnimation(0, convertDpToPixel(GetNextCardvalueList.get(0),this),
                0, toYDelta);
        animationt13.setDuration(1000);
        animationt13.setFillAfter(true);
        animationt13.setAnimationListener(this);



        // button click event

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
        iv1.startAnimation(animFadein);
        iv13.startAnimation(animationt);
        iv12.startAnimation(animationt2);
        iv11.startAnimation(animationt3);
        iv10.startAnimation(animationt4);
        iv9.startAnimation(animationt5);
        iv8.startAnimation(animationt6);
        iv7.startAnimation(animationt7);
        iv6.startAnimation(animationt8);
        iv5.startAnimation(animationt9);
        iv4.startAnimation(animationt10);
        iv3.startAnimation(animationt11);
        iv2.startAnimation(animationt12);
//                iv1.startAnimation(animationt13);

//            }
//        });

    }

    ArrayList<Integer> GetNextCardvalueListInt = new ArrayList<>();
    float MarginTop = 0;
    private void InitMarginView() {

        MarginTop = convertDpToPixel(150,this);
        CardSel_Margin = convertDpToPixel(120, this);


        setMargins(iv13,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(12),this),
                (int) MarginTop,
                0,
                0);

        setMargins(iv12,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(11),this),
                (int) MarginTop,
                0,
                0);

        setMargins(iv11,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(10),this),
                (int) MarginTop,
                0,
                0);
        setMargins(iv10,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(9),this),
                (int) MarginTop,
                0,
                0);
        setMargins(iv9,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(8),this),
                (int) MarginTop,
                0,
                0);
        setMargins(iv8,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(7),this),
                (int) MarginTop,
                0,
                0);

        setMargins(iv7,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(6),this),
                (int) MarginTop,
                0,
                0);
        setMargins(iv6,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(5),this),
                (int) MarginTop,
                0,
                0);
        setMargins(iv5,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(4),this),
                (int) MarginTop,
                0,
                0);
        setMargins(iv4,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(3),this),
                (int) MarginTop,
                0,
                0);
        setMargins(iv3,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(2),this),
                (int) MarginTop,
                0,
                0);
        setMargins(iv2,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(1),this),
                (int) MarginTop,
                0,
                0);
        setMargins(iv1,
                (int) convertDpToPixel(GetNextCardvalueListInt.get(0),this),
                (int) MarginTop,
                0,
                0);

    }


    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();

//            Funtions.LOGE("MainAcitvity","Left : "+left);

        }

    }


}