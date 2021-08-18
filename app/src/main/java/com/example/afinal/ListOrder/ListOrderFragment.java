package com.example.afinal.ListOrder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.afinal.Constants;
import com.example.afinal.OnItemClickListener;
import com.example.afinal.OnItemClickListener1;
import com.example.afinal.R;
import com.example.afinal.databinding.FragmentItemBinding;
import com.example.afinal.databinding.FragmentListOrderBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class
ListOrderFragment extends Fragment {
    FragmentListOrderBinding binding;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    String uid;
    ArrayList<Order> order;
    public static final String ITEM_KEY = "item_key";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListOrderBinding.inflate(inflater);
//        db = FirebaseFirestore.getInstance();


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
        uid = sp.getString(Constants.USER_UID_KEY,null);

        db.collection("Orders").whereEqualTo("userId",uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                order = (ArrayList<Order>) queryDocumentSnapshots.toObjects(Order.class);
                UserInfo(order);
            }
        });
        return binding.getRoot();

    }

    private void UserInfo(ArrayList<Order> orderItems) {
        OrderAdapter adapter = new OrderAdapter(orderItems, new OnItemClickListener() {
            @Override
            public void onclickItem(String id) {
                Intent i = new Intent(requireContext(), OrderDetails.class);
                i.putExtra(ITEM_KEY, id);
                startActivity(i);
            }

        }, new OnItemClickListener1() {
            @Override
            public void onclickItem(String id, int indexStatus) {
                if(indexStatus==0){
                    db.collection("Orders").document(id).delete();
                    Toast.makeText(getContext(), R.string.request_canceled, Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), getString(R.string.cannot_cancel_request), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.orderListRv.setAdapter(adapter);
        binding.orderListRv.setHasFixedSize(true);
        binding.orderListRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}