package est.una.ac.cr.nonavegable.model

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

fun httpRequestGet(apiUrl:String):String{
    var connection:HttpURLConnection
    var reader: BufferedReader
    var line:String
    var responseCont = StringBuffer()
    var url = URL(apiUrl)
    connection = url.openConnection() as HttpURLConnection
    try{
        connection.requestMethod="GET"
        connection.connectTimeout=5000
        connection.readTimeout=5000
        var status: Int = connection.responseCode
        if(status>299){
            reader = BufferedReader(InputStreamReader(connection.errorStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }else{
            reader = BufferedReader(InputStreamReader(connection.inputStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }
        Log.println(Log.INFO,"Info","Se obtuvo: ${responseCont.toString()}")
    }catch (ex: Exception){
        Log.println(Log.ERROR,"Error","Exception: ${ex.message}")
        ex.printStackTrace()
    }finally {
        connection.disconnect()
    }
    return responseCont.toString()
}

fun httpRequestPost(apiUrl:String,params:String):String{
    var connection:HttpURLConnection
    var reader: BufferedReader
    var line:String
    var responseCont = StringBuffer()
    var url = URL(apiUrl)
    connection = url.openConnection() as HttpURLConnection
    try{
        connection.requestMethod="POST"
        connection.connectTimeout=5000
        connection.readTimeout=5000
        connection.setRequestProperty("Content-Type","application/json; charset=utf-8")
        connection.doOutput=true
        connection.getOutputStream().use { os ->
            val input: ByteArray = params.toByteArray(Charsets.UTF_8)
            os.write(input, 0, input.size)
        }
        var status: Int = connection.responseCode
        if(status>299){
            reader = BufferedReader(InputStreamReader(connection.errorStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }else{
            reader = BufferedReader(InputStreamReader(connection.inputStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }
        Log.println(Log.INFO,"Info","Se obtuvo: ${responseCont.toString()}")
    }catch (ex: Exception){
        Log.println(Log.ERROR,"Error","Exception: ${ex.message}")
        ex.printStackTrace()
    }finally {
        connection.disconnect()
    }
    return responseCont.toString()
}