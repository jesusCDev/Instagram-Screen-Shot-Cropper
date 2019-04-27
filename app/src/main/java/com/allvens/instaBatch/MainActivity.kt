package com.allvens.instaBatch

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.allvens.instaBatch.assets.Constants
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions


class MainActivity : AppCompatActivity() {

    private val PICK_IMAGES_REQUESTCODE = 123
    private var imagesUriPaths:ArrayList<Uri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs: SharedPreferences = getSharedPreferences(Constants.PREFS_NAMES.toString(), Context.MODE_PRIVATE)
        val prefEditor:SharedPreferences.Editor = prefs.edit()

        permissionManager(prefEditor)

        val sDeleteCroppedImage = findViewById<Switch>(R.id.s_MainActivity_deleteCroppedImages)
        val sAddPadding = findViewById<Switch>(R.id.s_MainActivity_AddPadding)

        /********** Switch Methods **********/
        set_SwitchParameterValues(Constants.DELETE_IMAGES_AFTER_CROPPING.toString(), false, sDeleteCroppedImage, prefs)
        set_SwitchParameterValues(Constants.ADD_PADDING_TO_TOP_BOTTOM.toString(), true, sAddPadding, prefs)

        sDeleteCroppedImage.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            change_SwitchParameterValues(Constants.DELETE_IMAGES_AFTER_CROPPING.toString(), isChecked, prefEditor)
        })
        sAddPadding.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            change_SwitchParameterValues(Constants.ADD_PADDING_TO_TOP_BOTTOM.toString(), isChecked, prefEditor)
        })

        /********** Show Cropping Complete **********/
        if(prefs.getBoolean(Constants.Boolean_CROPPED_IMAGES.toString(), false)){
            show_CroppingComploeteMessage()
            prefEditor.putBoolean(Constants.Boolean_CROPPED_IMAGES.toString(), false)
            prefEditor.commit()
        }
    }

    private fun show_CroppingComploeteMessage(){
        val hiddenLayout: LinearLayout = findViewById(R.id.ll_home_imagesHaveBeenCropped)
        hiddenLayout.visibility = View.VISIBLE
    }

    /****************************************
    **** SWITCH METHODS
    ****************************************/

    private fun set_SwitchParameterValues(prefID: String, defaultValue: Boolean, switch: Switch, prefs: SharedPreferences){
        switch.isChecked = prefs.getBoolean(prefID, defaultValue)
    }

    private fun change_SwitchParameterValues(prefID: String, value: Boolean, prefEditor: SharedPreferences.Editor){
        prefEditor.putBoolean(prefID, value)
        prefEditor.commit()
    }

    /****************************************
     **** BUTTON ACTIONS
     ****************************************/

    fun btnActionGoToAppInfo(view: View){
        startActivity(Intent(this, AppInfoActivity::class.java))
    }

    fun btnActionSelectImagesToCrop(view: View){
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.main_image_selector_title)), PICK_IMAGES_REQUESTCODE)
    }

    /****************************************
    **** IMAGE SELECTOR
    ****************************************/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGES_REQUESTCODE) {
            if(resultCode == Activity.RESULT_OK) {
                if(data != null){
                    imagesUriPaths.clear()

                    if(data.clipData != null) {
                        for(i:Int in 0..(data.clipData.itemCount - 1)) {
                            val imageUri: Uri = data.clipData.getItemAt(i).uri
                            imagesUriPaths.add(imageUri)
                        }
                    }else if(data.data != null){
                        val imageUri = data.data
                        imagesUriPaths.add(imageUri)
                    }

                    /************************* Go To Image Parameter Getter *************************/
                    val intent = Intent(this, CropSaveImagesActivity::class.java)
                    intent.putExtra(Constants.BOOLEAN_GET_CROP_PARAMETERS_FOR_IMAGES.toString(), true)
                    intent.putParcelableArrayListExtra(Constants.LIST_IMAGE_URI_PATHS.toString(), imagesUriPaths)
                    startActivity(intent)
                }
            }
        }
    }

    /****************************************
     **** PERMISSION METHODS
     ****************************************/

    private fun permissionManager(prefEditor:SharedPreferences.Editor){
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        Permissions.check(this, permissions, null, null, object : PermissionHandler() {
            override fun onGranted() {
                prefEditor.putBoolean(Constants.PERMISSIONS_ACCEPTED.toString(), true)
                prefEditor.commit()
            }

            override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                prefEditor.putBoolean(Constants.PERMISSIONS_ACCEPTED.toString(), false)
                prefEditor.commit()
                Toast.makeText(this@MainActivity, resources.getString(R.string.permission_deny_message), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
