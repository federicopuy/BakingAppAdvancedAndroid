package com.example.federico.bakingappadvancedandroid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.federico.bakingappadvancedandroid.R;
import com.example.federico.bakingappadvancedandroid.model.Recipe;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private final List<Recipe> recipesList;
    private final Context mContext;
    private final ListItemClickListener listItemClickListener;

    public RecipesAdapter(List<Recipe> recipesList, Context mContext, ListItemClickListener listItemClickListener) {
        this.recipesList = recipesList;
        this.mContext = mContext;
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.each_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapter.ViewHolder holder, int position) {

        Recipe recipe = recipesList.get(position);
        String recipeName = "";
        try {
            recipeName = recipe.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvRecipeName.setText(recipeName);
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tvRecipeName;

        ViewHolder(View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Recipe clickedRecipe = recipesList.get(clickedPosition);
            listItemClickListener.onListItemClick(clickedPosition, clickedRecipe);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, Recipe clickedRecipe);
    }
}
