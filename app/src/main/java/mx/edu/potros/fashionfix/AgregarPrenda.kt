package mx.edu.potros.fashionfix

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import kotlin.math.log


class AgregarPrenda : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val usuarioId = currentUser?.uid

//BACK Up
    private var imageUrl: String = "" // Declaración de imageUrl como variable global
    companion object {
        private const val REQUEST_CODE_IMAGE_PICK = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_prenda)

        val spinnerTipo: Spinner = findViewById(R.id.spTipo)
        val spinnerTalla: Spinner = findViewById(R.id.spTalla)

        // Define los datos a mostrar en el Spinner
        val opcionesTipo = arrayOf("Top", "Bottom", "Shoes")
        val opcionesTalla = arrayOf("XS", "S", "M", "L", "XL")

        // Crea un ArrayAdapter utilizando el array de opciones y un layout simple
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesTipo)
        val adapterTalla = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesTalla)

        // Especifica el layout a usar cuando se despliega la lista de opciones
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterTalla.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asigna el adaptador al Spinner
        spinnerTipo.adapter = adapterTipo
        spinnerTalla.adapter = adapterTalla

        // Maneja la selección del usuario
        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()

                // Verifica si se ha seleccionado "Shoes"
                if (selectedItem == "Shoes") {
                    // Define las nuevas opciones de talla para "Shoes"
                    val nuevasOpcionesTalla = ArrayList<String>()
                    for (i in 21..39) {
                        nuevasOpcionesTalla.add(i.toString())
                    }

                    // Crea un nuevo adaptador con las nuevas opciones de talla
                    val nuevoAdapterTalla = ArrayAdapter(this@AgregarPrenda, android.R.layout.simple_spinner_item, nuevasOpcionesTalla)
                    nuevoAdapterTalla.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    // Asigna el nuevo adaptador al spinner de talla
                    spinnerTalla.adapter = nuevoAdapterTalla
                } else {
                    // Si no se selecciona "Shoes", restaura el adaptador original
                    spinnerTalla.adapter = adapterTalla
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se necesita implementar
            }
        }


        // Maneja la selección del usuario
        spinnerTalla.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent?.selectedItemPosition != 0) { // Verifica que la selección no sea la posición cero (si se selecciona un elemento por defecto)
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    // Aquí puedes realizar acciones basadas en la opción seleccionada
                    Toast.makeText(applicationContext, "Opción seleccionada: $selectedItem", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se necesita implementar
            }
        }



        //Se declara el boton para subir una imagen y el imageViewer para mostrarlr
        val imagenPrenda = findViewById<ImageView>(R.id.imagenPreview)
        val btnImgPrenda = findViewById<Button>(R.id.btnSeleccionarImagen)
        btnImgPrenda.setOnClickListener(){
            fileUpload()
        }

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        btnAgregar.setOnClickListener {
            btnAgregar.setOnClickListener {
                if (imagenSeleccionada()) {
                    if (camposVacios()) {
                        Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                    } else {
                        agregarPrenda()
                    }
                } else {
                    Toast.makeText(this, "Por favor, seleccione una imagen.", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    // Método para manejar la selección de imagen
    private fun fileUpload() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)

        // Deshabilita la interacción con la interfaz mientras se carga la imagen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            val imagenPrenda = findViewById<ImageView>(R.id.imagenPreview)
            imagenPrenda.setImageURI(imageUri)
            imagenPrenda.visibility = View.VISIBLE
            handleImageUploadSuccess(imageUri!!)
        } else {
            // Habilita la interacción con la interfaz en caso de que la selección de la imagen sea cancelada o falle
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun handleImageUploadSuccess(imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("images/${imageUri.lastPathSegment}")

        val uploadTask = imagesRef.putFile(imageUri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                agregarPrenda()
                // Habilita la interacción con la interfaz después de que se haya subido la imagen
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al subir la imagen: ${it.message}", Toast.LENGTH_SHORT).show()
            // Habilita la interacción con la interfaz en caso de que la carga de la imagen falle
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun agregarPrenda() {
        val tipo = findViewById<Spinner>(R.id.spTipo).selectedItem.toString()
        val talla = findViewById<Spinner>(R.id.spTalla).selectedItem.toString()
        val marca = findViewById<EditText>(R.id.etMarca).text.toString()
        val color = findViewById<EditText>(R.id.etColor).text.toString()

        val prenda = hashMapOf(
            "tipo" to tipo,
            "talla" to talla,
            "marca" to marca,
            "color" to color,
            "imageUrl" to imageUrl,
            "usuarioId" to usuarioId
        )

        db.collection("prendas")
            .add(prenda)
            .addOnSuccessListener {
                Toast.makeText(this, "Prenda agregada exitosamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Closet::class.java))
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al agregar la prenda: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun imagenSeleccionada(): Boolean {
        val imagenPrenda = findViewById<ImageView>(R.id.imagenPreview)
        return imagenPrenda.drawable != null
    }

    private fun camposVacios(): Boolean {
        val tipoSeleccionado = findViewById<Spinner>(R.id.spTipo).selectedItem.toString()
        val tallaSeleccionada = findViewById<Spinner>(R.id.spTalla).selectedItem.toString()
        val marca = findViewById<EditText>(R.id.etMarca).text.toString()
        val color = findViewById<EditText>(R.id.etColor).text.toString()
        return tipoSeleccionado.isEmpty() || tallaSeleccionada.isEmpty() || marca.isEmpty() || color.isEmpty()
    }
}