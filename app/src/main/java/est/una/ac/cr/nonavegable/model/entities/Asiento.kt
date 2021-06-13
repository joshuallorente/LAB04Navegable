package est.una.ac.cr.nonavegable.model.entities

import java.io.Serializable

class Asiento(
    var id:Int=-1,
    var fila:Int= -1,
    var columna:Char='z',
    var tiquete_id:Int=-1
):Serializable {
    override fun toString(): String {
        return "Asiento(id=$id, fila=$fila, " +
                "columna=$columna, tiquete_id=$tiquete_id)"
    }
}