package com.example.mymobileapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    Toolbar myToolbar;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Electricity Bill Application");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                if (item.getItemId() == R.id.item_about)
                {
                    Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(aboutIntent);
                    return true;
                }

                else if (item.getItemId() == R.id.item_share)
                {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Here is my source code: \n \n https://github.com/ajimnasib/electricity-bill-calculator\n" +
                            "\n" +
                            "\n" +
                            "*Developer Information:*\n" +
                            "Name: Azim Nasib\n" +
                            "Student Number: 2023184619\n" +
                            "Programme: RCDCS240\n" +
                            "Phone Number: 0134445160\n" +
                            "Email: 2023184619@student.uitm.edu.my");
                    startActivity(Intent.createChooser(shareIntent, null));
                    return true;
                }
                else if (item.getItemId() == R.id.item_instruction)
                {
                    Intent instructionIntent = new Intent(MainActivity.this, InstructionPage.class);
                    startActivity(instructionIntent);
                    return true;
                }
                else if (item.getItemId() == R.id.item_calculatorbill)
                {
                    Intent calculatorIntent = new Intent(MainActivity.this, CalculatorActivity.class);
                    startActivity(calculatorIntent);
                    return true;
                }

                else
                {
                    return false;
                }
            }
        });
    }
}