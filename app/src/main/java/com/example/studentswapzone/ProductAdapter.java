package com.example.studentswapzone;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.studentswapzone.Product;
import com.example.studentswapzone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private String username;

    ArrayList<String> currentWishlist=new ArrayList<>();

    public ProductAdapter(List<Product> productList,String username) {
        this.productList = productList;
        this.username=username;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemproductcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = productList.get(position);

        holder.productName.setText(product.getName());
//        holder.productDesc.setText(" Description: "+product.getDescription());
//        holder.productYrsUsed.setText(" Years Used: "+product.getYearsUsed());
        holder.productSP.setText("Rs "+product.getSellingPrice());



        // Retrieve the image URL from Firebase Storage based on the product ID
        String imageFileName = product.getId();
        // Assuming the image file extension is ".jpeg"
        Log.e("product url",imageFileName);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("product_images");
        StorageReference imageRef = storageRef.child(imageFileName);


        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Use Glide library to load the image into the ImageView
            Glide.with(holder.itemView)
                    .load(uri)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder_image)) // Placeholder image while loading
                    .error(R.drawable.error_image) // Error image if unable to load
                    .into(holder.productImage);
        }).addOnFailureListener(e -> {
            // Handle the failure to retrieve the image URL
            // You can set a default image or show an error message
            holder.productImage.setImageResource(R.drawable.error_image);
            Log.e("Firebase Storage", "Failed to retrieve image URL: " + e.getMessage());
        });



        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the new activity here
                Context context = view.getContext();
                Intent intent = new Intent(context, ProductDetails.class);



                // Pass the necessary data to the new activity using intent extras
                intent.putExtra("productId", product.getId());

               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                DatabaseReference buyerRef = databaseReference.child("users").child(username).child("Wishlist");
                buyerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Retrieve the current wishlist array from the database using GenericTypeIndicator
                            GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {
                            };
                            currentWishlist = dataSnapshot.getValue(typeIndicator);
                            Log.d("Wishlist in product adapter",String.valueOf(currentWishlist));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Error", databaseError.getMessage());
                    }
                });

                intent.putExtra("Wishlist", currentWishlist);
                intent.putExtra("Buyer Uname",username);
                Log.e("Uname",username);


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName,productDesc,productYrsUsed,productSP;
        Button buyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.item1);
            productName = itemView.findViewById(R.id.item1name);
//            productDesc=itemView.findViewById(R.id.item1desc);
//            productYrsUsed=itemView.findViewById(R.id.item1yrsused);
            productSP=itemView.findViewById(R.id.item1sp);
            buyButton=itemView.findViewById(R.id.buyButton);
        }
    }
}
