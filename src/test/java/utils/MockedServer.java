package utils;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class MockedServer {

    private static Server server;

    public static void startServer() throws Exception {
        server = new Server(5555);

        final ResourceHandler resHandler = new ResourceHandler();
        resHandler.getMimeTypes().addMimeMapping("json", "application/json");
        resHandler.setResourceBase("./src/test/resources/content.json");
        final ContextHandler ctx = new ContextHandler("/api/someString");
        ctx.setHandler(resHandler);
        server.setHandler(ctx);

        server.start();
    }

    public static void stopServer() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    public static void main(String[] args) throws Exception {
        startServer();
        server.join();
    }
}
