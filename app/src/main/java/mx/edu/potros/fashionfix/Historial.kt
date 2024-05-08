package mx.edu.potros.fashionfix

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.firestore.Query
import kotlin.math.log


class Historial : AppCompatActivity() {

    val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val recomendacionButton: ImageButton = findViewById(R.id.btnRecomendaciones)
        val favoritosButton: ImageButton = findViewById(R.id.btnFavoritosMenu)
        val historialButton: ImageButton = findViewById(R.id.btnHistorial)
        val closetButton: ImageButton = findViewById(R.id.btnCloset)
        val agregarButton: ImageButton = findViewById(R.id.btnAgregarPrenda)
        val usuarioButton: ImageButton = findViewById(R.id.btnUsuario)

        usuarioButton.setOnClickListener {
            var intent: Intent = Intent(this, User::class.java)
            startActivity(intent)
        }


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
        obtenerOutfitsDesdeFirestore { outfitsList ->
            if(outfitsList.isEmpty()){
                Log.d("ERROR", "No se encontraron los outfits: ")
            }else{
                imprimirListaOutfits(outfitsList)
            }
        }

        // Llamada a la función para obtener los outfits desde Firestore
        obtenerOutfitsDesdeFirestore { outfitsList ->
            val recyclerView: RecyclerView = findViewById(R.id.recyclerViewOutfits)
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            val adapter = OutfitAdapter(outfitsList)
            recyclerView.adapter = adapter
        }
    }

    // Método para obtener la lista de outfits de ejemplo
    private fun obtenerOutfitsDeEjemplo(): List<Outfit> {
        val outfits = mutableListOf<Outfit>()


        return outfits
    }
    private fun obtenerOutfitsDesdeFirestore(callback: (List<Outfit>) -> Unit) {
        val outfitsRef = db.collection("outfits")
        val usuarioId = currentUser?.uid

        if (usuarioId != null) {
            outfitsRef.whereEqualTo("usuarioId", usuarioId)
                .whereEqualTo("usado", true)
                //.orderBy("timestamp",Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    val listaOutfits = mutableListOf<Outfit>()
                    var outfitsProcesados = 0 // Contador para rastrear el número de outfits procesados

                    for (document in documents) {
                        val topId = document.getString("top")
                        val bottomId = document.getString("bottom")
                        val shoesId = document.getString("shoes")

                        if (topId != null && bottomId != null && shoesId != null) {
                            obtenerUrlImagenPrenda(topId) { topImageUrl ->
                                obtenerUrlImagenPrenda(bottomId) { bottomImageUrl ->
                                    obtenerUrlImagenPrenda(shoesId) { shoesImageUrl ->
                                        val outfit = Outfit(topImageUrl, bottomImageUrl, shoesImageUrl)
                                        listaOutfits.add(outfit)

                                        // Incrementar el contador de outfits procesados
                                        outfitsProcesados++

                                        // Si se han procesado todos los outfits, llamar al callback con la lista obtenida
                                        if (outfitsProcesados == documents.size()) {
                                            callback(listaOutfits)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar el error
                }
        }
    }


    private fun obtenerUrlImagenPrenda(prendaId: String, callback: (String?) -> Unit) {
        val prendaRef = db.collection("prendas").document(prendaId)
        prendaRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val imageUrl = document.getString("imageUrl")
                    callback(imageUrl)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el error
                callback(null)
            }
    }





    private fun imprimirListaOutfits(outfitsList: List<Outfit>) {
        for ((index, outfit) in outfitsList.withIndex()) {
            Log.d("Outfit", "Outfit $index:")
            Log.d("Outfit", "Top: ${outfit.top}")
            Log.d("Outfit", "Bottom: ${outfit.bottom}")
            Log.d("Outfit", "Shoes: ${outfit.shoes}")
            Log.d("Outfit", "")
        }
    }



}