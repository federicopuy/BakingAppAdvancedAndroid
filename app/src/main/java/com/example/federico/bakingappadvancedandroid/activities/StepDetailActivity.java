package com.example.federico.bakingappadvancedandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.federico.bakingappadvancedandroid.R;
import com.example.federico.bakingappadvancedandroid.adapters.StepDetailFragment;
import com.example.federico.bakingappadvancedandroid.model.Step;
import com.example.federico.bakingappadvancedandroid.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepMasterActivity}.
 */
@SuppressWarnings("WeakerAccess")
public class StepDetailActivity extends AppCompatActivity {

    @BindView(R.id.fabNextStep)
    FloatingActionButton fabNextStep;
    private Step selectedStep;
    List<Step> stepsList;
    String stepsListJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        //get step from inteent
        Intent intent = getIntent();
        String stepJsonString = intent.getStringExtra(Constants.INTENT_STEP_FRAGMENT);
        Gson gsonStep = new Gson();
        selectedStep = gsonStep.fromJson(stepJsonString, Step.class);

        //get all steps of selected recipe from intent
        stepsListJsonString = intent.getStringExtra(Constants.INTENT_STEPS_LIST);
        Gson gsonStepList = new Gson();
        Type type = new TypeToken<List<Step>>() {
        }.getType();
        stepsList = gsonStepList.fromJson(stepsListJsonString, type);

        //check if its last step
        if (selectedStep.getId() == stepsList.get(stepsList.size() - 1).getId()) {
            fabNextStep.setVisibility(View.INVISIBLE);
        }

        if (savedInstanceState == null) {
            String stepJson = getIntent().getStringExtra(Constants.INTENT_STEP_FRAGMENT);
            Bundle arguments = new Bundle();
            arguments.putString(Constants.STEP_FRAGMENT_JSON, stepJson);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

    @OnClick(R.id.fabNextStep)
    public void intentToNextStep() {

        Step nextStep = null;
        //get next step
        for (int i = 0; i < stepsList.size(); i++) {
            Step step = stepsList.get(i);
            if (step.getId() == selectedStep.getId()) {
                nextStep = stepsList.get(i + 1);
            }
        }

        //put step as extra
        Gson stepGson = new Gson();
        String stepJson = stepGson.toJson(nextStep);
        Intent intent = new Intent(StepDetailActivity.this, StepDetailActivity.class);
        intent.putExtra(Constants.INTENT_STEP_FRAGMENT, stepJson);
        //put list of steps as extra
        intent.putExtra(Constants.INTENT_STEPS_LIST, stepsListJsonString);
        startActivity(intent);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, StepMasterActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
