package com.osaid.taskmaster;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;

import static androidx.test.espresso.contrib.RecyclerViewActions.*;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nullable;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<Home> activityRule = new ActivityScenarioRule<>(Home.class);

    @Test
    public void homeActivityTesting() {

        onView(withText("Task Master")).check(matches(isDisplayed()));
        onView(withId(R.id.imageView3)).perform(click());
        onView(withText("Settings")).check(matches(isDisplayed()));
        onView(withId(R.id.username)).perform(typeText("Doaa"));
        onView(withId(R.id.saveBtn)).perform(click());
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withText("Task Master")).check(matches(isDisplayed()));
        onView(withId(R.id.title)).check(matches(withText("Doaa's tasks")));
        onView(withId(R.id.addTskBtn)).perform(click());

        onView(withId(R.id.taskTitleEditText)).perform(typeText("Testing Task Title"));
        onView(withId(R.id.taskBodyEditText)).perform(typeText("Testing Task Body"));
        Espresso.closeSoftKeyboard();

        GetTextAction action = new GetTextAction();
        onView(withId(R.id.textView6)).perform(action);
        int counter = Integer.parseInt(action.getText());


        onView(withId(R.id.addTskButton)).perform(click());

        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        onView(withId(R.id.recyclerView)).perform(actionOnItemAtPosition(counter + 1, click()));

        onView(withText("Task Details")).check(matches(isDisplayed()));

        onView(withId(R.id.taskTitle)).check(matches(withText("Testing Task Title")));
        onView(withId(R.id.taskBody)).check(matches(withText("Testing Task Body")));

    }

    static class GetTextAction implements ViewAction {

        private String text;

        @Override
        public Matcher<View> getConstraints() {
            return isAssignableFrom(TextView.class);
        }

        @Override
        public String getDescription() {
            return "get text";
        }

        @Override
        public void perform(UiController uiController, View view) {
            TextView textView = (TextView) view;
            text = textView.getText().toString();
        }

        @Nullable
        public String getText() {
            return text;
        }
    }

}