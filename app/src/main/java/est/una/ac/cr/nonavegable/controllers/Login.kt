package est.una.ac.cr.nonavegable.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.model.Constant
import est.una.ac.cr.nonavegable.model.CoroutinesAsyncTask
import est.una.ac.cr.nonavegable.model.Model
import est.una.ac.cr.nonavegable.model.entities.Usuario
import est.una.ac.cr.nonavegable.model.httpRequestPost


class Login : AppCompatActivity() {

    var gson= Gson()
    lateinit var Us:String
    lateinit var Ps:String
    private var task:LoginAsync?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_login)
        var user_nameED = findViewById<TextInputLayout>(R.id.text_user_name)
        var paswordED = findViewById<TextInputLayout>(R.id.text_password)
        var btnIngresar= findViewById<Button>(R.id.btn_ingresar)
        var btnRegistrar= findViewById<Button>(R.id.btn_registrar)
        btnIngresar.setOnClickListener {
             Us=user_nameED.editText?.text.toString()
             Ps=paswordED.editText?.text.toString()
            //if(Us=="Pepe"&& Ps=="pepe123"){
                ejecutarTarea("{\"user_name\":\"${Us}\"}")
            //}
        }
        btnRegistrar.setOnClickListener {
            val bundle = Bundle()
            val i = Intent(this,Register::class.java)
            startActivity(i)
        }
    }

    fun getLoginIntent():Intent{
        return Intent(this,MainActivity::class.java)
    }

    fun ejecutarTarea(parametros: String){
        if(task?.status==Constant.Status.RUNNING){
            task?.cancel(true)
        }
        task = LoginAsync(parametros)
        task?.execute()
    }

    inner class LoginAsync(var parametros:String):CoroutinesAsyncTask<Int,Int,String>("Login Async Task"){
        override fun doInBackground(vararg params: Int?): String {
            return httpRequestPost("http://201.200.0.31/LAB001BACKEND/services/Usuario/Recuperar",parametros)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var sType = object: TypeToken<Usuario>(){}.type
            var data = gson.fromJson<Usuario>(result,sType)
            if (Us == data.user_name && Ps == data.contraseña){
                val bundle = Bundle()
                val i = getLoginIntent()
                i.putExtra("UserName",Us)
                Model.instance.user_name=data.user_name!!
                startActivity(i)
            }
        }

    }

}