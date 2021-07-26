package com.games.ms.model;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.games.ms.Interface.Callback;


public class Animations {
    public Animation fromAtoB(float fromX, float fromY, float toX, float toY, Animation.AnimationListener l, int speed, final Callback callback){

//
//        Animation fromAtoB = new TranslateAnimation(
//                Animation.ABSOLUTE, //from xType
//                fromX,
//                Animation.ABSOLUTE, //to xType
//                toX,
//                Animation.ABSOLUTE, //from yType
//                fromY,
//                Animation.ABSOLUTE, //to yType
//                toY
//        );
//
//        fromAtoB.setDuration(speed);
//        fromAtoB.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
//
//
//        if(l != null)
//            fromAtoB.setAnimationListener(l);



        Animation fromAtoB = new TranslateAnimation(
                fromX, toX,
                fromY, toY);
        fromAtoB.setDuration(speed);
//        fromAtoB.setFillAfter(true);
        fromAtoB.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                animation.cancel();
                callback.Responce("end","",null);
                Log.d("Tag","Animation End");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return fromAtoB;
    }
}
