package com.example.wxchatdemo.tools;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class WorksSizeCheckUtil {
    static IEditTextChangeListener mChangeListener;

    public static void setChangeListener(IEditTextChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    //检测输入框是否都输入了内容 从而改变按钮的是否可点击
    public static class textChangeListener {
        private Button button;
        private EditText[] editTexts;

        public textChangeListener(Button button) {
            this.button = button;
        }

        public textChangeListener addAllEditText(EditText... editTexts) {
            this.editTexts = editTexts;
            initEditListener();
            return this;
        }

        private void initEditListener() {
            //调用了遍历editext的方法
            for (EditText editText : editTexts) {
                editText.addTextChangedListener(new textChange());
            }
        }

        // edit输入的变化来改变按钮的是否点击
        private class textChange implements TextWatcher {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkAllEdit()) {
                    //所有EditText有值了
                    mChangeListener.textChange(true);
                    button.setEnabled(true);
                } else {
                    //所有EditText值为空
                    button.setEnabled(false);
                    mChangeListener.textChange(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        }

        //检查所有的edit是否输入了数据
        private boolean checkAllEdit() {
            for (EditText editText : editTexts) {
                if (!TextUtils.isEmpty(editText.getText() + "")) {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        }
    }
}

