package com.example.studentswapzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView;
    FirebaseAuth auth;

    FirebaseUser user;

    ImageButton profileBtn;

    ImageView electronicsImg,booksImg,clothingImg,furnitureImg,accessoriesImg,eventTicketsImg,logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        String uname=getIntent().getStringExtra("Username");


        getSupportActionBar().hide();

        electronicsImg=(ImageView) findViewById(R.id.electronics);
        booksImg=(ImageView) findViewById(R.id.books);
        clothingImg=(ImageView) findViewById(R.id.clothing);
        furnitureImg=(ImageView) findViewById(R.id.furnitures);
        accessoriesImg=(ImageView) findViewById(R.id.Accessories);
        eventTicketsImg=(ImageView) findViewById(R.id.eventTickets);
        logo=(ImageView) findViewById(R.id.logo);

        Glide.with(this).load("https://i.postimg.cc/d3TQnfKW/logo-Img-removebg-preview.png").into(logo);
        Glide.with(this).load("https://i.postimg.cc/HWJrXYjc/modern-stationary-collection-arrangement-1.jpg").into(electronicsImg);
        Glide.with(this).load("https://i.postimg.cc/wT4PXnHf/front-view-stacked-books-ladders-education-day.jpg").into(booksImg);
        Glide.with(this).load("https://i.postimg.cc/GtcNKCHN/clothing.jpg").into(clothingImg);
        Glide.with(this).load("https://i.postimg.cc/HkJNB3kp/furnitures.jpg").into(furnitureImg);
        Glide.with(this).load("https://i.postimg.cc/NfjSrQh2/model-career-kit-still-life.jpg").into(accessoriesImg);
        Glide.with(this).load("https://i.postimg.cc/3Jz1Jh3T/realistic.jpg").into(eventTicketsImg);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        textView=findViewById(R.id.textView);
        toolbar=findViewById(R.id.toolbar);

//        setSupportActionBar(toolbar);
        menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(true);
        menu.findItem(R.id.nav_profile).setVisible(true);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        electronicsImg.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent=new Intent(Home.this,Electronics.class);
                intent.putExtra("Username",uname);
                Log.e("Buyer username in home page",uname);
                intent.putExtra("Category","Electronics");

                startActivity(intent);


            }
        });
        booksImg.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent=new Intent(Home.this,Electronics.class);
                intent.putExtra("Username",uname);
                intent.putExtra("Category","Books");

                startActivity(intent);

            }
        });
        clothingImg.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent=new Intent(Home.this,Electronics.class);
                intent.putExtra("Username",uname);
                intent.putExtra("Category","Clothing");

                startActivity(intent);

            }
        });
        furnitureImg.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent=new Intent(Home.this,Electronics.class);
                intent.putExtra("Username",uname);
                intent.putExtra("Category","Furniture");

                startActivity(intent);

            }
        });
        accessoriesImg.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent=new Intent(Home.this,Electronics.class);
                intent.putExtra("Username",uname);
                intent.putExtra("Category","Accessories");

                startActivity(intent);

            }
        });
        eventTicketsImg.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent=new Intent(Home.this,Electronics.class);
                intent.putExtra("Username",uname);
                intent.putExtra("Category","Event Tickets");

                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        String uname=getIntent().getStringExtra("Username");
        switch (menuItem.getItemId()) {
            case R.id.nav_home: break;
            case R.id.nav_myorders:
                Intent intent = new Intent(Home.this, myorders.class);
                intent.putExtra("Username",uname);
                startActivity(intent);
                break;
            case R.id.nav_myitems:
                Intent intent1 = new Intent(Home.this, myitems.class);
                intent1.putExtra("Username",uname);
                startActivity(intent1);
                break;
            case R.id.nav_addproduct:
//                Log.e("Username",uname);
                Intent intent2 = new Intent(Home.this, addproduct.class);
                intent2.putExtra("Username",uname);
                startActivity(intent2);
                break;
            case R.id.nav_logout:
                Intent intent4 = new Intent(Home.this, Login.class);
                startActivity(intent4);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(Home.this, Profile.class);
                intent3.putExtra("Username",uname);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}