package com.si.qrscanner2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.si.qrscanner2.ui.theme.QrScanner2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    val codes : HashSet<String> = HashSet()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codes.add("Dummy test code")

        setContent {
            QrScanner2Theme {
                var statusTxt : String by remember { mutableStateOf("") }
                var code : String by remember { mutableStateOf("--none--") }
                val context = LocalContext.current
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
                Scaffold(
                    topBar = {
                        Row {
                            TextButton(onClick = {
                                statusTxt = "Sending..."
                                CoroutineScope(Dispatchers.Default).launch {
                                    val ok = sendEmail("starksm64@gmail.com",
                                        subject = "Scanned codes",
                                        body = codes.toString())
                                    withContext(Dispatchers.Main) {
                                        statusTxt = ok
                                    }
                                }
                                }) {
                                Text("Send Codes")
                            }
                            Text(text = statusTxt)
                        }
                    },
                    bottomBar = {
                        Text(
                            text = code,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                    }
                ) {
                    CameraScreen({newCode ->
                                 if(codes.add(newCode)) {
                                     code = newCode
                                     Log.i("analyzer", "New code: ${newCode}")
                                 }
                    }, modifier = Modifier.padding(it).fillMaxWidth().fillMaxHeight())
                }
            }
        }
    }

}