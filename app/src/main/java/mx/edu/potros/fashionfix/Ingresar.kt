package mx.edu.potros.fashionfix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Ingresar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresar)

        val button: Button = findViewById(R.id.btnIngresar)

        button.setOnClickListener {
            var intent: Intent = Intent(this, Closet::class.java)
            startActivity(intent)
        }
    }
}