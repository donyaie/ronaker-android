package com.ronaker.app.ui.dashboard


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.ronaker.app.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DashboardActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(DashboardActivity::class.java)

    @Test
    fun dashboardActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1000)

        val appCompatButton = onView(
            allOf(
                withId(R.id.login_button), withText("Login"),
                childAtPosition(
                    allOf(
                        withId(R.id.bottom_layout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.input_edit)
                , isDescendantOfA(withId(R.id.email_input))
                , isDisplayed()
            )
        ).check(matches(isDisplayed()))
        appCompatEditText.perform(replaceText("alidonyaie@gmail.com"), closeSoftKeyboard())


        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.input_edit)
                , isDescendantOfA(withId(R.id.password_input))
            )
        ).check(matches(isDisplayed()))
        appCompatEditText4.perform(replaceText("123456"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.next_button), withText("Login to your account"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.frame_container),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView = onView(
            allOf(
                withId(R.id.explore_text), withText("Explore"),
                childAtPosition(
                    allOf(
                        withId(R.id.explore_layout),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Explore")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
