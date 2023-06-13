package algonquin.cst2335.li000808;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * test for the main activity
     */
    @Test
    public void mainActivityTest() {

        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test for missing uppercase of the password
     */
    @Test
    public void testFindMissingUpperCase() {
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        //type in password123#$*
        appCompatEditText.perform(replaceText("password123#$*"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text
        textView.check(matches(withText("You shall not pass!")));
    }
    /**
     * Test for missing lower case of the password
     */
    @Test
    public void testFindMissingLowerCase() {
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        //type in PASS123#$*
        appCompatEditText.perform(replaceText("PASS123#$*"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test for missing Special characters of the password
     */
    @Test
    public void testFindMissingSpecial() {
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        //type in PASSp123
        appCompatEditText.perform(replaceText("PASSp123"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text
        textView.check(matches(withText("You shall not pass!")));
    }
    /**
     * Test for missing numbers of the password
     */
    @Test
    public void testFindMissingNumber() {
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        //type in PASSp#$*
        appCompatEditText.perform(replaceText("PASSp#$*"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Successful password test
     */
    @Test
    public void testFindEverything() {
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        //type in PASSp123#$*
        appCompatEditText.perform(replaceText("PASSp123#$*"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text
        textView.check(matches(withText("Your password meets the requirements")));
    }

    /**
     * Returns a Matcher<View> that matches a child View at the specified position within a parent View.
     *
     * @param parentMatcher The Matcher<View> for the parent View.
     * @param position The position of the child View within the parent View.
     * @return A Matcher<View> that matches the child View at the specified position.
     */
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        /**
         * A TypeSafeMatcher implementation that matches a child View at the specified position within a parent View.
         */
        return new TypeSafeMatcher<View>() {
            /**
             * Generates a human-readable description of the Matcher.
             *
             * @param description The Description to append the description of this Matcher to.
             */
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            /**
             * Determines whether the given View matches the Matcher safely.
             *
             * @param view The View to compare against the Matcher.
             * @return true if the View matches the Matcher, false otherwise.
             */
            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
