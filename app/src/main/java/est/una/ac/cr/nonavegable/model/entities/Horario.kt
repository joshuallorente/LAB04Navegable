package est.una.ac.cr.nonavegable.model.entities

import java.io.Serializable
import java.sql.Time

class Horario(
    var id:Int=-1,
    var precio:Double=-1.0,
    var tiempo: Time,
    var dia:String
):Serializable {
    override fun toString(): String {
        return "Horario(id=$id, precio=$precio, tiempo=$tiempo, dia='$dia')"
    }
}