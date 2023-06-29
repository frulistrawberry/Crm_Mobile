package com.baihe.lib_common.ui.widget.state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.dylanc.loadingstateview.LoadingStateView
import com.dylanc.loadingstateview.ViewType

class ErrorViewDelegate : LoadingStateView.ViewDelegate(ViewType.ERROR) {
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(R.layout.common_error_view, parent, false)
}