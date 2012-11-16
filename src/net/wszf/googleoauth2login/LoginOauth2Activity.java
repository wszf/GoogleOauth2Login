package net.wszf.googleoauth2login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class LoginOauth2Activity extends Activity
	{
		private WebView webView;
		private TextView title_view;
		private ProgressDialog dialog;

		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				// TODO Auto-generated method stub
				super.onCreate(savedInstanceState);
				requestWindowFeature(Window.FEATURE_PROGRESS);
				setContentView(R.layout.oauth_login);
				webView = (WebView) findViewById(R.login.webview);
				webView.setWebViewClient(new MyWebViewClient());
				WebSettings webSettings = webView.getSettings();
				webSettings.setJavaScriptEnabled(true);
				webSettings.setSupportZoom(true);
				webSettings.setSupportMultipleWindows(true);
				webView.loadUrl(FiledMark.AUTH_URL + "scope=" + FiledMark.SCOPE
						+ "&redirect_uri=" + FiledMark.REDIRECT_URI + "&response_type=code&client_id="
						+ FiledMark.CLIENT_ID);
			}

		public class MyWebViewClient extends WebViewClient
			{
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url)
					{
						view.loadUrl(url);
						return true;
					}

				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon)
					{
						if(url.contains(FiledMark.REDIRECT_URI))
							{
								String str=url.substring(url.indexOf("=")+1, url.length());
								System.out.println(str);//token
								
							}

					}
			}

		@Override
		public void onBackPressed()
			{
				if (webView.canGoBack())
					{
						webView.goBack();
					} else
					{
						finish();
					}
			}

	}