package com.geoassist;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CompassCorrection extends Activity implements OnClickListener{
	Button   cmpsOkBtn;
	Button   cmpsCnclBtn;
	EditText declTxt = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass_correction);
		cmpsOkBtn = (Button) findViewById(R.id.cmpsSaveBtn);
		cmpsOkBtn.setOnClickListener(this);
		cmpsCnclBtn = (Button) findViewById(R.id.cmpsCnclBtn);
		cmpsCnclBtn.setOnClickListener(this);
		declTxt = ( EditText ) findViewById(R.id.declanationTxt);
		WindowManager windowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);
		int rotation = windowManager.getDefaultDisplay().getRotation();
//		this.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.setRequestedOrientation (rotation);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compass_correction, menu);
		return true;
	}

	private void prepareReturn(boolean okBtn){
		Intent rslt = new Intent();
		float declanation = 0;
		if ((declTxt != null) && (okBtn) &&(declTxt.length()!= 0)){
			String declTxtVal = declTxt.getText().toString();
			Log.e("ERROR", "Got Value" + String.valueOf(declTxtVal));
			Toast.makeText(this, "Got Value" + String.valueOf(declTxtVal), Toast.LENGTH_LONG).show();
			if (declTxtVal != "") {
				declanation  = (float) Float.valueOf(declTxtVal);
			}
		}
		rslt.putExtra("declanation", declanation);
		setResult(RESULT_OK, rslt); 
		return;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cmpsSaveBtn:
			if (this.declTxt != null)
			{
				prepareReturn(true);
			}
//			Toast.makeText(this, "OK Btn", Toast.LENGTH_SHORT).show();
			break;
		case R.id.cmpsCnclBtn:
//			Toast.makeText(this, "Cancel Btn", Toast.LENGTH_SHORT).show();
			prepareReturn(false);
			break;
			
		}
		this.finish();
	}
	
}
