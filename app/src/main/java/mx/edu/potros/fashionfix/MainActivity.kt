package mx.edu.potros.fashionfix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnIniciar: Button = findViewById(R.id.btnIniciar)
        val btnCrearCuenta: Button = findViewById(R.id.btnCrearCuenta)

        btnIniciar.setOnClickListener {
            var intent: Intent = Intent(this, Ingresar::class.java)
            startActivity(intent)
        }

        btnCrearCuenta.setOnClickListener {
            var intent: Intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }
    }
}