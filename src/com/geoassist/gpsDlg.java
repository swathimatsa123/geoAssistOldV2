package com.geoassist;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class gpsDlg extends DialogFragment implements OnEditorActionListener, OnClickListener {
	EditText mLatTxt;
	EditText mLngTxt;
	Button   gpsOkBtn;
	Button   gpsCnclBtn;
	public collectTab frg;
	public gpsDlg(){
	}
	public interface gpsDlgListener {
		void onGpsGetDialog(String lat, String lng) ;    
	}


   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gps_editdlg, container);
        getDialog().setTitle("Change Coordinate");

        mLatTxt = (EditText) view.findViewById(R.id.latTxt);
        mLatTxt.setOnEditorActionListener(this);
        mLngTxt = (EditText) view.findViewById(R.id.lngTxt);
        mLngTxt.setOnEditorActionListener(this);
        gpsOkBtn = (Button) view.findViewById(R.id.gpsOk);
        gpsOkBtn.setOnClickListener(this);
        gpsCnclBtn = (Button) view.findViewById(R.id.gpsCncl);
        gpsCnclBtn.setOnClickListener(this);
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	     if (EditorInfo.IME_ACTION_DONE == actionId) {
	    	    frg.onGpsGetDialog(mLatTxt.getText().toString(), mLngTxt.getText().toString());
	            this.dismiss();
	            return true;
	        }
	        return false;
	}
	public void onGpsBtnClick (View view) {
		switch (view.getId()) {
		case R.id.gpsOk:
			Toast.makeText(getActivity(), "OK Btn", Toast.LENGTH_SHORT).show();
			break;
		case R.id.gpsCncl:
			Toast.makeText(getActivity(), "Cancel Btn", Toast.LENGTH_SHORT).show();
			break;
			
		}
		this.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onGpsBtnClick (v);
	}
}
