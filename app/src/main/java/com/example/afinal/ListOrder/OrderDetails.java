package com.example.afinal.ListOrder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.afinal.databinding.ActivityOrderDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class OrderDetails extends AppCompatActivity {
    ActivityOrderDetailsBinding binding;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        db = FirebaseFirestore.getInstance();
//        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        id = intent.getStringExtra(ListOrderFragment.ITEM_KEY);

        db.collection("Orders").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Order order = documentSnapshot.toObject(Order.class);

                List<OrderItem> orderItems = order.getOrderItems();
                fillorder(orderItems);
            }
        });






//    private void fillorder(Order order) {
//        OrderDetailsAdapter adapter = new OrderDetailsAdapter(order);
//        binding.orderDetailsRv.setAdapter(adapter);
//        binding.orderDetailsRv.setHasFixedSize(true);
//        binding.orderDetailsRv.setLayoutManager(new LinearLayoutManager(this));
//    }
    }

    private void fillorder(List<OrderItem> orderItems) {
        OrderDetailsAdapter adapter = new OrderDetailsAdapter(orderItems);
        binding.orderDetailsRv.setAdapter(adapter);
        binding.orderDetailsRv.setHasFixedSize(true);
        binding.orderDetailsRv.setLayoutManager(new LinearLayoutManager(this));
    }
}
