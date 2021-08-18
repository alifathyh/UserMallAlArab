package com.example.afinal.ListOrder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.databinding.CustomCartItemBinding;
import com.example.afinal.databinding.CustomOrderDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsHolder> {
    List<OrderItem> orders;


    public OrderDetailsAdapter(List<OrderItem> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomOrderDetailsBinding binding = CustomOrderDetailsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderDetailsHolder(binding.getRoot());    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsHolder holder, int position) {
        OrderItem order = orders.get(position);
        holder.binding.customOrderItemTvPrice.setText(String.valueOf(order.getItem().getPrice()));
        holder.binding.customOrderItemTvName.setText(order.getItem().getName());
        holder.binding.customOrderItemTvCount.setText(String.valueOf(order.getCount()));
        holder.binding.customOrderItemTvSubTotal.setText(String.valueOf(order.getItem().getPrice()*order.getCount()));
        Picasso.get().load(order.getItem().getImage()).into(holder.binding.customOrderItemIv);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderDetailsHolder extends RecyclerView.ViewHolder{
        CustomOrderDetailsBinding binding;

        public OrderDetailsHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomOrderDetailsBinding.bind(itemView);

        }
    }
}