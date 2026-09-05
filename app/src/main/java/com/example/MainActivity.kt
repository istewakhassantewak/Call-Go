package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.repository.CallAndGoRepository
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.CallAndGoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CallAndGoTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          val repository = remember { CallAndGoRepository(applicationContext) }
          AppNavigation(repository = repository)
        }
      }
    }
  }
}

