package com.example.livemart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class MainDashboard extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Button logout;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    MainAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        List<MainItems> itemsList = new ArrayList<>();
        itemsList.add(new MainItems(R.drawable.ic_baseline_search_24, "Vegetables"));
        itemsList.add(new MainItems(R.drawable.ic_baseline_search_24, "Fruits"));
        itemsList.add(new MainItems(R.drawable.ic_baseline_search_24, "Readymade"));
        itemsList.add(new MainItems(R.drawable.ic_baseline_search_24, "apple"));
        itemsList.add(new MainItems(R.drawable.ic_baseline_search_24, "apple"));
        itemsList.add(new MainItems(R.drawable.ic_baseline_search_24, "apple"));



        GridView gridView = findViewById(R.id.maingrid_view);
        customAdapter = new MainAdapter(this, R.layout.custom_maindashboard, itemsList);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MainItems item=customAdapter.items_list_filtered.get(position);
                String query=item.getTitle().toLowerCase();
                Intent intent;
                if(query.equalsIgnoreCase("vegetables"))
                {
                    intent=new Intent(getApplicationContext(),VegDashboard.class);
                    startActivity(intent);
                }
                else if(query.equalsIgnoreCase("fruits"))
                {
                    intent=new Intent(getApplicationContext(),FruitsDashboard.class);
                    startActivity(intent);
                }

            }
        });


        //Logging out
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,  this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logOut();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                        }
                    }
                });


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_dashboard, menu);

        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.search)
        {
            return true;
        }
        else if(id==R.id.cart_icon)
        {
            Intent i=new Intent(MainDashboard.this,CartActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}