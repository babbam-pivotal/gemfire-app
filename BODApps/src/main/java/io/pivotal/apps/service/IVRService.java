package io.pivotal.apps.service;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.Pool;
import org.apache.geode.cache.client.PoolManager;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.Struct;
import org.apache.geode.pdx.PdxInstance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import org.springframework.validation.BeanPropertyBindingResult;

import io.pivotal.apps.constants.Constants;
import io.pivotal.error.ValidationError;
import io.pivotal.events.CxpEventDto;
import io.pivotal.events.CxpEventMetadata;
import io.pivotal.events.CxpEventQueryParams;
import io.pivotal.events.CxpEventQueryParams.QueryMode;

@Service
public class IVRService {

	private ClientCache cache;
	private Region<Object, Object> ivrRegion;

	@Autowired
	private Validator validator;

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public IVRService() {
		this.cache = new ClientCacheFactory().set("cache-xml-file", Constants.IVR_CLIENT_CACHE).create();
		this.ivrRegion = cache.getRegion(Constants.IVR_REGION);
	}

	public ResponseEntity<?> get(Long eventTypeId, String customerId, Long customerIdTypeId, QueryMode queryMode,
			OffsetDateTime earliest, OffsetDateTime latest) {
		System.out.println("IVR Service requested: " + customerId);
		List<CxpEventQueryParams> queryParams = Collections.singletonList(
				createQueryParameter(eventTypeId, customerId, customerIdTypeId, earliest, latest, queryMode));
		final Errors bindingResult = new BeanPropertyBindingResult(queryParams, "queryParams");
		ValidationError validationErrorResponse = maybeCreateValidationErrorResponse(bindingResult, queryParams);
		if (validationErrorResponse != null) {
			return new ResponseEntity<>(validationErrorResponse, validationErrorResponse.getHttpStatus());
		}
		List<CxpEventDto> events = findADSLShapingEvents(getDataClassification(), queryParams);
		return new ResponseEntity<>(events != null ? events : Collections.emptyList(), HttpStatus.OK);

	}

