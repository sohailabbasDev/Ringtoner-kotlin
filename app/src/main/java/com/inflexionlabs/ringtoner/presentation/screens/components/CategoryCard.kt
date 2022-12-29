package com.inflexionlabs.ringtoner.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.inflexionlabs.ringtoner.firebase_database.model.Category
import com.inflexionlabs.ringtoner.ui.theme.PrimaryTextColor

@Composable
fun CategoryCard(modifier: Modifier, category: Category){

    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), elevation = 12.dp) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds,
                painter = rememberImagePainter(data = category.url, builder =  {
                    crossfade(500)
                    scale(Scale.FILL)
                }),
                contentDescription = category.text)
            Text(text = category.text!!,
                color = PrimaryTextColor, textAlign = TextAlign.Center,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                fontSize = 22.sp,
                fontStyle = MaterialTheme.typography.body1.fontStyle,
                fontWeight = MaterialTheme.typography.body1.fontWeight)
        }
    }

}