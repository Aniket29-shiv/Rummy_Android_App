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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.games.ms.Adapter.MyWinningAdapte;
import com.games.ms.model.MyWinnigmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyWinningAcitivity extends AppCompatActivity {

    RecyclerView rec_winning;
    MyWinningAdapte myWinningAdapte;
    ArrayList<MyWinnigmodel> myWinnigmodelArrayList;
    private static final String MY_PREFS_NAME = "Login_data" ;
    TextView tb_name,nofound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_winning_acitivity);

        rec_winning = findViewById(R.id.rec_winning);
        rec_winning.setLayoutManager(new LinearLayoutManager(this));

        tb_name = findViewById(R.id.tb_name);
        nofound = findViewById(R.id.nofound);
        tb_name.setText("Winning record");

        ((ImageView) findViewById(R.id.imgback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserWinnigAPI();
    }

    private void UserWinnigAPI(){

        final ProgressDialog progressDialog = new ProgressDialog(MyWinningAcitivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Winning Listing...");
        progressDialog.show();

        myWinnigmodelArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_WINNIG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");

                            if(code.equals("200"))
                            {


                                JSONArray ListArray = jsonObject.getJSONArray("GameWins");
                                if(ListArray.length() > 0)
                                {


                                for (int i = 0; i < ListArray.length() ; i++) {

                                    JSONObject ListObject= ListArray.getJSONObject(i);
                                    MyWinnigmodel usermodel = new MyWinnigmodel();

                                    usermodel.id = ListObject.getString("id");
                                    usermodel.table_id = ListObject.getString("table_id");
                                    usermodel.amount = ListObject.getString("amount");
                                    usermodel.winner_id = ListObject.getString("winner_id");

                                    myWinnigmodelArrayList.add(usermodel);
                                }


                                    myWinningAdapte = new MyWinningAdapte(MyWinningAcitivity.this,myWinnigmodelArrayList);
                                    rec_winning.setAdapter(myWinningAdapte);

                                }
                                else {
                                    nofound.setVisibility(View.VISIBLE);
                                }

                            }
                            else {
                                nofound.setVisibility(View.VISIBLE);
                            }







                        } catch (JSONException e) {
                            e.printStackTrace();
                            nofound.setVisibility(View.VISIBLE);

                        }




                        progressDialog.dismiss();


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MyWinningAcitivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                nofound.setVisibility(View.VISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id",prefs.getString("user_id", ""));
//                params.put("user_id","54");
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
