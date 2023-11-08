package com.example.helloworld

import android.R
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.webkit.MimeTypeMap
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File




class UploadUtility(activity: Activity,appContainer:RelativeLayout,) {

    var activity = activity;
    var appContainer= appContainer
    var dialog: ProgressDialog? = null

    //http://10.0.2.2:1000/
    var serverURL: String = "http://10.0.2.2:1000/"
    var serverUploadDirectoryPath: String = "https://handyopinion.com/tutorials/uploads/"
    val client = OkHttpClient()

    fun uploadFile(sourceFilePath: String, uploadedFileName: String? = null , activity: Activity) {
        uploadFile(File(sourceFilePath), uploadedFileName, activity = activity)
    }

    fun uploadFile(sourceFileUri: Uri, uploadedFileName: String? = null, activity: Activity) {
        val pathFromUri = URIPathHelper().getPath(activity,sourceFileUri)
        uploadFile(File(pathFromUri), uploadedFileName, sourceFileUri, activity = activity)
    }


 //toDo Change Upload to also Pass the URI



    fun uploadFile(sourceFile: File, uploadedFileName: String? = null , imageUri: Uri? = null, activity: Activity) {

        val extentionRegex = "\\..*".toRegex()


       // if(fileName==fileName)
        if(sourceFile.name==null) return
        Thread {

           val mimeType = extentionRegex.find(sourceFile.name)?.groups?.get(0)?.value;
            val test = "jpg"?.toMediaTypeOrNull()
            if(test==test){}
            if (mimeType==null) {
                Log.e("file error", "Not able to get mime type")
                return@Thread
            }
            val fileName: String = if (uploadedFileName == null)  sourceFile.name else uploadedFileName
            toggleProgressDialog(true)
            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                       .addFormDataPart("canvasImage", fileName,sourceFile.asRequestBody("jpg".toMediaTypeOrNull()))
                        .build()

                val request: Request = Request.Builder().url(serverURL).post(requestBody).build()

                val response: Response = client.newCall(request).execute()


                if (response.isSuccessful) {
                    Log.d("File upload","success, path: $serverUploadDirectoryPath$fileName")
                    showToast("File uploaded successfully at $serverUploadDirectoryPath$fileName")
                    var intent = Intent (activity, AnimalClassificationActivity::class.java)
                    intent.putExtra("Image Uri",imageUri.toString())

                    var jsonBody: JSONObject
                    response.body?.let {
                        jsonBody = JSONObject(it.string())
                        intent.putExtra("breed",jsonBody.get("breed") as String)
                        intent.putExtra("link",jsonBody.get("href") as String)


                    }

                    activity.startActivity(intent)
                } else {
                    Log.e("File upload", "failed")
                    Log.e("Error: ",response.toString() )
                    showToast("File uploading failed")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("Error: ",ex.toString() )
                Log.e("File upload", "failed")
               // showToast("File uploading failed")
            }
            toggleProgressDialog(false)
        }.start()
    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun showToast(message: String) {
        activity.runOnUiThread {
            Toast.makeText( activity, message, Toast.LENGTH_LONG ).show()
        }
    }

    fun toggleProgressDialog(show: Boolean) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", "Uploading file...", true);
            } else {
                dialog?.dismiss();
            }
        }
    }

}