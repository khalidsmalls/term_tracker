package com.smalls.termtracker.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.smalls.termtracker.R;


public class MainActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn_view_terms);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                assert data != null;
                                Toast.makeText(getApplication(), data.describeContents(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "no worky", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

        btn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), TermListActivity.class);
            launcher.launch(i);
        });
    }


}