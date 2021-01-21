package com.example.swim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.qrcode.encoder.QRCode;

import java.sql.Connection;
import java.sql.DriverManager;

public class Home extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggel;
    private Toolbar mtoolbar;
    ImageButton openQr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        


        openQr = (ImageButton)findViewById(R.id.openQr);

        openQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenQR = new Intent(Home.this, QrCodeScannerActivity.class);
                startActivity(intentOpenQR);
                finish();
            }
        });

        mtoolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mtoolbar);
        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggel = new ActionBarDrawerToggle(this, mDrawer,R.string.open,R.string.close);
        mDrawer.addDrawerListener(mToggel);
        mToggel.syncState();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nv = (NavigationView)findViewById(R.id.nv1);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case(R.id.Logout):
                        
                        break;
                    case(R.id.Cart):
                        Intent intent = new Intent(Home.this,ShoppingCart.class);
                        startActivity(intent);
                        break;
                    case(R.id.qrCode):
                        Intent intentqr = new Intent(Home.this,QrCodeScannerActivity.class);
                        startActivity(intentqr);
                        break;
                    case(R.id.Order):
                        Intent intentorder = new Intent(Home.this,YourOrder.class);
                        startActivity(intentorder);
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggel.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
