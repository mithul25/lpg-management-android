package com.example.lpgdemo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Booking extends AppCompatActivity {
    private EditText date;
    private EditText time;
    private EditText cylinderSize2;
    private TextView priceTextView;
    private String format;
    private String cartDate;
    private String cartTime;
    private String cartSize;
    private String cartPrice;
    Spinner sp;

    int lid=0;
    JSONArray jsonArray;
    JSONObject obj=null;
    HashMap<String, String> details;
String[] kg={"Select Kg","15","30"};
    String pr[];
    String selkg;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        sp=findViewById(R.id.spinner);

        Intent i=getIntent();
        lid=i.getIntExtra("log_id",0);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             selkg=   kg[i];
               // Toast.makeText(Booking.this, selkg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter aa=new ArrayAdapter(this,android.R.layout.simple_spinner_item,kg);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(aa);
        new GetPrice().execute();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent cartCheckout = new Intent(Booking.this, Cart.class);

                // assign values to variables, so that we can init the intent values.
                cartDate = date.getText().toString();
                cartTime = time.getText().toString();
                cartSize = cylinderSize2.getText().toString();
                cartPrice = priceTextView.getText().toString();
//                cartDelivery = radioButton.getText().toString();


                // send the values from this activity to the next which is the cart activity, for checkout.
                cartCheckout.putExtra("cartDate", cartDate);
                cartCheckout.putExtra("cartTime", cartTime);
                cartCheckout.putExtra("cartSize", cartSize);
                cartCheckout.putExtra("cartPrice", cartPrice);
                cartCheckout.putExtra("log_id",lid);
                cartCheckout.putExtra("selkg",selkg);
                startActivity(cartCheckout);


            }
        });

        date = findViewById(R.id.homeDatePicker);
        time = findViewById(R.id.homeTimePicker);
//        radioGroup = findViewById(R.id.radioGroup);
        cylinderSize2 = findViewById(R.id.kgText2);

        priceTextView = findViewById(R.id.priceTextView);

        //get the current price


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int d, m, y;
                Calendar calendar = Calendar.getInstance();
                d = calendar.get(Calendar.DAY_OF_MONTH);
                m = calendar.get(Calendar.MONTH);
                y = calendar.get(Calendar.YEAR);
                DatePickerDialog pickerDialog = new DatePickerDialog(
                        Booking.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int yyyy, int mm, int dd) {
                                mm += 1;
                                date.setText(dd +"/"+ mm +"/"+ yyyy );
                            }
                        },
                        y, m, d);

                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                int hr, min;
                Calendar calendar = Calendar.getInstance();
                hr = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);

                selectedTimeFormat(hr);

                TimePickerDialog pickerDialog = new TimePickerDialog(
                        Booking.this,
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hr, int min) {
                                selectedTimeFormat(hr);
                                if((hr>7&&format.equals("AM")||(hr<8&& format.equals("PM")))) {
                                    time.setText(hr + ":" + min + " " + format);
                                }
                                else{
                                    Toast.makeText(Booking.this, "Choose Time between 7:00 AM and 8:00 PM", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        hr, min, false);
                pickerDialog.show();



            }
        });


    }


    public void increment(View view){
        if (cylinderSize2.getText().length() == 0){
            priceTextView.setText("0");
            Toast.makeText(this, "Type in a value...", Toast.LENGTH_SHORT).show();
        } else {
            Float intCylinderSize2 = Float.parseFloat(cylinderSize2.getText().toString());
            if ((intCylinderSize2 >= 0) && (intCylinderSize2 <= 1000)) {
                intCylinderSize2++;
                if (intCylinderSize2 > 1000) {
                    Toast.makeText(this, "This shouldnt be greater than 1000", Toast.LENGTH_SHORT).show();
                } else {
                    String intCylinderSizeIncreased = intCylinderSize2.toString();
                    cylinderSize2.setText(intCylinderSizeIncreased);
                    // update price function
                    display(intCylinderSize2);
                }
            } else {
                Toast.makeText(this, "Kindly pick a size lower than 1000kg", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void display(Float number){

//        selectedDeliveryMethod();

//        switch (radioButton.getText().toString()) {
//            case "Home Service":
//                number *= Integer.parseInt(homePriceFirebase);
//                break;
//            case "Come to our office":
//                number *= Integer.parseInt(officePriceFirebase);
//                break;
//            default:
//                return;
//        }
if(selkg.equals("15")){
   number*= Integer.parseInt(pr[0]);
}
else{
    number*= Integer.parseInt(pr[1]);
}
//        number *= homePriceOfGas;
        String intCylinderSize2ToString = number.toString();
Log.i("prrrrrrrrrr",intCylinderSize2ToString);
        priceTextView.setText(intCylinderSize2ToString);

    }
    public void decrement(View view) {
        if (cylinderSize2.getText().length() == 0) {
            priceTextView.setText("0");
            Toast.makeText(this, "Type in a value...", Toast.LENGTH_SHORT).show();
        } else {
            Float intCylinderSize2 = Float.parseFloat(cylinderSize2.getText().toString());
            if ((intCylinderSize2 >= 0) && (intCylinderSize2 <= 1000)) {
                intCylinderSize2--;
                if (intCylinderSize2 < 0) {
                    Toast.makeText(this, "This shouldnt be less than zero.", Toast.LENGTH_SHORT).show();
                } else {
                    String intCylinderSizeDecreased = intCylinderSize2.toString();
                    cylinderSize2.setText(intCylinderSizeDecreased);
                    // update price function
                    display(intCylinderSize2);
                }
            } else {
                Toast.makeText(this, "Kindly pick a size higher than 0kg", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void selectedTimeFormat(int hr) {
        if(hr == 0){
            format = "AM";
        } else if(hr == 12){
            format = "PM";
        } else if(hr > 12){
            format = "PM";
        } else {
            format = "AM";
        }
    }
    class GetPrice extends AsyncTask<Void, Void, JSONArray> {

        HashMap<String, String> postDataParams;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog= new ProgressDialog(Booking.this);
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
