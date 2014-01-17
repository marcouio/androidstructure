package com.molinari.androidstructure.data.db;

import java.sql.Connection;
import java.util.ArrayList;

import android.content.Context;
import android.database.SQLException;

/**
 * @author marco.molinari
 *
 */
public abstract class ConnectionPool {

	private static ArrayList<Connection> freeConnections; // La coda di connessioni libere
	private static Connection lastConnection;
	public Context			applicationContext	= null;
	
	public ConnectionPool(Context context) {
		this.applicationContext = context;
		ConnectionPool.freeConnections = new ArrayList<Connection>();
		
		
		for(int i = 0; i < getNumeroConnessioniDisponibili(); i++){
			releaseNewConnection();
		}
	}
	
	/**
	 * Restituisce la prima connessione disponibile all'interno del pool di connessioni disponibili
	 * @return {@link Connection} 
	 */
	public synchronized Connection getConnection(){
		Connection cn = null;
		if (freeConnections.size() > 0) {
			cn = freeConnections.get(0);
			freeConnections.remove(0);
			
			try {
				if(cn.isClosed()){
					cn = getConnection();
				}
			} catch (SQLException e) {
				cn = getConnection();
			} catch (java.sql.SQLException e) {
				cn = getConnection();
			}
			
		}else{
			releaseNewConnection();
			cn = getConnection();
		}
		lastConnection = cn;
		return cn;
	}
	

	/**
	 * Il metodo newConnection restituisce una nuova connessione
	 * @return {@link Connection} 
	 */
	protected abstract Connection newConnection();
	

	/**
	 * 	Il metodo releaseConnection rilascia una connessione inserendola
	 *  nella coda delle connessioni libere
	 */
	public synchronized void releaseNewConnection() {
		final Connection cn = newConnection();
		releaseConnection(cn);
	}

	/**
	 * 	Il metodo releaseConnection rilascia una connessione inserendola
	 *  nella coda delle connessioni libere
	 */
	public synchronized static void releaseConnection(final Connection con) {
	        // Inserisce la connessione nella coda
	        freeConnections.add(con);
	}
	
	
	/**
	 * Chiude la connessione e se c'è il relativo resultSet o la statement
	 * @param cn
	 * @throws SQLException
	 * @throws java.sql.SQLException 
	 */
	public void chiudiOggettiDb(Connection cn) throws SQLException, java.sql.SQLException {
		if(cn == null){
			cn = lastConnection;
		}
		if(cn != null){
			cn.close();
		}
			
	}

	/**
	 * @return numero massimo di connessioni disponibili
	 */
	protected abstract int getNumeroConnessioniDisponibili();
	
	public Context getApplicationContext() {
		return applicationContext;
	}
	public void setApplicationContext(Context applicationContext) {
		this.applicationContext = applicationContext;
	}
}
