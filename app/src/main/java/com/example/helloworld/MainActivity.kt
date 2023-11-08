package com.example.helloworld

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.helloworld.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yalantis.ucrop.UCrop
import okhttp3.OkHttpClient
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_OK ) {
            val uri = UCrop.getOutput(data!!)
            var dogImage = findViewById(R.id.mainImage) as ImageView
            dogImage.setImageURI(uri)
        }


        else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle the case where there was an error during cropping
            val cropError = UCrop.getError(data!!)
            Log.e("UCrop", "Error during crop: $cropError")
        }

        if (requestCode == 80 ) {


           var myUri: Uri  = data?.data!!
            UCrop.of(myUri,myUri )
                .withAspectRatio(16f, 9f)
                .withMaxResultSize(224, 224)

                .start(this)
        }


        if (requestCode == UCrop.REQUEST_CROP ) {

            if(data==null) return
            var appContainer = findViewById(R.id.bodyContainer) as RelativeLayout
            val uri = UCrop.getOutput(data!!)
//ToDO Open Visual Studio Code and Start the Server Instance.
            UploadUtility(this,appContainer).uploadFile(uri!!,"myImage",this)
            var dogImage = findViewById(R.id.mainImage) as ImageView
            dogImage.setImageURI(uri)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var appContainer = findViewById(R.id.bodyContainer) as RelativeLayout
        lateinit var binding:ActivityMainBinding
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            val galleryUri = it
            try{
               // binding..setImageURI(galleryUri)
                val options = UCrop.Options()
                val selectedImageUri: Uri = galleryUri as Uri // The Uri of the selected image
                val copyUri = createCopyUri(selectedImageUri)


                UCrop.of(copyUri as Uri,copyUri as Uri)
                    .withOptions(options)
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(224, 224)

                    .start(this)
            }catch(e:Exception){
                e.printStackTrace()
            }

        }




        var galleryButton = findViewById(R.id.galleryButton) as View
        // var cameraButton  = findViewById(R.id.cameraButton) as View


        var dogImage = findViewById(R.id.mainImage) as ImageView

        var fab = findViewById(R.id.backButton) as FloatingActionButton
        // fab.elevation=300f
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {


            if (it.resultCode == Activity.RESULT_OK) {

                val uri = it.data?.data!!
                // Toast.makeText(this, "potato po tatooo",Toast.LENGTH_LONG).show()
                dogImage.setImageURI(uri)

                // run("http://10.0.2.2:8000/",uri)
                UploadUtility(this,appContainer).uploadFile(uri,"myImage",this)
                // var intent = Intent (this, AnimalClassificationActivity::class.java)
                // intent.putExtra("Image Uri",uri.toString())
                //  startActivity(intent)
                // Use the uri to load the image
            }
        }



 val myVal=this
        ActivityResultContracts.PickVisualMedia()
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.

                if (uri != null) {
                    val originalUri = uri// Your original Uri goes here

// Extract the components of the original Uri
                    val scheme = originalUri.scheme // e.g., "content" or "file"
                    val authority = originalUri.authority // e.g., "com.example.provider" for content URIs
                    val path = originalUri.path // e.g., "/path/to/your/original/file"
                    val query = originalUri.encodedQuery // If your original Uri has query parameters

// Define the new file name
                    val newFileName = "new_file_name.jpg" // Your desired file name

// Create a new Uri with the same scheme, authority, path, and any query parameters
                    val newUri = Uri.Builder()
                        .scheme(scheme)
                        .authority(authority)
                        .path(path)
                        .encodedQuery(query) // Add query parameters if necessary
                        .appendPath(newFileName) // Append the new file name
                        .build()




                    UCrop.of(uri,newUri )
                        .withAspectRatio(16f, 9f)
                        .withMaxResultSize(224, 224)

                        .start(myVal)

                    //dogImage.setImageURI(uri)
                    Toast.makeText(this, "PhotoPicker Selected URI: $uri", Toast.LENGTH_LONG).show()
                } else {

                    Toast.makeText(this, "PhotoPicker No media selected", Toast.LENGTH_LONG).show()
                }
            }



