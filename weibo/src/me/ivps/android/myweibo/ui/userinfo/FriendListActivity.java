package me.ivps.android.myweibo.ui.userinfo;

import me.ivps.android.myweibo.R;
import me.ivps.android.myweibo.bean.UserBean;
import me.ivps.android.myweibo.support.utils.GlobalContext;
import me.ivps.android.myweibo.ui.interfaces.AbstractAppActivity;
import me.ivps.android.myweibo.ui.main.MainTimeLineActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * User: Jiang Qi Date: 12-8-16
 */
public class FriendListActivity extends AbstractAppActivity {
    
    private UserBean bean;
    
    public UserBean getUser() {
        return bean;
    }
    
    public static Intent newIntent(String token, UserBean userBean) {
        Intent intent = new Intent(GlobalContext.getInstance(),
                FriendListActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("user", userBean);
        return intent;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.following_list));
        getActionBar().setIcon(R.drawable.ic_ab_friendship);
        bean = (UserBean) getIntent().getParcelableExtra("user");
        if (getSupportFragmentManager().findFragmentByTag(
                FriendsListFragment.class.getName()) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content,
                            FriendsListFragment.newInstance(bean),
                            FriendsListFragment.class.getName()).commit();
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
