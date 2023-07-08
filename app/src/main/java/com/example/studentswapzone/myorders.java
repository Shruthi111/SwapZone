package com.example.studentswapzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class myorders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    Menu menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProductAdapter productAdapter;
    RecyclerView productRecyclerView;
    String username;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);

        getSupportActionBar().hide();
        username=getIntent().getStringExtra("Username");
        Log.e("Buyer username in myorders",username);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);

        menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(true);
        menu.findItem(R.id.nav_profile).setVisible(true);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_myorders);


        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("Wishlist");
        wishlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> productIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productId = snapshot.getValue(String.class);
                    productIds.add(productId);
                }

                // Call a method to retrieve product details based on the product IDs
                retrieveProductDetails(productIds);
                Log.e("Product Ids",String.valueOf(productIds));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });



    }
    private void retrieveProductDetails(List<String> productIds) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    String productId = productSnapshot.getKey();
                    if (productIds.contains(productId)) {
                        // Retrieve product details
                        String productName = productSnapshot.child("name").getValue(String.class);
                        String productDesc = productSnapshot.child("description").getValue(String.class);
                        Double productPrice = productSnapshot.child("sellingPrice").getValue(Double.class);
                        String productSname = productSnapshot.child("uname").getValue(String.class);
                        String productCat = productSnapshot.child("category").getValue(String.class);
                        int productYrsUsed = productSnapshot.child("yearsUsed").getValue(Integer.class);

                        // Create a Product object
                        Product product = new Product(productId, productName,productDesc,productYrsUsed,productPrice,productCat,productSname);

                        // Add the product to the product list
                        productList.add(product);
                    }
                }

                // Display the product details (e.g., update the RecyclerView adapter)
                productAdapter = new ProductAdapter(productList,username);
                productRecyclerView.setAdapter(productAdapter);

                productAdapter.notifyDataSetChanged();


                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        String uname=getIntent().getStringExtra("Username");
        switch (menuItem.getItemId()) {
            case R.id.nav_home: Intent intent = new Intent(myorders.this, Home.class);
                intent.putExtra("Username",uname);
                startActivity(intent);
                break;
            case R.id.nav_myorders:break;

            case R.id.nav_myitems:
                Intent intent1 = new Intent(myorders.this, myitems.class);
                intent1.putExtra("Username",uname);
                startActivity(intent1);
                break;
            case R.id.nav_addproduct:
//                Log.e("Username",uname);
                Intent intent2 = new Intent(myorders.this, addproduct.class);
                intent2.putExtra("Username",uname);
                startActivity(intent2);
                break;
            case R.id.nav_logout:
                Intent intent4 = new Intent(myorders.this, Login.class);
                startActivity(intent4);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(myorders.this, Profile.class);
                intent3.putExtra("Username",uname);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}