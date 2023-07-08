package com.example.studentswapzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Seller extends AppCompatActivity {
int REQUEST_CALL_PERMISSION=1;
    TextView Name,Email,Dept;
    ImageView img;
    String buyeruname;

    Button callBtn,mailBtn,msgBtn,whatsappBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        getSupportActionBar().hide();
        buyeruname=getIntent().getStringExtra("Buyer uname");

        String name=getIntent().getStringExtra("Name");
        String email=getIntent().getStringExtra("Email");
        String mobileno=getIntent().getStringExtra("Mobile");
        String username=getIntent().getStringExtra("Username");
        String dept=getIntent().getStringExtra("Department");
        String prod=getIntent().getStringExtra("Product");

        Name=findViewById(R.id.sellerName);
        Email=findViewById(R.id.sellerEmail);
        Dept=findViewById(R.id.sellerDept);
        img=findViewById(R.id.seller_image);
        msgBtn=findViewById(R.id.msgBtn);
        callBtn=findViewById(R.id.callBtn);
        mailBtn=findViewById(R.id.mailBtn);
        whatsappBtn=findViewById(R.id.whatsapp);

        Name.setText(name);
        Email.setText(email);
        Dept.setText(dept+" department");


        String imageFileName = username;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("user_images");
        StorageReference imageRef = storageRef.child(imageFileName);


        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Use Glide library to load the image into the ImageView
            Glide.with(img)
                    .load(uri)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder_image)) // Placeholder image while loading
                    .error(R.drawable.error_image) // Error image if unable to load
                    .into(img);
        }).addOnFailureListener(e -> {
            // Handle the failure to retrieve the image URL
            // You can set a default image or show an error message
            img.setImageResource(R.drawable.error_image);
            Log.e("Firebase Storage", "Failed to retrieve image URL: " + e.getMessage());
        });

        if (ContextCompat.checkSelfPermission(Seller.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Seller.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            // Permission already granted
            // Add the click listener to the call button
            callBtn.setOnClickListener(v -> {
                // Launch the default call app with the retrieved mobile number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobileno));
                startActivity(intent);
            });
        }

        mailBtn.setOnClickListener(v -> {
            // Create an email intent
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + email)); // Specify the recipient email address

            // Set the subject
            intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry Regarding Product Purchase");

            // Set the email body
            String emailBody = "Hey " + name + "!\n\n" +
                    "I hope this email finds you well. I came across your post on the SwapZone app for the used " + prod + " and wanted to inquire about its availability. I'm in search of a similar product, and your listing caught my eye.\n\n" +
                    "Could you let me know if the " + prod + " is still available? I would also appreciate if you could share some details about its condition, age, and any included accessories. Additionally, it would be great to know the asking price and if you're open to negotiations.\n\n" +
                    "Looking forward to hearing back from you soon!\n\n" +
                    "Cheers,\n"+buyeruname+"\n";

            intent.putExtra(Intent.EXTRA_TEXT, emailBody);

            // Check if there is an app available to handle the intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Handle case where no mail app is available
                Toast.makeText(Seller.this, "No mail app available", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the click listener to the messaging button
        msgBtn.setOnClickListener(v -> {
            // Launch the default messaging app with a new SMS intent
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + mobileno));  // Specify the "smsto:" scheme

            String sms_body="Hey " + name + "!\n\n" +
                    "I hope this message finds you well. I came across your post on the SwapZone app for the used " + prod + " and wanted to inquire about its availability. I'm in search of a similar product, and your listing caught my eye.\n\n" +
                    "Could you let me know if the " + prod + " is still available? I would also appreciate if you could share some details about its condition, age, and any included accessories. Additionally, it would be great to know the asking price and if you're open to negotiations.\n\n" +
                    "Looking forward to hearing back from you soon!\n\n" +
                    "Cheers,\n"+buyeruname+"\n";

            // Set the initial text message (optional)
            intent.putExtra("sms_body", sms_body);

            // Check if there is an app available to handle the intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Handle case where no messaging app is available
                Toast.makeText(Seller.this, "No messaging app available", Toast.LENGTH_SHORT).show();
            }
        });
        whatsappBtn.setOnClickListener(v -> {
            // Check if WhatsApp is installed on the device
            PackageManager packageManager = getPackageManager();
            Intent whatsappIntent = new Intent(Intent.ACTION_SENDTO);
            whatsappIntent.setData(Uri.parse("smsto:" + mobileno));

            // Check if there is at least one WhatsApp activity found
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(whatsappIntent, 0);
            if (resolveInfoList.size() > 0) {
                // WhatsApp is installed, so create and launch the WhatsApp intent
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + mobileno));
                intent.setPackage("com.whatsapp"); // Specify the package name of WhatsApp

                startActivity(intent);
            } else {
                // WhatsApp is not installed, show an error message
                Toast.makeText(Seller.this, "WhatsApp is not installed on your device", Toast.LENGTH_SHORT).show();
            }
        });

    }
}