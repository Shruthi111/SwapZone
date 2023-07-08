package com.example.studentswapzone;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://collegeolx-2544a-default-rtdb.firebaseio.com/");
    TextView linkpage;
    EditText passwd,uname;
    Button login;
    FirebaseAuth mAuth;

    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(),Home.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        mAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.loginBtn);
        passwd=findViewById(R.id.password1Area);
        uname=findViewById(R.id.username1Area);
        progressBar=findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         progressBar.setVisibility(View.VISIBLE);
                                         String password,unam;
                                         unam=String.valueOf(uname.getText());
                                         password=String.valueOf(passwd.getText());

                                         if(TextUtils.isEmpty(unam)){
                                             progressBar.setVisibility(View.GONE);
                                             Toast.makeText(Login.this,"Please enter your Sahyadri mail-id!",Toast.LENGTH_SHORT).show();
                                         }
                                         if(TextUtils.isEmpty(password)){
                                             progressBar.setVisibility(View.GONE);
                                             Toast.makeText(Login.this,"Please enter your password!",Toast.LENGTH_SHORT).show();
                                         }
                                         else{
                                             databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                     if(snapshot.hasChild(unam)){
                                                         String getPassword=snapshot.child(unam).child("password").getValue(String.class);
                                                         if(getPassword.equals(password)){
                                                             Toast.makeText(Login.this,"Sucessfully Logged In!",Toast.LENGTH_SHORT).show();
                                                             progressBar.setVisibility(View.GONE);


                                                             Intent intent=new Intent(Login.this,Home.class);
                                                             intent.putExtra("Username", unam);
                                                             Log.d("Buyer Username in login page",unam);

                                                             startActivity(intent);
                                                         }
                                                         else{progressBar.setVisibility(View.GONE);
                                                             Toast.makeText(Login.this,"Please enter the correct password!",Toast.LENGTH_SHORT).show();
                                                         }
                                                     }
                                                     else{progressBar.setVisibility(View.GONE);
                                                         Toast.makeText(Login.this,"Please enter the correct Username and password!",Toast.LENGTH_SHORT).show();
                                                     }

                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                 }
                                             });
                                         }

//                                         mAuth.signInWithEmailAndPassword(email, password)
//                                                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                                     @Override
//                                                     public void onComplete(@NonNull Task<AuthResult> task) {
//                                                         progressBar.setVisibility(View.GONE);
//                                                         if (task.isSuccessful()) {
//                                                             Toast.makeText(getApplicationContext(), "Login successful!",
//                                                                     Toast.LENGTH_SHORT).show();
//                                                             Intent intent=new Intent(getApplicationContext(),Home.class);
//                                                             startActivity(intent);
//                                                             finish();
//                                                         } else {
//                                                             Toast.makeText(Login.this, "Authentication failed.",
//                                                                     Toast.LENGTH_SHORT).show();
//                                                         }
//                                                     }
//                                                 });
                                     }
                                 }
        );

        linkpage = findViewById(R.id.linkPage1);
        linkpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the signup button click event
                Intent intent = new Intent(Login.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}