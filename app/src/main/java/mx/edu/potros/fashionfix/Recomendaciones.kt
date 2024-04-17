package mx.edu.potros.fashionfix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Recomendaciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones)

        val recomendacionButton: ImageButton = findViewById(R.id.btnRecomendacion)
        val favoritosButton: ImageButton = findViewById(R.id.btnFavoritos)
        val historialButton: ImageButton = findViewById(R.id.btnHistorial)
        val closetButton: ImageButton = findViewById(R.id.btnCloset)
        val agregarButton: ImageButton = findViewById(R.id.btnAgregarPrenda)

        recomendacionButton.setOnClickListener {
            var intent: Intent = Intent(this, Recomendaciones::class.java)
            startActivity(intent)
        }

        favoritosButton.setOnClickListener {
            var intent: Intent = Intent(this, Favoritos::class.java)
            startActivity(intent)
        }

        historialButton.setOnClickListener {
            var intent: Intent = Intent(this, Historial::class.java)
            startActivity(intent)
        }

        closetButton.setOnClickListener {
            var intent: Intent = Intent(this, Closet::class.java)
            startActivity(intent)
        }

        agregarButton.setOnClickListener {
            var intent: Intent = Intent(this, AgregarPrenda::class.java)
            startActivity(intent)
        }
    }
}