package com.luke.caseappmatricula.Util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Lucas on 18/11/2017.
 */

public class ConfigFirebase {

    private static FirebaseAuth auth;
    private static FirebaseUser user;
    private static DatabaseReference ref;
    private static StorageReference storage;

    public static FirebaseAuth getFirebaseNAuth(){

            auth = FirebaseAuth.getInstance();

        return auth;
    }

    public static FirebaseUser getFirebaseCurrentUser(){
        user = getFirebaseNAuth().getCurrentUser();
        return user;
    }

    public static DatabaseReference getFirebaseRef(String referencia){
        ref = FirebaseDatabase.getInstance().getReference(referencia);
        return ref;
    }
}
