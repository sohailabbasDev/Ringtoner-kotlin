package com.inflexionlabs.ringtoner.presentation.screens.app_bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inflexionlabs.ringtoner.ui.theme.PrimaryColor
import com.inflexionlabs.ringtoner.ui.theme.PrimaryTextColor

@Composable
fun MainTopBar(text : String, spacing : Int, navigationBack : Boolean, onBackPressed : () -> Unit){
    TopAppBar(title = {
        Text(
        modifier = Modifier.fillMaxWidth(0.8f),
        textAlign = TextAlign.Center,
        text = text,
        fontSize = 16.sp,
        color = PrimaryTextColor,
        fontFamily = MaterialTheme.typography.body1.fontFamily,
        fontStyle = MaterialTheme.typography.body1.fontStyle,
        fontWeight = MaterialTheme.typography.body1.fontWeight,
        letterSpacing = spacing.sp)

        }
        , backgroundColor = PrimaryColor, elevation = 30.dp , navigationIcon = {
            if (navigationBack){
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.Default.ArrowBack,
                     contentDescription = "back arrow",
                     tint = PrimaryTextColor)
            }}
        })
}