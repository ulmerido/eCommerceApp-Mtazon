package com.example.ido.appex2;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;

import java.nio.charset.Charset;

public class ValidationChecker {

    public static final String TAG = "ValidationChecker:";


    public static void CheckPassword(final TextView i_Password) {
        i_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckValidPassword(i_Password);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    public static boolean CheckValidPassword(TextView i_Password) {
        Log.e(TAG, "checkValidPassword() >>");
        boolean res = false;
        String pass = i_Password.getText().toString();

        if (pass.length() < 6) {
            i_Password.setError("Too Short: Mim 6");
        } else if (pass.length() > 20) {
            i_Password.setError("Too Long: Max 20");
        } else {
            res = true;
        }

        Log.e(TAG, "checkValidPassword() <<");
        return res;
    }

    public  static  void CheckEmail(final TextView i_Email)
    {
        i_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckValidEmail(i_Email);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    public static boolean CheckValidEmail(TextView i_Email) {
        Log.e(TAG, "checkValidEmail() >>");
        boolean res = true;
        CharSequence csEmail = i_Email.getText().toString();
        if (TextUtils.isEmpty(csEmail) || !Patterns.EMAIL_ADDRESS.matcher(csEmail).matches()) {
            i_Email.setError("invalid email");
            res = false;
        }

        Log.e(TAG, "checkValidEmail() <<");
        return res;
    }


    public static  void CheckFullName(final TextView i_FullName)
    {
        i_FullName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                CheckValidName(i_FullName);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

    }


    public static boolean CheckValidName(TextView i_FullName)
    {

        Log.e(TAG, "checkValidName() >>");
        String name = i_FullName.getText().toString();
        boolean res = true;

        if (name.isEmpty())
        {
            i_FullName.setError("Name canot be Empty");
            res = false;
        }


        for (char ch : name.toCharArray())
        {
            if (ch < 'A' || ((ch < 'a') && (ch > 'Z')) || (ch > 'z'))
            {
                i_FullName.setError("invalid letters, use latin.");
                res = false;
            }
        }

        if (name.length() > 10) {
            i_FullName.setError("Too long: Max 10");
            res = false;
        }

        if (name.length() <= 1) {
            i_FullName.setError("Too short: Min 2");
            res = false;
        }

        if (name.contains(" ")) {
            i_FullName.setError("name cant have space in it");
            res = false;
        }

        if (!Charset.forName("US-ASCII").newEncoder().canEncode(name))
        {
            i_FullName.setError("Use only Latin");
            res = false;
        }


        Log.e(TAG, "checkValidName() <<");
        return res;
    }

}
