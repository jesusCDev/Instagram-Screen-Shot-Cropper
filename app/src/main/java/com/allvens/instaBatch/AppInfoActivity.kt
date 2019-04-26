package com.allvens.instaBatch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.allvens.instaBatch.assets.documentation.TextDocumentation_OpenSource

class AppInfoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_presenter)

        val llContainer: LinearLayout = findViewById(R.id.ll_infoPresenter_appInfo_Container)
        TextDocumentation_OpenSource(this).show_Views(llContainer)
    }
}