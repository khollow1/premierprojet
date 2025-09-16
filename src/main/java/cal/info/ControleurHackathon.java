package cal.info;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ControleurHackathon implements HttpHandler {

    private List<Hackathon> lesHackathons = new ArrayList<>();

    @Override
    public void handle(HttpExchange echange) throws IOException {
        String response = "Bienvenue au controle des hackathons !";
        echange.sendResponseHeaders(200, response.length());
        OutputStream os = echange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
