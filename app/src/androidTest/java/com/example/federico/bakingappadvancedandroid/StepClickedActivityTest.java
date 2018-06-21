package com.example.federico.bakingappadvancedandroid;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.federico.bakingappadvancedandroid.activities.RecipesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class StepClickedActivityTest {
    private static final String STEP_DESCRIPTION = "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.";

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void clickRecyclerViewSteps_Open_Detail(){
        //tests that the correct detail is launched when a step is clicked
    onView(withId(R.id.recipes_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
    onView(withId(R.id.step_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
    onView(withId(R.id.tvStepDescription)).check(matches(withText(STEP_DESCRIPTION)));
    }
}


