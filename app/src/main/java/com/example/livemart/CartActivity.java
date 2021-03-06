package com.example.livemart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class CartActivity extends AppCompatActivity {

    RecyclerView recycler_itemList;
    public static TextView tv_total;
    CartListAdapter cartListAdapter;
    public static int total=0;
    String jsonCartList;
    Items item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Set back button to activity
        androidx.appcompat.app.ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Cart");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tv_total =(TextView) findViewById(R.id.tv_total);

        recycler_itemList =(RecyclerView) findViewById(R.id.recycler_cart);
        recycler_itemList.setHasFixedSize(true);
        recycler_itemList.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recycler_itemList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recycler_itemList.getRecycledViewPool().setMaxRecycledViews(0, 0);

        cartListAdapter = new CartListAdapter(CartActivity.this,CustomAdapter.selecteditems);
        recycler_itemList.setAdapter(cartListAdapter);



        //Handling clicks on cart item
        cartListAdapter.setOnItemClickListener(new CartListAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                CustomAdapter.selecteditems.remove(position);
                cartListAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),
                        "Item removed from cart",
                        Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onQuantityClick(int position, boolean sign) {
                int quant=CustomAdapter.selecteditems.get(position).getQuantity();
                if(sign)
                {
                    quant++;
                }
                else {
                    quant--;
                }
                CustomAdapter.selecteditems.get(position).setQuantity(quant);
                cartListAdapter.notifyDataSetChanged();
            }
        });

        Gson gson = new Gson();
        jsonCartList = gson.toJson(CustomAdapter.selecteditems);
        Button placeOrder=(Button) findViewById(R.id.btn_placeorder);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), VegDashboard.class);
                i.putExtra("key", jsonCartList.toString());
                startActivity(i);
            }
        });
        calculateTotal();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Calculate total amount for order
    public static void calculateTotal(){
        int i=0;
        total=0;
        while(i<CustomAdapter.selecteditems.size()){
            total+= CustomAdapter.selecteditems.get(i).getPrice() * CustomAdapter.selecteditems.get(i).getQuantity();
            i++;
        }
        String s="Rs "+String.valueOf(total);
        tv_total.setText(s);
    }


    //The below functions to be used for placing order

//    public void insertOrder(View view){
//
//        if(total>0){
//
//            Gson gson = new Gson();
//            jsonCartList = gson.toJson(CustomAdapter.selecteditems);
//            Log.v("message",jsonCartList);
//            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    switch (which){
//                        case DialogInterface.BUTTON_POSITIVE:
//                            //Yes button clicked
//                            placeOrderRequest();
//                            break;
//
//                        case DialogInterface.BUTTON_NEGATIVE:
//                            //No button clicked
//                            break;
//                    }
//                }
//            };
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
//            builder.setMessage("Do you want to place Order ?").setPositiveButton("Yes", dialogClickListener)
//                    .setNegativeButton("No", dialogClickListener).show();
//
//        }else{
//            Toast.makeText(CartActivity.this,"No items in Cart !",Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//
//
//
//
//
//
//    private void placeOrderRequest(){
//        //Send Request to Server with required Parameters
//    /*
//   jsonCartList - Consists of Objects of all product selected.
//    */
//        Log.v("message","hello");
//    }
}