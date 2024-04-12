package com.example.tam;

import android.content.Intent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminHome extends AppCompatActivity {

    private Spinner spinner;
    private EditText editTextDate, editTextCourseCode, editTextVenue;
    private Button saveButton, view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        String[] options = {"8:30am", "12:00pm", "3:30pm"};

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        editTextDate = findViewById(R.id.editTextDate);
        editTextCourseCode = findViewById(R.id.editTextText8);
        editTextVenue = findViewById(R.id.editTextText9);
        saveButton = findViewById(R.id.button12);
        view= findViewById(R.id.button13);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this,AdminView.class));
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = options[position];
                // Display a toast message with the selected option
                Toast.makeText(AdminHome.this, "Selected option: " + selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void saveData() {
        String selectedTime = spinner.getSelectedItem().toString();
        String selectedDate = editTextDate.getText().toString();
        String courseCode = editTextCourseCode.getText().toString();
        String venue = editTextVenue.getText().toString();

        // Perform validation
        if (TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(courseCode) || TextUtils.isEmpty(venue)) {
            // Display an error message if any field is empty
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        } else {
            // Validate date format (dd/mm/yyyy)
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateFormat.setLenient(false); // To ensure strict date parsing
            try {
                Date date = dateFormat.parse(selectedDate);

                // Check if the parsed date is not in the past
                Calendar currentDate = Calendar.getInstance();
                currentDate.set(Calendar.HOUR_OF_DAY, 0);
                currentDate.set(Calendar.MINUTE, 0);
                currentDate.set(Calendar.SECOND, 0);
                currentDate.set(Calendar.MILLISECOND, 0);

                if (date.before(currentDate.getTime())) {
                    // Display an error message if the date is in the past
                    Toast.makeText(this, "Please select a future date", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save data to Firebase
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("entries");

                // Create a unique key for the entry
                String entryKey = myRef.push().getKey();

                // Create an Entry object with the data
                Entry entry = new Entry(selectedTime, selectedDate, courseCode, venue);

                // Save the entry to Firebase under the unique key
                myRef.child(entryKey).setValue(entry);

                // Display a success message
                Toast.makeText(this, "Entry saved successfully", Toast.LENGTH_SHORT).show();

                // Clear the input fields
                editTextDate.setText("");
                editTextCourseCode.setText("");
                editTextVenue.setText("");

            } catch (ParseException e) {
                // Display an error message if the date format is incorrect
                Toast.makeText(this, "Incorrect date format. Please use dd/mm/yyyy", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
