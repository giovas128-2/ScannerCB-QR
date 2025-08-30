package com.cg128.scannercbyqr.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cg128.scannercbyqr.R
import androidx.lifecycle.lifecycleScope
import com.cg128.scannercbyqr.data.HistoryManager
import kotlinx.coroutines.launch
import kotlin.text.isNotEmpty


class QRResultActivity : AppCompatActivity() {

    private lateinit var btnIngresar: Button
    private lateinit var btnCopiar: Button
    private lateinit var tvQRLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        val url = intent.getStringExtra("qr_url") ?: ""
        val fromHistory = intent.getBooleanExtra("fromHistory", false)

        tvQRLink = findViewById(R.id.tvQRLink)
        btnIngresar = findViewById(R.id.btnIngresar)
        btnCopiar = findViewById(R.id.btnCopiar)

        tvQRLink.text = url

        if (!fromHistory) {
            // Guardar historial solo si NO viene del historial
            val historyManager = HistoryManager(this)
            lifecycleScope.launch {
                historyManager.addHistory(
                    text = url,
                    imageUrl = null,
                    description = null,
                    barcode = url,
                    isQR = true
                )
            }
        }


        // Estado inicial: Ingresar activo, Copiar inactivo
        setActiveButton(btnIngresar, btnCopiar)

        btnIngresar.setOnClickListener {
            setActiveButton(btnIngresar, btnCopiar)
            if (url.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        btnCopiar.setOnClickListener {
            setActiveButton(btnCopiar, btnIngresar)
            if (url.isNotEmpty()) {
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("QR Link", url)
                clipboard.setPrimaryClip(clip)
            }
        }
    }

    private fun setActiveButton(active: Button, inactive: Button) {
        active.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purple_700)
        active.setTextColor(ContextCompat.getColor(this, R.color.black))

        inactive.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_700)
        inactive.setTextColor(ContextCompat.getColor(this, R.color.white))
    }
}
