package com.example.ssl_toffee_rnd

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.EditText
import com.example.awesomedialog.*
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCAdditionalInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener

class MainActivity : AppCompatActivity(), SSLCTransactionResponseListener {

    private lateinit var enterAmount: EditText
    private lateinit var paySSLButton: Button
    private lateinit var alert: AlertDialog

    private var sslCommerzInitialization: SSLCommerzInitialization? = null
    private var additionalInitializer: SSLCAdditionalInitializer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        enterAmount=findViewById(R.id.etAmountId)
        paySSLButton=findViewById(R.id.payNowButtonId)

        paySSLButton.setOnClickListener {
            val amount = enterAmount.text.toString().trim()
            if (amount.isNotEmpty()){
                sslSetUp(amount)
            }else{
                enterAmount.error="Error"
            }
        }
    }

    private fun sslSetUp(amount:String) {
        val randomNumber = (0..100000000000).random()
        sslCommerzInitialization = SSLCommerzInitialization(
            "nexde63e48d9521823",
            "nexde63e48d9521823@ssl",
            amount.toDouble(), SSLCCurrencyType.BDT,
            "TrxId_SSL$randomNumber",
            "yourProductType",
            SSLCSdkType.TESTBOX
        )
        additionalInitializer = SSLCAdditionalInitializer()
        additionalInitializer!!.valueA = "Value Option 1"
        additionalInitializer!!.valueB = "Value Option 1"
        additionalInitializer!!.valueC = "Value Option 1"
        additionalInitializer!!.valueD = "Value Option 1"
        IntegrateSSLCommerz
            .getInstance(this)
            .addSSLCommerzInitialization(sslCommerzInitialization)
            .addAdditionalInitializer(additionalInitializer)
            .buildApiCall(this)
    }

    override fun transactionSuccess(successInfo: SSLCTransactionInfoModel?) {
        if (successInfo != null) {
            Log.d("BODYDATA", successInfo.tranId)
            Log.d("BODYDATA", successInfo.status)

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity, R.style.DialogStyle).apply {
                setMessage("Id:${successInfo.tranId}")
                setTitle("Amount: ${successInfo.amount}\nPayment Status:${successInfo.apiConnect}")
                setCancelable(false)
                setPositiveButton("Continue") { _, _ ->
                    try {
                        alert.hide()
                    }
                    catch (e: ActivityNotFoundException) {
                        throw e
                    }
                }
            }
            alert = builder.create()
            alert.show()
            val updateButton: Button = alert.getButton(DialogInterface.BUTTON_POSITIVE)
            updateButton.setTextColor(Color.parseColor("#F50057"))
        }
    }

    override fun transactionFail(p0: String?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity, R.style.DialogStyle).apply {
            setTitle("transactionFail")
            setCancelable(false)
            setPositiveButton("Continue") { _, _ ->
                try {
                    alert.hide()
                }
                catch (e: ActivityNotFoundException) {
                    throw e
                }
            }
        }
        alert = builder.create()
        alert.show()
        val updateButton: Button = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        updateButton.setTextColor(Color.parseColor("#F50057"))
    }

    override fun closed(p0: String?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity, R.style.DialogStyle).apply {
            setTitle("closed")
            setCancelable(false)
            setPositiveButton("Continue") { _, _ ->
                try {
                    alert.hide()
                }
                catch (e: ActivityNotFoundException) {
                    throw e
                }
            }
        }
        alert = builder.create()
        alert.show()
        val updateButton: Button = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        updateButton.setTextColor(Color.parseColor("#F50057"))
    }
}