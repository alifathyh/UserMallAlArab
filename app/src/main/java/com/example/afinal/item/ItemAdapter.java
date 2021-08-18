package com.example.afinal.item;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.ListOrder.OrderItem;
import com.example.afinal.OnViewItemClickListener;
import com.example.afinal.R;
import com.example.afinal.databinding.CustomItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.afinal.MainActivity.orderItems;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<Item> items;
    private OnViewItemClickListener listener;
    Context context;
    double value;
//    private OnCartClickListener cartClickListener;
//
//    public OnCartClickListener getCartClickListener() {
//        return cartClickListener;
//    }
//
//    public void setCartClickListener(OnCartClickListener cartClickListener) {
//        this.cartClickListener = cartClickListener;
//    }


    public ItemAdapter(List<Item> items, OnViewItemClickListener listener, Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CustomItemBinding binding = CustomItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ItemHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = items.get(position);
        holder.item = item;
        holder.binding.customItemTvName.setText(item.getName());
        holder.binding.customItemTvPrice.setText(item.getPrice() + " NIS");
        Picasso.get().load(item.getImage()).into(holder.binding.customItemImg);
        holder.binding.customItemTvName.setTag(item.getDocId());
        holder.binding.customItemQuantity.setText(item.getQuantity());


        holder.binding.customItemCondition.setText(item.getTheCondition());
        if(item.getIndexTheCondition()==0){
            holder.binding.customItemCondition.setTextColor(Color.BLACK);
        }
        else{
            holder.binding.customItemCondition.setTextColor(Color.RED);
        }
        
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        CustomItemBinding binding;
        Item item;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomItemBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(binding.customItemTvName.getTag());
                    listener.onItemClick(id);
                }
            });

            binding.customItemImgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value = Double.parseDouble(binding.customItemTvCount.getText().toString());
                    if(item.getIndexTheCondition()==1){
                        Toast.makeText(context,  R.string.products_not_available, Toast.LENGTH_SHORT).show();
                    }else{
                        if(item.getIndexQuantity()==0){
                            value += 1;
                        }else{
                            value += 0.25;
                        }
                    }
                    binding.customItemTvCount.setText(String.valueOf(value));                    
                }
            });

            binding.customItemImgMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value = Double.parseDouble(binding.customItemTvCount.getText().toString());
                    if(item.getIndexTheCondition()==1){
                        Toast.makeText(context,  R.string.products_not_available, Toast.LENGTH_SHORT).show();
                    }else{
                        if(item.getIndexQuantity()==0){
                            if (value > 0)
                                value -= 1;
                        }else{
                            if (value > 0)
                                value -= 0.25;
                        }
                    }
                    
                    binding.customItemTvCount.setText(String.valueOf(value));
                }
            });


            binding.customAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                    if(item.getIndexTheCondition() == 0){
                        if(String.valueOf(value).equals("0.0")){
                            Toast.makeText(context, R.string.choose_quantity, Toast.LENGTH_SHORT).show();
                        }else {
                            double value = Double.parseDouble(binding.customItemTvCount.getText().toString());

                            OrderItem orderItem = new OrderItem(item,value,"");
                            orderItems.add(orderItem);
                            Toast.makeText(context, R.string.add_to_cart, Toast.LENGTH_SHORT).show();
                        }
                    }else if(item.getIndexTheCondition() == 1){
                        Toast.makeText(context,  R.string.products_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

