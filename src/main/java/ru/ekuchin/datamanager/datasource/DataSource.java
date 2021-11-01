package ru.ekuchin.datamanager.datasource;


import java.util.List;

public interface DataSource {

	//CRUD operations
	String create(String json) throws Exception;

	String create(String json, String params) throws Exception;

	String read(String unid, String params) throws Exception;

	String read(String unid) throws Exception;

	boolean update(String unid, String json) throws Exception;

	boolean update(String unid, String json, boolean replaceAllItems) throws Exception;

	boolean update(String unid, String json, String params) throws Exception;

	boolean update(String unid, String json, boolean replaceAllItems, String params) throws Exception;

	boolean delete(String unid, String params) throws Exception;

	boolean delete(String unid) throws Exception;

	//Collection operations
	List<String> findAll(String collection, String params) throws Exception;
	List<String> findAll(String collection) throws Exception;

	//String searchByKey(String collection, String key, boolean exactMatch, String params) throws Exception;
}