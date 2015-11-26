package no.embriq.quant.flow.elhub.pollingservice.common;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuantFlowContextHeadersTest {

    @Test
    public void headersAreSetAndAuthHeaderIsRemoved() throws Exception {
        QuantFlowContextHeaders processor = new QuantFlowContextHeaders();

        Exchange exchange = mock(Exchange.class);
        Message message = new DefaultMessage();
        message.setHeader(Exchange.BREADCRUMB_ID, "id");
        // elwin:adapter user:pass
        message.setHeader("Authorization", "Basic ZWx3aW46YWRhcHRlcg==");
        when(exchange.getIn()).thenReturn(message);
        processor.process(exchange);

        String msgId = message.getHeader(QuantFlowContextHeaders.MSG_ID_HEADER, String.class);
        assertThat(msgId).isEqualTo("id");

        String timestamp = message.getHeader(QuantFlowContextHeaders.MSG_TIMESTAMP_HEADER, String.class);
        assertThat(timestamp).isNotNull();

        String sender = message.getHeader(QuantFlowContextHeaders.MSG_SENDER_HEADER, String.class);
        assertThat(sender).isEqualTo("elwin");

        String receiver = message.getHeader(QuantFlowContextHeaders.MSG_RECEIVER_HEADER, String.class);
        assertThat(receiver).isEqualTo("Quant flow");

        assertThat(message.getHeader("Authorization")).isNull();
    }
}