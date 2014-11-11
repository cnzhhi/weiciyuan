package me.ivps.android.myweibo.dao.maintimeline;

import me.ivps.android.myweibo.bean.CommentListBean;
import me.ivps.android.myweibo.support.error.WeiboException;

/**
 * User: qii Date: 12-12-16
 */
public interface ICommentsTimeLineDao {
    public CommentListBean getGSONMsgList() throws WeiboException;
    
    public void setSince_id(String since_id);
    
    public void setMax_id(String max_id);
}
