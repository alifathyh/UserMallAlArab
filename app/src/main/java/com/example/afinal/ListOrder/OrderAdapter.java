package com.example.afinal.ListOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.OnItemClickListener;
import com.example.afinal.OnItemClickListener1;
import com.example.afinal.databinding.CustomOrderListBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    List<Order> orders;
    OnItemClickListener listener;
    OnItemClickListener1 deleteListener;

    Context context;
    FirebaseFirestore db =FirebaseFirestore.getInstance();

    public OrderAdapter(List<Order> orders, OnItemClickListener listener, OnItemClickListener1 deleteListener) {
        this.orders = orders;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomOrderListBinding binding = CustomOrderListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderHolder(binding.getRoot());    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        Order order = orders.get(position);
        holder.binding.stetus.setText(order.getStatus());
        holder.binding.date.setText(order.getDate());
        holder.binding.day.setText(order.getDay());
        holder.binding.time.setText(order.getTime());
        holder.binding.stetus.setTag(order.getDocId());
        holder.binding.date.setTag(order.getIndexStatus());
//        holder.binding.undoTheRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(order.getIndexStatus()==0){
//                    String id = String.valueOf(holder.binding.stetus.getTag());
//                    db.collection("Orders").document(id).delete();
//                } else{
//                    Toast.makeText(context, "You cannot cancel the order because your order is in progress", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }


        
        
    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderHolder extends RecyclerView.ViewHolder{
        CustomOrderListBinding binding;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomOrderListBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(binding.stetus.getTag());
                    listener.onclickItem(id);
                }
            });

            binding.undoTheRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(binding.stetus.getTag());
                    int indexStatus = (int) binding.date.getTag();
                    deleteListener.onclickItem(id,indexStatus);
                }
            });
        }


    }
}
