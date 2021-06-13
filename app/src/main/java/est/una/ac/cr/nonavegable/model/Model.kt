package est.una.ac.cr.nonavegable.model

import est.una.ac.cr.nonavegable.model.entities.Usuario

class Model {

    private object HOLDER{
        var INSTANCE = Model()
    }

    companion object{
        val instance : Model by lazy { HOLDER.INSTANCE }
    }

    fun crearUsuario(
         user_name:String?=null,
         contraseña:String?=null,
         fecha_nacimento:String?=null,
         rol:Int=-1,
         nombre: String?=null,
         apellidos: String?=null,
         celular:String?=null,
         direccion:String?=null,
         telefono:String?=null,
         correo:String?=null
    ): Usuario {
        return Usuario(user_name,contraseña,fecha_nacimento,rol,nombre,apellidos,celular,direccion,telefono,correo)

    }

}