package com.cg128.scannercbyqr.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cg128.scannercbyqr.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import androidx.lifecycle.lifecycleScope
import com.cg128.scannercbyqr.data.HistoryManager
import kotlinx.coroutines.launch
import kotlin.io.readText
import kotlin.text.isNullOrEmpty

class BarcodeResultActivity : AppCompatActivity() {

    private lateinit var txtProductName: TextView
    private lateinit var txtProductDesc: TextView
    private lateinit var imgProduct: ImageView
    private lateinit var btnGoogle: Button
    private lateinit var btnAmazon: Button
    private lateinit var btnMercadoLibre: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_result)

        txtProductName = findViewById(R.id.tvProductName)
        txtProductDesc = findViewById(R.id.tvProductDesc)
        imgProduct = findViewById(R.id.imgProduct)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnAmazon = findViewById(R.id.btnAmazon)
        btnMercadoLibre = findViewById(R.id.btnML)

        val fromHistory = intent.getBooleanExtra("fromHistory", false)
        val code = intent.getStringExtra("barcode_value") ?: ""

        if (fromHistory) {
            // Mostrar datos del historial
            val title = intent.getStringExtra("title") ?: "Producto"
            val description = intent.getStringExtra("description") ?: "Sin descripción"
            val imageUrl = intent.getStringExtra("imageUrl")

            txtProductName.text = title
            txtProductDesc.text = description

            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this).load(imageUrl).into(imgProduct)
            } else {
                imgProduct.setImageResource(R.drawable.ic_no_image)
            }

            // Configurar botones de búsqueda
            setupButtons(code)

        } else {
            // Escaneo nuevo → consulta API
            fetchProductInfo(code)
            setupButtons(code)
        }

    }

    private fun setupButtons(code: String) {
        btnGoogle.setOnClickListener {
            openWebSearch("https://www.google.com/search?q=$code")
        }
        btnAmazon.setOnClickListener {
            openWebSearch("https://www.amazon.com/s?k=$code")
        }
        btnMercadoLibre.setOnClickListener {
            openWebSearch("https://listado.mercadolibre.com.mx/$code")
        }
    }

    private fun openWebSearch(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun fetchProductInfo(barcode: String) {
        val apiUrl = "https://api.upcitemdb.com/prod/trial/lookup?upc=$barcode"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = URL(apiUrl).readText()
                val json = JSONObject(response)
                val items = json.getJSONArray("items")

                val historyManager = HistoryManager(this@BarcodeResultActivity)

                if (items.length() > 0) {
                    val item = items.getJSONObject(0)
                    val title = item.getString("title")
                    val description = item.optString("description", "Sin descripción")
                    val imageUrl = if (item.has("images") && item.getJSONArray("images").length() > 0) {
                        item.getJSONArray("images").getString(0)
                    } else null

                    withContext(Dispatchers.Main) {
                        txtProductName.text = title
                        txtProductDesc.text = description
                        if (imageUrl != null) {
                            Glide.with(this@BarcodeResultActivity).load(imageUrl).into(imgProduct)
                        } else {
                            imgProduct.setImageResource(R.drawable.ic_no_image)
                        }

                        // Guardar en historial
                        lifecycleScope.launch {
                            historyManager.addHistory(
                                text = title,
                                imageUrl = imageUrl,
                                description = description,
                                barcode = barcode,
                                isQR = false
                            )
                        }
                    }

                } else {
                    // Producto no encontrado → aún guardar en historial
                    withContext(Dispatchers.Main) {
                        txtProductName.text = "Código detectado: $barcode"
                        txtProductDesc.text = "No se encontró información"
                        imgProduct.setImageResource(R.drawable.ic_no_image)

                        // Guardar en historial
                        lifecycleScope.launch {
                            historyManager.addHistory(
                                text = barcode,
                                imageUrl = null,
                                description = "No se encontró información",
                                barcode = barcode,
                                isQR = false
                            )
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@BarcodeResultActivity,
                        "Error al consultar producto",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Guardar en historial incluso si hay error
                    lifecycleScope.launch {
                        val historyManager = HistoryManager(this@BarcodeResultActivity)
                        historyManager.addHistory(
                            text = barcode,
                            imageUrl = null,
                            description = "Error al consultar producto",
                            barcode = barcode,
                            isQR = false
                        )
                    }
                }
            }
        }
    }

}
