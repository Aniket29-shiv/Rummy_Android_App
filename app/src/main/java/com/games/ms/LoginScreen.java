package com.games.ms;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class LoginScreen extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "Login_data";
    EditText edtPhone, edtname, edtReferalCode;
    TextView tv;
    int pStatus = 0;
    private Handler handler = new Handler();
    ImageView imglogin;
    AlertDialog dialog;
    EditText edit_OTP;
    String verificationID;
    FirebaseAuth mAuth;
    RadioGroup radioGroup;
    boolean isSelected = false;
    RadioButton genderradioButton;
    ImageView imgBackground, imgBackgroundlogin;
    Context context = LoginScreen.this;

    String TAG_LOGIN_SCREEN = "LoginScreen";

    // Firebase Otp Required Feilds
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);


        imgBackground = findViewById(R.id.imgBackground);
        imgBackgroundlogin = findViewById(R.id.imgBackgroundlogin);

        String uri1 = "@drawable/" + "login_bg";  // where myresource " +
        int imageResource1 = getResources().getIdentifier(uri1, null,
                getPackageName());

        String uri2 = "@drawable/" + "login_box";  // where myresource " +
        int imageResource2 = getResources().getIdentifier(uri2, null,
                getPackageName());

       // Picasso.with(context).load(imageResource1).into(imgBackground);
        // Picasso.with(context).load(imageResource2).into(imgBackgroundlogin);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mAuth = FirebaseAuth.getInstance();
        edtPhone = findViewById(R.id.edtPhone);
        edtname = findViewById(R.id.edtname);
        edtReferalCode = findViewById(R.id.edtReferalCode);
        imglogin = findViewById(R.id.imglogin);
        imglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                VerifyCode("658952", "8589", "Male");

                if (formValidate()) {
                    if (CommonFunctions.isNetworkConnected(LoginScreen.this)) {
                        RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                        if (isSelected) {
                            login(rb.getText() + "");
                        } else {
                            Toast.makeText(LoginScreen.this, "Please select Gender first ?", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        CommonFunctions.showNoInternetDialog(LoginScreen.this);
                    }
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    isSelected = true;
                    //Toast.makeText(LoginScreen.this, rb.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void login(final String value) {

        Log.d(TAG_LOGIN_SCREEN, "Value: " + value);

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.SEND_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        handleResponse(response, value);
                    }

                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreen.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", edtPhone.getText().toString());
//                params.put("name", edtname.getText().toString());
//                params.put("gender", value.trim());
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

    private void handleResponse(String response, String value) {
        Log.d(TAG_LOGIN_SCREEN, "InHandleRes Response: " + response + " Value: " + value);

        try {
            JSONObject jsonObject = new JSONObject(response);

            String code = jsonObject.getString("code");

            if (code.equalsIgnoreCase("200")) {
                // TODO: Send Otp then call phoneLogin()
                String otp_id = jsonObject.getString("otp_id");
                sendOtp("+91" + edtPhone.getText().toString().trim(), otp_id, value);
                phoneLogin(otp_id, value, "+91" + edtPhone.getText().toString().trim());
            } else {

                if (jsonObject.has("message")) {
                    String message = jsonObject.getString("message");
                    Toast.makeText(LoginScreen.this, message, Toast.LENGTH_LONG).show();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void otpCallback(String otp_id, String value){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG_LOGIN_SCREEN, "onVerificationCompleted verification Completed: " + credential.getSmsCode());
                signInWithPhoneAuthCredential(credential, otp_id, value);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.d(TAG_LOGIN_SCREEN, "FirebaseAuthInvalidCredentialsException exception: " + e.getMessage());

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.d(TAG_LOGIN_SCREEN, "FirebaseTooManyRequestsException exception: " + e.getMessage());

                }
                Log.d(TAG_LOGIN_SCREEN, "globalException exception:" + e.getMessage());
                Toast.makeText(context, "Otp Send Fails msg: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                Log.d(TAG_LOGIN_SCREEN, "InOnCodeSentOtp Sends Succefully");
                Toast.makeText(context, "Otp Send Succefully", Toast.LENGTH_SHORT).show();
            }
        };
    }
    private void sendOtp(String phoneNumber,  String otp_id, String value){
        Log.d(TAG_LOGIN_SCREEN, "otp Sends on: " + phoneNumber);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
        }
        otpCallback(otp_id, value);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void phoneLogin(final String otp_id, final String value, String number) {
        Log.d(TAG_LOGIN_SCREEN, "InPhoneLogin otpId: " + otp_id + " Value: " + value);
        // String phoneNumber= "+91"+edtPhone.getText().toString().trim();
        //SendVerificationCode(phoneNumber);
        final Dialog dialog = new Dialog(LoginScreen.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialogbox_ctivity);
        dialog.setTitle("Title...");
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
        edit_OTP = (EditText) dialog.findViewById(R.id.edit_OTP);

        TextView tvResendOtp = dialog.findViewById(R.id.tv_dialogBox_resendOtp);
        TextView tvCountDown = dialog.findViewById(R.id.tv_dialogBox_countDown);

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageView imglogin = (ImageView) dialog.findViewById(R.id.imglogin);

        imglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_OTP.getText().toString().length() >= 4) {
                    // TODO: Verify Otp
                    String verify_code = edit_OTP.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verify_code);
                    signInWithPhoneAuthCredential(credential, otp_id, value);
//                    VerifyCode(verify_code, otp_id, value);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter OTP",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvCountDown.setText("sec");
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText("0:" + (millisUntilFinished/1000) % 60);
            }

            @Override
            public void onFinish() {
                tvResendOtp.setTextColor(Color.BLACK);
                tvResendOtp.setTextSize(16);
                tvResendOtp.setOnClickListener( view -> {
                    sendOtp(number, otp_id, value);
                    tvResendOtp.setClickable(false);
                    tvResendOtp.setTextColor(Color.rgb(28, 28, 28));
                });
            }
        }.start();

//        tvResendOtp.setOnClickListener( view -> {
//            sendOtp(number);
//        });

        dialog.show();

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, String otpId, String value) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG_LOGIN_SCREEN, "InIsSuccessful otp Verified Succefully");
                            VerifyCode(credential.getSmsCode(), otpId, value);
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.i(TAG_LOGIN_SCREEN, "InFirebaseAuthInvalidCredentialsException exception: " + task.getException().getMessage());
                            }
                            Log.i(TAG_LOGIN_SCREEN, "TaskIsUnSuccefull exception: " + task.getException().getMessage());
                            Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void VerifyCode(final String code, final String otp_id, final String value) {

        Log.d(TAG_LOGIN_SCREEN, "InVerifyCode code: " + code +" otpId: " + otp_id + " Value: " + value);

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");


                            if (code.equalsIgnoreCase("201")) {

                                String token = jsonObject.getString("token");

                                if (jsonObject.has("user")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONArray("user").getJSONObject(0);
                                    String id = jsonObject1.getString("id");
                                    String name = jsonObject1.getString("name");
                                    String mobile = jsonObject1.getString("mobile");


                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("user_id", id);
                                    editor.putString("name", name);
                                    editor.putString("mobile", mobile);
                                    editor.putString("token", token);
                                    editor.apply();

                                    Intent i = new Intent(LoginScreen.this, Homepage.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_LONG).show();
                                } else {

                                    if (jsonObject.has("message")) {
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(LoginScreen.this, "Wrong mobile number or password", Toast.LENGTH_LONG).show();
                                    }

                                }


                            } else if (code.equalsIgnoreCase("200")) {
                                String token = jsonObject.getString("token");
                                String user_id = jsonObject.getString("user_id");

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("user_id", user_id);
                                editor.putString("name", edtname.getText().toString());
                                editor.putString("mobile", edtPhone.getText().toString());
                                editor.putString("token", token);

                                editor.apply();

                                Intent i = new Intent(LoginScreen.this, Homepage.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_LONG).show();
                            } else {

                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Log.d(TAG_LOGIN_SCREEN, "Message: " + message);
                                    Toast.makeText(LoginScreen.this, message, Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //  handleResponse(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginScreen.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", edit_OTP.getText().toString());
//                params.put("name", edtname.getText().toString());
                params.put("otp_id", otp_id.trim());
                params.put("mobile", edtPhone.getText().toString());
                params.put("name", edtname.getText().toString());
                params.put("gender", value.trim());
                params.put("referral_code", edtReferalCode.getText().toString().trim());
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

    private boolean formValidate() {

        if (edtPhone.getText().toString().isEmpty() ) {
            return showToastNReturnFalse("Enter mobile number");
        }
        else if (edtPhone.getText().toString().length() < 10){
            return showToastNReturnFalse("Enter 10 digit mobile number");
        }
        else if (edtname.getText().toString().isEmpty()) {
            return showToastNReturnFalse("Enter Name");
        }

        return true;
    }
    private boolean showToastNReturnFalse(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        return false;
    }

}
