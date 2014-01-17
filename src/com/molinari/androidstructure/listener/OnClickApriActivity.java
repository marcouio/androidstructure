package com.molinari.androidstructure.listener;

import com.molinari.androidstructure.activity.BaseActivity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class OnClickApriActivity implements OnClickListener {

	private Class<?> classe = null;
	private BaseActivity activity;

	public BaseActivity getActivity() {
		return activity;
	}

	public OnClickApriActivity(final Class<?> classe, BaseActivity activity) {
		this.classe = classe;
		this.activity = activity;
	}

	@Override
	public void onClick(final View v) {
		boolean doSomethingPreStart = doSomethingPreStart(v);
		
		if(doSomethingPreStart){
			final Intent i = new Intent(v.getContext(), classe);
			v.getContext().startActivity(i);
		}
	}

	protected boolean doSomethingPreStart(View v){
		return true;
	}

}
