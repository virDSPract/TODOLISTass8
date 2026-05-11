package ToDOList;



import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class todolist {

    public static void main(String[] args) {

        try {

            // Create server on port 8008
            HttpServer server = HttpServer.create(new InetSocketAddress(8010), 0);

            // Create endpoint
            server.createContext("/", new TodoHandler());

            // Start server
            server.start();

            System.out.println("Server running at http://localhost:8010");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class TodoHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String task = "";
            String status = "";
            String priority = "";
            String result = "";

            // If form submitted
            if ("POST".equals(exchange.getRequestMethod())) {

                InputStreamReader isr =
                        new InputStreamReader(exchange.getRequestBody(), "utf-8");

                BufferedReader br = new BufferedReader(isr);

                String formData = br.readLine();

                String[] pairs = formData.split("&");

                task = URLDecoder.decode(pairs[0].split("=")[1], StandardCharsets.UTF_8);
                status = URLDecoder.decode(pairs[1].split("=")[1], StandardCharsets.UTF_8);
                priority = URLDecoder.decode(pairs[2].split("=")[1], StandardCharsets.UTF_8);

                if (status.equalsIgnoreCase("completed")) {
                    result = "Task Finished";
                } else {
                    result = "Task Pending";
                }
            }

            // HTML page
            String response = "<html>"
                    + "<head><title>Todo Web App</title></head>"
                    + "<body>"
                    + "<h2>Todo List Web App</h2>"

                    + "<form method='POST'>"

                    + "Task: <input type='text' name='task'><br><br>"

                    + "Status: "
                    + "<select name='status'>"
                    + "<option>completed</option>"
                    + "<option>pending</option>"
                    + "</select><br><br>"

                    + "Priority: "
                    + "<select name='priority'>"
                    + "<option>high</option>"
                    + "<option>medium</option>"
                    + "<option>low</option>"
                    + "</select><br><br>"

                    + "<input type='submit' value='Submit'>"

                    + "</form>"

                    + "<hr>"

                    + "<h3>Result</h3>"
                    + "Task : " + task + "<br>"
                    + "Status : " + status + "<br>"
                    + "Priority : " + priority + "<br>"
                    + "Message : " + result

                    + "</body>"
                    + "</html>";

            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();

            os.write(response.getBytes());

            os.close();
        }
    }
}//just open http://localhost:8010