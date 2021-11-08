package edu.neu.madcourse.team_j_sport;

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
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users");
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
//                System.out.println("dataSnapshot: " + dataSnapshot.getValue());
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if(userSnapshot.child("username").getValue().equals(username)){
                        System.out.println("dataSnapshot: " + userSnapshot.child("id").getValue());
                        id[0] = (long) userSnapshot.child("id").getValue();
                    }
                }

            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError){

            }
        });
        return id[0];
    }
}
