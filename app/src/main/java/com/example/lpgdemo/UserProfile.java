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

public class UserProfile extends AppCompatActivity {
    JSONArray jsonArray;
    JSONObject obj = null;
    HashMap<String, String> detail;
    String n, p, aa, ag, e, ad, s, d;
    TextView name, phone, aadhar, age, email, address, state, district;
    Button b;
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
        setContentView(R.layout.activity_user_profile);
        name = findViewById(R.id.nm);
        phone = findViewById(R.id.ph);
        aadhar = findViewById(R.id.aa);
        age = findViewById(R.id.ag);
        email = findViewById(R.id.em);
        address = findViewById(R.id.add);
        state = findViewById(R.id.st);
        district = findViewById(R.id.dt);
        b = findViewById(R.id.nxt);
        sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        lid = sharedPreferences.getInt("loginid",0);

        Log.i("lidddd", lid + "");
        new userprofile().execute();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(getApplicationContext(),userprofile2.class);
                        startActivity(j);
            }
        });
    }
    class userprofile extends AsyncTask<Void, Void, JSONArray> {

        HashMap<String, String> postDataParams;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1= new ProgressDialog(UserProfile.this);
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


                    n = obj.getString("name");
                    p = obj.getString("phone");
                    aa = obj.getString("aadhar");
                    ag =obj.getString("age");
                    e = obj.getString("email");
                    ad = obj.getString("address");
                    s = obj.getString("state");
                    d =obj.getString("district");


                }

                name.setText(n);
                phone.setText(p);
                aadhar.setText(aa);
                age.setText(ag);
                email.setText(e);
                address.setText(ad);
                state.setText(s);
                district.setText(d);


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

            String add = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/up1.php";
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




