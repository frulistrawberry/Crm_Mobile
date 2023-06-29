package com.baihe.lib_common.ui.widget.state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.dylanc.loadingstateview.LoadingStateView
import com.dylanc.loadingstateview.ViewType

class EmptyViewDelegate: LoadingStateView.ViewDelegate(ViewType.EMPTY) {
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.common_empty, parent, false)
    }
}