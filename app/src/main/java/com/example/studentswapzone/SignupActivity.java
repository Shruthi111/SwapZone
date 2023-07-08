package com.example.studentswapzone;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://collegeolx-2544a-default-rtdb.firebaseio.com/");
    TextView linkpage;
    EditText name,emailid,mobno,passwd,uname;
    Spinner dept;

    Button signup,upload;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Uri imageUri;
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        mAuth=FirebaseAuth.getInstance();

        signup=findViewById(R.id.signupBtn);
        name=findViewById(R.id.nameArea);
        emailid=findViewById(R.id.emailArea);
        dept=findViewById(R.id.deptArea);
        mobno=findViewById(R.id.mobnoArea);
        passwd=findViewById(R.id.passwordArea);
        uname=findViewById(R.id.usernameArea);
        progressBar=findViewById(R.id.progressBar);
        upload=findViewById(R.id.uploadBtn);

        // Define a constant string to identify the image selection request
        final String PICK_IMAGE_REQUEST = "pick_image_request";

// Create an ActivityResultLauncher to handle the image selection
        ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        // Get the Uri of the selected image
                        imageUri=uri;
                        Toast.makeText(SignupActivity.this,"Image uploaded sucessfully!",Toast.LENGTH_SHORT).show();
                        // Use the imageUri for further processing (e.g., uploading to Firebase Storage)
                    }
                }
        );
// Launch the image picker when the user clicks a button or performs an action
        upload.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password,mob,dep,nam,unam;
                email=String.valueOf(emailid.getText());
                password=String.valueOf(passwd.getText());
                mob=String.valueOf(mobno.getText());
                dep=dept.getSelectedItem().toString().trim();
                nam=String.valueOf(name.getText());
                unam=String.valueOf(uname.getText());

                if(email.isEmpty() || password.isEmpty() || mob.isEmpty() || dep.isEmpty() || nam.isEmpty()|| unam.isEmpty()){
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(SignupActivity.this,"Please fill all the details!",Toast.LENGTH_SHORT).show();

                }

                else{
                    if(!validatePassword(password)){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignupActivity.this,"Please enter a password with atleast 8 characters,one uppercase letter, one lowercase letter, and one digit!",Toast.LENGTH_SHORT).show();
                    }
                    else{

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(unam)){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SignupActivity.this,"Username is already taken!Please enter the new Username!",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                databaseReference.child("users").child(unam).child("name").setValue(nam);
                                databaseReference.child("users").child(unam).child("email").setValue(email);
                                databaseReference.child("users").child(unam).child("mobilenumber").setValue(mob);
                                databaseReference.child("users").child(unam).child("password").setValue(password);
                                databaseReference.child("users").child(unam).child("department").setValue(dep);

                                ArrayList<String> wishlist = new ArrayList<>();
//                                String arrayString = TextUtils.join(",", wishlist);
                                databaseReference.child("users").child(unam).child("Wishlist").setValue(wishlist);


                                Toast.makeText(SignupActivity.this, "Account got created successfully!",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(SignupActivity.this, Home.class);
                                intent.putExtra("Username",unam);
//                                intent.putExtra("Wishlist",wishlist);
                                startActivity(intent);

                                // Upload the image to Firebase Storage
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user_images/" + unam);


                                // Replace 'imageUri' with the actual Uri of the selected image file
                                UploadTask uploadTask = storageReference.putFile(imageUri);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
//                mAuth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressBar.setVisibility(View.GONE);
//                                if (task.isSuccessful()) {
//
//                                    Toast.makeText(SignupActivity.this, "Account got created successfully!",
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });
            }}
        });


        linkpage = findViewById(R.id.linkPage);
        linkpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
    public boolean validatePassword(String pwd){
        // Password regex pattern: At least 8 characters with at least one uppercase letter, one lowercase letter, and one digit
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!^%*?&#])[a-zA-Z\\d@$!%*?#&]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();

    }
}