package io.pivotal.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class CxpEventMetadata{
  public static final int CLASS_ID = 14;
  private Long eventTypeId;
  private Long customerIdTypeId;
  private String customerId;
  @JsonProperty(required = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX]",
          without = {JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS})
  private Timestamp eventTimestamp;
  @JsonProperty(required = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX]",
          without = {JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS})
  private Timestamp recordedTimestamp;
  public CxpEventMetadata() {
  }
  @JsonCreator
  public CxpEventMetadata(@JsonProperty(value = "eventTypeId") Long eventTypeId,
          @JsonProperty(value = "customerIdTypeId") Long customerIdTypeId,
          @JsonProperty(value = "customerId") String customerId,
          @JsonProperty(value = "eventTimestamp")
          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX]",
                  without = {JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS}) Timestamp eventTimestamp,
          @JsonProperty(value = "recordedTimestamp")
          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX]",
                  without = {JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS}) Timestamp recordedTimestamp
  ) {
    this.eventTypeId = eventTypeId;
    this.customerIdTypeId = customerIdTypeId;
    this.customerId = customerId;
    this.eventTimestamp = eventTimestamp;
    this.recordedTimestamp = recordedTimestamp;
  }
  public Long getEventTypeId() {
    return eventTypeId;
  }
  public void setEventTypeId(Long eventTypeId) {
    this.eventTypeId = eventTypeId;
  }
  public Long getCustomerIdTypeId() {
    return customerIdTypeId;
  }
  public void setCustomerIdTypeId(Long customerIdTypeId) {
    this.customerIdTypeId = customerIdTypeId;
  }
  public String getCustomerId() {
    return customerId;
  }
  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX]",
          without = {JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS})
  public Timestamp getEventTimestamp() {
    return eventTimestamp;
  }
  public void setEventTimestamp(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX]",
          without = {JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS}) Timestamp eventTimestamp) {
    this.eventTimestamp = eventTimestamp;
  }
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX]",
          without = {JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS})
  public Timestamp getRecordedTimestamp() {
    return recordedTimestamp;
  }
  public void setRecordedTimestamp(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX]",
          without = {JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS}) Timestamp recordedTimestamp) {
    this.recordedTimestamp = recordedTimestamp;
  }

  
}
