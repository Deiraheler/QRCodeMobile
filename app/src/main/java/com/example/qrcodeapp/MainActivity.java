package com.example.qrcodeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final int SCAN_REQUEST_CODE = 123; // Request code for scanning
    private TextView resultTextView;
    private Button scanButton;
    private EditText titleEditText;
    private EditText urlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleEditText = findViewById(R.id.titleEditText);
        urlEditText = findViewById(R.id.urlEditText);

        scanButton = findViewById(R.id.scanButton);

        // Set up the button click listener
        scanButton.setOnClickListener(v -> {
            // Launch the scanner
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent, SCAN_REQUEST_CODE);
        });

        // Set up the EditText click listener to open the browser
        urlEditText.setOnClickListener(v -> {
            String url = urlEditText.getText().toString();
            if (!url.isEmpty()) {
                // Create an intent to open the URL in the browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                // Get the scan result
                String scannedData = data.getStringExtra("SCAN_RESULT");
                try {
                    // Parse the scanned data as JSON
                    JSONObject jsonObject = new JSONObject(scannedData);

                    // Extract title and website fields
                    String title = jsonObject.getString("title");
                    String website = jsonObject.getString("website");

                    // Set the extracted data in the EditText views
                    titleEditText.setText(title);
                    urlEditText.setText(website);
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }
            }
        }
    }
}
