package com.allvens.instaBatch

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.allvens.instaBatch.assets.Constants
import com.allvens.instaBatch.image_cropped_presenter.CropImages_Adapter
import com.allvens.instaBatch.image_cropped_presenter.ImageParameters_Parcelable
import com.theartofdev.edmodo.cropper.CropImage
import android.view.View
import android.widget.Toast
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions


class PresenterCropImagesActivity : AppCompatActivity() {

    private lateinit var imagesAndParameters: ArrayList<ImageParameters_Parcelable>
    private lateinit var cropAdapter: CropImages_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image_presenter)
        title = resources.getString(R.string.presenterCropImages_title)

        imagesAndParameters = intent.getParcelableArrayListExtra<ImageParameters_Parcelable>(Constants.LIST_IMAGES_AND_PARAMETERS.toString())
        create_RecycleView()
    }

    /****************************************
     **** BUTTON ACTIONS
     ****************************************/

    fun save_CropImages(view: View){
        if(checkAndGetPermissions()){
            goToImageCropper()
        }
    }

    private fun goToImageCropper(){
        val intent: Intent = Intent(this, CropSaveImagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(Constants.BOOLEAN_GET_CROP_PARAMETERS_FOR_IMAGES.toString(), false)
        intent.putParcelableArrayListExtra(Constants.LIST_IMAGES_AND_PARAMETERS.toString(), imagesAndParameters)
        startActivity(intent)
    }

    /****************************************
     **** RECYCLER VIEW
     ****************************************/

    private fun create_RecycleView() {
        val rv = findViewById<RecyclerView>(R.id.rvAnimals)
        rv.setHasFixedSize(true)
        cropAdapter = CropImages_Adapter(this, imagesAndParameters)
        val gridManager = GridLayoutManager(this, 3)
        rv.layoutManager = gridManager as RecyclerView.LayoutManager?
        rv.adapter = cropAdapter
    }

    /****************************************
    **** CROP EDITOR
    ****************************************/

    private fun update_CroppedImage(uri: Uri, rect: Rect){
        for((index, value) in imagesAndParameters.withIndex()){
            if(value._ImagePath == uri) imagesAndParameters[index].setNewRectValues(rect)
        }
        cropAdapter.swap(imagesAndParameters)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if(data != null){
                    val result = CropImage.getActivityResult(data)
                    update_CroppedImage(result.originalUri, result.cropRect)
                }
            }
        }
    }

    /****************************************
     **** ANDROID ACTIVITY METHODS
     ****************************************/

    override fun onBackPressed(){
        super.onBackPressed()
        this.finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    /****************************************
     **** PERMISSION METHODS
     ****************************************/

    private fun checkAndGetPermissions(): Boolean{
        val prefs: SharedPreferences = getSharedPreferences(Constants.PREFS_NAMES.toString(), Context.MODE_PRIVATE)
        val prefEditor:SharedPreferences.Editor = prefs.edit()

        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        Permissions.check(this, permissions, null, null, object : PermissionHandler() {
            override fun onGranted() {
                prefEditor.putBoolean(Constants.PERMISSIONS_ACCEPTED.toString(), true)
                prefEditor.commit()

                goToImageCropper()
            }

            override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                prefEditor.putBoolean(Constants.PERMISSIONS_ACCEPTED.toString(), false)
                prefEditor.commit()
                Toast.makeText(this@PresenterCropImagesActivity, resources.getString(R.string.permission_deny_message), Toast.LENGTH_SHORT).show()
            }
        })

        return prefs.getBoolean(Constants.PERMISSIONS_ACCEPTED.toString(), false)
    }
}