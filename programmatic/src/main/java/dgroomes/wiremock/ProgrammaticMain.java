package dgroomes.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * Run a WireMock server programmatically from Java code
 */
public class ProgrammaticMain {

    private static final Logger log = LoggerFactory.getLogger(ProgrammaticMain.class);
    private static final int SLEEP_SECONDS = 10;
    private static final int PORT = 8080;
    private static final String ROOT_DIR = "wiremock/scenarios/happy-path";

    public static void main(String[] args) throws InterruptedException {
        var start = Instant.now();
        var options = new WireMockConfiguration()
                .port(PORT);
        WireMockUtil.configureStatistics(options);
        WireMockUtil.configureRootDir(options, ROOT_DIR);
        WireMockServer wireMockServer = new WireMockServer(options);
        log.debug("Starting the WireMock server");
        wireMockServer.start();
        log.info("WireMock server started!");
        var startDuration = Duration.between(start, Instant.now());
        log.debug("Boot up time was {}", startDuration);
        log.debug("View statistics at http://localhost:{}/stats/", PORT);

        var duration = Duration.ofSeconds(SLEEP_SECONDS);
        log.info("Sleeping for {}", duration);
        Thread.sleep(duration.toMillis());

        log.info("Stopping the WireMock server");
        wireMockServer.stop();
        log.info("The WireMock server is stopped");
    }

}
