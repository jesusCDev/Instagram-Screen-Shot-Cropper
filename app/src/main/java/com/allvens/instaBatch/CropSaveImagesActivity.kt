package com.allvens.instaBatch

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.allvens.instaBatch.image_cropped_presenter.ImageParameters_Parcelable
import com.allvens.instaBatch.assets.Constants
import com.allvens.instaBatch.assets.file_manager.File_Editor
import com.allvens.instaBatch.assets.notification_system.Notification_Manager
import com.allvens.instaBatch.image_parameter_system.ImageContentFinder_Parameter
import com.allvens.instaBatch.image_parameter_system.position.Rectangle_Parameter
import java.util.*

class CropSaveImagesActivity: AppCompatActivity(){

    private lateinit var tvProgessIndicater:TextView
    
    private var selectedImages: ArrayList<Uri> = ArrayList()
    private lateinit var imagesAndParameters: ArrayList<ImageParameters_Parcelable>

    private var createImagesParameters: Boolean = true
    private var userInsideApp: Boolean = true
    private var doneWithCurrentTask = false
    private var exitCurrentThread: Boolean = false

    private lateinit var notification_manager: Notification_Manager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cropping_loader)

        tvProgessIndicater = findViewById(R.id.tv_cropLoader_percentageShower)
        val tvActionLabel: TextView = findViewById(R.id.tv_cropLoader_ActionLabel)

        /************************* Receiver From Share Button *************************/
        when {
            intent?.action == Intent.ACTION_SEND &&
                    (intent.type?.startsWith("image/") == true)-> {
                handleSendImage(intent)
            }
            intent?.action == Intent.ACTION_SEND_MULTIPLE
                    && intent.type?.startsWith("image/") == true -> {
                handleSendMultipleImages(intent)
            }
            else -> {
                createImagesParameters = intent.getBooleanExtra(Constants.BOOLEAN_GET_CROP_PARAMETERS_FOR_IMAGES.toString(), true)
                if(createImagesParameters) selectedImages = intent.getParcelableArrayListExtra<Uri>(Constants.LIST_IMAGE_URI_PATHS.toString())
            }
        }

        val prefs: SharedPreferences = getSharedPreferences(Constants.PREFS_NAMES.toString(), Context.MODE_PRIVATE)
        notification_manager = Notification_Manager(this)

        /************************* Loading Methods *************************/
        if(createImagesParameters){
            tvActionLabel.text = resources.getString(R.string.loadCropImages_title_getCropParameters)
            Thread( Runnable {
                run {
                    notification_manager.createProgressNotification(resources.getString(R.string.loadCropImages_notificationTitle_GettingParameters), "0 %", selectedImages.size)
                    findAndCreate_ImageParameters(prefs.getBoolean(Constants.ADD_PADDING_TO_TOP_BOTTOM.toString(), true))
                }
            }).start()
        }else{
            tvActionLabel.text = resources.getString(R.string.loadCropImages_title_savingImages)
            imagesAndParameters = intent.getParcelableArrayListExtra<ImageParameters_Parcelable>(Constants.LIST_IMAGES_AND_PARAMETERS.toString())
            Thread( Runnable {
                run {
                    notification_manager.createProgressNotification(resources.getString(R.string.loadCropImages_notificationTitle_SavingImages),"0 %", imagesAndParameters.size)
                    saveImageWithCroppedParameter(prefs.getBoolean(Constants.DELETE_IMAGES_AFTER_CROPPING.toString(), false))
                }
            }).start()
        }
    }

    /****************************************
     **** RECEIVER IMAGES FROM SHARE BUTTON
     ****************************************/

    private fun handleSendImage(intent: Intent) {
        val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        if(imageUri != null) selectedImages.add(imageUri)
    }

    private fun handleSendMultipleImages(intent: Intent) {
        val imageUris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
        if(imageUris != null) selectedImages.addAll(imageUris)
    }

    /****************************************
     **** SAVE CROPPED IMAGES, SCAN, AND GO BACK TO MAIN PAGE
     ****************************************/

    private fun saveImageWithCroppedParameter(deleteImageAfterSaving: Boolean){
        val editor: File_Editor =
            File_Editor(this)

        for((index: Int, cropImageAndParameters: ImageParameters_Parcelable) in imagesAndParameters.withIndex()){
            editor.save_ImageToDevice(cropImageAndParameters)
            if(deleteImageAfterSaving) editor.delete_AlteredImage(cropImageAndParameters._ImagePath)
            update_ProgressIndicater(imagesAndParameters.size, index)
            if(exitCurrentThread){
                notification_manager.cancelNotification()
                break
            }
        }

        if(!exitCurrentThread){
            notification_manager.createFinishNotification()
            if(userInsideApp) goTo_MainPage()
            doneWithCurrentTask = true
        }
    }

    private fun goTo_MainPage(){

        /************************* Handles Completion Showing *************************/
        val prefs: SharedPreferences = getSharedPreferences(Constants.PREFS_NAMES.toString(), Context.MODE_PRIVATE)
        val prefEditor:SharedPreferences.Editor = prefs.edit()

        prefEditor.putBoolean(Constants.Boolean_CROPPED_IMAGES.toString(), true)
        prefEditor.commit()


        /************************* Sends Person Back *************************/
        notification_manager.cancelNotification()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    /****************************************
     **** FIND MAIN IMAGE AND CREATE PARAMETERS
     ****************************************/

    private fun findAndCreate_ImageParameters(addingPaddingToImage: Boolean){
        val contentFinder = ImageContentFinder_Parameter(this, addingPaddingToImage)
        imagesAndParameters = ArrayList()

        var currentImageParameters: Rectangle_Parameter
        for((positionOfCurrentImageInArray:Int , imagePath:Uri ) in selectedImages.withIndex()){
            currentImageParameters = contentFinder.get_ImageCropParameters(imagePath)
            if(currentImageParameters != null){
                imagesAndParameters.add(ImageParameters_Parcelable(currentImageParameters))
            }
            update_ProgressIndicater(selectedImages.size, positionOfCurrentImageInArray)
            if(exitCurrentThread){
                notification_manager.cancelNotification()
                break
            }
        }

        if(!exitCurrentThread){
            notification_manager.createFinishNotification()
            if(userInsideApp) goTo_CropImagePresenter()
            doneWithCurrentTask = true
        }else{
            notification_manager.cancelNotification()
        }
    }

    private fun goTo_CropImagePresenter(){
        notification_manager.cancelNotification()
        val intent = Intent(this, PresenterCropImagesActivity::class.java)
        intent.putParcelableArrayListExtra(Constants.LIST_IMAGES_AND_PARAMETERS.toString(), imagesAndParameters)
        startActivity(intent)
    }

    /****************************************
     **** UI METHODS
     ****************************************/

    private fun update_ProgressIndicater(totalValue: Int, value: Int){
        val progressValue = ((value.toDouble() / totalValue.toDouble()) * 100).toInt()
        this.runOnUiThread{tvProgessIndicater.text = "${progressValue} %"}
        notification_manager.updateProgressNotification(totalValue, value, progressValue)
    }

    /****************************************
     **** TRACK IN APP SCREEN CHANGING METHODS
     ****************************************/

    override fun onStart() {
        super.onStart()
        userInsideApp = true

        if(createImagesParameters && doneWithCurrentTask){
            goTo_CropImagePresenter()
        }else if (!createImagesParameters && doneWithCurrentTask){
            goTo_MainPage()
        }
    }

    override fun onStop() {
        super.onStop()
        userInsideApp = false
    }

    override fun onBackPressed(){
        super.onBackPressed()
        exitCurrentThread = true
        this.finish()
    }
}
