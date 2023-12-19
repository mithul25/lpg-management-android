package com.example.lpgdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class userprofile2 extends AppCompatActivity {

    JSONArray jsonArray;
    JSONObject obj = null;
    HashMap<String, String> detail;
    String b,rn,w,h;
    TextView bank,ration,ward,house;
    Button bt;
    //String url = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/requestconn.php";
    final ProgressDialog[] pDialog = new ProgressDialog[1];
    final HTTPURLConnection[] service = new HTTPURLConnection[1];
    final JSONObject[] json = new JSONObject[1];
    int lid = 0;
    private ProgressDialog pDialog1;
    SharedPreferences sharedPreferences;
    String pref = "lpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile2);
        bank=findViewById(R.id.bn);
        ration=findViewById(R.id.rn);
        ward=findViewById(R.id.wd);
        house=findViewById(R.id.hn);
        bt=findViewById(R.id.ok);
        sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        lid = sharedPreferences.getInt("loginid",0);
        Log.i("lidddd", lid + "");
        new userProfile2().execute();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(getApplicationContext(),Home.class);
                startActivity(j);
            }
        });

    }

    class userProfile2 extends AsyncTask<Void, Void, JSONArray> {

        HashMap<String, String> postDataParams;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1= new ProgressDialog(userprofile2.this);
            pDialog1.setMessage("Please Wait...");
            pDialog1.setCancelable(false);
            pDialog1.show();
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            pDialog1.dismiss();

            try{

                for (int i = 0; i < result.length(); i++) {

                    obj = result.getJSONObject(i);
                    detail=new HashMap<>();


                    b = obj.getString("bank");
                    rn = obj.getString("ration");
                    w = obj.getString("ward");
                    h =obj.getString("house");



                }

                bank.setText(b);
                ration.setText(rn);
                ward.setText(w);
                house.setText(h);



            } catch (final JSONException e) {
                Log.e("json", "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        }

        @Override
        protected JSONArray doInBackground (Void...pa){

            String add = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/up2.php";
            try {
                HTTPURLConnection  service = new HTTPURLConnection();
                postDataParams=new HashMap<String, String>();
                postDataParams.put("lid",String.valueOf(lid));
                String json = service.ServerData(add,postDataParams);


                if (json != null) {


                    jsonArray = new JSONArray(json);


                } else {
                    Log.e("ser", "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });


                }
            } catch (final JSONException e) {
                Log.e("json", "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            Log.i("doin", jsonArray.toString());
            return jsonArray;

        }
    }
}
