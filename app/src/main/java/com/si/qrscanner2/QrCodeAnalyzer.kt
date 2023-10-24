package com.si.qrscanner2

import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource

import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

class QrCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    val hints : HashMap<DecodeHintType, Any> = HashMap<DecodeHintType, Any>()
        .apply { put(DecodeHintType.POSSIBLE_FORMATS, arrayListOf(
            BarcodeFormat.QR_CODE, BarcodeFormat.CODE_128, BarcodeFormat.AZTEC,
            BarcodeFormat.CODABAR, BarcodeFormat.UPC_A)) }
        .apply { put(DecodeHintType.TRY_HARDER, true) }
    val reader = MultiFormatReader().apply { setHints(hints) }
    var imageCount = 0
    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
    )

    init {
        Log.i("analyzer", "New QrCodeAnalyzer()")
    }

    override fun analyze(image: ImageProxy) {
        imageCount ++;
        //Log.d("analyzer", "${imageCount}: ${image.format}, ${image.imageInfo.timestamp}")
        if (image.format in supportedImageFormats) {
            val bytes = image.planes.first().buffer.toByteArray()
            val source = PlanarYUVLuminanceSource(
                bytes,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )
            val binaryBmp = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = reader.decodeWithState(binaryBmp)
                onQrCodeScanned(result.text)
            } catch (e: Exception) {
                //Log.d("analyzer", "failure", e)
            } finally {
                image.close()
            }
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        return ByteArray(remaining()).also {
            get(it)
        }
    }
}