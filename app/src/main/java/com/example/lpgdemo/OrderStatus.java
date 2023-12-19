package com.example.lpgdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderStatus extends AppCompatActivity {
    private RecyclerView recyclerView;
    JSONArray jsonArray;
    JSONObject obj=null;
    ArrayList<Order> p;
    List<Order> rowItems;
    private static RecyclerView.Adapter adapter;
    String status,oid,bookdate,quan;
    int lid=0;
    SharedPreferences sharedPreferences;
    String pref = "lpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        recyclerView = findViewById(R.id.list_orders);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        lid = sharedPreferences.getInt("loginid",0);
        new GetOrder().execute();
    }

    class GetOrder extends AsyncTask<Void, Void,JSONArray> {
        ProgressDialog loading;
        HashMap<String, String> postDataParams;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(OrderStatus.this, "loading...", null, true, true);
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            loading.dismiss();
            p=new ArrayList<Order>();
            try{
                for (int i = 0; i < result.length(); i++) {

                    obj = result.getJSONObject(i);

status=obj.getString("status");
oid=obj.getString("oid");
                    bookdate=obj.getString("bookingdate");
                    quan=obj.getString("quantity");

                    rowItems = new ArrayList<Order>();

                    Order item = new Order(obj.getString("cartDate"), obj.getString("cartTime"), obj.getString("selkg")+"kg",obj.getString("cartPrice")+"Rs",oid,"Quantity:"+quan,status,bookdate);

                    p.add(item);
                    Log.i("rowit",rowItems.toString());

                }
                Log.i("p",p.toString());

                adapter = new OrderViewHolder(p);
                recyclerView.setAdapter(adapter);



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

            String add = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/vieworders.php";
            try {

                HTTPURLConnection   service = new HTTPURLConnection();
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
