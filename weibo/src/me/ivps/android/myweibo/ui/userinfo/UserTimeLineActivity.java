package me.ivps.android.myweibo.ui.userinfo;

import me.ivps.android.myweibo.bean.UserBean;
import me.ivps.android.myweibo.support.utils.GlobalContext;
import me.ivps.android.myweibo.ui.interfaces.AbstractAppActivity;
import me.ivps.android.myweibo.ui.main.MainTimeLineActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * User: qii Date: 13-6-21
 */
public class UserTimeLineActivity extends AbstractAppActivity {
    
    public static Intent newIntent(String token, UserBean userBean) {
        Intent intent = new Intent(GlobalContext.getInstance(),
                UserTimeLineActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("user", userBean);
        return intent;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        String token = getIntent().getStringExtra("token");
        UserBean bean = getIntent().getParcelableExtra("user");
        getActionBar().setTitle(bean.getScreen_name());
        if (getSupportFragmentManager().findFragmentByTag(
                StatusesByIdTimeLineFragment.class.getName()) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            android.R.id.content,
                            StatusesByIdTimeLineFragment.newInstance(bean,
                                    token),
                            StatusesByIdTimeLineFragment.class.getName())
                    .commit();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = MainTimeLineActivity.newIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
        }
        return false;
    }
}
