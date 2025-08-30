package com.cg128.scannercbyqr.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

val Context.dataStore by preferencesDataStore(name = "scan_history")

class HistoryManager(private val context: Context) {

    private val HISTORY_KEY = stringSetPreferencesKey("history_list")

    // Obtener historial
    fun getHistory(): Flow<List<ScanHistory>> {
        return context.dataStore.data.map { prefs ->
            prefs[HISTORY_KEY]?.map { entry ->
                val parts = entry.split("|")
                ScanHistory(
                    text = parts.getOrNull(0) ?: "Desconocido",
                    date = parts.getOrNull(1) ?: "",
                    imageUrl = parts.getOrNull(2)?.takeIf { it.isNotEmpty() },
                    description = parts.getOrNull(3),
                    barcode = parts.getOrNull(4),
                    isQR = parts.getOrNull(5)?.toBoolean() ?: false
                )
            } ?: emptyList()
        }
    }



    // Agregar historial
    suspend fun addHistory(
        text: String,
        imageUrl: String? = null,
        description: String? = null,
        barcode: String? = null,
        isQR: Boolean = false
    ) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val timestamp = sdf.format(Date())
        val entry = listOf(text, timestamp, imageUrl ?: "", description ?: "", barcode ?: "", isQR.toString()).joinToString("|")

        context.dataStore.edit { prefs ->
            val current = prefs[HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            current.add(entry)
            prefs[HISTORY_KEY] = current
        }
    }


    // Limpiar historial
    suspend fun clearHistory() {
        context.dataStore.edit { prefs ->
            prefs.remove(HISTORY_KEY)
        }
    }
}
