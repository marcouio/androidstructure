package com.molinari.androidstructure.activity;
import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIdLayout() != 0) {
			setContentView(getIdLayout());
		}
		try {
			onCreateCustom(savedInstanceState);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public int getIntExtra(String name, int defaultValue){
		return getIntent().getIntExtra(name, defaultValue);
	}
	
	public String getStringExtra(String name){
		return getIntent().getStringExtra(name);
	}
	
	/**
	 * @return Deve ritornare la costante nella classe R che rappresenta il layout dell'activity. Se l'activity
	 * non ha una vista lasciare il return a 0
	 */
	protected abstract int getIdLayout();

	/**
	 * Quessto è il metodo in cui vanno implementate le funzioni dell'applicazione al posto di fare l'override
	 * del metodo onCreate
	 * 
	 * @param savedInstanceState
	 * @throws Exception 
	 */
	protected abstract void onCreateCustom(final Bundle savedInstanceState) throws Exception;

	public void removeView(final View[] views) {
		for (View view : views) {
			((ViewGroup) view.getParent()).removeView(view);
		}
	}

	
	/**
	 * Se necessario fa l'inflating della vista dall'xml
	 * 
	 * @param view
	 * @param idView
	 * @return
	 */
	public View load(View view, final int idView) {
		if (view == null) {
			view = findViewById(idView);
		}
		return view;
	}

	public EditText loadEdit(final View view, final int idView) {
		return (EditText) load(view, idView);
	}

	public Spinner loadSpinner(final View view, final int idView) {
		return (Spinner) load(view, idView);
	}

	public Button loadButton(final View view, final int idView) {
		return (Button) load(view, idView);
	}

	public String getLanguage() {
		return Locale.getDefault().getLanguage();
	}
	
	public boolean isLang(String language){
		if(language != null){
			return language.equalsIgnoreCase(getLanguage());
		}
		return false;
	}
	
	public Editor getPreferenceEditor(final String nomeFile, int mode) {
		SharedPreferences userDefault = this.getSharedPreferences(nomeFile, mode);
		return userDefault.edit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int menuId = getIdMenu();
		if(menuId != 0){
			return customOnCreateOptionMenu(menuId, menu);
		}
		return false;
	}

	public abstract int getIdMenu();

	public boolean customOnCreateOptionMenu(int resource, Menu menu){

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(resource, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
