package com.example.face_detector

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn_camera = findViewById<Button>(R.id.btncamera)



        btn_camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager)!=null){
             startActivityForResult(intent,1)
            }else{
                Toast.makeText(this,"Please Capture Your Image First",Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1&&resultCode== RESULT_OK&&data!=null){
            val extra = data.extras
            val bitmap = extra!!.get("data") as Bitmap
            detectface(bitmap)

        }
    }

    private fun detectface(bitmap: Bitmap) {
       var faceText = findViewById<TextView>(R.id.faceText)
        var smileText = findViewById<TextView>(R.id.smileText)
        var leftEyeText = findViewById<TextView>(R.id.leftEyeText)
        var rightEyeText = findViewById<TextView>(R.id.rightEyeText)
        var smileImage = findViewById<ImageView>(R.id.smileImage)
        val allOption= FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()


        val detector = FaceDetection.getClient(allOption)
        val image = InputImage.fromBitmap(bitmap, 0)
        val result = detector.process(image)
            .addOnSuccessListener { faces ->
               var faceNumber =""
                var smilingPercentage=""
                var leftEyeOpen = ""
                var rightEyeOpen = ""
                var i = 1
                for (face in faces){
                  faceNumber= "Face = $i"
                    smilingPercentage = "Smile = ${face.smilingProbability?.times(100)}%"
                    leftEyeOpen="Left Eye Open= ${face.leftEyeOpenProbability?.times(100)}%"
                    rightEyeOpen="Right Eye Open= ${face.rightEyeOpenProbability?.times(100)}% "

                    i++
                }//end of for loop
              faceText.setText(faceNumber)
                if (smilingPercentage< 50.00.toString()){
                    smileImage.setImageResource(R.drawable.sad_two_icon)

                }
                smileText.setText(smilingPercentage)
                leftEyeText.setText(leftEyeOpen)
                rightEyeText.setText(rightEyeOpen)

                if (faces.isEmpty()){
                    Toast.makeText(this,"Face Not Detected ",Toast.LENGTH_LONG).show()
                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(this@MainActivity,"someThing is Wrong + ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }


}