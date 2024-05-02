package mx.edu.potros.fashionfix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class InfoPrenda : AppCompatActivity() {

    // Referencia a Firestore
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_prenda)

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

        val iv_prenda_image: ImageView = findViewById(R.id.imagenPreview)
        val et_prenda_tipo: EditText = findViewById(R.id.tvTipo1)
        val et_prenda_talla: EditText = findViewById(R.id.tvTalla1)
        val et_prenda_marca: EditText = findViewById(R.id.tvMarca1)
        val et_prenda_color: EditText = findViewById(R.id.tvColor1)

        val deleteBtn : ImageView = findViewById(R.id.btnDelete)
        val editBtn : ImageView = findViewById(R.id.btnEdit)

        val bundle = intent.extras

        if (bundle != null) {
            // Obtener la URL de la imagen del intento
            val id = bundle.getString("id")
            val imageUrl = bundle.getString("imagen")
            val tipo = bundle.getString("tipo")
            val talla = bundle.getString("talla")
            val marca = bundle.getString("marca")
            val color = bundle.getString("color")

            Log.d("INF", "URL PRENDA: " + imageUrl)
            Log.d("INF", "TIPO PRENDA: " + tipo)
            Log.d("INF", "TALLA PRENDA: " + talla)
            Log.d("inf", "MARCA PRENDA: " + marca)
            Log.d("inf", "COLOR PRENDA: " + color)


            // Cargar la imagen utilizando Glide desde la URL
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.loadingimg) // Imagen de marcador de posición mientras se carga la imagen
                .error(R.drawable.logo) // Imagen de error si la carga falla
                .into(iv_prenda_image)

            // Establecer otros detalles de la prenda
            et_prenda_tipo.setText(tipo)
            et_prenda_talla.setText(talla)
            et_prenda_marca.setText(marca)
            et_prenda_color.setText(color)
        }

        deleteBtn.setOnClickListener(){
          mostrarDialogoConfirmacionDelete()
        }

        editBtn.setOnClickListener(){
            activarEdicion()
        }


    }

    private fun activarEdicion(){
        val iv_prenda_image: ImageView = findViewById(R.id.imagenPreview)
        val et_prenda_tipo: EditText = findViewById(R.id.tvTipo1)
        val et_prenda_talla: EditText = findViewById(R.id.tvTalla1)
        val et_prenda_marca: EditText = findViewById(R.id.tvMarca1)
        val et_prenda_color: EditText = findViewById(R.id.tvColor1)

        val editBtn : ImageView = findViewById(R.id.btnEdit)
        val confirmBtn : ImageView = findViewById(R.id.btnEditConfirm)
        val cancelBtn : ImageView = findViewById(R.id.btnEditCancel)

        confirmBtn.visibility = View.VISIBLE
        cancelBtn.visibility= View.VISIBLE
        editBtn.visibility = View.INVISIBLE

        // Habilitar la edición de los EditText
        et_prenda_tipo.isEnabled = true
        et_prenda_tipo.isFocusable = true
        et_prenda_tipo.isFocusableInTouchMode = true

        et_prenda_talla.isEnabled = true
        et_prenda_talla.isFocusable = true
        et_prenda_talla.isFocusableInTouchMode = true

        et_prenda_marca.isEnabled = true
        et_prenda_marca.isFocusable = true
        et_prenda_marca.isFocusableInTouchMode = true

        et_prenda_color.isEnabled = true
        et_prenda_color.isFocusable = true
        et_prenda_color.isFocusableInTouchMode = true

        confirmBtn.setOnClickListener(){
            mostrarDialogoConfirmacionEdit()
        }

        cancelBtn.setOnClickListener(){
            val intent = Intent(this, Closet::class.java)
            startActivity(intent)
        }

    }
    private fun mostrarDialogoConfirmacionDelete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Prenda")
        builder.setMessage("¿Estás seguro de que deseas eliminar esta prenda?")

        builder.setPositiveButton("Sí") { dialog, which ->
            // Obtener el ID de la prenda de los extras del intent
            val bundle = intent.extras
            val idPrenda = bundle?.getString("id")

            if (idPrenda != null) {
                // Llamar a la función para eliminar la prenda de Firestore
                eliminarPrenda(idPrenda)
            }
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
    private fun mostrarDialogoConfirmacionEdit() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Modificar Prenda")
        builder.setMessage("¿Quieres confirmar las modificaciones?")

        builder.setPositiveButton("Sí") { dialog, which ->
            // Obtener el ID de la prenda de los extras del intent
            val bundle = intent.extras
            val idPrenda = bundle?.getString("id")

            if (idPrenda != null) {
                // Llamar a la función para eliminar la prenda de Firestore
                val et_prenda_tipo: EditText = findViewById(R.id.tvTipo1)
                val et_prenda_talla: EditText = findViewById(R.id.tvTalla1)
                val et_prenda_marca: EditText = findViewById(R.id.tvMarca1)
                val et_prenda_color: EditText = findViewById(R.id.tvColor1)

                val tipo = et_prenda_tipo.text.toString() // Convertir a String
                val talla = et_prenda_talla.text.toString() // Convertir a String
                val marca = et_prenda_marca.text.toString() // Convertir a String
                val color = et_prenda_color.text.toString() // Convertir a String
                val image = bundle.getString("imagen")

                val prendaMap = HashMap<String, Any?>()
                prendaMap["tipo"] = tipo
                prendaMap["talla"] = talla
                prendaMap["marca"] = marca
                prendaMap["color"] = color
                prendaMap["imageUrl"] = image

                // Ahora pasamos prendaMap a Firestore
                editarPrenda(idPrenda, prendaMap.toMutableMap(), // Convertir a MutableMap
                    onSuccess = {
                        // Manejar el éxito de la edición de la prenda, por ejemplo, mostrar un mensaje
                        Log.d("InfoPrenda", "¡La prenda se editó correctamente!")
                    }
                ) { errorMessage ->
                    // Manejar el error de la edición de la prenda, por ejemplo, mostrar un mensaje de error
                    Log.e("InfoPrenda", "Error al editar la prenda: $errorMessage")
                }
            }
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }


    // Función para eliminar una prenda de Firestore
    private fun eliminarPrenda(idPrenda: String) {
        // Referencia al documento de la prenda en Firestore
        val prendaRef = db.collection("prendas").document(idPrenda)

        // Eliminar el documento de la prenda
        prendaRef.delete()
            .addOnSuccessListener {
                val intent = Intent(this, Closet::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->

            }
    }

    // Función para modificar los campos de un documento en Firestore
    fun editarPrenda(documentoId: String, nuevosValores: MutableMap<String, Any?>, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // Referencia a la colección "prendas" en Firestore
        val db = FirebaseFirestore.getInstance()
        val prendasRef = db.collection("prendas")

        // Obtener una referencia al documento que deseas modificar
        val prendaDocRef = prendasRef.document(documentoId)

        // Actualizar los campos del documento con los nuevos valores
        prendaDocRef.update(nuevosValores)
            .addOnSuccessListener {
                // Éxito al actualizar el documento
                val intent = Intent(this, Closet::class.java)
                startActivity(intent)
                onSuccess()
            }
            .addOnFailureListener { e ->
                // Error al actualizar el documento
                onError(e.message ?: "Error al modificar la prenda en Firestore")
            }
    }


}
