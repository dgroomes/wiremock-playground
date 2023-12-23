package dgroomes.wiremock;

import com.github.tomakehurst.wiremock.common.HttpsSettings;
import com.github.tomakehurst.wiremock.common.JettySettings;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockApp;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.AdminRequestHandler;
import com.github.tomakehurst.wiremock.http.HttpServer;
import com.github.tomakehurst.wiremock.http.StubRequestHandler;
import com.github.tomakehurst.wiremock.jetty.JettyHttpServerFactory;
import com.github.tomakehurst.wiremock.jetty11.Jetty11HttpServer;
import org.eclipse.jetty.io.ConnectionStatistics;
import org.eclipse.jetty.io.NetworkTrafficListener;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.StatisticsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Utilities for working with WireMock
 */
public class WireMockUtil {

    private static final Logger log = LoggerFactory.getLogger(WireMockUtil.class);

    /**
     * Configure WireMock's underlying Jetty server with the "statistics" feature
     * <p>
     * This has a couple effects:
     * 1. Enables Jetty statistics, like number of requests and number of connections, to be available at the URL
     * "/stats/"
     * 2. Enables graceful shutdown
     */
    public static void configureStatistics(WireMockConfiguration options) {
        // A stop timeout and a StatisticsHandler are required to enable Jetty to shut down gracefully.
        // See the official guide at https://eclipse.dev/jetty/documentation/jetty-11/programming-guide/index.html#pg-server-http-handler-use-util-stats-handler
        // See the source code https://github.com/jetty/jetty.project/blob/5a9a771a9fbcb9d36993630850f612581b78c13f/jetty-server/src/main/java/org/eclipse/jetty/server/Server.java#L490
        // See the note at https://github.com/eclipse/jetty.project/issues/2076#issuecomment-353717761
        //
        // But how exactly does it work? Here are some links to dig deeper:
        //   * https://github.com/tomakehurst/wiremock/issues/710
        //   * https://github.com/eclipse/jetty.project/pull/2100/files
        //   * https://github.com/eclipse/jetty.project/pull/2047
        //   * https://github.com/eclipse/jetty.project/issues/2076
        //   * https://github.com/tipsy/javalin/pull/297/files
        //   * https://github.com/tipsy/javalin/issues/286
        options.jettyStopTimeout(10_000L);
        options.httpServerFactory(new JettyHttpServerFactory() {
            @Override
            public HttpServer buildHttpServer(Options options, AdminRequestHandler adminRequestHandler, StubRequestHandler stubRequestHandler) {
                return new Jetty11HttpServer(options, adminRequestHandler, stubRequestHandler) {

                    /**
                     * Hook into the handler configuration code and splice in the StatisticsHandler and the
                     * StatisticsServlet
                     */
                    @Override
                    protected HandlerCollection createHandler(Options options, AdminRequestHandler adminRequestHandler, StubRequestHandler stubRequestHandler) {
                        var handlers = super.createHandler(options, adminRequestHandler, stubRequestHandler);
                        var statisticsHandler = new StatisticsHandler();
                        statisticsHandler.setHandler(handlers);
                        var contexts = new ContextHandlerCollection();
                        ServletContextHandler statsContext = new ServletContextHandler(contexts, "/stats");
                        statsContext.addServlet(new ServletHolder(new StatisticsServlet()), "/");
                        statsContext.setSessionHandler(new SessionHandler());
                        Handler[] existing = handlers.getChildHandlers();
                        Handler[] children = new Handler[existing.length + 1];
                        children[0] = contexts;
                        System.arraycopy(existing, 0, children, 1, existing.length);
                        handlers.setHandlers(children);
                        // Unfortunately, the statisticsHandler needs to be wrapped in a HandlerCollection because the
                        // super method must return the type "HandlerCollection", but really the method can afford to
                        // return the more generic type "Handler". So we must accommodate the unnecessarily specific
                        // type signature.
                        return new HandlerCollection(statisticsHandler);
                    }

                    /**
                     * Enable connector statistics.
                     * <p>
                     * See <a href="https://www.eclipse.org/jetty/documentation/current/statistics-handler.html#connector-statistics">the Jetty docs</a>.
                     */
                    @Override
                    protected ServerConnector createHttpConnector(String bindAddress, int port, JettySettings jettySettings, NetworkTrafficListener listener) {
                        var serverConnector = super.createHttpConnector(bindAddress, port, jettySettings, listener);
                        serverConnector.addBean(new ConnectionStatistics());
                        return serverConnector;
                    }

                    @Override
                    protected ServerConnector createHttpsConnector(String s, HttpsSettings httpsSettings, JettySettings jettySettings, NetworkTrafficListener networkTrafficListener) {
                        throw new IllegalStateException("HTTPS support is not implemented.");
                    }
                };
            }
        });
    }

    /**
     * Configure a root directory for WireMock to load mappings from and to find supporting files.
     * <p>
     * WireMock offers some configurability for customizing the location of the mappings and the supporting files using
     * objects like {@link com.github.tomakehurst.wiremock.store.Stores} but some of this support is in flux and is
     * annotated with {@link org.wiremock.annotations.Beta}. I recommend sticking with the convention of pointing to a
     * "root" directory which contains two subdirectories: "mappings" and "__files". WireMock hard codes to this convention,
     * and it's confusing to stray from it. See {@link WireMockApp#MAPPINGS_ROOT} and {@link WireMockApp#FILES_ROOT}.
     */
    public static void configureRootDir(WireMockConfiguration options, String rootDir) {
        var currentDir = new File("").getAbsolutePath();
        var rootDirFile = new File(rootDir);
        log.debug("Configuring a root directory. Asserting that it exists. Current dir: '{}'\troot dir: '{}'", currentDir, rootDir);

        if (!rootDirFile.exists()) {
            var msg = String.format("Attempted to configure a WireMock root dir but the directory does not exist! '%s'", rootDirFile.getAbsolutePath());
            throw new IllegalArgumentException(msg);
        }

        var filesDir = new File(rootDirFile, WireMockApp.FILES_ROOT);
        if (!filesDir.exists()) {
            log.info("WireMock '{}' directory does not exist in the root. Expected to find it at '{}'. It is not required but you need to add this directory if your WireMock stubs depend on supporting files.", WireMockApp.FILES_ROOT, filesDir.getAbsolutePath());
        }

        var mappingsDir = new File(rootDirFile, WireMockApp.MAPPINGS_ROOT);
        if (!mappingsDir.exists()) {
            var msg = String.format("WireMock '%s' directory does not exist in the root. Expected to find it at '%s'. It is expected that user mappings are provided.", WireMockApp.MAPPINGS_ROOT, mappingsDir.getAbsolutePath());
            throw new IllegalArgumentException(msg);
        }

        options.withRootDirectory(rootDir);
    }

    public static void configureExtensions(WireMockConfiguration options) {
        options.extensions(new RandomIntegerTransformer());
    }
}
