package com.example.afinal.Categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.OnCategoryClickListener;
import com.example.afinal.databinding.CustomCategoryItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
        ArrayList<Category> categories;
        private OnCategoryClickListener listener;

    public CategoryAdapter(ArrayList<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
@Override
public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    CustomCategoryItemBinding binding = CustomCategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
    return new CategoryHolder(binding.getRoot());        }

@Override
public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {

        Category category = categories.get(position);

        holder.binding.customCategoryItemTvName.setText(category.getName());
        Picasso.get().load(category.getImage()).into(holder.binding.imageView);
        holder.binding.customCategoryItemTvName.setTag(category.getName());

        }

@Override
public int getItemCount() {
        return categories.size();
        }

class CategoryHolder extends RecyclerView.ViewHolder{

    CustomCategoryItemBinding binding;
    public CategoryHolder(@NonNull View itemView) {
        super(itemView);
        binding = CustomCategoryItemBinding.bind(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(binding.customCategoryItemTvName.getTag());
                listener.onItemClick(name);
            }
        });
    }
}
}

