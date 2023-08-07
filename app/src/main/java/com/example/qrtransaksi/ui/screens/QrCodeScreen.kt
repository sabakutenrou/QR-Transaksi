package com.example.qrtransaksi.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.qrtransaksi.utils.QrCodeAnalyzer
import com.example.qrtransaksi.data.TransactionEvent
import com.example.qrtransaksi.ui.state.TransactionState

@Composable
fun QrCodeScreen(
    navController: NavHostController,
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
    balance: Int?
) {

    var blocks by remember {
        mutableStateOf(emptyList<String>())
    }
    var balance by remember {
        mutableStateOf(balance)
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (hasCamPermission) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(50.dp)) {
                AndroidView(
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val preview = Preview.Builder().build()
                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(
                                Size(
                                    previewView.width,
                                    previewView.height
                                )
                            )
                            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QrCodeAnalyzer { result ->
                                blocks = result.split(".")
                            }
                        )
                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        previewView
                    },
                    modifier = Modifier.weight(2f)
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (blocks.isNotEmpty()) {
                        onEvent(TransactionEvent.SetIdTransaksi(blocks[1]))
                        onEvent(TransactionEvent.SetBank(blocks[0]))
                        onEvent(TransactionEvent.SetMerchant(blocks[2]))
                        onEvent(TransactionEvent.SetNominal(blocks[3].toInt()))

                        Text(text = "Merchant: ${state.merchant}")
                        Text(text = "Nominal: ${state.nominal}")
                        Text(text = "ID Transaksi: ${state.id}")

                        Button(
                            onClick = {
                                val count = balance?.minus(state.nominal)
                                if (count != null) {
                                    if (count > 0) {
                                        balance = count
                                        onEvent(TransactionEvent.SaveTransaction)
                                        Toast.makeText(context, "Sukses", Toast.LENGTH_SHORT).show()
                                    } else {Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()}
                                }
                                navController.navigate(route = "mainscreen/$balance") {
                                    popUpTo("mainscreen/$balance") {
                                        inclusive = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(text = "Bayar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}