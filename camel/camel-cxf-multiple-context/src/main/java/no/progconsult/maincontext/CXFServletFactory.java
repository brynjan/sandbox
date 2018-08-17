package no.progconsult.maincontext;

import org.apache.cxf.transport.servlet.CXFServlet;

/**
 * @author <a href="mailto:brynjar.norum@progconsult.no">Brynjar Norum</a> 2017-11-02.
 */
public class CXFServletFactory {
    private static CXFServlet cxf = new CXFServlet();

    public static CXFServlet getCxfServlet() {
        return cxf;
    }
}
