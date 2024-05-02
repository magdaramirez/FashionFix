package mx.edu.potros.fashionfix

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Recomendaciones : AppCompatActivity() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val usuarioId = currentUser?.uid
    private val db = FirebaseFirestore.getInstance()

    private lateinit var topImageView: ImageView
    private lateinit var bottomImageView: ImageView
    private lateinit var shoesImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones)

        // Obtener referencias a las vistas de imágenes
        topImageView = findViewById(R.id.ivTop)
        bottomImageView = findViewById(R.id.ivBottom)
        shoesImageView = findViewById(R.id.ivShoes)

        val generar: ImageButton = findViewById(R.id.btnShuffle)
        val recomendacionButton: ImageButton = findViewById(R.id.btnRecomendaciones)
        val favoritosButton: ImageButton = findViewById(R.id.btnFavoritosMenu)
        val historialButton: ImageButton = findViewById(R.id.btnHistorial)
        val closetButton: ImageButton = findViewById(R.id.btnCloset)
        val agregarButton: ImageButton = findViewById(R.id.btnAgregarPrenda)

        generar.setOnClickListener {
            generarOutfit()
        }

        recomendacionButton.setOnClickListener {
            startActivity(Intent(this, Recomendaciones::class.java))
        }

        favoritosButton.setOnClickListener {
            startActivity(Intent(this, Favoritos::class.java))
        }

        historialButton.setOnClickListener {
            startActivity(Intent(this, Historial::class.java))
        }

        closetButton.setOnClickListener {
            startActivity(Intent(this, Closet::class.java))
        }

        agregarButton.setOnClickListener {
            startActivity(Intent(this, AgregarPrenda::class.java))
        }
    }

    private fun generarOutfit() {
        obtenerPrendaAleatoria("Top") { top ->
            obtenerPrendaAleatoria("Bottom") { bottom ->
                obtenerPrendaAleatoria("Shoes") { shoes ->
                    cargarImagen(top.imagen) { topImage ->
                        cargarImagen(bottom.imagen) { bottomImage ->
                            cargarImagen(shoes.imagen) { shoesImage ->
                                // Actualizar las imágenes en las vistas correspondientes
                                runOnUiThread {
                                    topImageView.setImageDrawable(topImage)
                                    bottomImageView.setImageDrawable(bottomImage)
                                    shoesImageView.setImageDrawable(shoesImage)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun obtenerPrendaAleatoria(tipo: String, callback: (Prenda) -> Unit) {
        if (usuarioId != null) {
            val prendasRef = db.collection("prendas")
                .whereEqualTo("tipo", tipo)
                .whereEqualTo("usuarioId", usuarioId)

            prendasRef.get()
                .addOnSuccessListener { result ->
                    val listaPrendas = ArrayList<Prenda>()

                    for (document in result) {
                        val id = document.id
                        val imageUrl = document.getString("imageUrl")
                        val marca = document.getString("marca")
                        val tipo = document.getString("tipo")
                        val color = document.getString("color")
                        val talla = document.getString("talla")

                        val prenda = Prenda(id, imageUrl, tipo, talla, marca, color)
                        listaPrendas.add(prenda)
                    }

                    if (listaPrendas.isNotEmpty()) {
                        val prendaAleatoria = listaPrendas.random()
                        callback(prendaAleatoria)
                    } else {
                        Log.d(TAG, "No se encontraron prendas de tipo $tipo")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error al obtener las prendas de tipo $tipo", exception)
                }
        }
    }

    private fun cargarImagen(imageUrl: String?, callback: (Drawable) -> Unit) {
        Glide.with(this)
            .load(imageUrl)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    callback(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    companion object {
        private const val TAG = "Recomendaciones"
    }
}
