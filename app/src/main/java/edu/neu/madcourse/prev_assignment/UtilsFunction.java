package edu.neu.madcourse.prev_assignment;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class UtilsFunction {

    // get Id From Firebase
    public static long getIdFromFirebase(String username){
        long[] id = {0};
        int a = 0;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users");
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    System.out.println("dataSnapshot1: " + id[0]);
                    if(userSnapshot.child("username").getValue().equals(username)){
                        id[0] = (long) userSnapshot.child("id").getValue();
                    }
                }

            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError){

            }
        });
        System.out.println("dataSnapshot2: " + id[0]);
        return id[0];
    }

    // check Username Exists Or Not
    public static boolean checkUserExistsOrNot(String username){
        boolean[] checked = {true};
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users");
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if(userSnapshot.child("username").getValue().equals(username)){
                        checked[0] = false;
                    }
                }

            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError){

            }
        });
        return checked[0];
    }
}
