package com.example.firebasegooglesignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.firebasegooglesignin.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private companion object{
        private const val RC_SIGN_IN = 10

    }
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var fbAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        supportActionBar!!.setTitle("Raspberry Pi Security Camera")

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        fbAuth = FirebaseAuth.getInstance()
        val fbUser = fbAuth.currentUser
        if(fbUser != null)
        {
            startActivity(Intent(this, WebActivity:: class.java))
            finish()
        }

        val signInBtn = findViewById(R.id.signInBtn) as com.google.android.gms.common.SignInButton
        signInBtn.setOnClickListener(){
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)


        }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val accTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                   val account = accTask.getResult(ApiException::class.java)
                   firebaseAuthWithGoogle(account)
            }
            catch (e: Exception){
                Log.d("onActivityResult" , "Sign In Failed"   )
            }
        }


    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        fbAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful)
                    {
                        val fbUser = fbAuth.currentUser
                        val email = fbUser!!.email

                        Toast.makeText(this, "Logged In \n$email", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, WebActivity:: class.java))
                        finish()

                    }

                else
                {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()


                }

            }






    }
}