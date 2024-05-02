package mx.edu.potros.fashionfix

import java.io.Serializable

data class Prenda(var id:String?=null,
                  var imagen:String?=null,
                  var tipo:String?=null,
                  var talla:String?=null,
                  var marca:String?=null,
                  var color:String?=null,
                  var idUsuario:String?=null) : Serializable
