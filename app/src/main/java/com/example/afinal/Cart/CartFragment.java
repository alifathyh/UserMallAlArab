package com.example.afinal.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.afinal.ActivityBase;
import com.example.afinal.Constants;
import com.example.afinal.Home.HomeFragment;
import com.example.afinal.ListOrder.Order;
import com.example.afinal.ListOrder.OrderItem;
import com.example.afinal.OnDeleteCartClickListener;
import com.example.afinal.R;
import com.example.afinal.databinding.FragmentCartBinding;
import com.example.afinal.item.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.afinal.MainActivity.orderItems;


public class CartFragment extends Fragment {
    FragmentCartBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    double total = 0;
    CartAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater);
//        db = FirebaseFirestore.getInstance();

        for(OrderItem o: orderItems){
            total += o.getCount()*o.getItem().getPrice();
        }
        binding.cartTotalPrice.setText(String.valueOf(total));

        binding.cartContinuePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userLocation = binding.userLocation.getText().toString();

                if(total == 0){
                    Toast.makeText(requireContext(),  R.string.cart_status, Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(userLocation)){
                    binding.userLocation.setError(""+R.string.location_error);
                }else{

                    String docId = db.collection("Orders").document().getId();
                    String userID = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(Constants.USER_UID_KEY,null);
                    String userName = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(Constants.USER_NAME_KEY,null);
                    String userPhone = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(Constants.USER_PHONE_KEY,null);

                    Date dt = new Date();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                    SimpleDateFormat dayFormat = new SimpleDateFormat("E");

                    String orderDate = dateFormat.format(dt);
                    String orderTime = timeFormat.format(dt);
                    String orderDay = dayFormat.format(dt);


                    Order order = new Order(docId, userID, userName, userPhone, userLocation, orderItems, orderTime,
                            orderDate, orderDay, total, "بإنتظار الموافقة", 0 );
                    db.collection("Orders").document(docId).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(requireContext(), R.string.order_successfully, Toast.LENGTH_SHORT).show();
                            orderItems.clear();
                            binding.cartTotalPrice.setText("0.0");
                            binding.userLocation.setText("");
                            adapter.notifyDataSetChanged();
                        }
                    });
                }


            }
        });

       populateDataIntoRV(orderItems);
        return binding.getRoot();
    }

//    private void populateDataIntoRV(List<OrderItem> orderItems) {
//        CartAdapter adapter = new CartAdapter(orderItems);
//        binding.cartRv.setAdapter(adapter);
//        binding.cartRv.setHasFixedSize(true);
//        binding.cartRv.setLayoutManager(new LinearLayoutManager(requireContext()));
//    }

    private void populateDataIntoRV(List<OrderItem> orderItems) {
        adapter = new CartAdapter(orderItems);
        adapter.setListener(new OnDeleteCartClickListener() {
            @Override
            public void onCartClickListener(Item item, double value) {

                total = total - value;
                binding.cartTotalPrice.setText(String.valueOf(total));
                adapter.notifyDataSetChanged();
            }
        });
        binding.cartRv.setAdapter(adapter);
        binding.cartRv.setHasFixedSize(true);
        binding.cartRv.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}