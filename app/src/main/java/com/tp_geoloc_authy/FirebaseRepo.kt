package com.tp_geoloc_authy

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirebaseRepo {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getUser(): FirebaseUser?{
        return firebaseAuth.currentUser
    }

    fun createUser(): Task<AuthResult> {
        return firebaseAuth.signInAnonymously()
    }

    fun getLocationsList(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("locations").get()
    }
}