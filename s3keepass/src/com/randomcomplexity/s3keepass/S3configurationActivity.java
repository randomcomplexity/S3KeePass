package com.randomcomplexity.s3keepass;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class S3configurationActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.configure);
        
        Button saveButton = (Button) findViewById(R.id.button_SaveButton);
        saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences s3kpPrefs = getSharedPreferences("S3KeePassPrefs", MODE_PRIVATE);
				Editor e = s3kpPrefs.edit();
				EditText accessKey = (EditText) findViewById(R.id.editText_AccessKey);
				e.putString("AccessKey", accessKey.getText().toString());
				EditText secretKey = (EditText) findViewById(R.id.editText_SecretKey);
				e.putString("SecretKey", secretKey.getText().toString());
				EditText bucket = (EditText) findViewById(R.id.editText_Bucket);
				e.putString("Bucket", bucket.getText().toString());
				EditText localFile = (EditText) findViewById(R.id.editText_LocalFile);
				e.putString("LocalFile", localFile.getText().toString());
				EditText localFolder = (EditText) findViewById(R.id.editText_LocalFolder);
				e.putString("LocalFolder", localFolder.getText().toString());
				EditText remoteFile = (EditText) findViewById(R.id.editText_RemoteFile);
				e.putString("RemoteFile", remoteFile.getText().toString());
		        e.commit();
			}
        	
        });
        
        Button backButton = (Button) findViewById(R.id.button_BackButton);
        backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent LaunchIntent = new Intent(S3configurationActivity.this, S3keepassActivity.class);
				startActivity(LaunchIntent);
			}
        	
        });
        
        SharedPreferences s3kpPrefs = getSharedPreferences("S3KeePassPrefs", MODE_PRIVATE);
		EditText accessKey = (EditText) findViewById(R.id.editText_AccessKey);
		accessKey.setText(s3kpPrefs.getString("AccessKey", getString(R.string.AccessKeyDefault)));
		EditText secretKey = (EditText) findViewById(R.id.editText_SecretKey);
		secretKey.setText(s3kpPrefs.getString("SecretKey", getString(R.string.SecretKeyDefault)));
		EditText bucket = (EditText) findViewById(R.id.editText_Bucket);
		bucket.setText(s3kpPrefs.getString("Bucket", getString(R.string.BucketDefault)));
		EditText localFile = (EditText) findViewById(R.id.editText_LocalFile);
		localFile.setText(s3kpPrefs.getString("LocalFile", getString(R.string.LocalFileDefault)));
		EditText localFolder = (EditText) findViewById(R.id.editText_LocalFolder);
		localFolder.setText(s3kpPrefs.getString("LocalFolder", getString(R.string.LocalFolderDefault)));
		EditText remoteFile = (EditText) findViewById(R.id.editText_RemoteFile);
		remoteFile.setText(s3kpPrefs.getString("RemoteFile", getString(R.string.RemoteFileDefault)));
        
    }
}
