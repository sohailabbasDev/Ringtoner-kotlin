package com.inflexionlabs.ringtoner.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.inflexionlabs.ringtoner.R

private val UbuntuRegular = FontFamily(Font(R.font.ubuntu_regular))
private val RobotoLight = FontFamily(Font(R.font.roboto_light))
//private val SfPro = FontFamily(Font(R.font.sf_pro))


// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = UbuntuRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    body2 = TextStyle(
        fontFamily = RobotoLight,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)