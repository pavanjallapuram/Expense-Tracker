package com.example.zobazeexpensetracker.screens

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.zobazeexpensetracker.R
import com.example.zobazeexpensetracker.ui.theme.InterFontFamily
import kotlinx.coroutines.delay

@Preview
@Composable
fun SplashScreen(){

    Box(
        modifier = Modifier
            .fillMaxSize(), // Blue background
        contentAlignment = Alignment.Center
    ) {

        Image(painter = painterResource(R.drawable.ic_splash_png), contentDescription = "", modifier = Modifier.fillMaxSize())
        Text(
            text = "Zobaze ",
            fontSize = 36.sp,
            color = Color.White,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold
        )
    }

    // Delay then navigate
    LaunchedEffect(key1 = true) {
        delay(2000) // 2 seconds

    }

}