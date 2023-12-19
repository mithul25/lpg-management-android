package com.example.lpgdemo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // new password again
    final ProgressDialog[] pDialog = new ProgressDialog[1];
    final HTTPURLConnection[] service = new HTTPURLConnection[1];
    final JSONObject[] json = new JSONObject[1];
    String url = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/chpass.php";
    String url1 = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/checkconn.php";
String p,np;
Button b;
    String pref = "lpg";
    int lid;
    SharedPreferences shp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        b=findViewById(R.id.book);
        setSupportActionBar(toolbar);
        shp = getSharedPreferences("lpg", Context.MODE_PRIVATE);
       lid = shp.getInt("loginid",0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F82020")));


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i =new Intent(Home.this,Booking.class);
        i.putExtra("log_id",lid);
        startActivity(i);
    }
});




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.viewprice:

                Intent viewprice = new Intent(Home.this, ViewPrice.class);
                startActivity(viewprice);

                break;
            case R.id.nav_person:

                Intent userProfile = new Intent(Home.this, UserProfile.class);
                userProfile.putExtra("log_id",lid);
                startActivity(userProfile);

                break;
            case R.id.nav_orders:
                Intent ordersIntent = new Intent(Home.this, OrderStatus.class);
                ordersIntent.putExtra("log_id",lid);
                startActivity(ordersIntent);

                break;

            case R.id.newconn:
                new checkconn().execute(new String[]{url1});
//

                break;
            case R.id.nav_change_pass:
                showChangePasswordDialog();

                break;
            case R.id.nav_log_out:

                // Delete remember user and password
//                Paper.book().destroy();
                SharedPreferences.Editor editor = shp.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(Home.this,MainActivity.class);
                startActivity(i);
                Toast.makeText(Home.this, "Logged Out!!",
                        Toast.LENGTH_SHORT).show();
                // exit the app
//                Intent mainActivity = new Intent(Home.this, MainActivity.class);
//                mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(mainActivity);
//            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                break;
//            case R.id.nav_feedback:

//                Intent userFeedback = new Intent(Home.this, UserFeedback.class);
//                startActivity(userFeedback);

//                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showChangePasswordDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("CHANGE PASSWORD");
        alertDialog.setMessage("Please Fill All Information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.change_password_layout, null);

        final MaterialEditText pwd1 = layout_pwd.findViewById(R.id.pass1); // old password


        final  MaterialEditText pwd2 = layout_pwd.findViewById(R.id.pass2); // new password
        final   MaterialEditText pwd3 = layout_pwd.findViewById(R.id.pass3); // new password again
        alertDialog.setView(layout_pwd);

        // buttons for the alert dialog
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // change password here

                 p =pwd2.getText().toString();
                np=pwd3.getText().toString();
                if(p.length()==0){
                    Toast.makeText(Home.this, "Please enter the new password", Toast.LENGTH_SHORT).show();
                }else if(np.length()==0){
                    Toast.makeText(Home.this, "Please enter the confirm password", Toast.LENGTH_SHORT).show();
                }else if(!np.equals(p)){
                    Toast.makeText(Home.this, "Password Missmatch", Toast.LENGTH_SHORT).show();
                }else{
                    new changepass().execute(new String[]{url});
                }

                AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(Home.this);
                alertDialog1.show();


            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    class changepass extends AsyncTask<String, Integer, String> {

        String response = "";
        int success = 0;
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog[0] = new ProgressDialog(Home.this);
            pDialog[0].setMessage("Please Wait, password changing in progress...");
            pDialog[0].setCancelable(false);
            pDialog[0].show();

        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<>();
            postDataParams.put("lid", String.valueOf(lid));
            postDataParams.put("npass",p);
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
                    Toast.makeText(Home.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("register", true);
                    editor.commit();
                    Intent intent = new Intent(Home.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class checkconn extends AsyncTask<String, Integer, String> {

        String response = "";
        int success = 0;
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog[0] = new ProgressDialog(Home.this);
            pDialog[0].setMessage("Please Wait");
            pDialog[0].setCancelable(false);
            pDialog[0].show();

        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<>();
            postDataParams.put("lid",String.valueOf(lid));

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
                    Toast.makeText(Home.this, "You already requested", Toast.LENGTH_SHORT).show();


                }
                else if(success==2){
                    Toast.makeText(Home.this, "Approved new connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent conn = new Intent(Home.this, NewConnection.class);
                conn.putExtra("log_id",lid);
                startActivity(conn);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
