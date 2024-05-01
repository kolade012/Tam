package com.example.tam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserHome extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private LinearLayout linearLayoutTimetableEntries;
    private List<Entry> entryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        databaseReference = FirebaseDatabase.getInstance().getReference("entries");
        linearLayoutTimetableEntries = findViewById(R.id.linearLayoutTimetableEntries);

        entryList = new ArrayList<>();

        retrieveTimetableEntries();

        Button buttonSearchCourses = findViewById(R.id.buttonSearchCourses);
        Button buttonViewPrivateTimetable = findViewById(R.id.buttonViewPrivateTimetable);

        buttonSearchCourses.setOnClickListener(v -> startActivity(new Intent(UserHome.this, Search.class)));

        buttonViewPrivateTimetable.setOnClickListener(v -> startActivity(new Intent(UserHome.this, PrivateTimetable.class)));
    }

    private void retrieveTimetableEntries() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                entryList.clear();
                linearLayoutTimetableEntries.removeAllViews();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Entry entry = snapshot.getValue(Entry.class);
                    if (entry != null) {
                        entry.setKey(snapshot.getKey());
                        entryList.add(entry);
                    }
                }
                Collections.sort(entryList);

                for (Entry entry : entryList) {
                    View entryView = LayoutInflater.from(UserHome.this).inflate(R.layout.timetable_entry_layout, null);
                    populateEntryView(entryView, entry);
                    linearLayoutTimetableEntries.addView(entryView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserHome.this, "Failed to retrieve entries: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateEntryView(View view, Entry entry) {
        TextView textViewCourseName = view.findViewById(R.id.textViewCourseName);
        TextView textViewVenue = view.findViewById(R.id.textViewVenue);
        TextView textViewTime = view.findViewById(R.id.textViewTime);
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        Switch switcher = view.findViewById(R.id.switch1);

        textViewCourseName.setText("Course Name: " + entry.getCourseCode());
        textViewVenue.setText("Venue: " + entry.getVenue());
        textViewTime.setText("Time: " + entry.getTime());
        textViewDate.setText("Date: " + entry.getDate());

        switcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addEntryToPrivateTimetable(entry);
            } else {
                //deleteEntryFromPrivateTimetable(entry);
            }
        });
    }

    private void addEntryToPrivateTimetable(Entry entry) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userPrivateTimetableRef = FirebaseDatabase.getInstance()
                    .getReference("privateTimetables").child(userId);
            userPrivateTimetableRef.push().setValue(entry)
                    .addOnSuccessListener(aVoid -> Toast.makeText(UserHome.this, "Entry added to private timetable", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(UserHome.this, "Failed to add entry: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(UserHome.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

//    private void deleteEntryFromPrivateTimetable(Entry entry) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            DatabaseReference userPrivateTimetableRef = FirebaseDatabase.getInstance()
//                    .getReference("privateTimetables").child(userId).child(entry.getKey());
//            userPrivateTimetableRef.removeValue()
//                    .addOnSuccessListener(aVoid -> Toast.makeText(UserHome.this, "Entry removed from private timetable", Toast.LENGTH_SHORT).show())
//                    .addOnFailureListener(e -> Toast.makeText(UserHome.this, "Failed to remove entry: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//        } else {
//            Toast.makeText(UserHome.this, "User not authenticated", Toast.LENGTH_SHORT).show();
//        }
//    }
}
