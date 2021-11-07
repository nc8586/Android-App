package com.example.firebasegooglesignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class WebActivity : AppCompatActivity() {

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var mWebView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        supportActionBar!!.setTitle("Raspberry Pi Security Camera")


        mWebView = findViewById<View>(R.id.WebView) as WebView
        mWebView.loadUrl("https://drive.google.com/drive/folders/171l9iw4XMXbpuFhEqp5BCce_hhNW7QPu")
        CookieManager.getInstance().setAcceptCookie(true);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
        }

        mWebView.apply{

           settings.javaScriptEnabled = true

        }
        mWebView.webViewClient = WebViewClient()


        fbAuth = FirebaseAuth.getInstance()
        val fbUser = fbAuth.currentUser
        if(fbUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        val logoutBtn = findViewById(R.id.logoutBtn) as Button
        logoutBtn.setOnClickListener{
            fbAuth.signOut()
            val fbUser = fbAuth.currentUser
            if(fbUser == null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }


    }
}

    override fun onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack()
        }
        else {
            super.onBackPressed()
        }
    }

   }
