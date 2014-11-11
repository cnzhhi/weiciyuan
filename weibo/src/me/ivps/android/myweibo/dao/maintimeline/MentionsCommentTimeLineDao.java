package me.ivps.android.myweibo.dao.maintimeline;

import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.dao.unread.ClearUnreadDao;
import me.ivps.android.myweibo.support.error.WeiboException;

/**
 * User: qii Date: 12-10-21
 */
public class MentionsCommentTimeLineDao extends MainCommentsTimeLineDao {
    public MentionsCommentTimeLineDao(String access_token) {
        super(access_token);
    }
    
    @Override
    protected String getUrl() {
        return URLHelper.COMMENTS_MENTIONS_TIMELINE;
    }
    
    protected void clearUnread() {
        try {
            new ClearUnreadDao(access_token, ClearUnreadDao.MENTION_CMT)
                    .clearUnread();
        }
        catch (WeiboException ignored) {
            
        }
    }
}
