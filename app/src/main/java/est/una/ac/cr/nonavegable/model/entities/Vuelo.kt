package est.una.ac.cr.nonavegable.model.entities

import java.io.Serializable

class Vuelo(
    var id: Int = -1,
    var origen: String?=null,
    var destino: String?=null,
    var descuento: Int=-1,
    var cantidad_pasajeros:Int=-1,
    var fecha_despegue:String?=null,
    var ruta_id: Int =-1,
    var avion_id:Int =-1,
    var ruta:Ruta?=null
):Serializable {
    override fun toString(): String {
        return "Vuelo(id=$id, origen=$origen, destino=$destino, descuento=$descuento, " +
                "cantidad_pasajeros=$cantidad_pasajeros, ruta_id=$ruta_id, " +
                "avion_id=$avion_id, ruta=$ruta)"
    }
}