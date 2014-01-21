package com.molinari.androidstructure.data.cache;

import java.util.Map;

import com.molinari.androidstructure.data.db.IOggettoEntita;

public abstract class AbstractCacheBase {

	protected Map<String, IOggettoEntita> cache;
	protected boolean caricata = false;
	
	/**
	 * @return the caricata
	 */
	public boolean isCaricata() {
		return caricata;
	}

	/**
	 * @param caricata the caricata to set
	 */
	public void setCaricata(boolean caricata) {
		this.caricata = caricata;
	}
	
	public Map<String, IOggettoEntita> getCache() {
		return cache;
	}

	public void setCache(Map<String, IOggettoEntita> cache) {
		this.cache = cache;
	}

}
