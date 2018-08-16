package no.progconsult.springbootsqs.common;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2018-08-06.
 */
public interface Constants {

    /**
     * Used for correlating logs across all applications.
     * <p>
     * Corresponds to org.apache.camel.Exchange#BREADCRUMB_ID
     */
    String BREADCRUMB_ID = "breadcrumbId";
}
