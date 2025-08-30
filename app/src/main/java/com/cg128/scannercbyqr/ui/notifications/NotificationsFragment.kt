package com.cg128.scannercbyqr.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cg128.scannercbyqr.R
import com.cg128.scannercbyqr.databinding.FragmentNotificationsBinding
import androidx.appcompat.app.AppCompatDelegate

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 🔹 Expandir / ocultar sección de Permisos
        binding.btnAccess.setOnClickListener {
            toggleVisibility(binding.subAccess)
        }

        // 🔹 Expandir / ocultar sección de Preferencias
        binding.btnpreferencias.setOnClickListener {
            toggleVisibility(binding.subPreferencias)
        }

        // 🔹 Expandir / ocultar sección de Historial
        binding.btnHistorial.setOnClickListener {
            toggleVisibility(binding.subHistorial)
        }

        // 🔹 Expandir / ocultar sección de Acerca de
        binding.btnAcercaDe.setOnClickListener {
            toggleVisibility(binding.subAcercaDe)
        }

        binding.switchModoClaro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // 🌞 Claro
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // 🌙 Oscuro
            }
        }
        return root
    }
    // 🔹 Función reutilizable para mostrar/ocultar layouts
    private fun toggleVisibility(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}