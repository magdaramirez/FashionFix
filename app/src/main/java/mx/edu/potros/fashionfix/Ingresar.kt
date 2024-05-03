package mx.edu.potros.fashionfix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import android.widget.EditText
import mx.edu.potros.fashionfix.databinding.ActivityIngresarBinding

class Ingresar : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityIngresarBinding
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        binding = ActivityIngresarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnIniciar.setOnClickListener{
            val mEmail = binding.etEmail.text.toString()
            val mPassword = binding.etPassword.text.toString()

            when {
                mEmail.isEmpty() || mPassword.isEmpty() -> {
                    Toast.makeText(baseContext,"Datos incorrectos.", Toast.LENGTH_SHORT).show()
                    etEmail.text.clear()
                    etPassword.text.clear()
                }
                else -> {
                    ingresar(mEmail, mPassword)
                }
            }
        }
    }

    private fun ingresar(email:String,password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    reload()
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun reload(){
        val intent = Intent(this, Closet::class.java)
        startActivity(intent)
    }
}