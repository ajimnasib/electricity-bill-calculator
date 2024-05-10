package com.example.mymobileapps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.text.DecimalFormat;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar billToolbar;
    EditText etUnitUsed;
    EditText etRebate;
    Button btnCalc, btnClear;
    TextView tvCost, tvUsed200, tvUsed100, tvUsed300, tvRate200, tvRate100, tvRate300,
            tvTotal200, tvTotal100, tvTotal300, tvTotalUsed, tvTotal, tvTotalRebate, tvTotalCost;

    ImageView info;

    // Declare DecimalFormat object as an instance variable
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // Toolbar setup
        billToolbar = findViewById(R.id.about_toolbar);
        setSupportActionBar(billToolbar);
        getSupportActionBar().setTitle("Electricity Bill Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize the DecimalFormat object
        decimalFormat = new DecimalFormat("#.00");

        // Initialize UI components
        etUnitUsed = findViewById(R.id.etUnitUsed);
        etRebate = findViewById(R.id.etRebate);
        btnCalc = findViewById(R.id.btnCalc);
        btnClear = findViewById(R.id.btnClear);
        tvCost = findViewById(R.id.tvCost);
        tvUsed200 = findViewById(R.id.tvUsed200);
        tvUsed100 = findViewById(R.id.tvUsed100);
        tvUsed300 = findViewById(R.id.tvUsed300);
        tvRate200 = findViewById(R.id.tvRate200);
        tvRate100 = findViewById(R.id.tvRate100);
        tvRate300 = findViewById(R.id.tvRate300);
        tvTotal200 = findViewById(R.id.tvTotal200);
        tvTotal100 = findViewById(R.id.tvTotal100);
        tvTotal300 = findViewById(R.id.tvTotal300);
        tvTotalRebate = findViewById(R.id.tvTotalRebate);
        tvTotalCost = findViewById(R.id.tvTotalCost);
        info = findViewById(R.id.info);

        tvTotalUsed = findViewById(R.id.tvTotalUsed);
        tvTotal = findViewById(R.id.tvTotal);

        // Set listeners for buttons and info image
        btnCalc.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        info.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCalc) {
            try {
                // Get input values
                String unitUsedStr = etUnitUsed.getText().toString();
                String rebateStr = etRebate.getText().toString();

                int unitUsed = Integer.parseInt(unitUsedStr);
                double rebatePercentage = Double.parseDouble(rebateStr);

                // Check if the rebate percentage is within the range of 1-5%
                if (rebatePercentage < 1 || rebatePercentage > 5) {
                    showToast("Please input a valid rebate percentage (0-5%).");
                    return; // Stop execution and do not proceed with calculations
                }

                // Convert the rebate percentage to a decimal value
                double rebate = rebatePercentage / 100;

                // Declare electricity rates
                double first200 = 0.218;
                double next100 = 0.334;
                double next300 = 0.516;
                double moreThan700 = 0.546;

                // Calculate charges and final cost
                double charges = calculateCharges(unitUsed, first200, next100, next300, moreThan700);
                double finalCost = calculateFinalCost(charges, rebate);

                // Display final cost
                tvCost.setText("RM" + decimalFormat.format(finalCost));
                tvTotalCost.setText("Total Cost: RM" + decimalFormat.format(finalCost));

                // Update usage and cost values in the table
                tvUsed200.setText(decimalFormat.format(Math.min(unitUsed, 200)));
                int next100Used = Math.max(Math.min(unitUsed - 200, 100), 0);
                tvUsed100.setText(String.valueOf(next100Used));
                int next300Used = Math.max(Math.min(unitUsed - 300, 300), 0);
                tvUsed300.setText(String.valueOf(next300Used));

                tvRate200.setText(String.valueOf(first200));
                tvRate100.setText(String.valueOf(next100));
                tvRate300.setText(String.valueOf(next300));

                tvTotal200.setText(decimalFormat.format(Math.min(unitUsed, 200) * first200));
                tvTotal100.setText(decimalFormat.format(next100Used * next100));
                tvTotal300.setText(decimalFormat.format(next300Used * next300));

                // Calculate and set total usage and total charges
                int totalUsed = Math.min(unitUsed, 200) + next100Used + next300Used;
                tvTotalUsed.setText(String.valueOf(totalUsed));

                double totalRebate = charges * rebate;
                tvTotalRebate.setText("Total Rebate: RM0" + decimalFormat.format(totalRebate));
                tvTotal.setText(String.valueOf(charges));

            } catch (NumberFormatException nfe) {
                showToast("Please enter valid numerical values.");
            } catch (Exception exp) {
                showToast("An unexpected error occurred. Please try again.");
                Log.e("CalculatorActivity", "Exception: ", exp);
            }
        } else if (v.getId() == R.id.btnClear) {
            // Clear all fields
            etUnitUsed.setText("");
            etRebate.setText("");
            tvCost.setText("Final Cost (RM)");
            tvUsed200.setText("0");
            tvUsed100.setText("0");
            tvUsed300.setText("0");
            tvRate200.setText("0");
            tvRate100.setText("0");
            tvRate300.setText("0");
            tvTotal200.setText("0");
            tvTotal100.setText("0");
            tvTotal300.setText("0");
            tvTotalUsed.setText("0");
            tvTotal.setText("0");
            tvTotalRebate.setText("Total Rebate: RM0.00");
            tvTotalCost.setText("Total Cost: RM0.00");

            showToast("Clear Clicked");
        } else if (v.getId() == R.id.info) {
            // Open the instruction page
            Intent intent = new Intent(CalculatorActivity.this, InstructionPage.class);
            startActivity(intent);
        }
    }

    // Calculate charges based on the given unit usage and rates
    private double calculateCharges(int unitUsed, double rateFirst200, double rateNext100, double rateNext300, double rateMoreThan700) {
        double charges = 0.0;

        if (unitUsed <= 200) {
            charges = unitUsed * rateFirst200;
        } else if (unitUsed <= 300) {
            charges = (200 * rateFirst200) + ((unitUsed - 200) * rateNext100);
        } else if (unitUsed <= 600) {
            charges = (200 * rateFirst200) + (100 * rateNext100) + ((unitUsed - 300) * rateNext300);
        } else {
            charges = (200 * rateFirst200) + (100 * rateNext100) + (300 * rateNext300) + ((unitUsed - 600) * rateMoreThan700);
        }

        // Round charges to two decimal places
        return Double.parseDouble(decimalFormat.format(charges));
    }

    // Calculate the final cost after applying the rebate
    private double calculateFinalCost(double charges, double rebate) {
        double finalCost = charges - (charges * rebate);
        // Round final cost to two decimal places
        return Double.parseDouble(decimalFormat.format(finalCost));
    }

    // Utility method to show a toast message
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}


