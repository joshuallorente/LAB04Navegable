package est.una.ac.cr.nonavegable.model.entities

import java.io.Serializable

class Avion(
    var id:String?=null,
    var tipo:String?=null
):Serializable {
    override fun toString(): String {
        return "Avion(id=$id, tipo=$tipo)"
    }
}