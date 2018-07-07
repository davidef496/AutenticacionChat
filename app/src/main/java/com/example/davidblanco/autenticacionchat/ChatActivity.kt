package com.example.davidblanco.autenticacionchat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*


class ChatActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        val msj = txtMensaje.text.toString()
        if(!msj.isEmpty()) {
            enviarMensaje(txtMensaje.text.toString())
            txtMensaje.setText("")
        }
    }

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("message")
    private var mensajes: ArrayList<Mensaje>? = ArrayList<Mensaje>()
    private val TAG:String="---FirebaseTest"
    private var adapter: MensajeAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        btnEnviar.setOnClickListener(this)
        escucharMensajes()
        recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        adapter = MensajeAdapter(mensajes!!)
        recyclerView1.adapter = adapter
    }

    private fun escucharMensajes() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mensajes!!.removeAll(mensajes!!)
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Mensaje = snapshot.getValue(Mensaje::class.java)!!
                    mensajes!!.add(pjt)
                }
                adapter!!.notifyDataSetChanged()

            }
        })
    }
    private fun enviarMensaje(mensaje: String) {
        val msj = Mensaje(mensaje,obtenerFecha());
        myRef!!.push().setValue(msj)
    }
    private fun obtenerFecha(): String {
        val c = Calendar.getInstance()
        var hora = "" + c.get(Calendar.HOUR) + ":" + (c.get(Calendar.MINUTE) );
        var fecha = "" + c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        return hora+" - "+fecha;
    }
    }
