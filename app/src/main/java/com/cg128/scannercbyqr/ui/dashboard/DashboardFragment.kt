package com.cg128.scannercbyqr.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg128.scannercbyqr.databinding.FragmentDashboardBinding
import androidx.lifecycle.lifecycleScope
import com.cg128.scannercbyqr.data.HistoryManager
import com.cg128.scannercbyqr.ui.home.BarcodeResultActivity
import com.cg128.scannercbyqr.ui.home.QRResultActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryAdapter
    private lateinit var historyManager: HistoryManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // Configurar RecyclerView
        // Configurar RecyclerView con click en cada item
        adapter = HistoryAdapter(emptyList()) { historyItem ->
            if (historyItem.isQR) {
                val intent = Intent(requireContext(), QRResultActivity::class.java).apply {
                    putExtra("fromHistory", true)  // Indica que viene del historial
                    putExtra("qr_url", historyItem.barcode ?: historyItem.text)
                }
                startActivity(intent)
            } else {
                val intent = Intent(requireContext(), BarcodeResultActivity::class.java).apply {
                    putExtra("fromHistory", true)
                    putExtra("title", historyItem.text)
                    putExtra("description", historyItem.description ?: "Sin descripción")
                    putExtra("imageUrl", historyItem.imageUrl)
                    putExtra("barcode_value", historyItem.barcode ?: historyItem.text)
                }
                startActivity(intent)
            }
        }



        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter

        // Botón de eliminar historial (luego lo conectamos con DataStore)
        binding.fabDeleteTask.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                historyManager.clearHistory()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyManager = HistoryManager(requireContext())

        // Observar historial desde DataStore
        viewLifecycleOwner.lifecycleScope.launch {
            historyManager.getHistory().collect { list ->
                adapter.updateData(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
