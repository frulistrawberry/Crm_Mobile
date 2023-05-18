package com.baihe.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baihe.common.dialog.LoadingDialog;

/**
 * Author：xubo
 * Time：2020-03-01
 * Description：
 */
public abstract class BaseFragment extends Fragment {
    protected String tagName;
    protected BaseApplication app;
    protected LoadingDialog loadingDialog;

    public BaseFragment() {
        tagName = this.getClass().getSimpleName();
        app = BaseApplication.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 显示操作加载对话框
     */
    public void showOptionLoading() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) getActivity();
            baseActivity.showOptionLoading();
        } else {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog.Builder(getContext()).build();
            }
            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
            }
        }
    }

    /**
     * 关闭操作加载对话框
     */
    public void dismissOptionLoading() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) getActivity();
            baseActivity.dismissOptionLoading();
        }
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
