package com.cg128.scannercbyqr.ui.home


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cg128.scannercbyqr.R
import com.cg128.scannercbyqr.databinding.FragmentHomeBinding
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.File


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    // Estado de la lampara
    private var isLamparaOn = false
    private var cameraManager: CameraManager? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: androidx.camera.core.Camera? = null
    private var cameraId: String? = null
    private val CAMERA_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
    private val REQUEST_CAMERA_CODE = 200
    private var imageCapture: ImageCapture? = null
    private var barcodeDetected = false
    private enum class ScanMode { BARCODE, QR }
    private var currentScanMode = ScanMode.BARCODE





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCameraPermission()


        // 🎯 Aquí ya puedes usar tus botones
        binding.btnGaleria.setOnClickListener {
            setButtonActive(binding.btnGaleria)
            openGallery()
        }

        binding.btnCamara.setOnClickListener {
            activateTempButton(binding.btnCamara)
            // Aquí tu lógica para sacar la foto
            takePhoto()
            //tomar foto en btncamara
        }

        // Inicializar CameraManager
        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager?.cameraIdList?.firstOrNull { id ->
                cameraManager?.getCameraCharacteristics(id)
                    ?.get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        // 🔦 Botón Lampara
        binding.btnLampara.setOnClickListener {
            isLamparaOn = !isLamparaOn
            toggleFlash(isLamparaOn)
        }

        binding.btnEscanearBarras.setOnClickListener {
            setActiveButton(true) // botón de barras activo
            // Aquí va tu lógica para escanear código de barras
            currentScanMode = ScanMode.BARCODE
            barcodeDetected = false
        }

        binding.btnEscanearQR.setOnClickListener {
            setActiveButton(false) // botón de QR activo
            // Aquí va tu lógica para escanear QR
            currentScanMode = ScanMode.QR
            barcodeDetected = false
        }
    }

    /** Encender/apagar linterna */
    private fun toggleFlash(turnOn: Boolean) {
        if (camera == null) {
            Toast.makeText(requireContext(), "Cámara no inicializada aún", Toast.LENGTH_SHORT).show()
            return
        }

        // Revisamos si la cámara tiene flash
        if (camera?.cameraInfo?.hasFlashUnit() == true) {
            try {
                camera?.cameraControl?.enableTorch(turnOn)
                if (turnOn) {
                    setButtonActive(binding.btnLampara)
                } else {
                    setButtonInactive(binding.btnLampara)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error al controlar la linterna", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Este dispositivo no tiene flash disponible", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setActiveButton(isBarCodeActive: Boolean) {
        if (isBarCodeActive) {
            binding.btnEscanearBarras.setBackgroundColor(resources.getColor(R.color.purple_700))
            binding.btnEscanearBarras.setTextColor(resources.getColor(android.R.color.black))

            binding.btnEscanearQR.setBackgroundColor(resources.getColor(R.color.teal_700))
            binding.btnEscanearQR.setTextColor(resources.getColor(android.R.color.white))
        } else {
            binding.btnEscanearQR.setBackgroundColor(resources.getColor(R.color.purple_700))
            binding.btnEscanearQR.setTextColor(resources.getColor(android.R.color.black))

            binding.btnEscanearBarras.setBackgroundColor(resources.getColor(R.color.teal_700))
            binding.btnEscanearBarras.setTextColor(resources.getColor(android.R.color.white))
        }
    }
    // 🔹 Función para botones temporales (0.5 seg)
    private fun activateTempButton(button: View) {
        setButtonActive(button)
        Handler(Looper.getMainLooper()).postDelayed({
            setButtonInactive(button)
        }, 100) // 0.1 segundos
    }

    // 🔹 Función para poner botón activo
    private fun setButtonActive(button: View) {
        when (button) {
            is ImageButton -> {
                button.background.setTint(ContextCompat.getColor(requireContext(), R.color.purple_700))
                button.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.black))
            }
            is Button -> {
                button.background.setTint(ContextCompat.getColor(requireContext(), R.color.purple_700))
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            }
        }
    }

    // 🔹 Función para poner botón inactivo
    private fun setButtonInactive(button: View) {
        when (button) {
            is ImageButton -> {
                button.background.setTint(ContextCompat.getColor(requireContext(), R.color.teal_700))
                button.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.white))
            }
            is Button -> {
                button.background.setTint(ContextCompat.getColor(requireContext(), R.color.teal_700))
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            }
        }
    }

    // 🔹 Función para abrir galería
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100) // Request code 100
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                try {
                    // Convierte URI a Bitmap
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                    val image = InputImage.fromBitmap(bitmap, 0)

                    // Detectar códigos
                    detectBarcodeFromImage(image)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error al procesar la imagen", Toast.LENGTH_SHORT).show()
                }
            }

            // Volvemos el botón a estado inactivo
            setButtonInactive(binding.btnGaleria)
        }
    }

    private fun detectBarcodeFromImage(image: InputImage) {
        // 🔹 Seleccionamos qué tipo de código detectar según el modo actual
        val formats = when(currentScanMode) {
            ScanMode.QR -> Barcode.FORMAT_QR_CODE
            ScanMode.BARCODE -> Barcode.FORMAT_CODE_128 or
                    Barcode.FORMAT_EAN_13 or
                    Barcode.FORMAT_UPC_A or
                    Barcode.FORMAT_UPC_E
        }

        val scanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(formats)
                .build()
        )

        barcodeDetected = false

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (!barcodeDetected && barcodes.isNotEmpty()) { // solo detecta el primer código
                    val barcode = barcodes.first()
                    barcodeDetected = true
                    if (barcode.format == Barcode.FORMAT_QR_CODE) {
                        val url = barcode.rawValue ?: ""
                        openQRResult(url)
                    } else {
                        val code = barcode.rawValue ?: ""
                        openBarcodeResult(code)
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(requireContext(), "No se detectó código de barras ni QR", Toast.LENGTH_SHORT).show()
            }
    }



    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(CAMERA_PERMISSION, REQUEST_CAMERA_CODE)
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }
    //funcion para iniciar la camara
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.pvcamera.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(requireActivity().windowManager.defaultDisplay.rotation)
                .build()

            val imageAnalyzer = ImageAnalysis.Builder().build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(requireContext())) { imageProxy ->
                        processImageProxy(imageProxy)
                    }
                }

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }


    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // 🔹 Ajustamos el tipo de códigos a detectar según el botón presionado
            val formats = when (currentScanMode) {
                ScanMode.QR -> Barcode.FORMAT_QR_CODE
                ScanMode.BARCODE -> Barcode.FORMAT_CODE_128 or
                        Barcode.FORMAT_EAN_13 or
                        Barcode.FORMAT_UPC_A or
                        Barcode.FORMAT_UPC_E
            }

            val scanner = BarcodeScanning.getClient(
                BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(formats)
                    .build()
            )

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (!barcodeDetected && barcodes.isNotEmpty()) { // solo detecta el primer código
                        val barcode = barcodes.first()
                        barcodeDetected = true
                        if (barcode.format == Barcode.FORMAT_QR_CODE) {
                            val url = barcode.rawValue ?: ""
                            openQRResult(url)
                        } else {
                            val code = barcode.rawValue ?: ""
                            openBarcodeResult(code)
                        }
                    }
                }
                .addOnFailureListener { it.printStackTrace() }
                .addOnCompleteListener { imageProxy.close() }

        } else {
            imageProxy.close()
        }
    }


    override fun onResume() {
        super.onResume()
        barcodeDetected = false
    }


    //funciones para abrir nuevas ventanas
    //ventana para QR
    private fun openQRResult(url: String) {
        val intent = Intent(requireContext(), QRResultActivity::class.java)
        intent.putExtra("qr_url", url)
        startActivity(intent)
    }
    //ventana para codigo en barra
    private fun openBarcodeResult(code: String) {
        val intent = Intent(requireContext(), BarcodeResultActivity::class.java)
        intent.putExtra("barcode_value", code)
        startActivity(intent)
    }



    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            requireContext().externalMediaDirs.first(),
            "foto_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Error tomando foto: ${exc.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(requireContext(), "Foto guardada: ${photoFile.absolutePath}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

}
