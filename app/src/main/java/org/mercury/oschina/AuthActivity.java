package org.mercury.oschina;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.mercury.oschina.bean.AccessToken;
import org.mercury.oschina.http.HttpApi;
import org.mercury.oschina.main.activity.MainActivity;
import org.mercury.oschina.tweet.util.ToastUtil;
import org.mercury.oschina.utils.AccessTokenHelper;
import org.mercury.oschina.utils.SpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * created by Mercury at 2017/7/30
 * descript: 调用openapi的auth页面
 */
public class AuthActivity extends AppCompatActivity {

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.webview)
    WebView     webview;

    String redirectUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_web);
        ButterKnife.bind(this);

        webview.getSettings().setJavaScriptEnabled(true);
        String authUrl=Constant.BASE_URL+"action/oauth2/authorize?response_type=code&client_id=" + BuildConfig
                .CLIENT_ID + "&redirect_uri=" + BuildConfig.REDIRECT_URI;

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //定位到重定向的Url,获得授权登录后回调得到的授权码 code
                redirectUrl = url;
                try {
                    String code = Uri.parse(redirectUrl).getQueryParameter("code");
                    requestToken(code);
                } catch (Exception e) {
                    ToastUtil.showToast("获取授权码失败");
                }
                //默认方法返回false，如果返回true的话，就可以让我们自己决定怎么处理重定向的url，而不是直接跳转
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbLoading.setMax(100);
                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pbLoading.setProgress(newProgress);
            }
        });
        webview.loadUrl(authUrl);
    }

    //获取access_token
    private void requestToken(String code) {
        //因为RequestHelper中的Retrofit做了统一封装，这里并不需要公共参数和拦截身份验证
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory
                .create())
                .baseUrl(Constant.BASE_API_URL).build();
        HttpApi httpApi = retrofit.create(HttpApi.class);

        Map<String, String> params = new HashMap<>();
        params.put("client_id", BuildConfig.CLIENT_ID);
        params.put("client_secret", BuildConfig.CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("redirect_uri", BuildConfig.REDIRECT_URI);
        Call<AccessToken> call = httpApi.getAccessToken(params);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                AccessToken bean = response.body();
                if (bean != null) {
                    //存储当前登录的userId到sp文件中
                    SpUtils.put(AuthActivity.this, Constant.USER_ID, String.valueOf(bean.getUid()));
                    //将当前登录的userId以原始的数据格式存入sp中
                    AccessTokenHelper.saveUserId(bean.getUid());

                    //将access_token 转换为sha-1存入sp中
                    AccessTokenHelper.saveAccessToken(String.valueOf(bean.getUid()), bean
                            .getAccess_token());

                    //将跳转首页放到回调里进行,否则会由于异步的原因导致token还没有存储在本地,首页导致获取数据失败
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                ToastUtil.showToast("网络连接异常");
            }
        });

//        PropertyHelper.get();

    }


}
