package com.example.tam;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PrivateTimetableList {

    private static PrivateTimetableList instance;
    private List<String> privateTimetableKeys; // List to store entry keys in order
    private DatabaseReference userPrivateTimetableRef;
    private FirebaseUser currentUser;

    private PrivateTimetableList() {
        privateTimetableKeys = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userPrivateTimetableRef = FirebaseDatabase.getInstance().getReference().child("privateTimetables").child(userId);
            listenForPrivateTimetableChanges();
        }
    }

    public static synchronized PrivateTimetableList getInstance() {
        if (instance == null) {
            instance = new PrivateTimetableList();
        }
        return instance;
    }

    private void listenForPrivateTimetableChanges() {
        if (userPrivateTimetableRef != null) {
            userPrivateTimetableRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    String entryKey = dataSnapshot.getKey();
                    if (!privateTimetableKeys.contains(entryKey)) {
                        privateTimetableKeys.add(entryKey);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    // Handle change if needed
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String entryKey = dataSnapshot.getKey();
                    privateTimetableKeys.remove(entryKey);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    // Handle move if needed
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    public List<String> getPrivateTimetableKeys() {
        return privateTimetableKeys;
    }

    public void addEntryToPrivateTimetable(String entryKey) {
        if (userPrivateTimetableRef != null) {
            userPrivateTimetableRef.child(entryKey).setValue(true); // Use true as value to indicate presence
        }
    }

    public void removeEntryFromPrivateTimetable(String entryKey) {
        if (userPrivateTimetableRef != null) {
            userPrivateTimetableRef.child(entryKey).removeValue();
        }
    }
}
