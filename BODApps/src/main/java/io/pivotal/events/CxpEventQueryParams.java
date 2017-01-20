package io.pivotal.events;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import java.time.OffsetDateTime;

@Validated
public class CxpEventQueryParams
{
    public enum QueryMode
    {
        historical,
        current;
    }
    private long eventTypeId;
    private long customerIdTypeId;
    private String customerId;
    private QueryMode queryMode = QueryMode.historical;
    @JsonProperty
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX][X]", without = {
        JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS } )
    private OffsetDateTime earliest;
    @JsonProperty
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX][X]", without = {
        JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS } )
    private OffsetDateTime latest;
    public CxpEventQueryParams()
    {
        // no op
    }
    @JsonCreator
    public CxpEventQueryParams( @JsonProperty( value = "eventTypeId", required = true ) long eventTypeId,
                                @JsonProperty( value = "customerIdTypeId", required = true ) long customerIdTypeId,
                                @JsonProperty( value = "customerId", required = true ) String customerId,
                                @JsonProperty( value = "earliest" ) //
                                    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX][X]", //
                                    without = { JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS } ) OffsetDateTime earliest, //
                                @JsonProperty( value = "latest" ) //
                                    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX][X]", //
                                    without = { JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS } ) OffsetDateTime latest, //
                                @JsonProperty( value = "queryMode", defaultValue = "historical" ) final QueryMode queryMode )
    {
        this.eventTypeId = eventTypeId;
        this.customerIdTypeId = customerIdTypeId;
        this.customerId = customerId;
        this.earliest = earliest;
        this.latest = latest;
        this.queryMode = queryMode == null ? QueryMode.historical : queryMode;
    }
    public long getEventTypeId()
    {
        return eventTypeId;
    }
    public void setEventTypeId( long eventTypeId )
    {
        this.eventTypeId = eventTypeId;
    }
    public long getCustomerIdTypeId()
    {
        return customerIdTypeId;
    }
    public void setCustomerIdTypeId( long customerIdTypeId )
    {
        this.customerIdTypeId = customerIdTypeId;
    }
    public String getCustomerId()
    {
        return customerId;
    }
    public void setCustomerId( String customerId )
    {
        this.customerId = customerId;
    }
    public OffsetDateTime getEarliest()
    {
        return earliest;
    }
    public void setEarliest(
        @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX][X]", //
            without = { JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS } ) OffsetDateTime earliest )
    {
        this.earliest = earliest;
    }
    public OffsetDateTime getLatest()
    {
        return latest;
    }
    public void setLatest(
        @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX][X]", //
            without = { JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS } ) OffsetDateTime latest )
    {
        this.latest = latest;
    }
    public QueryMode getQueryMode()
    {
        return queryMode;
    }
    public void setQueryMode( QueryMode queryMode )
    {
        this.queryMode = queryMode;
    }
//    @Override
//    public void writeData( ObjectDataOutput out )
//        throws IOException
//    {
//        out.writeLong( eventTypeId );
//        out.writeLong( customerIdTypeId );
//        out.writeUTF( customerId );
//        if ( queryMode != null )
//        {
//            out.writeBoolean( true );
//            out.writeUTF( queryMode.name() );
//        }
//        else
//        {
//            out.writeBoolean( false );
//        }
//        if ( earliest != null )
//        {
//            out.writeBoolean( true );
//            out.writeObject( earliest );
//        }
//        else
//        {
//            out.writeBoolean( false );
//        }
//        if ( latest != null )
//        {
//            out.writeBoolean( true );
//            out.writeObject( latest );
//        }
//        else
//        {
//            out.writeBoolean( false );
//        }
//    }
//    @Override
//    public void readData( ObjectDataInput in )
//        throws IOException
//    {
//        eventTypeId = in.readLong();
//        customerIdTypeId = in.readLong();
//        customerId = in.readUTF();
//        if ( in.readBoolean() )
//        {
//            queryMode = QueryMode.valueOf( in.readUTF() );
//        }
//        if ( in.readBoolean() )
//        {
//            earliest = in.readObject();
//        }
//        if ( in.readBoolean() )
//        {
//            latest = in.readObject();
//        }
//    }
    @AssertTrue( message = "queryMode must be either 'current' or 'historical'" )
    @JsonIgnore
    public boolean isValidQueryMode()
    {
        return queryMode != null;
    }
    @AssertFalse( message = "'earliest' and 'latest' timestamps must be specified for historical queries" )
    @JsonIgnore
    public boolean isInvalidHistoricalQueryMode()
    {
        return QueryMode.historical.equals( getQueryMode() ) ? ( getEarliest() == null || getLatest() == null ) : false;
    }
}
