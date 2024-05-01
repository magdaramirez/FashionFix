package mx.edu.potros.fashionfix

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registrar : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellidoPaterno: EditText
    private lateinit var etApellidoMaterno: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmarPassword: EditText
    private lateinit var btnRegistrar: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios")

        etNombre = findViewById(R.id.etNombre)
        etApellidoPaterno = findViewById(R.id.etApellidoPaterno)
        etApellidoMaterno = findViewById(R.id.etApellidoMaterno)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val nombre = etNombre.text.toString().trim()
        val apellidoPaterno = etApellidoPaterno.text.toString().trim()
        val apellidoMaterno = etApellidoMaterno.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmarPassword = etConfirmarPassword.text.toString().trim()

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellidoPaterno) || TextUtils.isEmpty(apellidoMaterno) ||
            TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmarPassword)) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
        } else if (password != confirmarPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = mAuth.currentUser!!.uid
                        val currentUserDB = mDatabase.child(userId)
                        currentUserDB.child("nombre").setValue(nombre)
                        currentUserDB.child("apellidoPaterno").setValue(apellidoPaterno)
                        currentUserDB.child("apellidoMaterno").setValue(apellidoMaterno)
                        currentUserDB.child("email").setValue(email)

                        Toast.makeText(this@Registrar, "Registro exitoso.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@Registrar, Ingresar::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@Registrar, "Error al registrar. Por favor, inténtelo de nuevo.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
