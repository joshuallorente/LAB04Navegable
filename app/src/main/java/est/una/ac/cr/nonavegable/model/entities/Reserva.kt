package est.una.ac.cr.nonavegable.model.entities

import java.io.Serializable

class Reserva(
    var id : Int= -1,
    var user_name: String?=null,
    var tiquetes:List<Tiquete>?=null
):Serializable {
    override fun toString(): String {
        return "Reserva(id=$id, " +
                "user_name=$user_name, tiquetes=$tiquetes)"
    }
}