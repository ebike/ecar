package com.jcsoft.ecar.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jcsoft.ecar.R;
import com.jcsoft.ecar.base.BaseActivity;
import com.jcsoft.ecar.utils.CommonUtils;
import com.jcsoft.ecar.view.TopBarView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 网页
 */
public class WebActivity extends BaseActivity {
    @ViewInject(R.id.top_bar)
    TopBarView topBarView;
    @ViewInject(R.id.webView)
    WebView webView;
    private String title;//标题
    private String url;//链接

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_web);
        x.view().inject(this);

    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
    }

    @Override
    public void init() {
        topBarView.setCenterTextView(title);
        // WebView加载web资源
        if (CommonUtils.strIsEmpty(url)) {
            return;
        }
        webView.loadUrl(url);
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);// 启用支持javascript
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存
        // 判断页面加载过程
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                } else {
                    // 加载中
                }

            }
        });
    }

    @Override
    public void setListener() {

    }

    @Override
    public void setData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();// 返回上一页面
                return true;
            } else {
                WebActivity.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
