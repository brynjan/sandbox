package no.progconsult.flow.elhub.pollingservice.common;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

/**
 * @author bno
 */
public final class DateUtils {

    public static LocalDateTime utc() {
        return ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();
    }

    public static XMLGregorianCalendar zonedDateTimeToXmlGregorian(ZonedDateTime now) {
        GregorianCalendar gregorianCalendar = GregorianCalendar.from(now);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

}
