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
        boolean res = false;

        if (name.isEmpty())
        {
            i_FullName.setError("Name canot be Empty");
        }
        else
        {
            if (name.length() > 20)
            {
                i_FullName.setError("Too long: Max 20");
            }
            else if (name.length() < 3)
            {
                i_FullName.setError("To short: Min 3");
            }
            else if (!name.contains(" "))
            {
                i_FullName.setError("Name has to be from 2 words");
            }
            else if (!Charset.forName("US-ASCII").newEncoder().canEncode(name))
            {
                i_FullName.setError("Use only Latin");
            }
            else
            {
                res = true;
            }
        }

        Log.e(TAG, "checkValidName() <<");
        return res;
    }

}
