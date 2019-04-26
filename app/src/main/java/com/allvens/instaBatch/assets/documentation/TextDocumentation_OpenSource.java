package com.allvens.instaBatch.assets.documentation;


import android.content.Context;
import com.allvens.instaBatch.R;

public class TextDocumentation_OpenSource extends TextDocumentation_Manager{
    public TextDocumentation_OpenSource(Context context) {
        super(context);
        create_Title(R.string.openSource_title);
        create_Paragraph(R.string.openSource_summary);

        create_SubTitle(R.string.openSource_credit_title_picasso);
        create_Paragraph(R.string.openSource_credit_summary_picasso);
        create_SubTitle(R.string.openSource_credit_title_android_image_cropper);
        create_Paragraph(R.string.openSource_credit_summary_android_image_cropper);
        create_SubTitle(R.string.openSource_credit_title_android_permissions);
        create_Paragraph(R.string.openSource_credit_summary_android_permissions);
    }
}