	private List<CxpEventDto> findADSLShapingEvents(String dataClassification, List<CxpEventQueryParams> queryParams) {
		List<CxpEventDto> list = new ArrayList<CxpEventDto>();
		try {
			long currentTimeMillis1 = System.currentTimeMillis();
			Pool p = PoolManager.find(ivrRegion);
			QueryService qs = p.getQueryService();
			Query q = qs.newQuery("select * from /" + ivrRegion.getName() + " where sbl_cust_no='"
					+ queryParams.get(0).getCustomerId() + "'");//
			SelectResults<Object> results = (SelectResults) q.execute();
			list = getADSLShapingFromResult(queryParams.get(0).getCustomerId(), results, queryParams);
			long currentTimeMillis2 = System.currentTimeMillis();
			long milliseconds = (currentTimeMillis2 - currentTimeMillis1);
			System.out.println("Time to fetch record " + milliseconds + " milliseconds");
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	private List<CxpEventDto> getADSLShapingFromResult(String id, SelectResults results,
			List<CxpEventQueryParams> queryParams) throws JSONException {
		JSONArray ja = new JSONArray();
		List<CxpEventDto> list = new ArrayList<CxpEventDto>();
		CxpEventDto ced = new CxpEventDto();
		Iterator resultIterator = results.iterator();
		if (results.size() == 0) // No records found
		{
			ja.put(new JSONObject().put("ERROR: ", "No record found for id: " + id).toString());
		} else {
			for (int k = 0; k < results.size(); k++) {
				Object row = resultIterator.next();
				if (row instanceof Struct) {
					Struct struct = (Struct) row;
					JSONObject jo = new JSONObject();
					Map<String,Object> map = new HashMap<String,Object>();
					for (int i = 0; i < struct.getStructType().getFieldNames().length; i++) {
						jo.put(struct.getStructType().getFieldNames()[i], struct.getFieldValues()[i]);
						// map.put(struct.getStructType().getFieldNames()[i],
						// struct.getFieldValues()[i]);
						map.put(struct.getStructType().getFieldNames()[i], struct.getFieldValues()[i]);
						if (struct.getStructType().getFieldNames()[i].equals("shaping_reason")) {
							map.put("shapingReason", struct.getFieldValues()[i]);
							map.put("shapingStatus", (Short) struct.getFieldValues()[i] == 0 ? "N" : "Y");
						}
						if (struct.getStructType().getFieldNames()[i].equals("sbl_cust_no")) {
							map.put("customerId", struct.getFieldValues()[i]);
						}
					}
					// JSONObject metadataObject = new JSONObject();
					// metadataObject.put("customerId",
					// queryParams.get(0).getCustomerId());
					// metadataObject.put("customerIdTypeId",
					// queryParams.get(0).getCustomerIdTypeId());
					// metadataObject.put("eventTimestamp",
					// jo.get("modified_ts"));
					// metadataObject.put("eventTypeId",
					// queryParams.get(0).getEventTypeId());
					// metadataObject.put("recordedTimestamp",
					// jo.get("modified_ts"));
					// ja.put(new JSONObject().put("data", jo).put("metadata",
					// metadataObject));
					CxpEventMetadata cem = new CxpEventMetadata();
					cem.setCustomerId(queryParams.get(0).getCustomerId());
					cem.setCustomerIdTypeId(queryParams.get(0).getCustomerIdTypeId());
					cem.setEventTypeId(queryParams.get(0).getEventTypeId());
					cem.setEventTimestamp((Timestamp) jo.get("modified_ts"));
					cem.setRecordedTimestamp((Timestamp) jo.get("modified_ts"));
					ced.setData(map);
					ced.setMetadata(cem);
				} else if (row instanceof PdxInstance) {
					PdxInstance record = (PdxInstance) row;
					JSONObject jo = new JSONObject();
					Map<String,Object> map = new HashMap<String,Object>();
					for (String field : record.getFieldNames()) {
						jo.put(field, record.getField(field));
						// map.put(field, record.getField(field));
						if (field.equals("shaping_reason")) {
							map.put("shapingReason", record.getField(field));
							map.put("shapingStatus", (Short) record.getField(field) == 0 ? "N" : "Y");
						}
						if (field.equals("sbl_cust_no")) {
							map.put("customerId", record.getField(field));
						}
					}
					// JSONObject metadataObject = new JSONObject();
					// metadataObject.put("customerId",
					// queryParams.get(0).getCustomerId());
					// metadataObject.put("customerIdTypeId",
					// queryParams.get(0).getCustomerIdTypeId());
					// metadataObject.put("eventTimestamp",
					// jo.get("modified_ts"));
					// metadataObject.put("eventTypeId",
					// queryParams.get(0).getEventTypeId());
					// metadataObject.put("recordedTimestamp",
					// jo.get("modified_ts"));
					// ja.put(new JSONObject().put("data", jo).put("metadata",
					// metadataObject));
					CxpEventMetadata cem = new CxpEventMetadata();

					cem.setCustomerId(queryParams.get(0).getCustomerId());
					cem.setCustomerIdTypeId(queryParams.get(0).getCustomerIdTypeId());
					cem.setEventTypeId(queryParams.get(0).getEventTypeId());
					cem.setEventTimestamp((Timestamp) record.getField("modified_ts"));
					cem.setRecordedTimestamp((Timestamp) record.getField("modified_ts"));
					ced.setData(map);
					ced.setMetadata(cem);
				}
				list.add(ced);
			}
		}

		return list;
	}
	
	@Valid
	private CxpEventQueryParams createQueryParameter(final Long eventTypeId, final String customerId,
			final Long customerIdType, final OffsetDateTime earliest, final OffsetDateTime latest,
			final QueryMode queryMode) {
		return new CxpEventQueryParams(eventTypeId, customerIdType, customerId, earliest, latest, queryMode);
	}

	private ValidationError maybeCreateValidationErrorResponse(final Errors bindingResult,
			final Collection<CxpEventQueryParams> queryParams) {
		for (CxpEventQueryParams entry : queryParams) {
			validator.validate(entry, bindingResult);
		}
		return bindingResult.hasErrors() ? new ValidationError(bindingResult.getAllErrors()) : null;
	}

	protected String getDataClassification() {
		return "CORP";
	}

}
