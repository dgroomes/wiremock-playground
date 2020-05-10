package dgroomes.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Run a WireMock server
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);
    public static final int SLEEP_SECONDS = 5;

    public static void main(String[] args) throws InterruptedException {
        WireMockConfiguration options = WireMockUtil.shutdownGracefully(new WireMockConfiguration());
        WireMockServer wireMockServer = new WireMockServer(options);
        log.info("Starting the WireMock server");
        wireMockServer.start();

        var duration = Duration.ofSeconds(SLEEP_SECONDS);
        log.info("Sleeping for {}", duration);
        Thread.sleep(duration.toMillis());

        log.info("Stopping the WireMock server");
        wireMockServer.stop();
        log.info("The WireMock server is stopped");
    }

}
