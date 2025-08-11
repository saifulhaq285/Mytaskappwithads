package com.example.mytask.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().reference

    private val _authStatus = MutableStateFlow<String?>(null)
    val authStatus = _authStatus.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _userData = MutableStateFlow<Pair<String, String>?>(null)
    val userData = _userData.asStateFlow()

    fun register(name: String, email: String, password: String) {
        _loading.value = true
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    val userMap = mapOf("name" to name, "email" to email)
                    dbRef.child("users").child(uid).setValue(userMap)
                        .addOnSuccessListener {
                            _authStatus.value = "success"
                        }
                        .addOnFailureListener {
                            _authStatus.value = it.message
                        }
                        .addOnCompleteListener {
                            _loading.value = false
                        }
                } else {
                    _authStatus.value = "User ID is null"
                    _loading.value = false
                }
            } else {
                _authStatus.value = task.exception?.message
                _loading.value = false
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        _loading.value = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            _loading.value = false
            if (task.isSuccessful) {
                _authStatus.value = "success"
                loadUserData()
                onResult(true)
            } else {
                _authStatus.value = task.exception?.message
                onResult(false)
            }
        }
    }

    fun loadUserData() {
        val uid = auth.currentUser?.uid ?: return
        dbRef.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java) ?: ""
                val email = snapshot.child("email").getValue(String::class.java) ?: ""
                _userData.value = name to email
            }

            override fun onCancelled(error: DatabaseError) {
                _userData.value = null
            }
        })
    }

    fun logout() {
        auth.signOut()
        _userData.value = null
    }

    fun clearStatus() {
        _authStatus.value = null
    }
}
