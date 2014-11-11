package me.ivps.android.myweibo.ui.maintimeline;

import me.ivps.android.myweibo.bean.GroupListBean;
import me.ivps.android.myweibo.dao.maintimeline.FriendGroupDao;
import me.ivps.android.myweibo.support.database.GroupDBTask;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.lib.MyAsyncTask;
import me.ivps.android.myweibo.support.utils.GlobalContext;

/**
 * User: qii Date: 12-12-28
 */
public class GroupInfoTask extends
        MyAsyncTask<Void, GroupListBean, GroupListBean> {
    
    private WeiboException e;
    private String token;
    private String accountId;
    
    public GroupInfoTask(String token, String accountId) {
        this.token = token;
        this.accountId = accountId;
    }
    
    @Override
    protected GroupListBean doInBackground(Void... params) {
        try {
            return new FriendGroupDao(token).getGroup();
        }
        catch (WeiboException e) {
            this.e = e;
            cancel(true);
        }
        return null;
    }
    
    @Override
    protected void onPostExecute(GroupListBean groupListBean) {
        super.onPostExecute(groupListBean);
        GroupDBTask.update(groupListBean, accountId);
        if (accountId.equalsIgnoreCase(GlobalContext.getInstance()
                .getCurrentAccountId())) {
            GlobalContext.getInstance().setGroup(groupListBean);
        }
    }
}