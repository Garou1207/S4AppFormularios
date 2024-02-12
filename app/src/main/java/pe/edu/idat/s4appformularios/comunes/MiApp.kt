package pe.edu.idat.s4appformularios.comunes

import android.app.Application

class MiApp : Application() {

    companion object{
        lateinit var instance : MiApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}