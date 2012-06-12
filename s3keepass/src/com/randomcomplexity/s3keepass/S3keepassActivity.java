package com.randomcomplexity.s3keepass;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class S3keepassActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main);
        
        ImageButton launchButton = (ImageButton) findViewById(R.id.button_launchKeePassDroid);
        launchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				 
				downloadKeePassDatabase();
				
				// TODO Auto-generated method stub
				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.android.keepass");
				startActivity(LaunchIntent);
			}
        	
        });
        
        Button configButton = (Button) findViewById(R.id.button_configureS3KeePass);
        configButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent LaunchIntent = new Intent(S3keepassActivity.this, S3configurationActivity.class);
				startActivity(LaunchIntent);
			}
        	
        });
    }
    
    public void downloadKeePassDatabase()
    {
    	SharedPreferences s3kpPrefs = getSharedPreferences("S3KeePassPrefs", MODE_PRIVATE);
    	if( isOnline()/* && !s3kpPrefs.getString("AccessKey", "NoCredentials").equals("NoCredentials") */)
    	{
    		Log.v("S3keepass", "Creating Client");
    		AmazonS3Client as3c;
			try
    		{
    			as3c = new AmazonS3Client(new BasicAWSCredentials(s3kpPrefs.getString("AccessKey", "NoCredentials"), s3kpPrefs.getString("SecretKey", "NoCredentials") ));
    			Log.v("S3keepass", "Getting Object");
    			List<S3ObjectSummary> objects = as3c.listObjects(URLEncoder.encode(s3kpPrefs.getString("Bucket", "NoCredentials"), "utf-8")).getObjectSummaries();
    			Log.v("S3keepass", "Available Objects");
    			int levenshteinDistance = 100;
    			int closestMatch = -1;
    			for(int i=0; i<objects.size(); i++)
    			{
    				Log.v("S3keepass", "Object Key:" + ((S3ObjectSummary)objects.get(i)).getKey());
    				if( StringUtils.getLevenshteinDistance(((S3ObjectSummary)objects.get(i)).getKey(), URLEncoder.encode(s3kpPrefs.getString("RemoteFile", "NoCredentials"), "utf-8")) < levenshteinDistance )
    				{
    					levenshteinDistance = StringUtils.getLevenshteinDistance(((S3ObjectSummary)objects.get(i)).getKey(), s3kpPrefs.getString("RemoteFile", "NoCredentials"));
    					closestMatch = i;
    				}
				}
    			if(closestMatch != -1)
    			{
    				Log.v("S3keepass", "Closest Match (" + levenshteinDistance + "):" + s3kpPrefs.getString("RemoteFile", "NoCredentials") + " : " + ((S3ObjectSummary)objects.get(closestMatch)).getKey());
	    			S3Object obj = as3c.getObject( URLEncoder.encode(s3kpPrefs.getString("Bucket", "NoCredentials"), "utf-8"), ((S3ObjectSummary)objects.get(closestMatch)).getKey() );
	    			File file = new File(s3kpPrefs.getString("LocalFolder", "NoCredentials") + s3kpPrefs.getString("LocalFile", "NoCredentials"));
	    			File folder = new File(s3kpPrefs.getString("LocalFolder", "NoCredentials"));
	    			if(folder.mkdirs()) Log.v("S3keepass", "Directories Created");
	    			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
	    			S3ObjectInputStream stream = obj.getObjectContent();
	    			int bytes = 0;
	    			int nextByte = stream.read();
	    			Log.v("S3keepass", "Writing Bytes");
	    			while( nextByte != -1 )
	    			{
		    			out.write(nextByte);
		    			nextByte = stream.read();
		    			bytes++;
	    			}
	    			Log.v("S3keepass", "Written " + bytes + "Bytes");
	    			stream.close();
	    			out.flush();
	    			out.close();
	    			if(file.exists()) Log.v("S3keepass", "FileExists");
    			}
    		} catch (Exception e) 
    		{
    			Log.v("S3keepass", "S3 Error: " + e.getClass().getName());
    			Log.v("S3keepass", "S3 Error: " + e.getMessage());
    		}
    	} else {
    		Log.v("S3keepass", "Offline or NoCredentials");
    	}
    }
    
    public boolean isOnline() {
        //ConnectivityManager cm =
            //(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //return cm.getActiveNetworkInfo() != null && 
           //cm.getActiveNetworkInfo().isConnectedOrConnecting();
        return true;
           
    }
}