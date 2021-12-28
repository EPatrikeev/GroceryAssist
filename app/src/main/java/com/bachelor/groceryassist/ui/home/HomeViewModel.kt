package com.bachelor.groceryassist.ui.home

import android.graphics.Bitmap
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bachelor.groceryassist.model.GroceryItemModel
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText

class HomeViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

//    private val _price = MutableLiveData<Double>()
//
//    val price : LiveData<Double> = _price

    private val mPurchasesList = ArrayList<GroceryItemModel>()
    private val mPurchasesLiveData = MutableLiveData<ArrayList<GroceryItemModel>>()


    //    val purchases : LiveData<ArrayList<GroceryItemModel>> = _purchases

//    private val _purchases = MutableLiveData<GroceryItemModel>()
//
//    val purchases : LiveData<GroceryItemModel> = _purchases

    fun getPurchasesList(): LiveData<ArrayList<GroceryItemModel>> {
        return mPurchasesLiveData
    }


    open fun detectImage(rawImage : Bitmap){
        val image = FirebaseVisionImage.fromBitmap(rawImage)
//        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer //on device

        val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
            .setLanguageHints(listOf("ru"))
            .build()
        val detector = FirebaseVision.getInstance().getCloudTextRecognizer(options)

        val result = detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                // Task completed successfully
                textProcessingControl(firebaseVisionText)
            }
            .addOnFailureListener { e ->
                println(e.message)
                // Task f ailed with an exception
            }
    }

    private fun textProcessingControl(text: FirebaseVisionText) {
        // retrieve blocks of text using Firebase getTextBlocks()
        val blocks = text.textBlocks

        // if no text found, set text field
        if (blocks.size == 0) {
            return
        }

        // call method in TextProcessing class
        val detectedItemOfPurchase = parseForPrice(blocks)

        // set recognizedTextView to detected price
//        recognizedTextView.setTextSize(24f)
//        recognizedTextView.setText(detectedPriceString)
//        if (detectedPriceString === "") {
//            return
//        }
//        val detectedPriceDouble: Double = convertToDouble(detectedPriceString)



        mPurchasesList.add(detectedItemOfPurchase)
        mPurchasesLiveData.value = mPurchasesList


//        _purchases.value = detectedItemOfPurchase

        // get name of item from user in nameText
        // on OK, send input and price to items ArrayList
//        openNameInputDialog(detectedPriceDouble)
    }

    open fun deleteElement(position: Int){
        mPurchasesList.removeAt(position)
        mPurchasesLiveData.value = mPurchasesList
    }

    fun restoreElement(item: GroceryItemModel, position: Int) {
        mPurchasesList.add(position, item)
        mPurchasesLiveData.value = mPurchasesList
    }

    fun replaceElement(item: GroceryItemModel, position: Int) {
        mPurchasesList.removeAt(position)
        mPurchasesList.add(position, item)
        mPurchasesLiveData.value = mPurchasesList
    }

    private fun parseForPrice(blocks: List<FirebaseVisionText.TextBlock>): GroceryItemModel {
        // stores the tallest detected element that matches price regex
        var largestDetectedPrice = ""

        // stores the height of tallest detected price element for comparison
//        var largestDetectedHeight = 0
        var largestDetectedSquare = 0
        var currentlyDetectedSquare = 0

        // iterate through blocks --> lines --> elements
        for (block in blocks) {
            for (line in block.lines) {
                for (element in line.elements) {
                    if (TextUtils.isDigitsOnly(element.text)){
                        val elementText = element.text
                        val elementFrame = element.boundingBox

                        currentlyDetectedSquare = (elementFrame!!.right - elementFrame.left ) *
                                (elementFrame.bottom - elementFrame.top)
                         if (currentlyDetectedSquare > largestDetectedSquare){
                            largestDetectedSquare = currentlyDetectedSquare
                            largestDetectedPrice = elementText
                        }
                    }


                }
            }
        }
        val name = blocks[0].text
        val price = convertToDouble(largestDetectedPrice)

        return GroceryItemModel(name,1.0,price, price)
    }

    // converts price string to double
    private fun convertToDouble(str: String): Double {
        return str.toDouble()
    }

//    private fun <T> MutableLiveData<T>.notifyObserver() {
//        this.value = this.value
//    }
}