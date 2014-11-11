package me.ivps.android.myweibo.ui.friendgroup;

import me.ivps.android.myweibo.R;
import me.ivps.android.myweibo.support.utils.AppConfig;
import me.ivps.android.myweibo.support.utils.Utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * User: qii Date: 13-2-16
 */
public class WordLengthLimitWatcher implements TextWatcher {
    
    private EditText editText;
    
    public WordLengthLimitWatcher(EditText editText) {
        this.editText = editText;
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        
    }
    
    @Override
    public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
    }
    
    @Override
    public void afterTextChanged(Editable s) {
        if (Utility.length(s.toString()) > AppConfig.CREATE_MODIFY_FRIEND_GROUP_NAME_LENGTH_LIMIT) {
            editText.setError(editText.getContext().getString(
                    R.string.group_name_must_less_than_ten_chinese_words));
        }
    }
}
