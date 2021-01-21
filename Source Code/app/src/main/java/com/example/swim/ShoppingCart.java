package com.example.swim;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swim.Lists.CartList;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {

    private ArrayList<CartList> itemArrayList;
    private MyAppAdapter myAppAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false;
    Connection con;
    SessionManager session;
    String uname;
    Button Checkout;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        session = new SessionManager(getApplicationContext());

        session.checkLogin();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        itemArrayList = new ArrayList<CartList>();

        Checkout = (Button)findViewById(R.id.checkout);
        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                MovetoOrder();
            }
        });

        SyncData orderData = new SyncData();
        orderData.execute("");

    }


    public void MovetoOrder(){
        con = connectionclass("admin","FJJMGDSBPLQKEPFY","Swim","sl-eu-gb-p04.dblayer.com");


        //String query = "INSERT INTO OrderMasters (ProductName,ProductQty,User,Address,Status,Date) VALUES ('"+productName+"','"+common.getQtyC()+"','"+uname+"','"+address+"','"+status+"','"+status+"')";
        String query = "INSERT INTO OrderMasters select * from CartMasters where User = '"+uname+"' ";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();

            String query2 = "delete from CartMasters where User = '"+uname+"' ";
            PreparedStatement ps1 = con.prepareStatement(query2);
            ps1.executeUpdate();
            progressBar.setVisibility(View.GONE);
            Intent intentCart = new Intent(ShoppingCart.this,YourOrder.class);
            startActivity(intentCart);
            finish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(ShoppingCart.this, "Please Wait",
                    "Shopping Cart Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try
            {
                con = connectionclass("admin","FJJMGDSBPLQKEPFY","Swim","sl-eu-gb-p04.dblayer.com");
                if (con == null)
                {
                    success = false;
                }
                else {
                    final HashMap<String,String> name = session.getUserDetails();
                    uname = name.get(SessionManager.KEY_NAME);
                    // Change below query according to your own database.
                    String query = "SELECT ProductName,ProductQty,ProductPrice,Address,Status FROM CartMasters where User = '"+uname+"' ";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new CartList(rs.getString("ProductName"),rs.getString("ProductPrice"),rs.getString("ProductQty"),rs.getString("Address"),rs.getString("Status")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my gridview
        {
            progress.dismiss();
            Toast.makeText(ShoppingCart.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {
            }
            else {
                try
                {
                    myAppAdapter = new MyAppAdapter(itemArrayList , ShoppingCart.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex)
                {

                }

            }
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder> {
        private List<CartList> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // public image title and image url
            public TextView pName,pQty,pAddress,pStatus,pPrice;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                pName = (TextView) v.findViewById(R.id.pname);
                pQty =(TextView)v.findViewById(R.id.pqty);
                pPrice =(TextView)v.findViewById(R.id.pprice);
                pAddress = (TextView)v.findViewById(R.id.paddress);
                pStatus = (TextView)v.findViewById(R.id.pstatus);
            }
        }

        // Constructor
        public MyAppAdapter(List<CartList> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.list_cart, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }



        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final CartList classListItems = values.get(position);
            holder.pName.setText(" Product Name     :" + " "+classListItems.getProductName());
            holder.pQty.setText(" Product Qty          :"+ " "+classListItems.getProductQty());
            holder.pPrice.setText(" Total Price            :" + " " + classListItems.getProductPrice());
            holder.pAddress.setText(" Delivery Address :" + " "+classListItems.getAddress());
            holder.pStatus.setText("Current Status      : "+ classListItems.getStatus());
        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
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
