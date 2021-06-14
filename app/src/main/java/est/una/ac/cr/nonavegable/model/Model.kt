package est.una.ac.cr.nonavegable.model

import est.una.ac.cr.nonavegable.model.entities.*

class Model {

    private object HOLDER{
        var INSTANCE = Model()
    }

    companion object{
        val instance : Model by lazy { HOLDER.INSTANCE }
    }

    var listaUsuario: ArrayList<Usuario> = ArrayList()
    var listaVuelo: ArrayList<Vuelo> = ArrayList()
    var listaAvion: ArrayList<Avion> = ArrayList()
    var listaTipoAvion: ArrayList<TipoAvion> = ArrayList()
    var listaRuta: ArrayList<Ruta> = ArrayList()
    var listaTiquete: ArrayList<Tiquete> = ArrayList()
    var listaReserva: ArrayList<Reserva> = ArrayList()
    var listaAsiento: ArrayList<Asiento> = ArrayList()

    fun init(){
        listaUsuario.add(Usuario("joshua.llor","1234","12-02-1999",0,
            "Joshua","Llorente","60928969","San Juan de Dios","22506638",
            "joshua.llor@gmail.com"))
        listaUsuario.add(Usuario("jperez","1234","12-02-1999",0,
            "Juan","Perez","87654321","San Juan de Dios","87654321",
            "juan@gmail.com"))

        listaTipoAvion.add(TipoAvion("Boeing-737",2018,32,8,256,"737","Boeing"))
        listaTipoAvion.add(TipoAvion("Boeing-223",2018,32,6,192,"223","Boeing"))

        listaAvion.add(Avion("AB123","Boeing-737"))
        listaAvion.add(Avion("CD456","Boeing-223"))

        var ruta1 = Ruta(1,"SJO","PTY","1:00","AB123",200.0,"10:00","LUN",1)
        var ruta2 = Ruta(2,"PTY","SJO","1:00","AB123",200.0,"10:00","JUE",1)

        var ruta3 = Ruta(3,"SJO","IST","16:00","CD456",1600.0,"10:00","MAR",2)
        var ruta4 = Ruta(4,"IST","SJO","16:00","CD456",1600.0,"10:00","VIE",2)

        listaRuta.add(ruta1)
        listaRuta.add(ruta2)
        listaRuta.add(ruta3)
        listaRuta.add(ruta4)

        listaVuelo.add(Vuelo(1,"SJO","PTY",0,256,"14-06-2021",
            1,"AB123",ruta1))
        listaVuelo.add(Vuelo(2,"PTY","SJO",0,256,"17-06-2021",
            1,"AB123",ruta2))

        listaVuelo.add(Vuelo(3,"SJO","IST",0,256,"15-06-2021",
            1,"AB123",ruta3))
        listaVuelo.add(Vuelo(4,"IST","SJO",0,256,"18-06-2021",
            1,"AB123",ruta4))

        listaReserva.add(Reserva(1,"jperez"))
        listaTiquete.add(Tiquete(1,1,"jperez",1))
        listaAsiento.add(Asiento(1,1,'B',1))
        listaAsiento.add(Asiento(2,2,'B',1))

        listaReserva.add(Reserva(2,"jperez"))
        listaTiquete.add(Tiquete(2,2,"jperez",1))
        listaAsiento.add(Asiento(3,1,'B',2))
        listaAsiento.add(Asiento(4,2,'B',2))

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