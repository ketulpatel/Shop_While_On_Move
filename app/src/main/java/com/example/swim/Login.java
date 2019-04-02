package com.example.swim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button LoginButton;
    Connection con;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        LoginButton = findViewById(R.id.login_btn);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute("");
            }
        });

    }
    public class CheckLogin extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;
        final EditText et1 = (EditText)findViewById(R.id.email1);
        final EditText et2 = (EditText)findViewById(R.id.password1);


        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(Login.this,s,Toast.LENGTH_SHORT).show();
            if(isSuccess){
                Toast.makeText(Login.this,"Login Success",Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected String doInBackground(String... strings) {

            String username = et1.getText().toString();
            String password = et2.getText().toString();

            if(username.trim().equals("") || password.trim().equals("")){
                z = "Please enter Mobile Number & Password";
            }
            else{
                try{
                    con = connectionclass("admin","FJJMGDSBPLQKEPFY","Swim","sl-eu-gb-p04.dblayer.com");
                    if(con == null){
                        z = "No internet connection";
                    }
                    else{
                        String query ="select * from registration where Email = '"+username+"' and Passwords = '"+password+"' ";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next()){
                            session.createLoginSession(username, password);
                            Intent intent = new Intent(Login.this,Home.class);
                            startActivity(intent);
                            finish();
                            z = "Login Successful";
                            isSuccess = true;
                        }
                        else{
                            z = "Invalid Username or Password";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception e){
                    Log.e("Error :::",e.getMessage());
                    isSuccess = false;
                }
            }
            return z;
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String username, String Password, String Database, String Server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://sl-eu-gb-p04.dblayer.com:17653/Swim?"
                    + "user=admin&password=FJJMGDSBPLQKEPFY");
        }catch (Exception ex){
            ex.getMessage();
        }
        return connection;
    }
}

