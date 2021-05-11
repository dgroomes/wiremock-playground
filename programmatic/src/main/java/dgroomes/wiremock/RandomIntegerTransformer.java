package dgroomes.wiremock;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

import java.util.Random;

/**
 * Return a random integer. Optionally, when a "limit" argument is supplied, use that as the exclusive maximum value.
 */
public class RandomIntegerTransformer extends ResponseTransformer {

    private Random random = new Random();

    @Override
    public String getName() {
        return "random-integer-transformer";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    private String template = "{\n" +
            "  \"random_integer\":" +
            "  %d\n" +
            "}";

    @Override
    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        if (parameters == null) {
            parameters = new Parameters();
        }

        var limit = ((int) parameters.getOrDefault("limit", 100));
        var randomInt = random.nextInt(limit);
        return Response.response()
                .body(String.format(template, randomInt))
                .build();
    }
}
