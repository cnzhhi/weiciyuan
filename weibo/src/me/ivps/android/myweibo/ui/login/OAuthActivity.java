package me.ivps.android.myweibo.ui.login;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.R;
import me.ivps.android.myweibo.bean.AccountBean;
import me.ivps.android.myweibo.bean.UserBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.dao.login.OAuthDao;
import me.ivps.android.myweibo.dao.login.RefreshOAuthDao;
import me.ivps.android.myweibo.support.database.AccountDBTask;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.lib.MyAsyncTask;
import me.ivps.android.myweibo.support.utils.Utility;
import me.ivps.android.myweibo.ui.interfaces.AbstractAppActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * User: qii Date: 12-7-28
 */
public class OAuthActivity extends AbstractAppActivity {
    
    private WebView webView;
    private MenuItem refreshItem;
    
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauthactivity_layout);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(getString(R.string.login));
        
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WeiboWebViewClient());
        
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearCache(true);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_oauthactivity, menu);
        refreshItem = menu.findItem(R.id.menu_refresh);
        refresh();
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = AccountActivity.newIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.menu_refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @SuppressLint("InflateParams")
    public void refresh() {
        webView.loadUrl("about:blank");
        ImageView iv = (ImageView) LayoutInflater.from(this).inflate(
                R.layout.refresh_action_view, null);
        
        iv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.refresh));
        
        refreshItem.setActionView(iv);
        webView.loadUrl(getWeiboOAuthUrl());
    }
    
    private void completeRefresh() {
        if (refreshItem.getActionView() != null) {
            refreshItem.getActionView().clearAnimation();
            refreshItem.setActionView(null);
        }
    }
    
    private String getWeiboOAuthUrl() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("client_id", URLHelper.APP_KEY);
        parameters.put("redirect_uri", URLHelper.DIRECT_URL);
        parameters.put("display", "mobile");
        return URLHelper.URL_OAUTH2_ACCESS_AUTHORIZE + "?"
                + Utility.encodeUrl(parameters)
                + "&scope=friendships_groups_read";
    }
    
    private class WeiboWebViewClient extends WebViewClient {
        
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
        
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (url.startsWith(URLHelper.DIRECT_URL)) {
                handleRedirectUrl(view, url);
                view.stopLoading();
                return;
            }
            super.onPageStarted(view, url, favicon);
        }
        
        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            new SinaWeiboErrorDialog().show(getSupportFragmentManager(), "");
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!url.equals("about:blank")) {
                completeRefresh();
            }
        }
    }
    
    private void handleRedirectUrl(WebView view, String url) {
        Bundle values = Utility.parseUrl(url);
        String error = values.getString("error");
        String error_code = values.getString("error_code");
        
        if (error == null && error_code == null) {
            String code = values.getString("code");
            new OAuthTask(this).execute(code);
        }
        else {
            Toast.makeText(OAuthActivity.this,
                    getString(R.string.you_cancel_login), Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();
        }
        else {
            Toast.makeText(OAuthActivity.this,
                    getString(R.string.you_cancel_login), Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
    }
    
    private static class OAuthTask extends
            MyAsyncTask<String, UserBean, DBResult> {
        
        private WeiboException e;
        private ProgressFragment progressFragment = ProgressFragment
                .newInstance();
        private WeakReference<OAuthActivity> oAuthActivityWeakReference;
        
        private AccountBean account;
        
        private OAuthTask(OAuthActivity activity) {
            oAuthActivityWeakReference = new WeakReference<OAuthActivity>(
                    activity);
        }
        
        @Override
        protected void onPreExecute() {
            progressFragment.setAsyncTask(this);
            OAuthActivity activity = oAuthActivityWeakReference.get();
            if (activity != null) {
                progressFragment.show(activity.getSupportFragmentManager(), "");
            }
        }
        
        @Override
        protected DBResult doInBackground(String... params) {
            String code = params[0];
            
            try {
                // 使用 code 刷新 access_token, expired_time 等
                account = new RefreshOAuthDao(code).refreshToken();
                
                UserBean user = new OAuthDao(account.getAccess_token())
                        .getOAuthUserInfo();
                
                account.setExpires_time(System.currentTimeMillis()
                        + account.getExpires_time() * 1000L);
                account.setInfo(user);
                
                AppLogger.d("token expires in "
                        + Utility.calcTokenExpiresInDays(account) + " days.");
                // 添加或更新授权账户
                return AccountDBTask.addOrUpdateAccount(account, false);
            }
            catch (WeiboException e) {
                AppLogger.e(e.getError());
                this.e = e;
                cancel(true);
                return null;
            }
        }
        
        @Override
        protected void onCancelled(DBResult dbResult) {
            super.onCancelled(dbResult);
            if (progressFragment != null) {
                progressFragment.dismissAllowingStateLoss();
            }
            
            OAuthActivity activity = oAuthActivityWeakReference.get();
            if (activity == null) {
                return;
            }
            
            if (e != null) {
                Toast.makeText(activity, e.getError(), Toast.LENGTH_SHORT)
                        .show();
            }
            activity.webView.loadUrl(activity.getWeiboOAuthUrl());
        }
        
        @Override
        protected void onPostExecute(DBResult dbResult) {
            if (progressFragment.isVisible()) {
                progressFragment.dismissAllowingStateLoss();
            }
            OAuthActivity activity = oAuthActivityWeakReference.get();
            if (activity == null) {
                return;
            }
            switch (dbResult) {
                case add_successfuly:
                    Toast.makeText(activity,
                            activity.getString(R.string.login_success),
                            Toast.LENGTH_SHORT).show();
                    break;
                case update_successfully:
                    Toast.makeText(
                            activity,
                            activity.getString(R.string.update_account_success),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            
            // 设置 bundle 数据
            Intent intent = new Intent();
            intent.putExtra("access_token", account.getAccess_token());
            intent.putExtra(
                    "expires_in",
                    (account.getExpires_time() - System.currentTimeMillis()) / 1000L);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            webView.stopLoading();
        }
    }
    
    public static class ProgressFragment extends DialogFragment {
        
        MyAsyncTask asyncTask = null;
        
        public static ProgressFragment newInstance() {
            ProgressFragment frag = new ProgressFragment();
            frag.setRetainInstance(true);
            Bundle args = new Bundle();
            frag.setArguments(args);
            return frag;
        }
        
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.oauthing));
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            return dialog;
        }
        
        @Override
        public void onCancel(DialogInterface dialog) {
            if (asyncTask != null) {
                asyncTask.cancel(true);
            }
            super.onCancel(dialog);
        }
        
        void setAsyncTask(MyAsyncTask task) {
            asyncTask = task;
        }
    }
    
    public static class SinaWeiboErrorDialog extends DialogFragment {
        
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.sina_server_error).setPositiveButton(
                    R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            
                        }
                    });
            return builder.create();
        }
    }
    
    public static enum DBResult {
        add_successfuly, update_successfully
    }
}
