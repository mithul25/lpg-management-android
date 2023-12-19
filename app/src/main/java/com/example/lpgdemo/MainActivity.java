package com.example.lpgdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView _signupLink;
    private EditText username, password;
    private ProgressDialog pDialog;
    String url = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/userlog.php";
    private HTTPURLConnection service;
    private JSONObject json;
    SharedPreferences sharedPreferences;
    String pref = "lpg";
    String phone,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        boolean logged = sharedPreferences.getBoolean("logged", false);
        if(logged)
        {
            int log_id = sharedPreferences.getInt("loginid",0);
           // Toast.makeText(this, "log_id = "+log_id, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,Home.class);
            intent.putExtra("log_id",log_id);
            startActivity(intent);
            finish();
        }

        Button buttonLogin = findViewById(R.id.buttonSignIn);

        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        _signupLink=findViewById(R.id.link_signup);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               phone= username.getText().toString();
               pass=password.getText().toString();
                // make sure the input fields are not empty
                if (username.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    new LoginTask().execute(new String[]{url});


                }else {
                    Toast.makeText(MainActivity.this, "Username/Password can not be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });



        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);

            }
        });

    }
    class LoginTask extends AsyncTask<String, Void, String> {

        String response;
        HashMap<String,String> postParams;
        int loginid = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog= new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            postParams = new HashMap<>();
            postParams.put("phone",phone);
            postParams.put("pass",pass);
            String url = params[0];
            service = new HTTPURLConnection();
            response = service.ServerData(url,postParams);

            try {
                json = new JSONObject(response);
                Log.d("Result",json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(pDialog.isShowing())
                pDialog.dismiss();

            try {
                loginid =Integer.parseInt(json.getString("success"));
               // Toast.makeText(MainActivity.this, "hhhhhhh"+loginid, Toast.LENGTH_LONG).show();
                if(loginid!=0){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("logged",true);
                    editor.putInt("loginid",loginid);
                    editor.putString("phone",phone);
                    editor.putString("pass",pass);
                    editor.apply();
                    Log.d("jjjjj","innnnnnnnnnnnnn");
                    Intent intent = new Intent(MainActivity.this,Home.class);
                    intent.putExtra("loginid",loginid);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
