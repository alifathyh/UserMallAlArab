package com.example.afinal.Cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.ListOrder.OrderItem;
import com.example.afinal.OnDeleteCartClickListener;
import com.example.afinal.databinding.CustomCartItemBinding;
import com.example.afinal.item.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private List<OrderItem> orderItems;
    private OnDeleteCartClickListener listener;

    public OnDeleteCartClickListener getListener() {
        return listener;
    }

    public void setListener(OnDeleteCartClickListener listener) {
        this.listener = listener;
    }

    public CartAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomCartItemBinding binding = CustomCartItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CartHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        holder.item = orderItem.getItem();
        Picasso.get().load(orderItem.getItem().getImage()).into(holder.binding.customCartItemIv);
        holder.binding.customCartItemTvName.setText(orderItem.getItem().getName());
        holder.binding.customCartItemTvPrice.setText(String.valueOf(orderItem.getItem().getPrice()));
        holder.binding.customCartItemTvCount.setText(String.valueOf(orderItem.getCount()));
        holder.binding.customCartItemTvSubTotal.setText(String.valueOf(orderItem.getCount()*orderItem.getItem().getPrice()));
        holder.binding.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderItems.remove(position);
                double value = Double.parseDouble(holder.binding.customCartItemTvSubTotal.getText().toString());
                listener.onCartClickListener(holder.item,value);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    class CartHolder extends RecyclerView.ViewHolder {
        CustomCartItemBinding binding;
        Item item;
        public CartHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomCartItemBinding.bind(itemView);

            binding.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
