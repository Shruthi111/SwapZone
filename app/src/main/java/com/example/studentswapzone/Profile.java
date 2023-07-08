package com.example.studentswapzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://collegeolx-2544a-default-rtdb.firebaseio.com/");

    TextInputEditText unamePlaceHolder,namePlaceHolder,emailPlaceHolder,passwordPlaceHolder,mobnoPlaceHolder,deptPlaceHolder;
    TextView emailfield,fullnamefield,itemslabel,orderslabel;
    ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();

        String unam=getIntent().getStringExtra("Username");

        profile=(ImageView) findViewById(R.id.profile_image);
        itemslabel=findViewById(R.id.items_label);
        orderslabel=findViewById(R.id.orders_label);

        String imageFileName = unam;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("user_images");
        StorageReference imageRef = storageRef.child(imageFileName);


        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Use Glide library to load the image into the ImageView
            Glide.with(profile)
                    .load(uri)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder_image)) // Placeholder image while loading
                    .error("https://i.postimg.cc/hj2GHn4Z/mother.png") // Error image if unable to load
                    .into(profile);
        }).addOnFailureListener(e -> {
            // Handle the failure to retrieve the image URL
            // You can set a default image or show an error message
            Glide.with(this).load("https://i.postimg.cc/hj2GHn4Z/mother.png").into(profile);

        });



        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");

        Query query = productsRef.orderByChild("uname").equalTo(unam);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int productCount = (int) dataSnapshot.getChildrenCount();
                itemslabel.setText(String.valueOf(productCount));
                Log.e("Total items",String.valueOf(dataSnapshot));
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("users").child(unam).child("Wishlist");
        wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int wishlistCount = (int) dataSnapshot.getChildrenCount();
                orderslabel.setText(String.valueOf(wishlistCount));
                Log.e("Wishlist Count", String.valueOf(wishlistCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });






        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String getPassword=snapshot.child(unam).child("password").getValue(String.class);
                String getUsername=unam;
                String getMobileNo=snapshot.child(unam).child("mobilenumber").getValue(String.class);
                String getEmail=snapshot.child(unam).child("email").getValue(String.class);
                String getDept=snapshot.child(unam).child("department").getValue(String.class);
                String getname=snapshot.child(unam).child("name").getValue(String.class);

                unamePlaceHolder=findViewById(R.id.username_profile);
//                namePlaceHolder=findViewById(R.id.full_name_profile);
                emailPlaceHolder=findViewById(R.id.email_profile);
                passwordPlaceHolder=findViewById(R.id.passwd_profile);
                mobnoPlaceHolder=findViewById(R.id.mobileno_profile);
                deptPlaceHolder=findViewById(R.id.dept_profile);
                fullnamefield=findViewById(R.id.fullname_field);
                emailfield=findViewById(R.id.email_field);

                unamePlaceHolder.setText("UserName:"+getUsername);
//                namePlaceHolder.setText("FullName:"+getname);
                emailPlaceHolder.setText("Email:"+getEmail);
                passwordPlaceHolder.setText("Password:"+getPassword);
                mobnoPlaceHolder.setText("Mobile Number:"+getMobileNo);
                deptPlaceHolder.setText("Department:"+getDept);

                fullnamefield.setText(getname);
                emailfield.setText(getEmail);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}