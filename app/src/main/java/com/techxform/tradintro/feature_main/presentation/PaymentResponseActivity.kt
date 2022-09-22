package com.techxform.tradintro.feature_main.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.ActivityPaymentResponseBinding
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class PaymentResponseActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentResponseBinding

    companion object {
        const val LINK = "link"
        fun newIntent(activity: FragmentActivity, link: String): Intent {
            val i = Intent(activity, PaymentResponseActivity::class.java)
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
                        binding.progressBar.progressOverlay.setVisibiltyGone()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        val keyWords = arrayOf("recharge_success?id", "cancel")
                        if (keyWords.find { url.toString().contains(it) } != null) {
                            Log.e("Monitor", url.toString())
                            binding.btClose.setVisible()
                            binding.btPrint.setVisible()
                            binding.btPrint.setOnClickListener {
                                if (view != null) {
                                    createWebPagePrint(view)
                                }
                            }
                            binding.btClose.setOnClickListener {
                                if (url.toString().contains("success")) {
                                    setResult(RESULT_OK)
                                } else {
                                    setResult(RESULT_CANCELED)
                                }
                                finish()
                            }
                        }
                        binding.progressBar.progressOverlay.setVisibiltyGone()

                    }
                }
                webView.loadUrl(paymentLink)

            }


        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        /*super.onBackPressed()
        setResult(Activity.RESULT_OK)
        finish()*/
    }

    fun createWebPagePrint(webView: WebView) {
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager
        val jobName = getString(R.string.app_name) + " Invoice " + Date()
        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        val builder = PrintAttributes.Builder()
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
        val printJob = printManager.print(jobName, printAdapter, builder.build())
        if (printJob.isCompleted) {
            Toast.makeText(applicationContext, R.string.print_complete, Toast.LENGTH_LONG).show()
        } else if (printJob.isFailed) {
            Toast.makeText(applicationContext, R.string.print_failed, Toast.LENGTH_LONG).show()
        }
        // Save the job object for later status checking
    }

}