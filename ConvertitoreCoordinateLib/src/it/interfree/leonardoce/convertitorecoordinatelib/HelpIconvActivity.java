package it.interfree.leonardoce.convertitorecoordinatelib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpIconvActivity extends AppCompatActivity
{
	private WebView helpHtmlView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.help_iconv);
		helpHtmlView = (WebView)findViewById(R.id.helpHtmlView);
		
		refresh();
	}
	
	private void refresh()
	{
		InputStream is = getResources().openRawResource(R.raw.aiuto);
		
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer htmlBuffer = new StringBuffer();
			while(true)
			{
				String line = br.readLine();
				if (line==null)
				{
					break;
				}
				
				htmlBuffer.append(line);
				htmlBuffer.append('\n');
			}
			
			helpHtmlView.loadDataWithBaseURL("fake://not/needed", htmlBuffer.toString(), "text/html", "utf-8", "");
		} 
		catch(IOException ioe)
		{
			// Log.e(HelpIconvActivity.class.getName(), ioe.toString(), ioe);
		}
		finally
		{
			try
			{
				is.close();
			} catch(IOException ioe)
			{
				// Log.e(HelpIconvActivity.class.getName(), ioe.toString(), ioe);
			}
		}
	}
}
