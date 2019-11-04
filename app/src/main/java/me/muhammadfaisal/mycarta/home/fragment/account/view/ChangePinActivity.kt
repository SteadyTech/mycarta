package me.muhammadfaisal.mycarta.home.fragment.account.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import me.muhammadfaisal.mycarta.R
import me.muhammadfaisal.mycarta.pin.model.Pin

class ChangePinActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)
        supportActionBar?.hide()

        initWidget()

    }

    private fun initWidget() {
        var changePin = findViewById<EditText>(R.id.inputLatestPin)
        var buttonSaveChanged = findViewById<Button>(R.id.buttonChangePIN)

        var auth = FirebaseAuth.getInstance().currentUser
        var database = FirebaseDatabase.getInstance()
        var reference = database.reference

        buttonSaveChanged.setOnClickListener {
            var pin = Pin(changePin.text.toString().toLong())

            reference.child("pin").child(auth!!.uid).child("-LrlY05r0iyb7nX9QsYj").setValue(pin).addOnSuccessListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
}