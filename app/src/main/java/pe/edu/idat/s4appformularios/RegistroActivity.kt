package pe.edu.idat.s4appformularios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.Toast
import pe.edu.idat.s4appformularios.comunes.AppMensaje
import pe.edu.idat.s4appformularios.comunes.TipoMensaje
import pe.edu.idat.s4appformularios.databinding.ActivityRegistroBinding
import java.io.StringReader

class RegistroActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var binding:ActivityRegistroBinding
    private var estadocivil = ""
    private val listaPreferencias = ArrayList<String>()
    private val listaPersonas = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnListar.setOnClickListener(this)
        binding.btnRegistrar.setOnClickListener(this)
        ArrayAdapter.createFromResource(
            this, R.array.estado_civil,android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spEstadoCivil.adapter = adapter
        }
        binding.spEstadoCivil.onItemSelectedListener = this
    }

    override fun onClick(vista: View) {
        if(vista is CheckBox){
            agregarQuitarPreferenciaSeleccionada(vista)
        }else {
            when (vista.id) {
                R.id.btnRegistrar -> registrarPersona()
                R.id.btnListar -> startActivity(
                    Intent(
                        applicationContext,
                        ListadoActivity::class.java)
                        .apply {
                            putExtra("listapersonas",listaPersonas)
                    })
            }
        }
    }

    private fun agregarQuitarPreferenciaSeleccionada(vista: CheckBox) {
        if (vista.isChecked){
            when(vista.id){
                R.id.cbDeportes -> listaPreferencias.add(vista.text.toString())
                R.id.cbMusica -> listaPreferencias.add(vista.text.toString())
                R.id.cbOtros -> listaPreferencias.add(vista.text.toString())
            }
        }else{
            when(vista.id){
                R.id.cbDeportes -> listaPreferencias.remove(vista.text.toString())
                R.id.cbMusica -> listaPreferencias.remove(vista.text.toString())
                R.id.cbOtros -> listaPreferencias.remove(vista.text.toString())
            }
        }
    }

    fun registrarPersona(){
        if(validarFormulario()){
            val infoPersona = binding.etNombres.text.toString()+""+
                    binding.etApellidos.text.toString()+""+
                    obtenerGeneroSeleccionado()+""+
                    listaPreferencias.toArray()+""+
                    estadocivil+""+
                    binding.swNotificacion.isChecked
            listaPersonas.add(infoPersona)
            AppMensaje.enviarMensaje(binding.root,
                getString(R.string.mensajeRegistroCorrecto),
            TipoMensaje.SUCCESSFULL)
            setearControles()
        }
        /*Toast.makeText(applicationContext, "Click en Registro de Persona",
            Toast.LENGTH_LONG).show()*/
    }

    private fun setearControles() {

        listaPreferencias.clear()
        binding.etNombres.setText("")
        binding.etApellidos.setText("")

        binding.swNotificacion.isChecked=false

        binding.cbDeportes.isChecked=false
        binding.cbMusica.isChecked=false
        binding.cbOtros.isChecked=false

        binding.rgGenero.clearCheck()
        binding.spEstadoCivil.setSelection(0)

        binding.etNombres.isFocusableInTouchMode=true
        binding.etNombres.requestFocus()
    }

    fun obtenerGeneroSeleccionado():String{
        var genero = ""
        when(binding.rgGenero.checkedRadioButtonId){
            R.id.rbMasculino ->{
                genero = binding.rbMasculino.text.toString()
            }
            R.id.rgGenero ->{
                genero = binding.rbFemenino.text.toString()
            }
        }
        return genero
    }


    // FUNCIONES PARA VALIDAR OPERACIONES

    fun validarNombreApellido(): Boolean {
        var respuesta = true
        if(binding.etNombres.text.toString().trim().isEmpty()){
            binding.etNombres.isFocusableInTouchMode = true
            binding.etNombres.requestFocus()
            respuesta = false
        }else if (binding.etApellidos.text.toString().trim().isEmpty()){
            binding.etApellidos.isFocusableInTouchMode = true
            binding.etApellidos.requestFocus()
            respuesta = false
        }
        return respuesta
    }

    fun validarGenero(): Boolean{
        var respuesta = true
        if(binding.rgGenero.checkedRadioButtonId == -1){
            respuesta = false
        }
        return respuesta
    }

    fun validarPreferencias(): Boolean{
        var respuesta = false
        if(binding.cbDeportes.isChecked ||
            binding. cbMusica.isChecked ||
            binding.cbOtros.isChecked){
            respuesta = true
        }
        return respuesta
    }

    fun validarEstadoCivil() : Boolean{
        return estadocivil != ""
    }

    fun validarFormulario():Boolean{
        var respuesta = false
        if (!validarNombreApellido()){
            AppMensaje.enviarMensaje(binding.root,
                "Ingrese nombre y apellido", TipoMensaje.ERROR)
        }else if (!validarGenero()){
            AppMensaje.enviarMensaje(binding.root,
                "Seleccione su género", TipoMensaje.ERROR)
        }else if (!validarPreferencias()){
            AppMensaje.enviarMensaje(binding.root,
                "Seleccione almenos una Preferencia", TipoMensaje.ERROR)
        }else if (!validarEstadoCivil()){
            AppMensaje.enviarMensaje(binding.root,
                "Seleccione su Estado Civil", TipoMensaje.ERROR)
        }else {
            respuesta = true
        }

        return respuesta
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        estadocivil = if (position>0){
            parent!!.getItemAtPosition(position).toString()
        }else ""
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}