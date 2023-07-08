package com.example.studentswapzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Electronics extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    Menu menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView heading,subheading;
    private List<Product> productList;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronics);

        ArrayList<String> wishlist =getIntent().getStringArrayListExtra("Wishlist");

        getSupportActionBar().hide();
        username=getIntent().getStringExtra("Username");
        String cat=getIntent().getStringExtra("Category");
        heading=findViewById(R.id.textView);
        subheading=findViewById(R.id.textView2);
        heading.setText(cat);
        subheading.setText("Explore the "+cat+" store!");
        retrieveProductsByCategory(cat);

        productRecyclerView = findViewById(R.id.productRecyclerView2);
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
        navigationView.setCheckedItem(R.id.nav_home);

    }
    private void retrieveProductsByCategory(String category) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");

        Query query = productsRef.orderByChild("category").equalTo(category);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    String productId = productSnapshot.getKey();
                    // Retrieve other product details
                    String productName = productSnapshot.child("name").getValue(String.class);
                    String description = productSnapshot.child("description").getValue(String.class);
                    int yearsUsed = productSnapshot.child("yearsUsed").getValue(Integer.class);
                    double sellingPrice = productSnapshot.child("sellingPrice").getValue(Double.class);
                    String uname = productSnapshot.child("uname").getValue(String.class);



                    // Do something with the retrieved product details
                    // For example, display the product information in the UI
                    // Create a Product object
                    Product product = new Product(productId, productName, description, yearsUsed, sellingPrice, category,uname);

                    // Add the product to the product list
                    productList.add(product);

                    // Create and set the adapter
                    productAdapter = new ProductAdapter(productList,username);
                    productRecyclerView.setAdapter(productAdapter);

                    productAdapter.notifyDataSetChanged();
                }

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
            case R.id.nav_home: Intent intent5 = new Intent(Electronics.this, Home.class);
                intent5.putExtra("Username",uname);
                startActivity(intent5);
                break;
            case R.id.nav_myorders:
                Intent intent = new Intent(Electronics.this, myorders.class);
                intent.putExtra("Username",uname);
                startActivity(intent);
                break;
            case R.id.nav_myitems:
                Intent intent1 = new Intent(Electronics.this, myitems.class);
                intent1.putExtra("Username",uname);
                startActivity(intent1);
                break;
            case R.id.nav_addproduct:
//                Log.e("Username",uname);
                Intent intent2 = new Intent(Electronics.this, addproduct.class);
                intent2.putExtra("Username",uname);
                startActivity(intent2);
                break;
            case R.id.nav_logout:
                Intent intent4 = new Intent(Electronics.this, Login.class);
                startActivity(intent4);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(Electronics.this, Profile.class);
                intent3.putExtra("Username",uname);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}