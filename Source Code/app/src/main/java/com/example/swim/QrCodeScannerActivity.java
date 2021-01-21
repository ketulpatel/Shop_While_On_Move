package com.example.swim;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.swim.Common.Common;
import com.google.zxing.Result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static android.Manifest.permission.CAMERA;

public class QrCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, QtyDialog.QtyDialogListner {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView mScannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    Connection con;
    PreparedStatement stmt;
    String uname;
    String qtyF = "";
    SessionManager session;
    String productName = "";
    String address = "";
    String KEY_TEXTPSS ;
    float price;
    float pricef;

    Common common = new Common();

    PreparedStatement ps = null;
   String formattedDate = (String) android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mScannerView = new ZXingScannerView(this);

        setContentView(mScannerView);
        session = new SessionManager(getApplicationContext());

        session.checkLogin();
        final HashMap<String,String> name = session.getUserDetails();
        uname = name.get(SessionManager.KEY_NAME);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), uname, Toast.LENGTH_LONG).show();

            } else {
                requestPermission();
            }
        }

    }

    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {

                                return;
                            }
                        }
                    }
                }
                break;
        }
    }



    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(mScannerView == null) {
                    mScannerView = new ZXingScannerView(this);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler(this);
                mScannerView.startCamera(camId);
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
        mScannerView = null;
    }

    @Override
    public void handleResult(Result rawResult) {

        final String result = rawResult.getText();
        Log.e("QRCodeScanner", rawResult.getText());
        Log.e("QRCodeScanner", rawResult.getBarcodeFormat().toString());


        common.setResult(result);
        Log.d("In QR",result);
        Log.d("In QR Common",common.getResult());
        openDialog();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                selectproduct(result);
                selectaddress();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms

                        AddOrder();
                    }
                }, 2000);

            }
        }, 5000);





    }



    public void openDialog(){

            QtyDialog qtyDialog = new QtyDialog();
            qtyDialog.show(getSupportFragmentManager(),"Qty");
    }

    @Override
    public void applytext(String qtynn) {
        common.setQtyC(qtynn);
    }


    public void selectaddress(){
        con = connectionclass("admin","FJJMGDSBPLQKEPFY","Swim","sl-eu-gb-p04.dblayer.com");
        final HashMap<String,String> name = session.getUserDetails();
        uname = name.get(SessionManager.KEY_NAME);
        String query1 = "SELECT * FROM registration where Email = '"+uname+"' ";
        try{
            ps = con.prepareStatement(query1);
            ResultSet rs = ps.executeQuery(query1);
            while(rs.next()){
                address = rs.getString("Addresss");
            }
        }
        catch (Exception e){

        }
    }


    public void selectproduct(String result){

        con = connectionclass("admin","FJJMGDSBPLQKEPFY","Swim","sl-eu-gb-p04.dblayer.com");
        String query = "select * from Product_Details where ProductID = '"+result+"' ";
        try{
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery(query);
            while(rs.next()){
                productName = rs.getString("Productname");
                price = rs.getFloat("ProductPrice");

                Log.d("ProductName",productName);
            }
            pricef = price * (Integer.parseInt(common.getQtyC()));

        }
        catch (Exception e){

        }
    }


    public void AddOrder(){
        con = connectionclass("admin","FJJMGDSBPLQKEPFY","Swim","sl-eu-gb-p04.dblayer.com");
        String status = "Pending";
        final HashMap<String,String> name = session.getUserDetails();
        uname = name.get(SessionManager.KEY_NAME);


        //String query = "INSERT INTO OrderMasters (ProductName,ProductQty,User,Address,Status,Date) VALUES ('"+productName+"','"+common.getQtyC()+"','"+uname+"','"+address+"','"+status+"','"+status+"')";
        String query = "INSERT INTO CartMasters (ProductName,ProductQty,ProductPrice,User,Address,Status,Date) VALUES ('"+productName+"','"+common.getQtyC()+"','"+pricef+"','"+uname+"','"+address+"','"+status+"','"+formattedDate+"')";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();
            Intent intentCart = new Intent(QrCodeScannerActivity.this,ShoppingCart.class);
            startActivity(intentCart);
            finish();
        }catch (Exception ex){
            ex.printStackTrace();
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