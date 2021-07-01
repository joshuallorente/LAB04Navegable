package est.una.ac.cr.nonavegable.model.entities

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Ruta(
    var id : Int =-1,
    var origen:String?=null,
    var destino:String?=null,
    var duracion:String?=null,
    var avion_id:String?=null,
    var precio:Double=-1.0,
    var tiempo:String?=null,
    var dia:String?=null,
    var horario_id:Int=-1,
):Serializable {
    override fun toString(): String {
        return "Ruta(id=$id, origen=$origen, destino=$destino, " +
                "duracion=$duracion, avion_id=$avion_id, horario_id=$horario_id)"
    }

    fun calcularHoraLlegada():String{
        var formatter:SimpleDateFormat= SimpleDateFormat("hh:mm")
        var date1:Date=formatter.parse(tiempo)
        var date2:Date=formatter.parse(duracion)
        var result:Long=date1.time+date2.time
        return formatter.format(Date(result))
    }

    fun calcularHora():String{
        var formatter:SimpleDateFormat= SimpleDateFormat("hh:mm")
        var date1:Date=formatter.parse(tiempo)
        return formatter.format(date1)
    }

}