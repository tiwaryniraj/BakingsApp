package com.example.niraj.bakingsapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityScreenTest {

    private static final String RECIPE_NAME_NUTELLA_PIE = "Nutella Pie";
    private static final String RECIPE_NAME_BROWNIES = "Brownies";
    private static final String RECIPE_NAME_YELLOW_CAKE = "Yellow Cake";
    private static final String RECIPE_NAME_CHEESECAKE = "Cheesecake";

    private static final String SAMPLE_INGREDIENT = "2.0 CUP Graham Cracker crumbs";

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule
            = new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void checkText_RecipeActivity() {
        onView(withId(R.id.recyclerview_recipe)).perform(scrollToPosition(0));
        onView(withText(RECIPE_NAME_NUTELLA_PIE)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_recipe)).perform(scrollToPosition(1));
        onView(withText(RECIPE_NAME_BROWNIES)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_recipe)).perform(scrollToPosition(2));
        onView(withText(RECIPE_NAME_YELLOW_CAKE)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_recipe)).perform(scrollToPosition(3));
        onView(withText(RECIPE_NAME_CHEESECAKE)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecipeRecyclerViewItem_OpensRecipeDetailActivity() {
        onView(withId(R.id.recyclerview_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(instanceOf(TextView.class), withText(RECIPE_NAME_NUTELLA_PIE))).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_ingredient)).perform(scrollToPosition(0));
        onView(allOf(instanceOf(TextView.class), withText(SAMPLE_INGREDIENT))).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
