package no.progconsult.flow.elhub.pollingservice.common;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * For an incoming message we need to add some headers to get some context.
 * The unique message id, incoming timestamp, and the user that sent the message.
 */
public class QuantFlowContextHeaders implements Processor {
    private static final String AUTH_HEADER = "Authorization";

    public static final String MSG_ID_HEADER = "QF_MSG_ID";

    public static final String MSG_SENDER_HEADER = "QF_MSG_SENDER";

    public static final String MSG_RECEIVER_HEADER = "QF_MSG_RECEIVER";


    public static final String MSG_TIMESTAMP_HEADER = "QF_MSG_TIMESTAMP";

    private Message receiver;


    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getIn();
        setMessageId(msg);
        setTimestamp(msg);
        setSender(msg);
        setReceiver(msg);
        //We can remove the auth header after we have gotten the username.
        msg.removeHeader(AUTH_HEADER);
    }

    public void setReceiver(Message msg) {
        msg.setHeader(MSG_RECEIVER_HEADER, "Quant flow");
    }

    private void setSender(Message msg) {
        msg.setHeader(MSG_SENDER_HEADER, getUsernameFromBasicAuthHeader(msg.getHeader(AUTH_HEADER, String.class)));
    }

    private String getUsernameFromBasicAuthHeader(String header) {
        if (header != null) {
            String decoded = new String(Base64.getDecoder().decode(header.replace("Basic ", "")));
            return decoded.substring(0, decoded.indexOf(":"));
        }
        return null;
    }

    private void setTimestamp(Message msg) {
        msg.setHeader(MSG_TIMESTAMP_HEADER, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()));
    }

    private void setMessageId(final Message msg) {
        msg.setHeader(MSG_ID_HEADER, msg.getHeader(Exchange.BREADCRUMB_ID));
    }
}
