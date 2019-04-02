package com.example.swim;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.swim.Common.Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class QtyDialog extends AppCompatDialogFragment {

    TextView qty;
    int minteger = 1;
    Button increase,decrease;

    Connection con;
    PreparedStatement stmt;
    String uname;
    String qty1;
    Common common = new Common();

    String result ;

    String productName;
    String address = "";
    PreparedStatement ps = null;
    SessionManager session;

    private QtyDialogListner listner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.qty_layout, null);
        session = new SessionManager(getActivity().getApplicationContext());

        session.checkLogin();

        result = common.getResult();
        increase = (Button)view.findViewById(R.id.increase);
        decrease = (Button)view.findViewById(R.id.decrease);


        builder.setView(view)
                .setTitle("Enter Qty")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d("In QTY",common.getResult());
                qty1 = qty.getText().toString();
                listner.applytext(qty1);


            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseInteger();
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseInteger();
            }
        });

        qty = (TextView) view.findViewById(R.id.qty);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listner = (QtyDialogListner)context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString());
        }
    }

    public interface QtyDialogListner{
        void applytext(String qty);

    }


    public void increaseInteger() {
        minteger = minteger + 1;
        display(minteger);

    }
    public void decreaseInteger() {
        if(minteger <= 1){
            decrease.setEnabled(false);
            if(minteger > 0 ){
                decrease.setEnabled(true);
            }
        }
        else {
            decrease.setEnabled(true);
            minteger = minteger - 1;
            display(minteger);
        }

    }

    private void display(int number) {

        qty.setText("" + number);

    }





}
