package net.wszf.googleoauth2login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClientLoginActivity extends Activity
	{
		private EditText email,pass;
		private Button login;
		private TextView tips;
		StringBuffer result=new StringBuffer();
		private Handler handler=new Handler(new Callback()
			{
				
				@Override
				public boolean handleMessage(Message msg)
					{
						// TODO Auto-generated method stub
						tips.setText(result);
						return false;
					}
			});
	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.client_login);
			fillView();
			login.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
						{
							new Thread(new Runnable()
								{
									
									@Override
									public void run()
										{
											getAuth();
											handler.sendEmptyMessage(1);
										}
								}).start();
						}
				});
		}
	private void fillView()
		{
			email=(EditText) findViewById(R.login.email);
			pass=(EditText) findViewById(R.login.pass);
			login=(Button) findViewById(R.login.client_login);
			tips=(TextView) findViewById(R.login.tips);
		}
	public String getAuth()
		{
			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("accountType", "GOOGLE"));
			param.add(new BasicNameValuePair("Email", email.getText().toString()));
			param.add(new BasicNameValuePair("Passwd", pass.getText().toString()));
			param.add(new BasicNameValuePair("service", "mail"));
			HttpPost post=new HttpPost("https://www.google.com/accounts/ClientLogin");
			try
				{
					post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
					HttpResponse httpResponse = client.execute(post);
					if (200 == httpResponse.getStatusLine().getStatusCode())// 判断是否正常
						{
							// Log.d(TAG, "statuscode = " + statusCode);
							int length = (int) httpResponse.getEntity().getContentLength();// 获取长度
							
							InputStreamReader inputStreamReader = new InputStreamReader(httpResponse.getEntity().getContent(), HTTP.UTF_8);
							char buffer[] = new char[length];
							int count;
							while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0)
								{
									result.append(buffer, 0, count);
								}
							inputStreamReader.close();
						}
				} catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			return result.toString();
		}
	}
