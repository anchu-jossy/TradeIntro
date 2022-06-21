package com.techxform.tradintro.feature_main.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.ActivityPaymentResponseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentResponseActivity : AppCompatActivity(){

    lateinit var binding: ActivityPaymentResponseBinding

    companion object{
        const val LINK = "link"
        fun newIntent(activity: FragmentActivity, link:String) : Intent
        {
            val i =  Intent(activity, PaymentResponseActivity::class.java)
            i.putExtra(LINK, link)
            return i
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_response)
        val paymentLink = intent.getStringExtra(LINK)

        if (paymentLink != null) {
            with(binding)
            {
                binding.progressBar.progressOverlay.visibility = View.VISIBLE
                webView.settings.javaScriptEnabled = true
                webView.settings.javaScriptCanOpenWindowsAutomatically = true
                webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        view?.loadUrl(request?.url.toString())
                        return true
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        binding.progressBar.progressOverlay.visibility = View.GONE
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        binding.progressBar.progressOverlay.visibility = View.GONE
                    }
                }
                webView.loadUrl(paymentLink)

            }


        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_OK)
        finish()
    }

}