package est.una.ac.cr.nonavegable.controllers

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.model.Constant
import est.una.ac.cr.nonavegable.model.CoroutinesAsyncTask
import est.una.ac.cr.nonavegable.model.Model
import est.una.ac.cr.nonavegable.model.entities.Usuario
import est.una.ac.cr.nonavegable.model.httpRequestPost
import java.util.Calendar



class Register : AppCompatActivity() {
    lateinit var picker:DatePickerDialog
    var task:RegisterAsync?=null
    var gson= Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var userName = findViewById<TextInputLayout>(R.id.text_user_name)
        var password = findViewById<TextInputLayout>(R.id.text_password)
        var confirpass= findViewById<TextInputLayout>(R.id.text_passwordCofirm)
        var fechNaci = findViewById<TextInputLayout>(R.id.text_fecha_naci)
        var nombre= findViewById<TextInputLayout>(R.id.text_nombre)
        var apellidos= findViewById<TextInputLayout>(R.id.text_apellidos)
        var celular = findViewById<TextInputLayout>(R.id.text_celular)
        var telefono = findViewById<TextInputLayout>(R.id.text_telefono)
        var direccion = findViewById<TextInputLayout>(R.id.text_direccion)
        var correo = findViewById<TextInputLayout>(R.id.text_correo)
        var btnCrear= findViewById<Button>(R.id.btn_Crear_Cuenta)
        fechNaci.editText?.inputType=InputType.TYPE_NULL
        fechNaci.editText?.setOnClickListener(View.OnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            // date picker dialog

            picker = DatePickerDialog(this,
                { view, year, monthOfYear, dayOfMonth ->
                    var month= if((monthOfYear+1)<9) "0"+(monthOfYear+1) else ""+(monthOfYear+1)
                    var day= if((dayOfMonth)<9) "0"+(dayOfMonth) else ""+(dayOfMonth)
                    fechNaci.editText?.setText("$year-" + month + "-"+day)},
                year,
                month,
                day
            )
            picker.datePicker.maxDate=System.currentTimeMillis()-1000
            picker.show()})
        btnCrear.setOnClickListener{
            var obj:Usuario= Model.instance.crearUsuario(userName.editText?.text.toString(),
                password.editText?.text.toString(), fechNaci.editText?.text.toString(),
                1, nombre.editText?.text.toString(),apellidos.editText?.text.toString(),
                celular.editText?.text.toString(),direccion.editText?.text.toString(),
                telefono.editText?.text.toString(),correo.editText?.text.toString())
            if(verificar(userName,confirpass,fechNaci,nombre,apellidos,
                celular,direccion,telefono,correo,obj)){
                ejecutarTarea(gson.toJson(obj))
            }
        }

    }
    fun verificar(
        userName:TextInputLayout,
        confirpass:TextInputLayout,
        fechNaci:TextInputLayout,
        nombre:TextInputLayout,
        apellidos:TextInputLayout,
        celular:TextInputLayout,
        direccion:TextInputLayout,
        telefono:TextInputLayout,
        correo:TextInputLayout,
        obj:Usuario): Boolean {
        if(obj.user_name.isNullOrEmpty()||obj.user_name=="Pepe"){
            userName.editText?.error="Este de rellenar ese campo/ El usuario pepe ya existe"
            return false
        }else if(obj.contraseña!=confirpass.editText?.text.toString()) {
            confirpass.editText?.error = "Las contraseñas deben de ser iguales"
            return false
        }else if(obj.direccion.isNullOrEmpty()){
            direccion.editText?.error="Debe de rellenar este espacio"
            return false
        }else if(obj.nombre.isNullOrEmpty()){
            nombre.editText?.error="Debe de rellenar este espacio"
            return false
        }else if(obj.apellidos.isNullOrEmpty()){
            apellidos.editText?.error="Debe de rellenar este espacio"
            return false
        }else if(obj.fecha_nacimiento.isNullOrEmpty()){
            fechNaci.editText?.error="Debe de rellenar este espacio"
            return false
        }else if(obj.celular.isNullOrEmpty()) {
            celular.editText?.error = "Debe de rellenar este espacio."
            return false
        }else if(obj.correo.isNullOrEmpty()){
            correo.editText?.error="Debe de rellenar este espacio."
            return false
        }else
            return true
    }

    fun ejecutarTarea(parametros:String){
        if(task?.status==Constant.Status.RUNNING){
            task?.cancel(true)
        }
        task = RegisterAsync(parametros)
        task?.execute()
    }

    fun getLoginIntent():Intent{
        return Intent(this,Login::class.java)
    }

    inner class RegisterAsync(var parametros:String):CoroutinesAsyncTask<Int,Int,String>("RegisterAsyncTask"){
        override fun doInBackground(vararg params: Int?): String {
           return httpRequestPost("http://201.200.0.31/LAB001BACKEND/services/Usuario/Agregar",parametros)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var i = getLoginIntent()
            startActivity(i)
        }

    }

}