package com.toddler.footsteps.ai

import android.app.Application
import com.toddler.footsteps.ml.LiteModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class Model(
    application: Application
) {

    val context = application

    fun runModel(
        feature0: ByteBuffer,
        feature1: ByteBuffer,
        feature2: ByteBuffer,
        feature3: ByteBuffer,
        feature4: ByteBuffer,
        feature5: ByteBuffer,
        feature6: ByteBuffer,
        feature7: ByteBuffer,
        feature8: ByteBuffer,
        feature9: ByteBuffer,
        feature10:ByteBuffer,
        feature11: ByteBuffer
    ): String{


        val model = LiteModel.newInstance(context)

// Creates inputs for reference
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature0.loadBuffer(feature0)
        val inputFeature1 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature1.loadBuffer(feature1)
        val inputFeature2 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature2.loadBuffer(feature2)
        val inputFeature3 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature3.loadBuffer(feature3)
        val inputFeature4 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature4.loadBuffer(feature4)
        val inputFeature5 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature5.loadBuffer(feature5)
        val inputFeature6 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature6.loadBuffer(feature6)
        val inputFeature7 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature7.loadBuffer(feature7)
        val inputFeature8 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature8.loadBuffer(feature8)
        val inputFeature9 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature9.loadBuffer(feature9)
        val inputFeature10 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature10.loadBuffer(feature10)
        val inputFeature11 =
            TensorBuffer.createFixedSize(intArrayOf(1, 100, 1), DataType.FLOAT32)
        inputFeature11.loadBuffer(feature11)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0, inputFeature1, inputFeature2, inputFeature3, inputFeature4, inputFeature5, inputFeature6, inputFeature7, inputFeature8, inputFeature9, inputFeature10, inputFeature11)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val outputData = outputFeature0.floatArray
        val outputString = outputData.joinToString("\n")


        // Releases model resources if no longer used.
        model.close()

        return outputString
    }

}