package no.progconsult.flow.elhub.pollingservice.common;

import org.apache.camel.Headers;
import org.apache.camel.component.sql.SqlConstants;
import org.apache.cxf.message.MessageContentsList;

import java.util.List;
import java.util.Map;

import static no.progconsult.flow.elhub.pollingservice.common.ElhubPollConstants.OUTBOUND_RECORDS;
import static org.apache.camel.component.sql.SqlConstants.SQL_RETRIEVE_GENERATED_KEYS;

/**
 * Delegate for Exchange functionality.
 *
 * @author bno
 */
public class ExchangeDelegate {

    /**
     * Fetch returning id from preceding insert to database. Assuming the column name is "id".
     *
     * @param headers
     * @return
     */
    public Long fetchReturningId(Map<String, Object> headers) {
        final List<Map<String, Long>> generatedKeys = (List<Map<String, Long>>) headers.get(SqlConstants.SQL_GENERATED_KEYS_DATA);
        final Map<String, Long> row = generatedKeys.get(0);
        return row.get("id");
    }

    @SuppressWarnings("unchecked")
    public List<Map<?, ?>> fetchOutboundRecords(Map<String, Object> headers) {
        return (List<Map<?, ?>>) headers.get(OUTBOUND_RECORDS);
    }

    /**
     * Enables returning of id when inserting to database.
     *
     * @param headers
     */
    public void enableReturningId(final Map<String, Object> headers) {
        headers.put(SQL_RETRIEVE_GENERATED_KEYS, true);
        headers.put(SqlConstants.SQL_GENERATED_COLUMNS, new String[]{"id"});
    }

    public <T> T unpackCxfBody(Object body) {
        return (T) ((MessageContentsList) body).get(0);
    }
}
