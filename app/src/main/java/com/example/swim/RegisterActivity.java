package com.example.swim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText FullName, Email, Mobile, Address, Passwords;
    Button registerButton;

    String URL = "http://swimrest.eu-gb.mybluemix.net/DemoService/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FullName = findViewById(R.id.fullname);
        Email = findViewById(R.id.email);
        Mobile = findViewById(R.id.mobile);
        Address = findViewById(R.id.address);
        Passwords = findViewById(R.id.password);
        registerButton = findViewById(R.id.btn_submit);

        final Context context = this;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if (s.equals("true")) {
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Can't Register", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegisterActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                        ;
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("FullName", FullName.getText().toString());
                        parameters.put("Email", Email.getText().toString());
                        parameters.put("Mobile", Mobile.getText().toString());
                        parameters.put("Address", Address.getText().toString());
                        parameters.put("Passwords", Passwords.getText().toString());
                        return parameters;
                    }
                };

                RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
                rQueue.add(request);


            }
        });


    }
}
