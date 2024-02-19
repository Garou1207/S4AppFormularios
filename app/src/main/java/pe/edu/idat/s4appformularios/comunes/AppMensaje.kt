package pe.edu.idat.s4appformularios.comunes

import android.renderscript.ScriptIntrinsicYuvToRGB
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import pe.edu.idat.s4appformularios.R

object AppMensaje {

    fun enviarMensaje(vista: View, mensaje: String, tipoMensaje: TipoMensaje){

        val snackBar = Snackbar.make(vista,mensaje,Snackbar.LENGTH_LONG)
        val snackBarView: View = snackBar.view

        if(tipoMensaje == TipoMensaje.ERROR){
            snackBar.setBackgroundTint(
                ContextCompat.getColor(MiApp.instance,R.color.errorColor)
            )
        }else{
            snackBar.setBackgroundTint(
                ContextCompat.getColor(MiApp.instance,R.color.exitoColor)
            )
        }
        snackBar.show()

    }

}