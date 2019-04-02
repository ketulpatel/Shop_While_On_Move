package com.example.swim;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swim.Lists.OrderList;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YourOrder extends AppCompatActivity {
    private ArrayList<OrderList> itemArrayList;
    private YourOrder.MyAppAdapter myAppAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false;
    Connection con;
    SessionManager session;
    String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        itemArrayList = new ArrayList<OrderList>();


        SyncData orderData = new SyncData();
        orderData.execute("");

    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(YourOrder.this, "Getting Details",
                    "Your Order Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                con = connectionclass("admin", "FJJMGDSBPLQKEPFY", "Swim", "sl-eu-gb-p04.dblayer.com");
                if (con == null) {
                    success = false;
                } else {
                    final HashMap<String, String> name = session.getUserDetails();
                    uname = name.get(SessionManager.KEY_NAME);
                    // Change below query according to your own database.
                    String query = "SELECT OrderID,ProductName,ProductQty,ProductPrice,Address,Status FROM OrderMasters where User = '" + uname + "' ";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next()) {
                            try {
                                itemArrayList.add(new OrderList(rs.getString("OrderID"),rs.getString("ProductName"), rs.getString("ProductPrice"), rs.getString("ProductQty"), rs.getString("Address"), rs.getString("Status")));
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
            } catch (Exception e) {
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
            Toast.makeText(YourOrder.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new YourOrder.MyAppAdapter(itemArrayList, YourOrder.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<YourOrder.MyAppAdapter.ViewHolder> {
        private List<OrderList> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // public image title and image url
            public TextView OrderId, pName, pQty, pAddress, pStatus,pPrice;
            TextView txtOrderId, txtProductName, txtProductQty,txtProductPrice,
                     txtDeliveryAddress, txtCurrentStatus;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                OrderId = (TextView) v.findViewById(R.id.orderid);
                pName = (TextView) v.findViewById(R.id.pname);
                pQty = (TextView) v.findViewById(R.id.pqty);
                pPrice =(TextView)v.findViewById(R.id.pprice);
                pAddress = (TextView) v.findViewById(R.id.paddress);
                pStatus = (TextView) v.findViewById(R.id.pstatus);
                txtOrderId = v.findViewById(R.id.txtOrderId);
                txtProductName = v.findViewById(R.id.txtProductName);
                txtProductQty = v.findViewById(R.id.txtProductQty);
                txtProductPrice = v.findViewById(R.id.txtProductPrice);
                txtDeliveryAddress = v.findViewById(R.id.txtDeliveryAddress);
                txtCurrentStatus = v.findViewById(R.id.txtCurrentStatus);
            }
        }

        // Constructor
        public MyAppAdapter(List<OrderList> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public YourOrder.MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.order_list, parent, false);
            YourOrder.MyAppAdapter.ViewHolder vh = new YourOrder.MyAppAdapter.ViewHolder(v);
            return vh;
        }


        // Binding items to the view
        @Override
        public void onBindViewHolder(YourOrder.MyAppAdapter.ViewHolder holder, final int position) {

            final OrderList classListItems = values.get(position);
            /*holder.txtOrderId.setText(" Order ID                :"+ " "+ classListItems.getOrderId());
            holder.txtProductName.setText(" Product Name     :" + " " + classListItems.getProductName());
            holder.txtProductQty.setText(" Product Qty          :" + " " + classListItems.getProductQty());
            holder.txtProductPrice.setText(" Total Price            :" + " " + classListItems.getProductPrice());
            holder.txtDeliveryAddress.setText(" Delivery Address :" + " " + classListItems.getAddress());
            holder.txtCurrentStatus.setText("Current Status      : " + classListItems.getStatus());*/

            holder.txtOrderId.setText(classListItems.getOrderId());
            holder.txtProductName.setText(classListItems.getProductName());
            holder.txtProductQty.setText(classListItems.getProductQty());
            holder.txtProductPrice.setText(classListItems.getProductPrice());
            holder.txtDeliveryAddress.setText(classListItems.getAddress());
            holder.txtCurrentStatus.setText(classListItems.getStatus());
        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }
    }


    @SuppressLint("NewApi")
    public Connection connectionclass(String username, String Password, String Database, String Server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://sl-eu-gb-p04.dblayer.com:17653/Swim?"
                    + "user=admin&password=FJJMGDSBPLQKEPFY");
        } catch (Exception ex) {
            ex.getMessage();
        }
        return connection;
    }
}