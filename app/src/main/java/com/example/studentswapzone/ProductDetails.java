package com.example.studentswapzone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductDetails extends AppCompatActivity {
    // Declare DatabaseReference and ValueEventListener
    private DatabaseReference productRef;
    private ValueEventListener productListener;

    private TextView productNameTextView;
    private TextView productCategoryTextView;
    private TextView productPriceTextView;
    private TextView productDescriptionTextView;
    private TextView productYearsUsedTextView;
    private TextView productSellerNameTextView;
    private ImageView productImageView;
    Button callBtn,viewBtn;
    String mobileNumber,emailadd,dept,name;
    String productSellerName,productName;
    ImageView heartButton;
    String productId;
    String buyerUname;
    public ArrayList<String> wishlist;

    DatabaseReference databaseReference;
    List<String> productIds;


    private static final int REQUEST_CALL_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        wishlist=getIntent().getStringArrayListExtra("Wishlist");
        Log.e("My wishlist",String.valueOf(wishlist));

        buyerUname=getIntent().getStringExtra("Buyer Uname");

        heartButton = findViewById(R.id.fav);

        getSupportActionBar().hide();

        // Retrieve the product ID from the intent
        productId= getIntent().getStringExtra("productId");

        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("users").child(buyerUname).child("Wishlist");
        wishlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productId = snapshot.getValue(String.class);
                    productIds.add(productId);
                }
                if(productIds.contains(productId)){
                    heartButton.setImageResource(R.drawable.baseline_favorite_24);
                }

            }
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });



        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the product ID to the wishlist array
                addToWishlist(productId);
            }
        });


        // Initialize views
        productNameTextView = findViewById(R.id.product_name);
        productCategoryTextView = findViewById(R.id.product_category);
        productPriceTextView = findViewById(R.id.product_price);
        productDescriptionTextView = findViewById(R.id.product_description);
        productYearsUsedTextView = findViewById(R.id.product_yrsused);
        productSellerNameTextView = findViewById(R.id.product_sellername);
        productImageView=findViewById(R.id.product_image);
        callBtn=findViewById(R.id.callBtn);
        viewBtn=findViewById(R.id.view);

        // Get a reference to the products node in the Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        productRef = databaseReference.child("products").child(productId);

        // Create a ValueEventListener to listen for changes in the product data
        productListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retrieve the product details from the snapshot
                productName = snapshot.child("name").getValue(String.class);
                String productCategory = snapshot.child("category").getValue(String.class);
                double productPrice = snapshot.child("sellingPrice").getValue(Double.class);
                String productDescription = snapshot.child("description").getValue(String.class);
                int productYearsUsed = snapshot.child("yearsUsed").getValue(Integer.class);
                productSellerName = snapshot.child("uname").getValue(String.class);

                // Set the product details to the respective TextViews
                productNameTextView.setText(productName);
                productCategoryTextView.setText(productCategory);
                productPriceTextView.setText("Rs "+String.valueOf(productPrice));
                productDescriptionTextView.setText(productDescription);
                productYearsUsedTextView.setText("Years used: "+String.valueOf(productYearsUsed));
                productSellerNameTextView.setText("Seller: "+productSellerName);


                // Retrieve the image URL from Firebase Storage based on the product ID
                String imageFileName = productId;
                // Assuming the image file extension is ".jpeg"
                Log.e("product url",imageFileName);
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("product_images");
                StorageReference imageRef = storageRef.child(imageFileName);


                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Use Glide library to load the image into the ImageView
                    Glide.with(ProductDetails.this)
                            .load(uri)
                            .apply(new RequestOptions().placeholder(R.drawable.placeholder_image)) // Placeholder image while loading
                            .error(R.drawable.error_image) // Error image if unable to load
                            .into(productImageView);
                }).addOnFailureListener(e -> {
                    // Handle the failure to retrieve the image URL
                    // You can set a default image or show an error message
                    productImageView.setImageResource(R.drawable.error_image);
                    Log.e("Firebase Storage", "Failed to retrieve image URL: " + e.getMessage());
                });

                DatabaseReference userRef = databaseReference.child("users").child(productSellerName);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mobileNumber = dataSnapshot.child("mobilenumber").getValue(String.class);
                        emailadd = dataSnapshot.child("email").getValue(String.class);
                        dept = dataSnapshot.child("department").getValue(String.class);
                        name = dataSnapshot.child("name").getValue(String.class);

                        Log.d("Mobile", mobileNumber);
                        Log.d("Email", emailadd);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Error", databaseError.getMessage());
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        };



        // Add the ValueEventListener to the product reference
        productRef.addListenerForSingleValueEvent(productListener);

        if (ContextCompat.checkSelfPermission(ProductDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProductDetails.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            // Permission already granted
            // Add the click listener to the call button
            callBtn.setOnClickListener(v -> {
                // Launch the default call app with the retrieved mobile number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobileNumber));
                startActivity(intent);
            });
        }

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductDetails.this,Seller.class);
                intent.putExtra("Name",name);
                intent.putExtra("Department",dept);
                intent.putExtra("Email",emailadd);
                intent.putExtra("Mobile",mobileNumber);
                intent.putExtra("Username",productSellerName);
                intent.putExtra("Product",productName);
                intent.putExtra("Buyer uname",buyerUname);


                startActivity(intent);
            }
        });





    }

    private void addToWishlist(String productId) {
        // Check if the product ID is not already in the wishlist
        if (!wishlist.contains(productId)) {
            // Add the product ID to the wishlist array
            wishlist.add(productId);

            heartButton.setImageResource(R.drawable.baseline_favorite_24);

            // Update the wishlist in the database
            DatabaseReference buyerRef = databaseReference.child("users").child(buyerUname).child("Wishlist");
            buyerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve the current wishlist array from the database using GenericTypeIndicator
                        GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> currentWishlist = dataSnapshot.getValue(typeIndicator);

                        // Check if the current wishlist is null or empty
                        if (currentWishlist != null && !currentWishlist.isEmpty()) {
                            // Check if the product ID is not already in the database wishlist
                            if (!currentWishlist.contains(productId)) {
                                // Add the new product ID to the wishlist array
                                currentWishlist.add(productId);

                                // Update the wishlist in the database
                                buyerRef.setValue(currentWishlist);
                            }
                        }
                    } else {
                        // No wishlist exists in the database, create a new one with the added product ID
                        buyerRef.setValue(wishlist);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Error", databaseError.getMessage());
                }
            });

            // Optional: Show a message or perform any UI updates to indicate that the product was added to the wishlist
            Toast.makeText(getApplicationContext(), "Product added to wishlist", Toast.LENGTH_SHORT).show();
        } else {
            // Optional: Show a message or perform any UI updates to indicate that the product is already in the wishlist
            Toast.makeText(getApplicationContext(), "Product already in wishlist", Toast.LENGTH_SHORT).show();
        }
    }








    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove the ValueEventListener when the activity is destroyed
        if (productRef != null && productListener != null) {
            productRef.removeEventListener(productListener);
        }
    }
}
