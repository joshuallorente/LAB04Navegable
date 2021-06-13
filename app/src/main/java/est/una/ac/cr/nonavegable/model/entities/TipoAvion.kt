package est.una.ac.cr.nonavegable.model.entities

import java.io.Serializable

class TipoAvion(
    var id : String?=null,
    var año: Int = -1,
    var max_fila: Int = -1,
    var max_columna: Int = -1,
    var cantidad_pasajeros: Int = -1,
    var modelo:String?=null,
    var marca:String?=null,
):Serializable {
    override fun toString(): String {
        return "TipoAvion(id=$id, año=$año, max_fila=$max_fila, max_columna=$max_columna," +
                " cantidad_pasajeros=$cantidad_pasajeros, modelo=$modelo, marca=$marca)"
    }
}