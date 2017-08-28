package com.example.lollipop.makeupapp.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.ui.view.RoundProgressBarWidthNumber;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lollipop on 2017/8/26.
 */

public class ImageUploadProgressDialog extends ProgressDialog {

    @BindView(R.id.progressbar)
    RoundProgressBarWidthNumber progressBar;
    @BindView(R.id.current_index)
    AppCompatTextView indexText;
    @BindView(R.id.current_percent)
    AppCompatTextView percentText;

    public ImageUploadProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image_upload);
        ButterKnife.bind(this);
    }

    public void setProgress(int curIndex, int curPercent, int total, int totalPercent){
        progressBar.setProgress(totalPercent);
        indexText.setText(curIndex+"/"+total);
        percentText.setText(curPercent+"%");
    }
}
