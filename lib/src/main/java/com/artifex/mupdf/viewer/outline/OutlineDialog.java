package com.artifex.mupdf.viewer.outline;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.treeview.TreeNode;
import com.amrdeveloper.treeview.TreeNodeManager;
import com.amrdeveloper.treeview.TreeViewAdapter;
import com.amrdeveloper.treeview.TreeViewHolder;
import com.amrdeveloper.treeview.TreeViewHolderFactory;
import com.artifex.mupdf.viewer.R;

import java.util.List;

public class OutlineDialog extends DialogFragment {
    TreeNodeManager nodeManager;
    TreeViewAdapter treeViewAdapter;
    RecyclerView recyclerView;
    SharedViewModel mViewModel;
    RotateAnimation rotate0to90;
    RotateAnimation rotate90to0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.outline_fragment, container, false);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory())
                .get(SharedViewModel.class);
        rotate0to90 = new RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate0to90.setDuration(400);
        rotate0to90.setFillAfter(true);
        rotate90to0 = new RotateAnimation(90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate90to0.setDuration(400);
        rotate90to0.setFillAfter(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);
        TreeViewHolderFactory factory = new TreeViewHolderFactory() {
            @Override
            public TreeViewHolder getTreeViewHolder(View view, int layout) {
                return new OutlineViewHolder(view);
            }
        };
        recyclerView = view1.findViewById(R.id.recycler_view_dialog);

        List<TreeNode> treeNodeList = mViewModel.outlineList;

        nodeManager = new TreeNodeManager();
        nodeManager.setTreeNodes(treeNodeList);

        treeViewAdapter = new TreeViewAdapter(factory, nodeManager);

        treeViewAdapter.setTreeNodeClickListener((treeNode, view) -> {
            if (mViewModel.outlineListener != null) {
                mViewModel.outlineListener.onOutlineClicked(treeNode.getPageNum());
                dismiss();
            }
        });
        recyclerView.setAdapter(treeViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

    }

    public class OutlineViewHolder extends TreeViewHolder {
        ImageButton btn;
        TextView textView;
        public OutlineViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.more_outline_btn);
            textView = itemView.findViewById(R.id.outline_item_text);
        }

        @Override
        public void bindTreeNode(TreeNode node, int position) {
            super.bindTreeNode(node, position);

            if (node.getChildren().size() == 0) {
                btn.setVisibility(View.INVISIBLE);
            }else {
                btn.setVisibility(View.VISIBLE);
                if (node.isSelected()) {
                    if (node.isExpanded()) {
                        btn.startAnimation(rotate0to90);
                    }else {
                        btn.startAnimation(rotate90to0);
                    }
                    node.setSelected(false);
                } else {
                    if (node.isExpanded()) {
                        btn.setRotation(90f);
                    }else {
                        btn.setRotation(0f);
                    }
                }
            }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    treeViewAdapter.collapseExpandNodes(node, position);
                    node.setSelected(true);
                }
            });
            textView.setText(node.getValue().toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = requireDialog().getWindow().getAttributes();
        params.width = 800;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.END + Gravity.TOP;
        requireDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (nodeManager != null) {
            mViewModel.outlineList = nodeManager.getTreeNodes();
        }
        super.onDismiss(dialog);
    }

    public interface Listeners {
        void onOutlineClicked(int pageNum);
    }
}
