package mx.edu.potros.fashionfix

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth


class Closet : AppCompatActivity() {

    var prendas: ArrayList<Prenda> = ArrayList<Prenda>()
    lateinit var adaptador: AdaptadorPrendas // Adaptador como variable miembro
    val currentUser = FirebaseAuth.getInstance().currentUser
    val usuarioId = currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet)


        val recomendacionButton: ImageButton = findViewById(R.id.btnRecomendaciones)
        val favoritosButton: ImageButton = findViewById(R.id.btnFavoritosMenu)
        val historialButton: ImageButton = findViewById(R.id.btnHistorial)
        val closetButton: ImageButton = findViewById(R.id.btnCloset)
        val agregarButton: ImageButton = findViewById(R.id.btnAgregarPrenda)

        val btnTops : Button = findViewById(R.id.btnTops)
        val btnBottoms : Button = findViewById(R.id.btnBottoms)
        val btnShoes : Button = findViewById(R.id.btnShoes)

        var gridView: GridView = findViewById(R.id.gridCloset) as GridView

        adaptador = AdaptadorPrendas(this, prendas)
        gridView.adapter = adaptador

        // Obtener los datos de las prendas desde Firebase (o cualquier otra fuente)
        obtenerPrendasDesdeFirebase()

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

        btnTops.setOnClickListener(){
            obtenerTopsDesdeFirebase()
        }
        btnShoes.setOnClickListener(){
            obtenerShoesDesdeFirebase()
        }
        btnBottoms.setOnClickListener({
             obtenerBottomsDesdeFirebase()
        })


        // Manejar el clic en la imagen del GridView
        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // Obtener la prenda seleccionada
            val prendaSeleccionada = intent.getSerializableExtra("prendaSeleccionada") as Prenda

            // Aquí puedes pasar la información de la prenda seleccionada al siguiente activity
            // Por ejemplo, puedes iniciar un Intent y pasar datos a través de extras
            val intent = Intent(this, InfoPrenda::class.java)
            intent.putExtra("prendaSeleccionada", prendaSeleccionada)
            startActivity(intent)
        }
    }
    private fun obtenerPrendasDesdeFirebase() {
        // Referencia a la colección "prendas" en Firebase Firestore
        val db = Firebase.firestore
        if (usuarioId != null) {
            val prendasRef = db.collection("prendas")
                .whereEqualTo("usuarioId", usuarioId)

            // Consulta todas las prendas
            prendasRef.get()
                .addOnSuccessListener { result ->
                    val listaPrendas = ArrayList<Prenda>()

                    for (document in result) {
                        // Para cada documento en la colección "prendas", obtén los datos y crea un objeto Prenda
                        val id = document.id
                        val imageUrl = document.getString("imageUrl")
                        val marca = document.getString("marca")
                        val tipo = document.getString("tipo")
                        val color = document.getString("color")
                        val talla = document.getString("talla")

                        Log.d("INF", "Documento obtenido: ${document.id}")
                        Log.d("INF", "Imagen obtenido: ${imageUrl}")
                        Log.d("INF", "Marca obtenido: ${marca}")
                        Log.d("INF", "Tipo obtenido: ${tipo}")
                        Log.d("INF", "Color obtenido: ${color}")
                        Log.d("INF", "Talla obtenido: ${talla}")


                        // Crea un objeto Prenda y agrégalo a la lista de prendas
                        val prenda = Prenda(id, imageUrl, tipo, talla, marca, color)
                        listaPrendas.add(prenda)
                    }

                    // Una vez que hayas obtenido todas las prendas, asigna la lista de prendas al adaptador
                    // Una vez que hayas obtenido todas las prendas, asigna la lista de prendas al adaptador
                    prendas.clear()
                    prendas.addAll(listaPrendas) // Agregar las nuevas prendas obtenidas
                    adaptador.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error al obtener las prendas", exception)
                }
        }
    }
    private fun obtenerTopsDesdeFirebase() {
        // Referencia a la colección "prendas" en Firebase Firestore
        val db = Firebase.firestore
        if (usuarioId != null) {
            val prendasRef = db.collection("prendas")
                .whereEqualTo("tipo", "Top")
                .whereEqualTo("usuarioId", usuarioId)

            // Consulta todas las prendas
            prendasRef.get()
                .addOnSuccessListener { result ->
                    val listaPrendas = ArrayList<Prenda>()

                    for (document in result) {
                        // Para cada documento en la colección "prendas", obtén los datos y crea un objeto Prenda
                        val id = document.id
                        val imageUrl = document.getString("imageUrl")
                        val marca = document.getString("marca")
                        val tipo = document.getString("tipo")
                        val color = document.getString("color")
                        val talla = document.getString("talla")

                        Log.d("INF", "Documento obtenido: ${document.id}")
                        Log.d("INF", "Imagen obtenido: ${imageUrl}")
                        Log.d("INF", "Marca obtenido: ${marca}")
                        Log.d("INF", "Tipo obtenido: ${tipo}")
                        Log.d("INF", "Color obtenido: ${color}")
                        Log.d("INF", "Talla obtenido: ${talla}")


                        // Crea un objeto Prenda y agrégalo a la lista de prendas
                        val prenda = Prenda(id, imageUrl, tipo, talla, marca, color)
                        listaPrendas.add(prenda)
                    }

                    // Una vez que hayas obtenido todas las prendas, asigna la lista de prendas al adaptador
                    // Una vez que hayas obtenido todas las prendas, asigna la lista de prendas al adaptador
                    prendas.clear()
                    prendas.addAll(listaPrendas) // Agregar las nuevas prendas obtenidas
                    adaptador.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error al obtener las prendas", exception)
                }
        }
    }
    private fun obtenerBottomsDesdeFirebase() {
        // Referencia a la colección "prendas" en Firebase Firestore
        val db = Firebase.firestore
        if (usuarioId != null) {
            val prendasRef = db.collection("prendas")
                .whereEqualTo("tipo", "Bottom")
                .whereEqualTo("usuarioId", usuarioId)


            // Consulta todas las prendas
            prendasRef.get()
                .addOnSuccessListener { result ->
                    val listaPrendas = ArrayList<Prenda>()

                    for (document in result) {
                        // Para cada documento en la colección "prendas", obtén los datos y crea un objeto Prenda
                        val id = document.id
                        val imageUrl = document.getString("imageUrl")
                        val marca = document.getString("marca")
                        val tipo = document.getString("tipo")
                        val color = document.getString("color")
                        val talla = document.getString("talla")

                        Log.d("INF", "Documento obtenido: ${document.id}")
                        Log.d("INF", "Imagen obtenido: ${imageUrl}")
                        Log.d("INF", "Marca obtenido: ${marca}")
                        Log.d("INF", "Tipo obtenido: ${tipo}")
                        Log.d("INF", "Color obtenido: ${color}")
                        Log.d("INF", "Talla obtenido: ${talla}")


                        // Crea un objeto Prenda y agrégalo a la lista de prendas
                        val prenda = Prenda(id, imageUrl, tipo, talla, marca, color)
                        listaPrendas.add(prenda)
                    }

                    // Una vez que hayas obtenido todas las prendas, asigna la lista de prendas al adaptador
                    // Una vez que hayas obtenido todas las prendas, asigna la lista de prendas al adaptador
                    prendas.clear()
                    prendas.addAll(listaPrendas) // Agregar las nuevas prendas obtenidas
                    adaptador.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error al obtener las prendas", exception)
                }
        }
    }
    private fun obtenerShoesDesdeFirebase() {
        // Referencia a la colección "prendas" en Firebase Firestore
        val db = Firebase.firestore
        if (usuarioId != null) {
            val prendasRef = db.collection("prendas")
                .whereEqualTo("tipo", "Shoes")
                .whereEqualTo("usuarioId", usuarioId)

            // Consulta todas las prendas
            prendasRef.get()
                .addOnSuccessListener { result ->
                    val listaPrendas = ArrayList<Prenda>()

                    for (document in result) {
                        // Para cada documento en la colección "prendas", obtén los datos y crea un objeto Prenda
                        val id = document.id
                        val imageUrl = document.getString("imageUrl")
                        val marca = document.getString("marca")
                        val tipo = document.getString("tipo")
                        val color = document.getString("color")
                        val talla = document.getString("talla")

                        Log.d("INF", "Documento obtenido: ${document.id}")
                        Log.d("INF", "Imagen obtenido: ${imageUrl}")
                        Log.d("INF", "Marca obtenido: ${marca}")
                        Log.d("INF", "Tipo obtenido: ${tipo}")
                        Log.d("INF", "Color obtenido: ${color}")
                        Log.d("INF", "Talla obtenido: ${talla}")


                        // Crea un objeto Prenda y agrégalo a la lista de prendas
                        val prenda = Prenda(id, imageUrl, tipo, talla, marca, color)
                        listaPrendas.add(prenda)
                    }

                    // Una vez que hayas obtenido todas las prendas, asigna la lista de prendas al adaptador
                    prendas.clear()
                    prendas.addAll(listaPrendas) // Agregar las nuevas prendas obtenidas
                    adaptador.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error al obtener las prendas", exception)
                }
        }
    }

    class AdaptadorPrendas: BaseAdapter{
        var prendas =ArrayList<Prenda>()
        var contexto: Context?=null

        constructor(contexto: Context,producto: ArrayList<Prenda>){
            this.prendas=producto
            this.contexto=contexto

        }


        override fun getCount(): Int {
            return prendas.size
        }

        override fun getItem(p0: Int): Any {
            return prendas[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var prod=prendas[p0]
            var inflator = contexto!!.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var vista=inflator.inflate(R.layout.grid_item_prenda,null)

            val prenda = prendas[p0]

            var image: ImageView=vista.findViewById(R.id.imgPrenda)

            // Utiliza Glide para cargar la imagen de la prenda en el ImageView
            Glide.with(contexto!!)
                .load(prenda.imagen)
                .placeholder(R.drawable.loadingimg) // Imagen de marcador de posición mientras se carga la imagen
                .error(R.drawable.logo) // Imagen de error si la carga falla
                .centerCrop()
                .into(image)

            image.setOnClickListener(){
                val intento = Intent(contexto,InfoPrenda::class.java)
                intento.putExtra("id",prod.id)
                intento.putExtra("imagen",prod.imagen)
                intento.putExtra("color", prod.color)
                intento.putExtra("marca", prod.marca)
                intento.putExtra("tipo", prod.tipo)
                intento.putExtra("talla", prod.talla)
                intento.putExtra("pos", p0)
                contexto!!.startActivity(intento)
            }
            return  vista

        }


    }

}


