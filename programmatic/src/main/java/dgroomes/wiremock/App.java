package dgroomes.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws InterruptedException {
        WireMockServer wireMockServer = new WireMockServer();
        log.info("Starting the WireMock server");
        wireMockServer.start();
        var duration = Duration.ofSeconds(5);
        log.info("Sleeping for {}", duration);
        Thread.sleep(duration.toMillis());
        log.info("Stopping the WireMock server");
        wireMockServer.stop();
        log.info("The WireMock server is stopped");
    }
}
