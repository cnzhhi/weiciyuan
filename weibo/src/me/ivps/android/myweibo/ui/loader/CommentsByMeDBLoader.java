package me.ivps.android.myweibo.ui.loader;

import me.ivps.android.myweibo.bean.android.CommentTimeLineData;
import me.ivps.android.myweibo.support.database.CommentByMeTimeLineDBTask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * User: qii Date: 13-4-10
 */
public class CommentsByMeDBLoader extends AsyncTaskLoader<CommentTimeLineData> {
    
    private String accountId;
    private CommentTimeLineData result;
    
    public CommentsByMeDBLoader(Context context, String accountId) {
        super(context);
        this.accountId = accountId;
    }
    
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (result == null) {
            forceLoad();
        }
        else {
            deliverResult(result);
        }
    }
    
    public CommentTimeLineData loadInBackground() {
        result = CommentByMeTimeLineDBTask.getCommentLineMsgList(accountId);
        return result;
    }
}