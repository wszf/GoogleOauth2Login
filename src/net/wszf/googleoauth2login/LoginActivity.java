package net.wszf.googleoauth2login;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * 登陆类
 * @author wszfer@gmail.com
 * @project G_reader
 * @date 2012-11-14下午8:37:47
 * @version 1.0
 */
public class LoginActivity extends Activity
	{

		private Spinner user_spinner;
		private Button login,oauth;
		private String user;
		AccountManager am;
		Account[] accounts;
		Bundle options = new Bundle();
		private SharedPreferences sharePreferences;

		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_login);
						am = AccountManager.get(this);
						accounts = am.getAccountsByType("com.google");
						fillView();

			}
/**
 * 回调
 * @author wszfer@gmail.com
 * @project G_reader
 * @date 2012-11-14下午8:37:36
 * @version 1.0
 */
		private class OnTokenAcquired implements AccountManagerCallback<Bundle>
			{
				@Override
				public void run(AccountManagerFuture<Bundle> result)
					{
						// Get the result of the operation from the AccountManagerFuture.
						String token = "";
						try
							{
								Bundle bundle = result.getResult();
								token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
								if(token!=null)
									{
										sharePreferences.edit().putString("token", token).commit();
										Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
									}
							} catch (OperationCanceledException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AuthenticatorException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						// The token is a named value in the bundle. The name of the value
						// is stored in the constant AccountManager.KEY_AUTHTOKEN.

						System.out.println(token);
					}
			}

		private void fillView()
			{
				login = (Button) findViewById(R.login.login);
				oauth=(Button) findViewById(R.login.oauth);
				user_spinner = initializeSpinner(R.login.spinner, getAccountNames(accounts));
				login.setOnClickListener(onClickListener);
				oauth.setOnClickListener(onClickListener);
			}

		private String[] getAccountNames(Account[] accounts)
			{
				String[] names = new String[accounts.length];
				for (int i = 0; i < names.length; i++)
					{
						names[i] = accounts[i].name;
					}
				return names;
			}

		private Spinner initializeSpinner(int id, String[] values)
			{
				ArrayAdapter<String> adapter = new ArrayAdapter<  String>(this, android.R.layout.simple_spinner_item, values);
				Spinner spinner = (Spinner) findViewById(id);
				spinner.setAdapter(adapter);
				return spinner;
			}

		private OnClickListener onClickListener = new OnClickListener()
			{
				@Override
				public void onClick(View v)
					{
						if(v.getId()==login.getId())
							{
								int accountIndex = user_spinner.getSelectedItemPosition();
								if (accountIndex < 0)
									{
										// this happens when the sample is run in an emulator which has no google account
										// added yet.
										Toast.makeText(LoginActivity.this, "当前手机未绑定任何google帐户", Toast.LENGTH_SHORT).show();
										return;
									}
								am.getAuthToken(accounts[accountIndex], // Account retrieved using getAccountsByType()
										"task", // Auth scope
										options, // Authenticator-specific options
										LoginActivity.this, // Your activity
										new OnTokenAcquired(), // Callback called when a token is successfully acquired
										new Handler()); // Callback called if an error occurs
							}
						else{
							Intent intent=new Intent(LoginActivity.this,LoginOauth2Activity.class);
							startActivity(intent);
						}
						
					}
			};

		@Override
		public boolean onCreateOptionsMenu(Menu menu)
			{
				getMenuInflater().inflate(R.menu.activity_login, menu);
				return true;
			}
	}
