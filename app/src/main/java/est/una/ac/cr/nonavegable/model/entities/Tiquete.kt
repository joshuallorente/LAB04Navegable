package est.una.ac.cr.nonavegable.model.entities

import java.io.Serializable

class Tiquete(
    var id : Int = -1,
    var vuelo_id: Int =-1,
    var user_name: String,
    var reserva_id:Int=-1,
):Serializable {
    override fun toString(): String {
        return "Tiquete(id=$id, vuelo_id=$vuelo_id, user_name='$user_name', reserva_id=$reserva_id)"
    }
}