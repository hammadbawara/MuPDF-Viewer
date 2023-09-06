package com.artifex.mupdf.viewer.outline;

import androidx.lifecycle.ViewModel;

import com.amrdeveloper.treeview.TreeNode;

import java.util.List;

public class SharedViewModel extends ViewModel {
    public List<TreeNode> outlineList = null;
    public OutlineDialog.Listeners outlineListener = null;
    public int selectedPosition = 0;
}
