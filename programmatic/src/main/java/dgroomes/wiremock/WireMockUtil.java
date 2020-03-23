package dgroomes.wiremock;

import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.AdminRequestHandler;
import com.github.tomakehurst.wiremock.http.HttpServer;
import com.github.tomakehurst.wiremock.http.StubRequestHandler;
import com.github.tomakehurst.wiremock.jetty9.JettyHttpServerFactory;
import com.github.tomakehurst.wiremock.jetty94.Jetty94HttpServer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.StatisticsHandler;

/**
 * Utilities for working with WireMock
 */
public class WireMockUtil {

    /**
     * Configure the Jetty server to shut down gracefully
     */
    public static WireMockConfiguration shutdownGracefully(WireMockConfiguration options) {
        // A stop timeout and a StatisticsHandler are required to enable Jetty to shutdown gracefully.
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

                    @Override
                    protected Handler[] extensionHandlers() {
                        return new Handler[]{new StatisticsHandler()};
                    }
                };
            }
        });
        return options;
    }
}
