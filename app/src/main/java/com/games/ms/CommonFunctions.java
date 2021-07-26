package com.games.ms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.os.Build;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonFunctions {

    public static boolean validate(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void showNoInternetDialog(Activity context) {
        showNoInternetDialog(context, false);
    }

    public static void showNoInternetDialog(final Activity context, final boolean close) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please check your internet connection!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (close) {
                            context.finish();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }


    public static void showAlertDialog(final Activity context, String title, String message, boolean isCancelable,
                                       String firstButtonText, DialogInterface.OnClickListener firstButtonListener
            , String secondButtonText, DialogInterface.OnClickListener secondButtonListener) {

        showAlertDialog(context,title
        ,message,isCancelable,firstButtonText,firstButtonListener,secondButtonText,secondButtonListener,null,null);
    }
        public static void showAlertDialog(final Activity context, String title, String message, boolean isCancelable,
                                       String firstButtonText, DialogInterface.OnClickListener firstButtonListener
            , String secondButtonText, DialogInterface.OnClickListener secondButtonListener
                , String thirdButtonText, DialogInterface.OnClickListener thirdButtonListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (title != null) {
            builder.setTitle(title);
        }

        if (message != null) {
            builder.setMessage(message);
        }

        builder.setCancelable(isCancelable);

        if (firstButtonText != null) {
            builder.setPositiveButton(firstButtonText, firstButtonListener);
        }

        if (secondButtonText != null) {
            builder.setNegativeButton(secondButtonText, secondButtonListener);
        }

        if (thirdButtonText != null) {
            builder.setNeutralButton(thirdButtonText, thirdButtonListener);
        }

        AlertDialog alert = builder.create();
        alert.show();

    }


//    public static boolean isUserRegistered(final Activity context) {
//
//
//        if (PrefMnger.getString(context, PrefMnger.EMAIL) == null) {
//
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setMessage("Please register first!")
//                    .setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            context.startActivity(new Intent(context, LoginScreen.class));
//                        }
//                    }).setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//
//            return false;
//        }
//
//        return true;
//    }

    public static String getImagesCommaSeperated(String mainImageUrl, JSONObject jsonObject1){

        String main_img = jsonObject1.optString("main_img");
        String img = jsonObject1.optString("img");

        String imgs = "";

        if (!main_img.isEmpty()) {
            imgs = mainImageUrl + main_img;
        }

        if (!img.isEmpty()) {

            String[] images = img.split(",");

            for (int a = 0; a < images.length; a++) {
                String imgg = images[a];

                if(imgs.isEmpty()){
                    imgs = mainImageUrl + imgg;
                }else {
                    imgs =imgs+","+ mainImageUrl + imgg;
                }
            }

        }

        if(imgs.isEmpty()){
            return null;
        }

        return imgs;

    }

    public static SoundPool SoundPoolFunction(){
        SoundPool soundPool;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else{

            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);
        }

        return soundPool;
    }

}
