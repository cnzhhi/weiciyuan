package me.ivps.android.myweibo.ui.search;

import me.ivps.android.myweibo.bean.SearchStatusListBean;
import me.ivps.android.myweibo.bean.android.AsyncTaskLoaderResult;
import me.ivps.android.myweibo.support.utils.GlobalContext;
import me.ivps.android.myweibo.ui.basefragment.AbstractMessageTimeLineFragment;
import me.ivps.android.myweibo.ui.browser.BrowserWeiboMsgActivity;
import me.ivps.android.myweibo.ui.loader.SearchStatusLoader;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;

/**
 * User: qii Date: 12-11-10
 */
public class SearchStatusFragment extends
        AbstractMessageTimeLineFragment<SearchStatusListBean> {
    
    private int page = 1;
    
    private SearchStatusListBean bean = new SearchStatusListBean();
    
    @Override
    public SearchStatusListBean getList() {
        return bean;
    }
    
    public SearchStatusFragment() {
        
    }
    
    public void search() {
        pullToRefreshListView.setRefreshing();
        loadNewMsg();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("bean", bean);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && bean.getItemList().size() == 0) {
            clearAndReplaceValue((SearchStatusListBean) savedInstanceState
                    .getParcelable("bean"));
            timeLineAdapter.notifyDataSetChanged();
        }
        
        refreshLayout(bean);
    }
    
    protected void listViewItemClick(AdapterView parent, View view,
            int position, long id) {
        startActivity(BrowserWeiboMsgActivity.newIntent(bean.getItem(position),
                GlobalContext.getInstance().getSpecialToken()));
    }
    
    @Override
    protected Loader<AsyncTaskLoaderResult<SearchStatusListBean>> onCreateNewMsgLoader(
            int id, Bundle args) {
        String token = GlobalContext.getInstance().getSpecialToken();
        String word = ((SearchMainParentFragment) getParentFragment())
                .getSearchWord();
        page = 1;
        return new SearchStatusLoader(getActivity(), token, word,
                String.valueOf(page));
    }
    
    @Override
    protected Loader<AsyncTaskLoaderResult<SearchStatusListBean>> onCreateOldMsgLoader(
            int id, Bundle args) {
        String token = GlobalContext.getInstance().getSpecialToken();
        String word = ((SearchMainParentFragment) getParentFragment())
                .getSearchWord();
        return new SearchStatusLoader(getActivity(), token, word,
                String.valueOf(page + 1));
    }
    
    @Override
    protected void newMsgLoaderSuccessCallback(SearchStatusListBean newValue,
            Bundle loaderArgs) {
        if (newValue != null && getActivity() != null && newValue.getSize() > 0) {
            getList().addNewData(newValue);
            getAdapter().notifyDataSetChanged();
            getListView().setSelectionAfterHeaderView();
            getActivity().invalidateOptionsMenu();
        }
    }
    
    @Override
    protected void oldMsgLoaderSuccessCallback(SearchStatusListBean newValue) {
        if (newValue != null && newValue.getSize() > 0) {
            getList().addOldData(newValue);
            getAdapter().notifyDataSetChanged();
            getActivity().invalidateOptionsMenu();
            page++;
        }
    }
}
