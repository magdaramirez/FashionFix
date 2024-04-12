package mx.edu.potros.fashionfix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class AgregarPrenda : AppCompatActivity() {
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
                // Aquí puedes realizar acciones basadas en la opción seleccionada
                Toast.makeText(applicationContext, "Opción seleccionada: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se necesita implementar
            }
        }

        // Maneja la selección del usuario
        spinnerTalla.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                // Aquí puedes realizar acciones basadas en la opción seleccionada
                Toast.makeText(applicationContext, "Opción seleccionada: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se necesita implementar
            }
        }
    }
}