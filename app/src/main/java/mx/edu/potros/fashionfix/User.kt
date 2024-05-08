package mx.edu.potros.fashionfix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class User: AppCompatActivity()  {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val currentUser = firebaseAuth.currentUser
    val usuarioId = currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        val recomendacionButton: ImageButton = findViewById(R.id.btnRecomendaciones)
        val favoritosButton: ImageButton = findViewById(R.id.btnFavoritosMenu)
        val historialButton: ImageButton = findViewById(R.id.btnHistorial)
        val closetButton: ImageButton = findViewById(R.id.btnCloset)
        val agregarButton: ImageButton = findViewById(R.id.btnAgregarPrenda)
        val usuarioButton: ImageButton = findViewById(R.id.btnUsuario)

        val btnCerrarSesion: Button = findViewById(R.id.btnSingOut)
        val tv_correo : TextView = findViewById(R.id.tvCuenta2)
        val tv_nroPrendas : TextView = findViewById(R.id.tvnroPrendas2)
        val tv_favOutfits : TextView = findViewById(R.id.tvFavOutfits2)
        val tv_usadosOutfits : TextView = findViewById(R.id.tvOutfitsUsados2)

        tv_correo.setText(obtenerCorreoSesion())
        lifecycleScope.launch {
            val cantidadPrendas = obtenerCantidadPrendas()
            tv_nroPrendas.text = cantidadPrendas.toString()

            val OutfitsFav = obtenerCantidadOutfitsFavs()
            tv_favOutfits.text = OutfitsFav.toString()

            val OutfitsUsados = obtenerCantidadOutfitsUsados()
            tv_usadosOutfits.text = OutfitsUsados.toString()
        }



        btnCerrarSesion.setOnClickListener(){
            cerrarSesion()
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

    private fun cerrarSesion() {
        // Cerrar sesión en Firebase
        firebaseAuth.signOut()
    }

    private fun obtenerCorreoSesion(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }

    private suspend fun obtenerCantidadPrendas(): Int {
        val prendasRef = firebaseFirestore.collection("prendas")
            .whereEqualTo("usuarioId", usuarioId)
        return try {
            val documents = prendasRef.get().await()
            documents.size()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private suspend fun obtenerCantidadOutfitsUsados(): Int {
        val usuarioId = currentUser?.uid
        if (usuarioId != null) {
            val outfitsRef = firebaseFirestore.collection("outfits")
            return try {
                val documents = outfitsRef.whereEqualTo("usuarioId", usuarioId).get().await()
                val cantidadUsados = documents.count { it.getBoolean("usado") == true }
                cantidadUsados
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
        return 0
    }

    private suspend fun obtenerCantidadOutfitsFavs(): Int {
        val usuarioId = currentUser?.uid
        if (usuarioId != null) {
            val outfitsRef = firebaseFirestore.collection("outfits")
            return try {
                val documents = outfitsRef.whereEqualTo("usuarioId", usuarioId).get().await()
                val cantidadFavoritos = documents.count { it.getBoolean("favorito") == true }
                cantidadFavoritos
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
        return 0
    }
}

