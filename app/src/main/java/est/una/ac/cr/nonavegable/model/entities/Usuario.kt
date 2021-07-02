package est.una.ac.cr.nonavegable.model.entities

import java.io.Serializable

class Usuario(
    var user_name:String?=null,
    var contraseña:String?=null,
    var fecha_nacimiento:String?=null,
    var rol:Int=-1,
    var nombre: String?=null,
    var apellidos: String?=null,
    var celular:String?=null,
    var direccion:String?=null,
    var telefono:String?=null,
    var correo:String?=null
):Serializable {
    override fun toString(): String {
        return "Usuario(user_name=$user_name, contraseña=$contraseña, " +
                "fecha_nacimento=$fecha_nacimiento, rol=$rol, nombre=$nombre, " +
                "apellidos=$apellidos, celular=$celular, direccion=$direccion, " +
                "telefono=$telefono, correo=$correo)"
    }
}