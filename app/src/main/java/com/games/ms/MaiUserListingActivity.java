package com.games.ms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.games.ms.Adapter.UserListingAdapter;
import com.games.ms.model.Usermodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MaiUserListingActivity extends AppCompatActivity {

    RecyclerView rec_userListing;
    UserListingAdapter userListingAdapter;
    ArrayList<Usermodel> usermodelArrayList;
    private static final String MY_PREFS_NAME = "Login_data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mai_user_listing);

        ((ImageView) findViewById(R.id.imgback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rec_userListing = findViewById(R.id.rec_userlisting);
        rec_userListing.setLayoutManager(new LinearLayoutManager(this));

        UserListingApi();

    }

    private void UserListingApi(){
        final ProgressDialog progressDialog = new ProgressDialog(MaiUserListingActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("User Listing...");
        progressDialog.show();

        usermodelArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.MAIL_USERlISTING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();


                        try {
                            JSONObject   jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");

                            if(code.equals("200"))
                            {


                                JSONArray ListArray = jsonObject.getJSONArray("List");
                                for (int i = 0; i < ListArray.length() ; i++) {

                                    JSONObject ListObject= ListArray.getJSONObject(i);
                                    Usermodel usermodel = new Usermodel();

                                    usermodel.userid = ListObject.getString("id");
                                    usermodel.userName = ListObject.getString("name");
                                    usermodel.userGender = ListObject.getString("gender");
                                    usermodel.userWallet = ListObject.getString("wallet");
                                    usermodel.userMobile = ListObject.getString("mobile");
                                    usermodel.userImage = ListObject.getString("profile_pic");

                                    usermodelArrayList.add(usermodel);
                                }


                                userListingAdapter = new UserListingAdapter(MaiUserListingActivity.this,usermodelArrayList);
                                rec_userListing.setAdapter(userListingAdapter);
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MaiUserListingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", prefs.getString("token", ""));
                params.put("user_id", prefs.getString("user_id", ""));
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

}
