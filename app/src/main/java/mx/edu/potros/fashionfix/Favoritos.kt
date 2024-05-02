package mx.edu.potros.fashionfix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Favoritos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        val recomendacionButton: ImageButton = findViewById(R.id.btnRecomendaciones)
        val favoritosButton: ImageButton = findViewById(R.id.btnFavoritosMenu)
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

        // Obtener la lista de outfits predefinidos
        val outfitsList = obtenerOutfitsDeEjemplo()

        // Configurar el RecyclerView con la lista de outfits
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewOutfits)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = OutfitAdapter(outfitsList)
        recyclerView.adapter = adapter
    }

    // Método para obtener la lista de outfits de ejemplo
    private fun obtenerOutfitsDeEjemplo(): List<Outfit> {
        val outfits = mutableListOf<Outfit>()

        return outfits
    }
}