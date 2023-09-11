package com.artifex.mupdf.viewer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

public class GoToPageDialog extends AlertDialog.Builder {
    EditText mGoToPage;

    public GoToPageDialog(Context context) {
        super(context);
    }

    @Override
    public AlertDialog.Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
        return super.setPositiveButton(textId, listener);
    }
}
