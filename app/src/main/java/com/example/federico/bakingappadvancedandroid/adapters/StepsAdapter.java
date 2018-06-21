package com.example.federico.bakingappadvancedandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.federico.bakingappadvancedandroid.R;
import com.example.federico.bakingappadvancedandroid.activities.StepDetailActivity;
import com.example.federico.bakingappadvancedandroid.activities.StepMasterActivity;
import com.example.federico.bakingappadvancedandroid.model.Step;
import com.example.federico.bakingappadvancedandroid.utils.Constants;
import com.google.gson.Gson;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private final StepMasterActivity mParentActivity;
    private final List<Step> stepsList;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Step step = (Step) view.getTag();
            Gson stepGson = new Gson();
            String stepJson = stepGson.toJson(step);

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(Constants.STEP_FRAGMENT_JSON, stepJson);
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, StepDetailActivity.class);
                intent.putExtra(Constants.INTENT_STEP_FRAGMENT, stepJson);

                Gson gsonStepsList = new Gson();
                String jsonStepsList = gsonStepsList.toJson(stepsList);
                intent.putExtra(Constants.INTENT_STEPS_LIST, jsonStepsList);
                context.startActivity(intent);
            }
        }
    };

    public StepsAdapter(StepMasterActivity parent,
                        List<Step> items,
                        boolean twoPane) {
        stepsList = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Step step = stepsList.get(position);
        holder.tvIdStep.setText(String.valueOf(step.getId()));
        holder.tvShortDescriptionStep.setText(step.getShortDescription());
        holder.itemView.setTag(stepsList.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvIdStep;
        final TextView tvShortDescriptionStep;

        ViewHolder(View view) {
            super(view);
            tvIdStep = view.findViewById(R.id.tvIdStep);
            tvShortDescriptionStep = view.findViewById(R.id.tvShortDescriptionStep);
        }
    }
}