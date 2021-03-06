package no.porqpine.settlersgame;

import no.porqpine.settlersgame.api.EventServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static no.porqpine.settlersgame.GameHolder.GAME_LIST;

public class Main {

   public static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        Thread gameLoop = new Thread(GAME_LIST);
        gameLoop.start();

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        int port = 8080;
        String envPort = System.getenv("PORT");
        if(envPort != null){
            port = Integer.parseInt(envPort);
        }
        connector.setPort(port);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");



        // Add a websocket to a specific path spec
        EventServlet gameStateServlet = new EventServlet();
        ServletHolder holderEvents = new ServletHolder("ws-events", gameStateServlet);
        context.addServlet(holderEvents, "/game-state/*");

        context.setBaseResource(Resource.newResource(Main.class.getClassLoader().getResource("webapp")));
        DefaultServlet defaultServlet = new DefaultServlet();
        context.addServlet(new ServletHolder("default",defaultServlet),"/");

        server.setHandler(context);
        server.setStopAtShutdown(true);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down");
            GAME_LIST.stop();
            try {
                gameLoop.join();
                server.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Game stopped.");
        }));

        try
        {
            server.start();
            server.dump(System.err);
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
}
