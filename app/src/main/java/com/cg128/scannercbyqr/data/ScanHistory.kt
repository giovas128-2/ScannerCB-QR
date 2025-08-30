package com.cg128.scannercbyqr.data

data class ScanHistory(
    val text: String,
    val date: String,
    val imageUrl: String? = null, // ← nueva propiedad
    val description: String? = null,
    val barcode: String? = null,
    val isQR: Boolean = false // nuevo campo

)
