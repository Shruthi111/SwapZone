package com.example.studentswapzone;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.studentswapzone.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class addproduct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    Menu menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private EditText editTextProductName, editTextDescription, editTextYearsUsed, editTextSellingPrice;
    private Spinner spinnerCategory;
    private Button buttonAddProduct,buttonUploadImage;

    private DatabaseReference databaseReference;

    // Create a member variable to store the selected image Uri
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

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
        navigationView.setCheckedItem(R.id.nav_addproduct);

        getSupportActionBar().hide();

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        // Bind views
        editTextProductName = findViewById(R.id.editTextTextProductName);
        editTextDescription = findViewById(R.id.editTextTextDescription);
        editTextYearsUsed = findViewById(R.id.editTextYrsUsed);
        editTextSellingPrice = findViewById(R.id.editTextSP);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonAddProduct = findViewById(R.id.addBtn);
        buttonUploadImage=findViewById(R.id.buttonUploadImage);

        // Handle Add Product button click
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
        // Define a constant string to identify the image selection request
        final String PICK_IMAGE_REQUEST = "pick_image_request";

// Create an ActivityResultLauncher to handle the image selection
        ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        // Get the Uri of the selected image
                        imageUri=uri;
                        Toast.makeText(addproduct.this,"Image uploaded sucessfully!",Toast.LENGTH_SHORT).show();
                        // Use the imageUri for further processing (e.g., uploading to Firebase Storage)
                    }
                }
        );
// Launch the image picker when the user clicks a button or performs an action
        buttonUploadImage.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

    }



    private void addProduct() {
        String uname=getIntent().getStringExtra("Username");
        Log.e("Username here",uname);
        String productName = editTextProductName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int yearsUsed = Integer.parseInt(editTextYearsUsed.getText().toString().trim());
        double sellingPrice = Double.parseDouble(editTextSellingPrice.getText().toString().trim());
        String category = spinnerCategory.getSelectedItem().toString().trim();

        // Create a unique product ID in the database
        String productId = databaseReference.push().getKey();

        // Upload the image to Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("product_images/" + productId);


        // Replace 'imageUri' with the actual Uri of the selected image file
        UploadTask uploadTask = storageReference.putFile(imageUri);

        // Create a Product object with the entered details
        Product product = new Product(productId, productName, description, yearsUsed, sellingPrice, category,uname);

        // Store the product object in the database under the generated key
        databaseReference.child(productId).setValue(product);

        // Display a success message or perform any desired actions after adding the product
        Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
}
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        String uname=getIntent().getStringExtra("Username");
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intent2 = new Intent(addproduct.this, Home.class);
                intent2.putExtra("Username",uname);
                startActivity(intent2);
                break;

            case R.id.nav_myorders:
                Intent intent = new Intent(addproduct.this, myorders.class);
                intent.putExtra("Username",uname);
                startActivity(intent);
                break;
            case R.id.nav_myitems:
                Intent intent1 = new Intent(addproduct.this, myitems.class);
                intent1.putExtra("Username",uname);
                startActivity(intent1);
                break;
            case R.id.nav_addproduct:
                break;
            case R.id.nav_logout:
                Intent intent4 = new Intent(addproduct.this, Login.class);
                startActivity(intent4);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(addproduct.this, Profile.class);
                intent3.putExtra("Username",uname);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}