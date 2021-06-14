package com.example.hw3_sql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button viewDB;
    Spinner spinner = null;
    String errorMessage;

    /*
    @TODO:
        1. Work on error checks
        2. Having database completely viewable for alll aspects
        3.



     */

    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.spinner_Orders);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_of_orders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        viewDB = findViewById(R.id.btn_viewDB);


        EditText pt_ticker = findViewById(R.id.pt_Ticker);
        pt_ticker.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        EditText pt_amnt = findViewById(R.id.et_TransAmnt);
        pt_amnt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5, 2)});
    }

    public void viewDatabase(View view)
    {
        Intent switchToDB = new Intent(this, DBViewer.class);
        startActivity(switchToDB);
    }

    public void resetDatabase(View view)
    {
        MyContentProvider.MainDatabaseHelper db = new MyContentProvider.MainDatabaseHelper(this);
        db.clearDatabase();
        Toast.makeText(this, "Database Cleared.", Toast.LENGTH_SHORT).show();
    }



    // for the reset button
    public void resetValues(View view)
    {
        EditText pt_ticker = findViewById(R.id.pt_Ticker);
        pt_ticker.setText("DIS");

        EditText pt_CompanyName = findViewById((R.id.pt_CompanyName));
        pt_CompanyName.setText("Disney");

        RadioGroup rg = findViewById(R.id.rg_TransType);
        rg.check(R.id.rb_Buy);

        EditText pt_amnt = findViewById(R.id.et_TransAmnt);
        pt_amnt.setText("0.00");

        EditText pps = findViewById(R.id.et_PPS);
        pps.setText("0.00");

        EditText cc = findViewById(R.id.et_ConfCode);
        cc.setText("");

        spinner.setSelection(0);
    }


    public void submitValues(View view)
    {
        errorMessage = "";
        boolean goodInput = true;

        EditText pt_ticker = findViewById(R.id.pt_Ticker);
        EditText pt_CompanyName = findViewById((R.id.pt_CompanyName));
        RadioGroup rg = findViewById(R.id.rg_TransType);
        EditText pt_amnt = findViewById(R.id.et_TransAmnt);
        EditText pps = findViewById(R.id.et_PPS);
        EditText cc = findViewById(R.id.et_ConfCode);

        int selectedBtn = rg.getCheckedRadioButtonId();
        RadioButton tempBtn = (RadioButton) findViewById(selectedBtn);

        String spinSelection = spinner.getSelectedItem().toString();


        char[] charArr1 = pt_ticker.getText().toString().toCharArray();
        char[] charArr2 = pt_CompanyName.getText().toString().toCharArray();
        if (pt_ticker.length() < 3 || pt_ticker.length() > 6 || charArr1[0] != charArr2[0])
        {
            //error
            errorMessage += "Please fix the 'Ticker' field. \n";
            TextView tvTicker = findViewById(R.id.tv_Ticker);
            tvTicker.setTextColor(Color.RED);
            goodInput = false;
        }
        else
        {
            TextView tvTicker = findViewById(R.id.tv_Ticker);
            tvTicker.setTextColor(Color.BLACK);
        }


        String tempLen = pt_CompanyName.getText().toString();
        int lenName = tempLen.replace(" ", "").length();
        if (lenName < 6 || lenName > 20 || tempLen.matches(".*\\d.*"))
        {
            // error
            errorMessage += "Please fix the 'Company Name' field.\n";
            TextView tvCompanyName = findViewById(R.id.tv_CompanyName);
            tvCompanyName.setTextColor(Color.RED);
            goodInput = false;
        }
        else
        {
            TextView tvCompanyName = findViewById(R.id.tv_CompanyName);
            tvCompanyName.setTextColor(Color.BLACK);
        }

        // ----------- iterating through to find non alphanumeric character ---------
        char[] charArr = cc.getText().toString().toCharArray();
        boolean nonAlphaNum = false;
        for (char c: charArr)
        {
            if (!Character.isLetterOrDigit(c))
            {
                nonAlphaNum = true;
            }
        }

        // ----- CONFIRMATION CODE CHECKS ---------------


        String tempCC = cc.getText().toString();
        MyContentProvider.MainDatabaseHelper db = new MyContentProvider.MainDatabaseHelper(this);
        boolean exists = db.checkConfirmationCodeExists(tempCC);
        // checking length, checking if non alpha character, checking if code is NOT in database already
        if (cc.length() < 4 || cc.length() > 8 || nonAlphaNum || !exists)
        {
            // error
            errorMessage += "Your confirmation code already exists.\n";
            TextView ConfCodes = findViewById(R.id.tv_ConfCode);
            ConfCodes.setTextColor(Color.RED);
            goodInput = false;
        }
        else
        {
            TextView ConfCodes = findViewById(R.id.tv_ConfCode);
            ConfCodes.setTextColor(Color.BLACK);
        }


        // ----- TRANSACTION AMOUNT CHECK --------------
        String tempTransAmount = pt_amnt.getText().toString();
        if (tempTransAmount.contentEquals("0.00") || !tempTransAmount.matches("^[0-9]*\\.[0-9]{2}$"))
        {
            errorMessage += "Please enter a valid transaction amount.\n";
            TextView transAmount = findViewById(R.id.tv_TransAmnt);
            transAmount.setTextColor(Color.RED);
            goodInput = false;
        }
        else
        {
            TextView transAmount = findViewById(R.id.tv_TransAmnt);
            transAmount.setTextColor(Color.BLACK);
        }


        // ---- PRICE PER SHARE CHECK ---------------------
        String tempPPS = pps.getText().toString();
        if (tempPPS.contentEquals("0.00") || !tempPPS.matches("^[0-9]*\\.[0-9]{2}$"))
        {
            errorMessage += "Please enter a valid price per share.\n";
            TextView tPPS = findViewById(R.id.tv_PPS);
            tPPS.setTextColor(Color.RED);
            goodInput = false;
        }
        else
        {
            TextView tPPS = findViewById(R.id.tv_PPS);
            tPPS.setTextColor(Color.BLACK);
        }


        // ---------- TRANS ORDER SPINNER CHECK -------------
        if (spinner.getSelectedItemPosition() == 0)
        {
            errorMessage += "Please select an order type.\n";
            TextView spin = findViewById(R.id.tv_TypeOrder);
            spin.setTextColor(Color.RED);
            goodInput = false;
        }
        else
        {
            TextView spin = findViewById(R.id.tv_TypeOrder);
            spin.setTextColor(Color.BLACK);
        }


        // -------- CHECKING IF ALL THE INPUT IS GOOD ----------
        if (!goodInput)
        {
            Bundle bundle = new Bundle();
            bundle.putString("errors", errorMessage);

            ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "Errors");
            Toast.makeText(this, "Please fix highlighted fields.", Toast.LENGTH_SHORT).show();
        }
        else
        {

            ContentValues contentValues = new ContentValues();
            contentValues.put(MyContentProvider.COL_TICKER, pt_ticker.getText().toString().trim());
            contentValues.put(MyContentProvider.COL_COMPANY, pt_CompanyName.getText().toString().trim());
            contentValues.put(MyContentProvider.COL_TRANSTYPE, tempBtn.getText().toString().trim());
            contentValues.put(MyContentProvider.COL_PPS, pps.getText().toString().trim());
            contentValues.put(MyContentProvider.COL_TRANSAMOUNT, pt_amnt.getText().toString().trim());
            contentValues.put(MyContentProvider.COL_ORDERTYPE, spinSelection.trim());
            contentValues.put(MyContentProvider.COL_CONFCODE, cc.getText().toString().trim());

            getContentResolver().insert(MyContentProvider.CONTENT_URI, contentValues);

            Toast.makeText(this, "Information stored in database.", Toast.LENGTH_SHORT).show();
            resetValues(view);
        }

    }

/*
// credit to: @lib4 on S.O, post found here: https://stackoverflow.com/questions/48753337/android-edittext-two-decimal-places
class DecimalDigitsInputFilter implements InputFilter {

    Pattern mPattern;

    public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
        mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher=mPattern.matcher(dest);
        if(!matcher.matches())
            return "";
        return null;
    }

}
 */
}

class DecimalDigitsInputFilter implements InputFilter {

    Pattern mPattern;

    public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
        mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher=mPattern.matcher(dest);
        if(!matcher.matches())
            return "";
        return null;
    }

}