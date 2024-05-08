package mx.edu.potros.fashionfix

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.hashMapOf as hashMapOf1

class Recomendaciones : AppCompatActivity() {

    private var outfitFavoritoId: String? = null // ID del outfit favorito, si existe

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val usuarioId = currentUser?.uid
    private val db = FirebaseFirestore.getInstance()

    private lateinit var topImageView: ImageView
    private lateinit var bottomImageView: ImageView
    private lateinit var shoesImageView: ImageView

    private var top: Prenda? = null
    private var bottom: Prenda? = null
    private var shoes: Prenda? = null

    private var favorito = false



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
        val usuarioButton: ImageButton = findViewById(R.id.btnUsuario)



        val btnFav: ImageButton = findViewById(R.id.btnFav)
        val btnUsar: Button = findViewById(R.id.btnUsar)
        generarOutfit()

        verificarExistenciaPrendas { prendasEncontradas->
            if (prendasEncontradas){
                Log.d("TEST","Si Existen 3 Prendas de cada Una")
            }else{
                Log.d("TEST","No Existen 3 Prendas de cada Una")
            }
        }

        btnFav.setOnClickListener {
            verificarExistenciaPrendas { prendasEncontradas ->
                if (prendasEncontradas) {
                    if (favorito) {
                        eliminarOutfitFavorito()
                    } else {
                        agregarOutfitFavorito()
                    }
                } else {
                    Toast.makeText(this, "Registra Minimo 3 Prendas", Toast.LENGTH_SHORT).show()
                }
            }
        }



        btnUsar.setOnClickListener(){
            verificarExistenciaPrendas { prendasEncontradas->
                if (prendasEncontradas){
                    OutfitUsado()
                }else{
                    Toast.makeText(this, "Registra Minimo 3 Prendas", Toast.LENGTH_SHORT).show()
                }
            }

        }

        generar.setOnClickListener {
            generarOutfit()
            setNormalIcon(btnFav)
        }

        usuarioButton.setOnClickListener {
            var intent: Intent = Intent(this, User::class.java)
            startActivity(intent)
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

    private fun setFavoriteIcon(btn: ImageButton) {
        val id = R.drawable.favorito_fill
        btn.setImageResource(id)
    }

    private fun setNormalIcon(btn: ImageButton) {
        val id = R.drawable.favorito_empty
        btn.setImageResource(id)
    }

    private fun generarOutfit() {
        obtenerPrendaAleatoria("Top") { top ->
            this.top = top
            obtenerPrendaAleatoria("Bottom") { bottom ->
                this.bottom = bottom
                obtenerPrendaAleatoria("Shoes") { shoes ->
                    this.shoes = shoes
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

    private fun agregarOutfitFavorito() {
        // Verifica si el outfit ya está marcado como favorito
        if (outfitFavoritoId != null) {
            Toast.makeText(this, "Este outfit ya está marcado como favorito", Toast.LENGTH_SHORT).show()
            return
        }

        // Crea un nuevo outfit favorito
        val btnFav: ImageButton = findViewById(R.id.btnFav)
        val outfit = hashMapOf1(
            "top" to top?.id,
            "bottom" to bottom?.id,
            "shoes" to shoes?.id,
            "favorito" to true,
            "timestamp" to obtenerFechaActual(),
            "usuarioId" to usuarioId
        )

        db.collection("outfits")
            .add(outfit)
            .addOnSuccessListener { documentReference ->
                outfitFavoritoId = documentReference.id
                Toast.makeText(this, "Outfit añadido a favoritos", Toast.LENGTH_SHORT).show()
                setFavoriteIcon(btnFav)
                favorito = true
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al añadir el outfit: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarOutfitFavorito() {
        // Verifica si el outfit ya está marcado como favorito
        if (outfitFavoritoId == null) {
            Toast.makeText(this, "Este outfit no está marcado como favorito", Toast.LENGTH_SHORT).show()
            return
        }

        val btnFav: ImageButton = findViewById(R.id.btnFav)
        // Elimina el outfit favorito
        db.collection("outfits").document(outfitFavoritoId!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Outfit eliminado de favoritos", Toast.LENGTH_SHORT).show()
                setNormalIcon(btnFav)
                favorito = false
                outfitFavoritoId = null
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar el outfit: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun OutfitUsado() {
        val outfit = hashMapOf1(
            "top" to top?.id,
            "bottom" to bottom?.id,
            "shoes" to shoes?.id,
            "usado" to true,
            "usuarioId" to usuarioId,
            "timestamp" to obtenerFechaActual()
        )

        db.collection("outfits")
            .add(outfit)
            .addOnSuccessListener {
                Toast.makeText(this, "Outfit Añadido a Histoial", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al añadir el outfit: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun verificarExistenciaPrendas(callback: (Boolean) -> Unit) {
        val usuarioId = currentUser?.uid
        if (usuarioId != null) {
            val prendasRef = db.collection("prendas").whereEqualTo("usuarioId", usuarioId)
            prendasRef.get()
                .addOnSuccessListener { documents ->
                    var topEncontrado = false
                    var bottomEncontrado = false
                    var shoesEncontrado = false

                    for (document in documents) {
                        val tipoPrenda = document.getString("tipo")
                        if (tipoPrenda != null) {
                            when (tipoPrenda) {
                                "Top" -> topEncontrado = true
                                "Bottom" -> bottomEncontrado = true
                                "Shoes" -> shoesEncontrado = true
                            }
                        }
                    }

                    val todasLasPrendasEncontradas = topEncontrado && bottomEncontrado && shoesEncontrado
                    callback(todasLasPrendasEncontradas)
                }
                .addOnFailureListener { exception ->
                    // Manejar el error
                    callback(false) // Si hay un error, consideramos que no se encontraron todas las prendas
                }
        } else {
            callback(false) // Si el usuario no está autenticado, consideramos que no se encontraron todas las prendas
        }
    }
    private fun obtenerFechaActual():Timestamp {
        val currentDate = Date()
        // Crear un objeto Timestamp con la fecha y hora actual
        val timestamp = Timestamp(currentDate)
        return timestamp
    }

    companion object {
        private const val TAG = "Recomendaciones"
    }
}
