package com.inflexionlabs.ringtoner.presentation.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inflexionlabs.ringtoner.operations.Downloader
import com.inflexionlabs.ringtoner.operations.states.DownloadState
import com.inflexionlabs.ringtoner.operations.states.RingtoneSetState

@Composable
fun RingtoneOptions(modifier: Modifier,
                    isFromDownload : Boolean = false,
                    isForContact : Boolean = false,
                    imageVector: ImageVector,
                    ringtoneSetState: RingtoneSetState = RingtoneSetState.Empty,
                    text : String){
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(22.dp))
        if (isFromDownload){
            when (Downloader.downloadingStatePub.value) {
                DownloadState.Empty -> {
                    Icon(modifier = Modifier
                        .height(44.dp).width(44.dp).padding(6.dp),
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download")
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Download",
                        fontSize = 26.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        color = MaterialTheme.colors.surface)
                }
                DownloadState.Started -> {
                    CircularProgressIndicator( color = MaterialTheme.colors.surface)
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Downloading...",
                        fontSize = 26.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        color = MaterialTheme.colors.surface)
                }
                DownloadState.Preparing -> {
                    CircularProgressIndicator( color = MaterialTheme.colors.surface)
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Starting download",
                        fontSize = 26.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        color = MaterialTheme.colors.surface)
                }
                DownloadState.Downloaded -> {
                    Icon(modifier = Modifier
                        .height(44.dp).width(44.dp).padding(6.dp),
                        imageVector = Icons.Default.DownloadDone,
                        contentDescription = "Downloaded")
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Downloaded",
                        fontSize = 26.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        color = MaterialTheme.colors.surface)
                }
                DownloadState.Failed -> {
                    Icon(modifier = Modifier
                        .height(44.dp).width(44.dp).padding(6.dp),
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download")
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Download",
                        fontSize = 26.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        color = MaterialTheme.colors.surface)
                }
            }
        }else{
            when(ringtoneSetState){
                is RingtoneSetState.Empty -> {
                    Icon(modifier = Modifier
                        .height(44.dp).width(44.dp).padding(6.dp),
                        imageVector = imageVector,
                        contentDescription = text)
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = text,
                        maxLines = 1,
                        fontSize = 26.sp,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        color = MaterialTheme.colors.surface)
                }
                is RingtoneSetState.Failed -> {
                    Icon(modifier = Modifier
                        .height(44.dp).width(44.dp).padding(6.dp),
                        imageVector = imageVector,
                        contentDescription = text)
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = text,
                        fontSize = 26.sp,
                        maxLines = 1,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        color = MaterialTheme.colors.surface)
                }
                is RingtoneSetState.Setting -> {
                    CircularProgressIndicator( color = MaterialTheme.colors.surface)
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Setting...",
                        fontSize = 26.sp,
                        maxLines = 1,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        color = MaterialTheme.colors.surface)
                }
                is RingtoneSetState.Set -> {
                    Icon(modifier = Modifier
                        .height(44.dp).width(44.dp).padding(6.dp),
                        imageVector = if (isForContact) Icons.Default.Contacts else Icons.Default.Done,
                        contentDescription = text)
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = text,
                        fontSize = 26.sp,
                        maxLines = 1,
                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                        color = MaterialTheme.colors.surface)
                }
            }
        }
    }
}