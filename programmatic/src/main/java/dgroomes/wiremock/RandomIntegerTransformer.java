package dgroomes.wiremock;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformerV2;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.Random;

/**
 * Return a random integer. Optionally, when a "limit" argument is supplied, use that as the exclusive maximum value.
 */
public class RandomIntegerTransformer implements ResponseTransformerV2 {

    private final Random random = new Random();

    @Override
    public String getName() {
        return "random-integer-transformer";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    private static final String template = """
            {
              "random_integer":  %d
            }""";

    @Override
    public Response transform(Response response, ServeEvent serveEvent) {
        Parameters parameters = serveEvent.getTransformerParameters();
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
