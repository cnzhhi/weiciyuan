package me.ivps.android.myweibo.ui.loader;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ivps.android.myweibo.bean.MessageListBean;
import me.ivps.android.myweibo.dao.maintimeline.MentionsWeiboTimeLineDao;
import me.ivps.android.myweibo.support.error.WeiboException;

import android.content.Context;

/**
 * User: qii Date: 13-4-14
 */
public class MentionsWeiboMsgLoader extends
        AbstractAsyncNetRequestTaskLoader<MessageListBean> {
    
    private static Lock lock = new ReentrantLock();
    
    private String token;
    private String sinceId;
    private String maxId;
    private String accountId;
    
    public MentionsWeiboMsgLoader(Context context, String accountId,
            String token, String sinceId, String maxId) {
        super(context);
        this.token = token;
        this.sinceId = sinceId;
        this.maxId = maxId;
        this.accountId = accountId;
    }
    
    public MessageListBean loadData() throws WeiboException {
        MentionsWeiboTimeLineDao dao = new MentionsWeiboTimeLineDao(token);
        dao.setSince_id(sinceId);
        dao.setMax_id(maxId);
        MessageListBean result = null;
        lock.lock();
        
        try {
            result = dao.getGSONMsgList();
        }
        finally {
            lock.unlock();
        }
        
        return result;
    }
}
