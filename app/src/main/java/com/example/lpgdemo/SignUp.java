package com.example.lpgdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
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

public class SignUp extends AppCompatActivity {
    String n, e, p, a, ad, h, c, ps, ag, w, ra, b, s, d;
    EditText name, email, address, aadhar, phone, house, cpass, pass, age, ward, ration, bank, state, district;
    Button r;
    String url = "https://lpgcoc.000webhostapp.com/lpg/mylpg/mylpg/userreg.php";
    final ProgressDialog[] pDialog = new ProgressDialog[1];
    final HTTPURLConnection[] service = new HTTPURLConnection[1];
    final JSONObject[] json = new JSONObject[1];

    String pref = "lpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView alreadyHaveAcct = findViewById(R.id.alreadyHaveAcct);
        name = findViewById(R.id.nm);
        email = findViewById(R.id.eid);
        age = findViewById(R.id.ag);
        ward = findViewById(R.id.wrd);
        ration = findViewById(R.id.rcd);
        bank = findViewById(R.id.bacc);
        phone = findViewById(R.id.ph);
        aadhar = findViewById(R.id.ad);
        address = findViewById(R.id.add);
        state = findViewById(R.id.st);
        district = findViewById(R.id.di);
        house = findViewById(R.id.hn);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
        r = findViewById(R.id.btnSignUp);




        alreadyHaveAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIn = new Intent(SignUp.this, MainActivity.class);
                startActivity(signIn);

            }

        });

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n = name.getText().toString();
                ad = address.getText().toString();
                ag = age.getText().toString();
                String agepattern = "[0-9]{2}";
                w = ward.getText().toString();
                String wpattern = "[0-9]{1,2}";
                ra = ration.getText().toString();
                String rpattern = "[0-9]{10}";
                b = bank.getText().toString();
                String bpattern = "[0-9]{11,12}";
                ps = pass.getText().toString();
                c = cpass.getText().toString();
                e = email.getText().toString().trim();
                String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String MobilePattern = "[0-9]{10}";
                p = phone.getText().toString();
                String aadharpattern = "[0-9]{12}";
                a = aadhar.getText().toString();
                String housepattern = "[0-9]{2,3}";
                h = house.getText().toString();
                s = state.getText().toString();
                d = district.getText().toString();
                if (n.length() == 0) {
                    Toast.makeText(SignUp.this, "please enter name", Toast.LENGTH_SHORT).show();
                } else if (ad.length() == 0) {
                    Toast.makeText(SignUp.this, "please enter address", Toast.LENGTH_SHORT).show();
                } else if (!(e.matches(emailpattern)) && e.length() > 0) {
                    Toast.makeText(SignUp.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (!(p.matches(MobilePattern)) && p.length() > 0) {
                    Toast.makeText(SignUp.this, "Invalid phone no", Toast.LENGTH_SHORT).show();
                } else if (!(a.matches(aadharpattern)) && a.length() > 0) {
                    Toast.makeText(SignUp.this, "Invalid aadhar no", Toast.LENGTH_SHORT).show();
                } else if (!(h.matches(housepattern)) && h.length() > 0) {
                    Toast.makeText(SignUp.this, "Invalid house no.", Toast.LENGTH_SHORT).show();
                } else if (!(ag.matches(agepattern)) && ag.length() > 0) {
                    Toast.makeText(SignUp.this, "please enter age", Toast.LENGTH_SHORT).show();
                } else if (!(w.matches(wpattern)) && w.length() > 0) {
                    Toast.makeText(SignUp.this, "please enter ward no.", Toast.LENGTH_SHORT).show();
                } else if (!(ra.matches(rpattern)) && ra.length() > 0) {
                    Toast.makeText(SignUp.this, "please enter ration card no.", Toast.LENGTH_SHORT).show();
                } else if (!(b.matches(bpattern)) && b.length() > 0) {
                    Toast.makeText(SignUp.this, "please enter bank acc no.", Toast.LENGTH_SHORT).show();
                } else if (ps.length() == 0) {
                    Toast.makeText(SignUp.this, "please enter the password", Toast.LENGTH_SHORT).show();
                } else if (c.length() == 0) {
                    Toast.makeText(SignUp.this, "please confirm the password", Toast.LENGTH_SHORT).show();
                } else if (!ps.equals(c)) {
                    Toast.makeText(SignUp.this, "password missmatch", Toast.LENGTH_SHORT).show();
                } else if (s.length() == 0) {
                    Toast.makeText(SignUp.this, "please enter the state", Toast.LENGTH_SHORT).show();
                } else if (d.length() == 0) {
                    Toast.makeText(SignUp.this, "please enter the district", Toast.LENGTH_SHORT).show();
                } else {
                    new ExecuteTask().execute(new String[]{url});
                }

            }
        });
    }


    class ExecuteTask extends AsyncTask<String, Integer, String> {

        String response = "";
        int success = 0;
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog[0] = new ProgressDialog(SignUp.this);
            pDialog[0].setMessage("Please Wait, registration in progress...");
            pDialog[0].setCancelable(false);
            pDialog[0].show();

        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<>();
            postDataParams.put("name", n);
            postDataParams.put("email", e);
            postDataParams.put("address", ad);
            postDataParams.put("state", s);
            postDataParams.put("district", d);
            postDataParams.put("phone", p);
            postDataParams.put("aadhar", a);
            postDataParams.put("house", h);
            postDataParams.put("pass", ps);
            postDataParams.put("cpass", c);
            postDataParams.put("ward", w);
            postDataParams.put("bank", b);
            postDataParams.put("ration", ra);
            postDataParams.put("age", ag);
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
                    Toast.makeText(SignUp.this, "Registration Completed Successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("register", true);
                    editor.commit();
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, "Error", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

