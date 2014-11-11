package me.ivps.android.myweibo.ui.task;

import me.ivps.android.myweibo.R;
import me.ivps.android.myweibo.bean.FavBean;
import me.ivps.android.myweibo.dao.fav.FavDao;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.lib.MyAsyncTask;
import me.ivps.android.myweibo.support.utils.GlobalContext;

import android.widget.Toast;

/**
 * User: qii Date: 12-9-12
 */
public class FavAsyncTask extends MyAsyncTask<Void, FavBean, FavBean> {
    
    private String token;
    private String id;
    private WeiboException e;
    
    public FavAsyncTask(String token, String id) {
        this.token = token;
        this.id = id;
    }
    
    @Override
    protected FavBean doInBackground(Void... params) {
        FavDao dao = new FavDao(token, id);
        try {
            return dao.favIt();
        }
        catch (WeiboException e) {
            this.e = e;
            cancel(true);
            return null;
        }
    }
    
    @Override
    protected void onCancelled(FavBean favBean) {
        super.onCancelled(favBean);
        if (favBean == null && this.e != null) {
            Toast.makeText(GlobalContext.getInstance(), e.getError(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onPostExecute(FavBean favBean) {
        super.onPostExecute(favBean);
        if (favBean != null) {
            Toast.makeText(
                    GlobalContext.getInstance(),
                    GlobalContext.getInstance().getString(
                            R.string.fav_successfully), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
