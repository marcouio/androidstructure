package com.molinari.androidstructure.widget.arrayAdapter;

import android.app.Activity;
import android.content.Context;

import com.molinari.androidstructure.activity.BaseActivity;
import com.molinari.androidstructure.listener.OnClickApriActivity;

public abstract class ArrayAdapterApriActivity<T> extends ArrayAdapterCustom<T>{

	public ArrayAdapterApriActivity(Context context, 
								    Activity activity, 
								    int resource, 
								    T[] objects, 
								    Class<?> classeActivityDaAprire) {
		
		super(context, activity, resource, objects, new OnClickApriActivity(classeActivityDaAprire, (BaseActivity)activity));
	}

}
