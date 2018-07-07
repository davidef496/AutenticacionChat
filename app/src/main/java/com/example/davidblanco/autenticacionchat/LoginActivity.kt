package com.example.davidblanco.autenticacionchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    private var TAG="Login Activity---->"
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        btnLogin.setOnClickListener(this)
        cBoxShow.setOnClickListener(this)
    }
    override fun onClick(view: View?) {
        val i = view!!.getId()
        if(i==R.id.cBoxShow) {
            if (!cBoxShow.isChecked) {
                txtClave.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                txtClave.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }else if(i==R.id.btnLogin){
            if(validateForm()) {
                LogearUsuario()
            }}}
    private fun LogearUsuario(){
        mAuth!!.signInWithEmailAndPassword(txtUsuario.text.toString(), txtClave.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth!!.getCurrentUser()
                        val i = Intent(applicationContext, ChatActivity::class.java)//lanza la siguiente actividad
                        i.putExtra("Email",txtUsuario.text.toString())
                        this.finish()
                        startActivity(i)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Autenticacion fallida",
                                Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }
}
    fun validateForm():Boolean {
        var valid = true

        val email = txtUsuario.text.toString()
        if (TextUtils.isEmpty(email)) {
            txtUsuario.setError("Required.")
            valid = false
        } else {
            txtUsuario.setError(null)
        }

        val password = txtClave.text.toString()
        if (TextUtils.isEmpty(password)) {
            txtClave.setError("Required.")
            valid = false
        } else {
            txtClave.setError(null)
        }

        return valid
    }
}
