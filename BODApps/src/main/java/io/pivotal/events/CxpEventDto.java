package io.pivotal.events;

import java.io.Serializable;
import java.util.Map;

public class CxpEventDto implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//  public static CxpEventDto fromEvent(final CxpEvent event) {
//    CxpEventDto results = new CxpEventDto();
//    results.setData(new HashMap<>(event.getData()));
//    results.setMetadata(event.getMetadata());
//    return results;
//  }
  private Map<String, Object> data;
  private CxpEventMetadata metadata;
  public Map<String, Object> getData() {
    return data;
  }
  public void setData(Map<String, Object> data) {
    this.data = data;
  }
  public CxpEventMetadata getMetadata() {
    return metadata;
  }
  public void setMetadata(CxpEventMetadata metadata) {
    this.metadata = metadata;
  }
}