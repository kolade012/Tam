package com.example.tam;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EntryAdapter adapter;
    private List<Entry> entryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this); // Enable edge-to-edge mode
        setContentView(R.layout.activity_admin_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        entryList = new ArrayList<>();
        adapter = new EntryAdapter(entryList);
        recyclerView.setAdapter(adapter);

        loadEntries();
    }

    private void loadEntries() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("entries");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                entryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Entry entry = snapshot.getValue(Entry.class);
                    entry.setKey(snapshot.getKey());
                    entryList.add(entry);
                }
                Collections.sort(entryList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminView.this, "Failed to load entries.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

        private List<Entry> entryList;
        private Context context;

        public EntryAdapter(List<Entry> entryList) {
            this.entryList = entryList;
        }

        @NonNull
        @Override
        public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_row, parent, false);
            return new EntryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
            Entry entry = entryList.get(position);
            holder.textViewCourseCode.setText(entry.getCourseCode());
            holder.textViewVenue.setText(entry.getVenue());
            holder.textViewTime.setText(entry.getTime());
            holder.textViewDate.setText(entry.getDate());

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform edit action with the selected entry
                    Entry selectedEntry = entryList.get(holder.getAdapterPosition());
                    editEntry(selectedEntry, v.getContext());
                }
            });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform delete action with the selected entry
                    Entry selectedEntry = entryList.get(holder.getAdapterPosition());
                    deleteEntry(selectedEntry);
                }
            });
        }

        @Override
        public int getItemCount() {
            return entryList.size();
        }

        public class EntryViewHolder extends RecyclerView.ViewHolder {
            TextView textViewCourseCode, textViewVenue, textViewTime, textViewDate;
            Button editButton, deleteButton;

            public EntryViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewCourseCode = itemView.findViewById(R.id.textViewCourseCode);
                textViewVenue = itemView.findViewById(R.id.textViewVenue);
                textViewTime = itemView.findViewById(R.id.textViewTime);
                textViewDate = itemView.findViewById(R.id.textViewDate);
                editButton = itemView.findViewById(R.id.editButton);
                deleteButton = itemView.findViewById(R.id.deleteButton);
            }
        }
        // Method to edit an entry
        private void editEntry(Entry entry, Context context) {
            // Implement edit functionality, for example:
            // You can pass the entry details to an edit activity or fragment
            // Or you can show a dialog for editing
            // Here, let's show a toast message with the course code of the selected entry
            Toast.makeText(context, "Editing entry: " + entry.getCourseCode(), Toast.LENGTH_SHORT).show();
        }

        // Method to delete an entry
        private void deleteEntry(Entry entry) {
            // Implement delete functionality using Firebase CRUD operations
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("entries").child(entry.getKey());
            databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Entry deleted successfully
                    // You may want to update the RecyclerView after deletion
                    entryList.remove(entry);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Entry deleted successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Failed to delete entry
                    Toast.makeText(context, "Failed to delete entry", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
