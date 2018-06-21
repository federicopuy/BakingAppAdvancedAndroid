package com.example.federico.bakingappadvancedandroid;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.example.federico.bakingappadvancedandroid.activities.RecipesActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class VideoPlayerExistsActivityTest {

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void videoPlayer_is_Displayed(){
        // tests that exoplayer is displayed correctly
        onView(withId(R.id.recipes_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.step_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
        //checks that the first view with id playerView_exo1 is displayed.
        onView(withIndex(withId(R.id.playerView_exo1), 0)).check(matches(isDisplayed()));
    }

    // copied from https://stackoverflow.com/questions/29378552/in-espresso-how-to-choose-one-the-view-who-have-same-id-to-avoid-ambiguousviewm
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }
}

