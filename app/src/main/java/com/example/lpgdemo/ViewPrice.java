package com.example.lpgdemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewPrice extends AppCompatActivity {
    JSONArray jsonArray;
    JSONObject obj=null;
    HashMap<String, String> details;
    TextView price1,price2;
    String pr[];
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_price);
        price1=findViewById(R.id.p1);
        price2=findViewById(R.id.p2);
        new GetPrice().execute();
    }
    class GetPrice extends AsyncTask<Void, Void, JSONArray> {

        HashMap<String, String> postDataParams;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog= new ProgressDialog(ViewPrice.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            try{
                pr=new String[2];
                for (int i = 0; i < result.length(); i++) {

                    obj = result.getJSONObject(i);
                    details=new HashMap<>();

                    String  price=obj.getString("price");
                    String pid=obj.getString("proid");
                    pr[i]=price;
                }

                price1.setText(pr[0]+"Rs");
                price2.setText(pr[1]+"Rs");

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

            String add = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/viewprice.php";
            try {
                HTTPURLConnection  service = new HTTPURLConnection();
                postDataParams=new HashMap<String, String>();

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
