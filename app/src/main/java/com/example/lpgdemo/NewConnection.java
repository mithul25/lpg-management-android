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

public class NewConnection extends AppCompatActivity {
    JSONArray jsonArray;
    JSONObject obj = null;
    HashMap<String, String> detail;
    String n,e,p,a,ad,s;
    TextView name, aadhar, phone, age, address, bank;
    Button b;
    String url = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/requestconn.php";
    final ProgressDialog[] pDialog = new ProgressDialog[1];
    final HTTPURLConnection[] service = new HTTPURLConnection[1];
    final JSONObject[] json = new JSONObject[1];
    int lid=0;
    private ProgressDialog pDialog1;
    SharedPreferences sharedPreferences;
    String pref = "lpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_connection);
        name=findViewById(R.id.nm);
        phone=findViewById(R.id.ph);
        aadhar=findViewById(R.id.aa);
        age=findViewById(R.id.ag);
        address=findViewById(R.id.add);
        bank=findViewById(R.id.bn);
        b = findViewById(R.id.nxt);
        sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        lid = sharedPreferences.getInt("loginid",0);

        Log.i("lidddd",lid+"");
        new user().execute();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestNewCOnn().execute(new String[]{url});
            }

        });
    }


    class user extends AsyncTask<Void, Void, JSONArray> {

        HashMap<String, String> postDataParams;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1= new ProgressDialog(NewConnection.this);
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
                    e = obj.getString("aadhar");
                    a = obj.getString("age");
                    ad = obj.getString("address");
                    s = obj.getString("bank");


                }

                name.setText(n);
                phone.setText(p);
                aadhar.setText(e);
                age.setText(a);
                address.setText(ad);
                bank.setText(s);


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

            String add = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/page1.php";
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


    class RequestNewCOnn extends AsyncTask<String, Integer, String> {

        String response = "";
        int success = 0;
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog[0] = new ProgressDialog(NewConnection.this);
            pDialog[0].setMessage("Please Wait, request in progress...");
            pDialog[0].setCancelable(false);
            pDialog[0].show();

        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<>();

            postDataParams.put("lid", String.valueOf(lid));
            service[0] = new HTTPURLConnection();
            String url = params[0];
            response = service[0].ServerData(url, postDataParams);
            try {
                json[0] = new JSONObject(response);
                Log.d("Result", json[0].toString());
            } catch (Exception ex) {
                Log.d("ERROR", ex.getMessage());
                return ex.getMessage();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            if (pDialog[0].isShowing()) {
                pDialog[0].dismiss();
            }
            try {
                success = json[0].getInt("success");
                if (success == 1) {
                    Toast.makeText(NewConnection.this, "Requested Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(NewConnection.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(NewConnection.this, "Error", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