// Create an AlphaAnimation for fading in/out

//// Create an AlphaAnimation for fading in/out
//        val alphaAnimation = AlphaAnimation(0.2f, 1.0f)
//        alphaAnimation.repeatMode = Animation.REVERSE
//        alphaAnimation.repeatCount = Animation.INFINITE
//        alphaAnimation.duration = 1000
//
//// Create a ScaleAnimation for scaling the glow effect
//
//// Create a ScaleAnimation for scaling the glow effect
//        val scaleAnimation = ScaleAnimation(
//            1.0f,
//            1.2f,
//            1.0f,
//            1.2f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f
//        )
//        scaleAnimation.repeatMode = Animation.REVERSE
//        scaleAnimation.repeatCount = Animation.INFINITE
//        scaleAnimation.duration = 1000
//
//// Create an AnimationSet to combine the animations
//
//// Create an AnimationSet to combine the animations
//        val animationSet = AnimationSet(true)
//        animationSet.addAnimation(alphaAnimation)
//        animationSet.addAnimation(scaleAnimation)
//
//// Apply the animation to the FAB
//
//// Apply the animation to the FAB
//        fab.startAnimation(animationSet)

//

//
//
//

////Photo by Lum3n: https://www.pexels.com/photo/closeup-photo-of-brown-and-black-dog-face-406014/
        galleryButton.setOnClickListener{
//            launcher.launch( ImagePicker.with(this)
//                //...
//                .galleryOnly()
//                .crop(1f,1f)
//               // .provider(ImageProvider.GALLERY) //Or bothCameraGallery()
//                .maxResultSize(224, 224)
//
//                .createIntent()
//            )
            //pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
          // val pickIntent =
         //       Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
          //  pickIntent.type = "image/*"
           // startActivityForResult(pickIntent, 80)

            galleryLauncher.launch("image/*")
        }
//
//        galleryButton.setOnClickListener{
//            launcher.launch( ImagePicker.with(this)
//                //...
//                .galleryOnly()
//                //.bothCameraGallery()
//                .crop(1f,1f)
//                //.provider(ImageProvider.BOTH) //Or bothCameraGallery()
//                .maxResultSize(224, 224)
//
//                .createIntent()
//
//            )
//        }
//        fab.setOnClickListener{
//           finish()
//        }

//
//
//        }
//




/*
    fun run(url: String, uri:Uri) {
        val JSON = MediaType.get("application/json; charset=utf-8")
      //  val jsonBody= "{\"name\":\"mario\" , \"description\":\"luigi\"}"
        val jsonBody= "{\"data\":\"$uri\"}"
        val body = RequestBody.create(JSON, jsonBody)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
       Log.d("my tag","sending req")
        Log.d("my tag",uri.toString())
        client.newCall(request).enqueue(object : Callback {
           override fun onFailure(call: Call, e: IOException) { Log.d("my tag",e.toString())}
           override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
        })
    }
*/

    }

    fun createNewImageUri(context: Context): Uri? {
        val randomName =  (UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE)
        val displayName =  (0..1000000).random().toString()+".jpg" // Replace with your desired filename
        val mimeType = "image/*" // Set the MIME type according to your image type

        val imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val newImage = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val newImageUri = context.contentResolver.insert(imageCollection, newImage)
        return newImageUri
    }


    fun createCopyUri(originalUri: Uri): Uri {
        val resolver = contentResolver
        val sourceFileDescriptor = resolver.openFileDescriptor(originalUri, "r")
        val sourceInputStream = FileInputStream(sourceFileDescriptor?.fileDescriptor)

        val copyUri = createNewImageUri(this) // Function to create a new writable Uri
        val targetFileDescriptor = resolver.openFileDescriptor(copyUri!!, "w")
        val targetOutputStream = FileOutputStream(targetFileDescriptor?.fileDescriptor)

        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (sourceInputStream.read(buffer).also { bytesRead = it } > 0) {
            targetOutputStream.write(buffer, 0, bytesRead)
        }

        sourceInputStream.close()
        targetOutputStream.close()

        return copyUri
    }

}

