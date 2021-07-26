package com.games.ms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.games.ms.Adapter.MessageAdapter;
import com.games.ms.model.Chats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameChatActivity extends AppCompatActivity {

    EditText edtText;
    ImageView btnSendMessage;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    private static final String MY_PREFS_NAME = "Login_data";
    String Gameid,profile_pic;
    List<Chats> chatsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_chat);

        Intilization();

    }
    private void Intilization(){
        recyclerView = findViewById(R.id.recylerview);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatsArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(GameChatActivity.this,chatsArrayList);
        recyclerView.setAdapter(messageAdapter);

        Gameid = getIntent().getStringExtra("gameid");
        profile_pic = getIntent().getStringExtra("profile_pic");


        edtText = findViewById(R.id.edtText);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = edtText.getText().toString().trim();

                if(!message.equals(""))
                {
                    ChatListinAndSendMessages(message);
                }

                edtText.setText("");

            }
        });

        ((ImageView)findViewById(R.id.imgclose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

     Handler handler;
    Runnable runnable;

    @Override
    protected void onResume() {
        super.onResume();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {


                ChatListinAndSendMessages("");


                handler.postDelayed(this, 10000);


            }
        };
        handler.postDelayed(runnable, 0) ;

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(handler != null)
            handler.removeCallbacks(runnable);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(handler != null)
            handler.removeCallbacks(runnable);

    }

    private void ChatListinAndSendMessages(final String message){


        chatsArrayList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_CHATS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {

                                JSONArray ListArray = jsonObject.getJSONArray("list");

                                for (int i = 0; i < ListArray.length() ; i++) {
                                    JSONObject jsonObject2 = ListArray.getJSONObject(i);
                                    Chats  chatsmodel = new Chats();

                                    chatsmodel.setMessage(jsonObject2.getString("chat"));
                                    chatsmodel.setId(jsonObject2.getString("id"));
                                    chatsmodel.setUser_id(jsonObject2.getString("user_id"));
                                    chatsmodel.setSender(jsonObject2.getString("user_id"));
                                    chatsmodel.setSenderImage(profile_pic);
                                    chatsmodel.setMessage(jsonObject2.getString("chat"));

                                    chatsArrayList.add(chatsmodel);
                                }


                                Collections.reverse(chatsArrayList);

                                messageAdapter.notifyDataSetChanged();

                                if(message != null)
                                {
                                    recyclerView.scrollToPosition(chatsArrayList.size());
                                }


                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(GameChatActivity.this, message,
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
                Toast.makeText(GameChatActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("game_id", Gameid);
                if(message != null)
                params.put("chat",message);
                else
                    params.put("chat","");
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

}
