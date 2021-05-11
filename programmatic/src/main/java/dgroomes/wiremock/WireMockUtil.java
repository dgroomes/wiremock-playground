package dgroomes.wiremock;

import com.github.tomakehurst.wiremock.common.JettySettings;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockApp;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.AdminRequestHandler;
import com.github.tomakehurst.wiremock.http.HttpServer;
import com.github.tomakehurst.wiremock.http.StubRequestHandler;
import com.github.tomakehurst.wiremock.jetty9.JettyHttpServerFactory;
import com.github.tomakehurst.wiremock.jetty94.Jetty94HttpServer;
import org.eclipse.jetty.io.ConnectionStatistics;
import org.eclipse.jetty.io.NetworkTrafficListener;
import org.eclipse.jetty.server.ConnectionFactory;
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
        // A stop timeout and a StatisticsHandler are required to enable Jetty to shutdown gracefully.
        // See the official guide at https://www.eclipse.org/jetty/documentation/current/statistics-handler.html
        // See the docs at https://www.eclipse.org/jetty/javadoc/9.4.27.v20200227/org/eclipse/jetty/server/Server.html#setStopTimeout(long)
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
                return new Jetty94HttpServer(options, adminRequestHandler, stubRequestHandler) {

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
                     *
                     * See https://www.eclipse.org/jetty/documentation/current/statistics-handler.html#connector-statistics
                     */
                    @Override
                    protected ServerConnector createServerConnector(String bindAddress, JettySettings jettySettings, int port, NetworkTrafficListener listener, ConnectionFactory... connectionFactories) {
                        var serverConnector = super.createServerConnector(bindAddress, jettySettings, port, listener, connectionFactories);
                        serverConnector.addBean(new ConnectionStatistics());
                        return serverConnector;
                    }
                };
            }
        });
    }

    /**
     * Configure a root directory for WireMock to load mappings from. This directory should contain the requisite
     * "__files/" and "mappings/__files/" directories.
     */
    public static void configureRootDir(WireMockConfiguration options, String rootDir) {
        var currentDir = new File("").getAbsolutePath();
        var rootDirFile = new File(rootDir);
        log.debug("Configuring a root directory. Asserting that it exists. Current dir: {}\troot dir: {}", currentDir,
                rootDir);

        if (!rootDirFile.exists()) {
            var msg = String.format("Attempted to configure a WireMock root dir but the directory does not exist! %s",
                    rootDirFile.getAbsolutePath());
            throw new IllegalArgumentException(msg);
        }

        var filesDir = new File(rootDirFile, WireMockApp.FILES_ROOT);
        if (!filesDir.exists()) {
            log.info("WireMock '{}' directory does not exist in the root. Expected to find it at {}. It is not " +
                    "required but you need to add this directory if your WireMock stubs depend on supporting " +
                    "files.", WireMockApp.FILES_ROOT, filesDir.getAbsolutePath());
        }

        var mappingsDir = new File(rootDirFile, WireMockApp.MAPPINGS_ROOT);
        if (!mappingsDir.exists()) {
            throw new IllegalArgumentException(String.format("WireMock '%s' directory does not exist in the root. " +
                            "Expected to find it at %s. It is required.", WireMockApp.MAPPINGS_ROOT,
                    mappingsDir.getAbsolutePath()));
        }

        options.withRootDirectory(rootDir);
    }

    public static void configureExtensions(WireMockConfiguration options) {
        options.extensions(new RandomIntegerTransformer());
    }
}
