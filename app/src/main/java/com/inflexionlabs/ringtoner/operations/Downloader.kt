package com.inflexionlabs.ringtoner.operations

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.media.RingtoneManager
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.inflexionlabs.ringtoner.firebase_database.model.Ringtone
import com.inflexionlabs.ringtoner.operations.states.DownloadState
import com.inflexionlabs.ringtoner.operations.states.RingtoneSetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.*
import java.net.URL

object Downloader {

    private val downloadingState : MutableState<DownloadState> = mutableStateOf(DownloadState.Empty)
    val downloadingStatePub : MutableState<DownloadState> = downloadingState

    private val ringtoneSetState : MutableState<RingtoneSetState> = mutableStateOf(RingtoneSetState.Empty)
    val ringtoneSetStatePub : MutableState<RingtoneSetState> = ringtoneSetState

    private val alarmToneSetState : MutableState<RingtoneSetState> = mutableStateOf(RingtoneSetState.Empty)
    val alarmToneSetStatePub : MutableState<RingtoneSetState> = alarmToneSetState

    private val notificationToneSetState : MutableState<RingtoneSetState> = mutableStateOf(RingtoneSetState.Empty)
    val notificationToneSetStatePub : MutableState<RingtoneSetState> = notificationToneSetState

    private val contactRingtoneSetState : MutableState<RingtoneSetState> = mutableStateOf(RingtoneSetState.Empty)
    val contactRingtoneSetStatePub : MutableState<RingtoneSetState> = contactRingtoneSetState

    private val file: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

    private val downloadJob = Job()
    private val downloadScope = CoroutineScope(downloadJob)

    private val ringtoneSetJob = Job()
    private val ringtoneSetScope = CoroutineScope(ringtoneSetJob)

    private val contactRingtoneSetJob = Job()
    private val contactRingtoneSetScope = CoroutineScope(contactRingtoneSetJob)

    fun downloadFromURL(ringtone: Ringtone) = downloadScope.launch (Dispatchers.IO){
        val songFile = File(file, ringtone.name+".mp3")

        downloadingState.value = DownloadState.Preparing
        if (!file.exists()){
            file.mkdir()
        }

        if (songFile.exists()){
            downloadingState.value = DownloadState.Downloaded
            Log.d("TAG", "from URl@")
            return@launch
        }else{
            try {
                downloadingState.value = DownloadState.Started
                val url1 = URL(ringtone.uri.toString())
                Log.d("TAG", "from URl")
                val input: InputStream = BufferedInputStream(url1.openStream())
                val output: OutputStream = FileOutputStream(songFile)
                val data = ByteArray(1024)
                var count: Int
                while (input.read(data).also { count = it } != -1) {
                    output.write(data, 0, count)
                }
                output.flush()
                output.close()
                input.close()
                Log.d("TAG", "from URl@")
                downloadingState.value = DownloadState.Downloaded
            } catch (e: Exception) {
                downloadingState.value = DownloadState.Failed
                e.printStackTrace()
                Log.d("TAG", "from Catch $e")
            }
        }

    }

    fun setRingtone(context: Context, file: File, ringtoneManagerTypeInt : Int) = ringtoneSetScope.launch(Dispatchers.Default){

        if (ringtoneManagerTypeInt == RingtoneManager.TYPE_RINGTONE){
            ringtoneSetState.value = RingtoneSetState.Setting
        }
        if (ringtoneManagerTypeInt == RingtoneManager.TYPE_ALARM){
            alarmToneSetState.value = RingtoneSetState.Setting
        }
        if (ringtoneManagerTypeInt == RingtoneManager.TYPE_NOTIFICATION){
            notificationToneSetState.value = RingtoneSetState.Setting
        }

        try {
            // check if file already exists in MediaStore
            val projection = arrayOf(MediaStore.Audio.Media._ID)
            val selectionClause = MediaStore.Audio.Media.DATA + " = ? "
            val selectionArgs = arrayOf(file.absolutePath)
            val cursor: Cursor? = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                null
            )

            val insertedUri: Uri? = if (cursor?.count!! < 1) {
                // not exist, insert into MediaStore
                val cv = ContentValues()
                cv.put(MediaStore.Audio.Media.DATA, file.absolutePath)
                cv.put(MediaStore.MediaColumns.TITLE, file.name+System.currentTimeMillis())
                cv.put(MediaStore.Audio.Media.IS_ALARM, true)

//        context.contentResolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, filePathToDelete, null);
                context.contentResolver
                    .insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cv)
            } else {
                // already exist
                cursor.moveToNext()
                val id = cursor.getLong(0)
                ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
            }

            cursor.close()
            RingtoneManager.setActualDefaultRingtoneUri(
                context,
                ringtoneManagerTypeInt,
                insertedUri
            )
            if (ringtoneManagerTypeInt == RingtoneManager.TYPE_RINGTONE){
                ringtoneSetState.value = RingtoneSetState.Set
            }
            if (ringtoneManagerTypeInt == RingtoneManager.TYPE_ALARM){
                alarmToneSetState.value = RingtoneSetState.Set
            }
            if (ringtoneManagerTypeInt == RingtoneManager.TYPE_NOTIFICATION){
                notificationToneSetState.value = RingtoneSetState.Set
            }
        }catch (e : Exception){
            if (ringtoneManagerTypeInt == RingtoneManager.TYPE_RINGTONE){
                ringtoneSetState.value = RingtoneSetState.Failed
            }
            if (ringtoneManagerTypeInt == RingtoneManager.TYPE_ALARM){
                alarmToneSetState.value = RingtoneSetState.Failed
            }
            if (ringtoneManagerTypeInt == RingtoneManager.TYPE_NOTIFICATION){
                notificationToneSetState.value = RingtoneSetState.Failed
            }
            e.stackTrace
        }
    }

    fun setContactRingtone(context: Context, file: File, contactUri: Uri) = contactRingtoneSetScope.launch(Dispatchers.Default) {

        contactRingtoneSetState.value = RingtoneSetState.Setting

        try {
            val projection = arrayOf(MediaStore.Audio.Media._ID)
            val selectionClause = MediaStore.Audio.Media.DATA + " = ? "
            val selectionArgs = arrayOf(file.absolutePath)
            val cursor: Cursor? = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                null
            )

            val insertedUri: Uri? = if (cursor?.count!! < 1) {
                // not exist, insert into MediaStore
                val cv = ContentValues()
                cv.put(MediaStore.Audio.Media.DATA, file.absolutePath)
                cv.put(MediaStore.MediaColumns.TITLE, file.name+System.currentTimeMillis())

//        context.contentResolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, filePathToDelete, null);
                context.contentResolver
                    .insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cv)
            } else {
                // already exist
                cursor.moveToNext()
                val id = cursor.getLong(0)
                ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
            }

            cursor.close()

            // Apply the custom ringtone
            val values = ContentValues(1)
            values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, insertedUri.toString())

            context.contentResolver.update(contactUri, values, null, null)

            contactRingtoneSetState.value = RingtoneSetState.Set
        }catch (e : Exception){
            contactRingtoneSetState.value = RingtoneSetState.Failed
            e.stackTrace
        }
    }

    fun resetStates(){
        ringtoneSetState.value = RingtoneSetState.Empty
        alarmToneSetState.value = RingtoneSetState.Empty
        contactRingtoneSetState.value = RingtoneSetState.Empty
        notificationToneSetState.value = RingtoneSetState.Empty
        downloadingState.value = DownloadState.Empty
    }

    fun downloadExists(name: String) : Boolean{
        val file = File(file, "$name.mp3")
        return file.exists()
    }
}