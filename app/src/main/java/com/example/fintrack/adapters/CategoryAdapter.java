package com.example.fintrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fintrack.R;
import com.example.fintrack.databinding.SampleCategoryItemBinding;
import com.example.fintrack.models.Category;

import java.util.ArrayList;
import java.util.Calendar;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<Category> categories;

    public interface CategoryClickListener{
        void onCategoryClicked(Category category);
    }

    CategoryClickListener categoryClickListener;

    public CategoryAdapter(Context context,ArrayList<Category> categories,CategoryClickListener categoryClickListener){
        this.context=context;
        this.categories=categories;
        this.categoryClickListener=categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_category_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        holder.binding.categoryText.setText(categories.get(position).getCategoryName());
        holder.binding.categoryIcon.setImageResource(categories.get(position).getCategoryImage());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(categories.get(position).getCategoryColor()));
        holder.itemView.setOnClickListener(view -> {
            categoryClickListener.onCategoryClicked(categories.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

            SampleCategoryItemBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleCategoryItemBinding.bind(itemView);
        }
    }
}
