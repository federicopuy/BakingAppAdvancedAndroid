package com.example.federico.bakingappadvancedandroid.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.federico.bakingappadvancedandroid.R;
import com.example.federico.bakingappadvancedandroid.model.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final List<Ingredient> ingredientsList;

    public IngredientsAdapter(List<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Ingredient ingredient = ingredientsList.get(position);
        String ingredientString = "";
        try {
            ingredientString = String.valueOf(ingredient.getQuantity()) + " " +
                    ingredient.getMeasure() + " " +
                    ingredient.getIngredient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvIngredient.setText(ingredientString);
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvIngredient;

        ViewHolder(View view) {
            super(view);
            tvIngredient = view.findViewById(R.id.tvIngredientName);
        }
    }
}
