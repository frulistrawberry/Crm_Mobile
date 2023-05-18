package com.baihe.common.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


/**
 * 格式化带空格的手机号输入TextWatcher 例如：137 1609 3201
 */
public  class FormatPhoneTextWatcher implements TextWatcher {

    private EditText editText;

    public FormatPhoneTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = s.toString().length();
        //删除数字
        if (count == 0) {
            if (length == 4) {
                editText.setText(s.subSequence(0, 3));
            }
            if (length == 9) {
                editText.setText(s.subSequence(0, 8));
            }
        }
        //添加数字
        if (count == 1) {
            if (length == 4) {
                String part1 = s.subSequence(0, 3).toString();
                String part2 = s.subSequence(3, length).toString();
                editText.setText(part1 + " " + part2);
            }
            if (length == 9) {
                String part1 = s.subSequence(0, 8).toString();
                String part2 = s.subSequence(8, length).toString();
                editText.setText(part1 + " " + part2);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //将光标移动到末尾
        editText.setSelection(editText.getText().toString().length());
    }

}
