package est.una.ac.cr.nonavegable.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import est.una.ac.cr.nonavegable.R


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_login)
        var user_nameED = findViewById<TextInputLayout>(R.id.text_user_name)
        var paswordED = findViewById<TextInputLayout>(R.id.text_password)
        var btnIngresar= findViewById<Button>(R.id.btn_ingresar)
        var btnRegistrar= findViewById<Button>(R.id.btn_registrar)
        btnIngresar.setOnClickListener {
            var Us :String=user_nameED.editText?.text.toString()
            var Ps:String =paswordED.editText?.text.toString()
            //if(Us=="Pepe"&& Ps=="pepe123"){
                val bundle = Bundle()
                val i = Intent(this,MainActivity::class.java)
                i.putExtra("UserName",Us)
                startActivity(i)
            //}
        }
        btnRegistrar.setOnClickListener {
            val bundle = Bundle()
            val i = Intent(this,Register::class.java)
            startActivity(i)
        }
    }
}