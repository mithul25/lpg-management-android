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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Cart extends AppCompatActivity {
    private String cartDate, cartTime, cartSize, cartPrice,size;
    int lid=0;
    private ProgressDialog pDialog;
    String url = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/booking.php";
    private HTTPURLConnection service;
    private JSONObject json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        TextView checkoutDate = findViewById(R.id.datetxt);
        TextView checkoutTime = findViewById(R.id.timetxt);
        TextView checkoutSize = findViewById(R.id.sizetxt);
        TextView checkoutPrice = findViewById(R.id.pricetxt);

        size = getIntent().getExtras().getString("selkg");
        cartDate = getIntent().getExtras().getString("cartDate");
        cartTime = getIntent().getExtras().getString("cartTime");
        cartSize = getIntent().getExtras().getString("cartSize");
        cartPrice = getIntent().getExtras().getString("cartPrice");
        lid=getIntent().getIntExtra("log_id",0);
//
        checkoutDate.setText("Date of Delivery: "+cartDate);
        checkoutTime.setText("Time of Delivery: "+cartTime);
        checkoutSize.setText("Size of Cylinder: "+size+" kg");
        checkoutPrice.setText("Total Amount Payable:  "+cartPrice+"Rs");

//        cartDate,
//                cartTime,
//                cartSize,
//                cartPrice,
//                cartDelivery,Date of delivery,
//                cartComment --------------Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();System.currentTimeMillis()

    }
    public void goBackToHome(View view) {
        Intent home = new Intent(Cart.this, Home.class);
        startActivity(home);
    }
    public void submit(View view) {
        new book().execute();
    }



    class book extends AsyncTask<String, Integer, String> {

        String response = "";
        int success = 0;
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog= new ProgressDialog(Cart.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<>();
            postDataParams.put("cartDate", cartDate);
            postDataParams.put("cartTime", cartTime);
            postDataParams.put("quantity", cartSize);
            postDataParams.put("selkg", size);
            postDataParams.put("cartPrice", cartPrice);
            postDataParams.put("lid", String.valueOf(lid));


            service = new HTTPURLConnection();
            response = service.ServerData(url, postDataParams);
            try {
                json = new JSONObject(response);
                Log.d("Result", json.toString());
            } catch (Exception ex) {
                Log.d("ERROR", ex.getMessage());
                return ex.getMessage();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            try {
                success = json.getInt("success");
                if (success == 1) {
                    Toast.makeText(Cart.this, "Booking Completed Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Cart.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Cart.this, "Error", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
