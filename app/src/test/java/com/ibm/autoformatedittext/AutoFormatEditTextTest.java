package com.ibm.autoformatedittext;

import android.text.TextWatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.Scheduler;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(sdk=21)
public class AutoFormatEditTextTest {
    private MainActivity activity;
    private AutoFormatEditText autoFormatEditText;
    private TextWatcher textWatcher;

    @Before
    public void setUp() {
        Scheduler scheduler = Robolectric.getForegroundThreadScheduler();
        scheduler.pause();
        activity = Robolectric.buildActivity(MainActivity.class).setup().get();
        scheduler.advanceToLastPostedRunnable();

        autoFormatEditText = activity.findViewById(R.id.phoneNumberAutoFormatEditText);
        textWatcher = autoFormatEditText.getTextWatcher();
    }

    @Test
    public void setInputMaskTest() {
        autoFormatEditText.setInputMask("##/##/##");
        assertEquals("", autoFormatEditText.getText().toString());

        autoFormatEditText.setText("012345");
        assertEquals("01/23/45", autoFormatEditText.getText().toString());
    }

    @Test
    public void setInputMaskPlaceholderTest() {
        autoFormatEditText.setInputMask("&&/&&/&&");
        autoFormatEditText.setInputMaskPlaceholder("&");
        assertEquals("", autoFormatEditText.getText().toString());

        autoFormatEditText.setText("012345");
        assertEquals("01/23/45", autoFormatEditText.getText().toString());
    }

    //shadowOf(getMainLooper()).idle();

    @Test
    public void onTextChangedTest() {
        autoFormatEditText.setInputMask("&&/&&/&&");
        autoFormatEditText.setInputMaskPlaceholder("&");

        insertSingleCharacter("1", 0);
        validateFormattedAndUnformatted("1", "1");

        insertSingleCharacter("04", 1);
        validateFormattedAndUnformatted("14/", "14");

        insertSingleCharacter("145/", 2);
        validateFormattedAndUnformatted("14/5", "145");

        insertSingleCharacter("124/5", 1);
        validateFormattedAndUnformatted("12/45/", "1245");

        insertSingleCharacter("123/45/", 2);
        validateFormattedAndUnformatted("12/34/5", "12345");

        insertSingleCharacter("012/34/5", 0);
        validateFormattedAndUnformatted("01/23/45", "012345");

        insertSingleCharacter("01/23/456", 8);
        validateFormattedAndUnformatted("01/23/45", "012345");
    }

    private void insertSingleCharacter(String newString, int index) {
        autoFormatEditText.setText(newString);
        textWatcher.onTextChanged(newString, index, 0, 1);
    }

    private void validateFormattedAndUnformatted(String formatted, String unformatted) {
        assertEquals(formatted, autoFormatEditText.getText().toString());
        assertEquals(formatted, autoFormatEditText.getFormattedText());
        assertEquals(unformatted, autoFormatEditText.getUnformattedText());
    }
}