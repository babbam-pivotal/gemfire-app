package io.pivotal.apps.service;

import java.util.Iterator;

import org.apache.geode.cache.client.Pool;
import org.apache.geode.cache.client.PoolManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import io.pivotal.apps.constants.Constants;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.Struct;
import org.apache.geode.pdx.PdxInstance;

@Service
public class CATService {

	private ClientCache cache;
	private Region<Object, Object> catRegion;

	public CATService() {
		this.cache = new ClientCacheFactory().set("cache-xml-file", Constants.CAT_CLIENT_CACHE).create();
		this.catRegion = cache.getRegion(Constants.CAT_REGION);
	}
	
	public String get(String id) {
		System.out.println("CAT Service requested: " + id);
		JSONArray jsonResult = new JSONArray();
		try {
			long currentTimeMillis1 = System.currentTimeMillis();
			Pool p = PoolManager.find(catRegion);
			QueryService qs = p.getQueryService();
			Query q = qs.newQuery("select * from /"+catRegion.getName()+" where customer_id='" + id + "'");//
			SelectResults results = (SelectResults) q.execute();
			jsonResult = getJSONFromResult(id, results);
			long currentTimeMillis2 = System.currentTimeMillis();
			long milliseconds = (currentTimeMillis2 - currentTimeMillis1);
			System.out.println("Time to fetch record " + milliseconds + " milliseconds");
		} catch (Exception e) {

			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return jsonResult.toString() + "\n";
	}
	
	private JSONArray getJSONFromResult(String id, SelectResults results) throws JSONException
	{
		JSONArray ja = new JSONArray();
		Iterator resultIterator = results.iterator();
		System.out.println(results.getClass());
		if (results.size() == 0) // No records found
		{
			ja.put(new JSONObject().put("ERROR: ", "No record found for id: " + id));
		} else {
			for (int k = 0; k < results.size(); k++) {
				Object row = resultIterator.next();
				System.out.println(row.getClass());
				if (row instanceof Struct) {
					Struct struct = (Struct) row;
					JSONObject jo = new JSONObject();
					for (int i = 0; i < struct.getStructType().getFieldNames().length; i++) {
						jo.put(struct.getStructType().getFieldNames()[i], struct.getFieldValues()[i]);
					}
					ja.put(jo);
				} else if (row instanceof PdxInstance) {
					PdxInstance record = (PdxInstance) row;
					JSONObject jo = new JSONObject();
					for(String field : record.getFieldNames())
					{
						jo.put(field, record.getField(field));
					}
					ja.put(jo);
				}
			}
		}
		return ja;
	}

}
