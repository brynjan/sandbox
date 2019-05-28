package no.progconsult.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * @author <a href="mailto:brynjar.norum@rejlers.no">Brynjar Norum</a> 2019-05-28.
 */
public class HelloLambda implements RequestHandler<Integer, String> {

    @Override
    public String handleRequest(Integer integer, Context context) {
        return String.valueOf(integer);
    }
}
