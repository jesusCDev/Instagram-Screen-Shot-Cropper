package com.allvens.instaBatch.assets.file_manager

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.allvens.instaBatch.R
import com.allvens.instaBatch.image_cropped_presenter.ImageParameters_Parcelable
import java.io.File
import java.io.FileOutputStream

class File_Editor(val context: Context){

    private fun get_FileName(uri: Uri): String {
        var fullName: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    fullName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }

        if (fullName == null) {
            fullName = uri.path
            val cut = fullName!!.lastIndexOf('/')
            val period = fullName.lastIndexOf('.')
            if (cut != -1) {
                fullName = fullName.substring(cut + 1, period)
            }
        }else{
            val period = fullName.lastIndexOf('.')
            fullName = fullName.substring(0, period)
        }

        return fullName + "_${context.resources.getString(R.string.loadCropImages_saveFolderName)}"
    }

    /********** Save Image **********/
    fun save_ImageToDevice(cropImageAndParameters_Parcelable: ImageParameters_Parcelable){
        val root: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val myDir: File = File(root + File.separator + context.resources.getString(R.string.loadCropImages_saveFolderName))
        myDir.mkdirs()

        val imageName: String = get_FileName(cropImageAndParameters_Parcelable._ImagePath) + ".png";

        val file: File = File(myDir, imageName)
        if (file.exists()) file.delete ()

        try {
            val out: FileOutputStream = FileOutputStream(file)
            get_CroppedBitmap(cropImageAndParameters_Parcelable).compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        broadCast_ImageStatusChanged(file)
    }

    private fun get_CroppedBitmap(cropImageAndParameters_Parcelable: ImageParameters_Parcelable): Bitmap {
        return Bitmap.createBitmap(
            MediaStore.Images.Media.getBitmap(context.getContentResolver(),cropImageAndParameters_Parcelable._ImagePath),
            cropImageAndParameters_Parcelable._X, cropImageAndParameters_Parcelable._Y,
            cropImageAndParameters_Parcelable._Width, cropImageAndParameters_Parcelable._Height)
    }

    /**
     * Update Device On Media Change
     */
    private fun broadCast_ImageStatusChanged(savedImagePath: File){
        MediaScannerConnection.scanFile(context, arrayOf(savedImagePath.getAbsolutePath()),null, null)
    }

    /**
     * Delete Image
     */
    fun delete_AlteredImage(imageUri: Uri){
        val file = File(FilePath_Finder.getFilePath(context, imageUri))
        if(file.delete()) broadCast_ImageStatusChanged(file)
    }
}