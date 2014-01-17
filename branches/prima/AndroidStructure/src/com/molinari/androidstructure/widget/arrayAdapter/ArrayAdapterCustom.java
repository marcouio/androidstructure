package com.molinari.androidstructure.widget.arrayAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class ArrayAdapterCustom<T> extends ArrayAdapter<T>{

	private int resource;
	private Activity activity;
	private View.OnClickListener listener = null;
	
	public ArrayAdapterCustom(Context context, Activity activity,int resource, T[] objects) {
		super(context, resource, objects);
		this.resource = resource;
		this.activity = activity;
	}
	
	public ArrayAdapterCustom(Context context, Activity activity,int resource, T[] objects, View.OnClickListener listener) {
		super(context, resource, objects);
		this.resource = resource;
		this.activity = activity;
		this.listener = listener;
		
	}
	
	public void setOnClickListener(View.OnClickListener clickListener){
		this.listener = clickListener;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		boolean viewEsistente = true;
		
		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(getResource(), null);
			viewEsistente = false;
		}
		
		Object viewMemento = cercaViewMemento(convertView, viewEsistente);
		
		T item = getItem(position);
		aggiornaViewMemento(item, viewMemento);
		if(listener != null){
			convertView.setOnClickListener(listener);
		}
		
		return convertView;
	
	}

	private Object cercaViewMemento(View convertView, boolean viewEsistente) {
		Object viewMemento;
		if (!viewEsistente) {

			viewMemento = initViewMemento(convertView);
			convertView.setTag(viewMemento);
		}else{
			viewMemento = convertView.getTag();
		}
		return viewMemento;
	}
	
	public abstract void aggiornaViewMemento(T item, Object viewMemento);

	public abstract Object initViewMemento(View convertView);

	public int getResource() {
		return resource;
	}
	
	

}
