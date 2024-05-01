package com.example.tam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrivateTimetable extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EntryAdapter adapter;
    private List<Entry> entries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_timetable);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        entries = new ArrayList<>();
        adapter = new EntryAdapter(entries);
        recyclerView.setAdapter(adapter);

        // Retrieve private timetable entries for the current user
        retrievePrivateTimetableEntries();
    }

    private void retrievePrivateTimetableEntries() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference privateTimetableRef = FirebaseDatabase.getInstance().getReference()
                    .child("privateTimetables").child(userId);

            privateTimetableRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    entries.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Entry entry = snapshot.getValue(Entry.class);
                        if (entry != null) {
                            entry.setKey(snapshot.getKey()); // Set the key for the entry
                            entries.add(entry);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PrivateTimetable.this, "Failed to retrieve entries: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

        private List<Entry> entries;

        public EntryAdapter(List<Entry> entries) {
            this.entries = entries;
        }

        @NonNull
        @Override
        public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_row, parent, false);
            return new EntryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
            Entry entry = entries.get(position);
            holder.bind(entry);
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }

        public class EntryViewHolder extends RecyclerView.ViewHolder {
            TextView textViewCourseCode, textViewVenue, textViewTime, textViewDate;
            Button deleteButton;

            public EntryViewHolder(View itemView) {
                super(itemView);
                textViewCourseCode = itemView.findViewById(R.id.textViewCourseCode);
                textViewVenue = itemView.findViewById(R.id.textViewVenue);
                textViewTime = itemView.findViewById(R.id.textViewTime);
                textViewDate = itemView.findViewById(R.id.textViewDate);
                deleteButton = itemView.findViewById(R.id.deleteButton);

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Entry entry = entries.get(position);
                            deleteEntry(entry);
                        }
                    }
                });
            }

            public void bind(Entry entry) {
                textViewCourseCode.setText(entry.getCourseCode());
                textViewVenue.setText(entry.getVenue());
                textViewTime.setText(entry.getTime());
                textViewDate.setText(entry.getDate());
            }
        }
    }

    private void deleteEntry(Entry entry) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference entryRef = FirebaseDatabase.getInstance().getReference()
                    .child("privateTimetables").child(userId).child(entry.getKey());

            entryRef.removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(PrivateTimetable.this, "Entry deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PrivateTimetable.this, "Failed to delete entry: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(PrivateTimetable.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
