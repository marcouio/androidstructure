package com.molinari.androidstructure.data.db;

import java.util.Iterator;

public interface IDao {

	public IOggettoEntita getEntitaPadre() throws Exception;

	public Object selectById(int id) throws Exception;

	public Iterator<Object> selectWhere(String where) throws Exception;

	public Iterator<Object> selectAll() throws Exception;

	public boolean insert(Object oggettoEntita) throws Exception;

	public boolean delete(int id) throws Exception;

	public boolean update(Object oggettoEntita) throws Exception;

	public boolean deleteAll() throws Exception;
}
