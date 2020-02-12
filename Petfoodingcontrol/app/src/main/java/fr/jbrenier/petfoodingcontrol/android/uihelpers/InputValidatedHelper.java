package fr.jbrenier.petfoodingcontrol.android.uihelpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.jbrenier.petfoodingcontrol.utils.InputValidationUtils;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Helper class for email inputs with a validation message TextView associated.
 * @author Jérpome Brenier
 */
public class InputValidatedHelper {

    private InputValidatedHelper() {}

    /**
     * Returns a date input EditText with a validation listener that trigger the visibility of an
     * error message if date is invalid.
     * @param editTextDate the EditText corresponding to the email
     * @param txtViewMessage the TextView of the error message
     * @return the EditText corresponding to the email with validation
     */
    public static EditText getWithValidationControlDateEditText(
            EditText editTextDate,
            TextView txtViewMessage) {
        txtViewMessage.setVisibility(GONE);
        editTextDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (InputValidationUtils.isDateValid(s.toString())) {
                        txtViewMessage.setVisibility(GONE);
                    } else {
                        txtViewMessage.setVisibility(VISIBLE);
                    }
                } else {
                    txtViewMessage.setVisibility(GONE);
                }
            }
        });
        return editTextDate;
    }

    /**
     * Returns a mail input EditText with a validation listener that trigger the visibility of an
     * error message if email is invalid.
     * @param editTextEmail the EditText corresponding to the email
     * @param txtViewMessage the TextView of the error message
     * @return the EditText corresponding to the email with validation
     */
    public static EditText getWithValidationControlEmailEditText(
            EditText editTextEmail,
            TextView txtViewMessage) {
        txtViewMessage.setVisibility(GONE);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (InputValidationUtils.isEmailValid(s.toString())) {
                        txtViewMessage.setVisibility(GONE);
                    } else {
                        txtViewMessage.setVisibility(VISIBLE);
                    }
                } else {
                    txtViewMessage.setVisibility(GONE);
                }
            }
        });
        return editTextEmail;
    }

    /**
     * Returns a mail input EditText with a validation listener that trigger the visibility of an
     * error message if email is invalid and set the action (save, add...) button visible only when
     * a valid email has been entered.
     * @param editTextEmail the EditText corresponding to the email
     * @param txtViewMessage the TextView of the error message
     * @param actionButton the action (save, add...) button
     * @return the EditText corresponding to the email with validation
     */
    public static EditText getWithValidationControlEmailEditText(
            EditText editTextEmail,
            TextView txtViewMessage,
            Button actionButton) {
        txtViewMessage.setVisibility(GONE);
        actionButton.setVisibility(INVISIBLE);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (InputValidationUtils.isEmailValid(s.toString())) {
                        actionButton.setVisibility(VISIBLE);
                        txtViewMessage.setVisibility(GONE);
                    } else {
                        actionButton.setVisibility(INVISIBLE);
                        txtViewMessage.setVisibility(VISIBLE);
                    }
                } else {
                    actionButton.setVisibility(INVISIBLE);
                    txtViewMessage.setVisibility(GONE);
                }
            }
        });
        return editTextEmail;
    }
}
