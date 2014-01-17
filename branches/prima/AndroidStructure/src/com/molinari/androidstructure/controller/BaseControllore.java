package com.molinari.androidstructure.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.molinari.androidstructure.command.AbstractCommand;
import com.molinari.androidstructure.command.CommandManager;
import com.molinari.androidstructure.data.db.ConnectionPool;

public abstract class BaseControllore {
	
	private CommandManager commandManager;
	private ConnectionPool connectionPool;
	
	public ConnectionPool getConnectionPool(Context context){
		if (connectionPool == null) {
			connectionPool = createConnectionPool(context);
		}
		connectionPool.setApplicationContext(context);
		return connectionPool;
	}
	
	protected abstract ConnectionPool createConnectionPool(Context context);

	public CommandManager getCommandManager() {
		if (commandManager == null) {
			commandManager = CommandManager.getIstance();
		}
		return commandManager;
	}
	
	public boolean invocaComando(final AbstractCommand comando) throws Exception {
		return getCommandManager().invocaComando(comando);
	}
	
	public abstract boolean isLogged();
	
	public void apriActivity(View v, Class<Activity> classActivity){
		final Intent i = new Intent(v.getContext(), classActivity);
		v.getContext().startActivity(i);
	}
}
