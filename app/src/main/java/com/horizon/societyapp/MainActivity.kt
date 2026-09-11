package com.horizon.societyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.horizon.societyapp.ui.navigation.SocietyNavGraph
import com.horizon.societyapp.ui.theme.SocietyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocietyAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SocietyNavGraph(application = application as SocietyApplication)
                }
            }
        }
    }
}
