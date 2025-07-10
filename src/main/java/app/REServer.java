package app;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

import java.io.ObjectInputStream.GetField;
import java.net.URI;
import java.net.http.*;
import com.google.gson.JsonObject;


public class REServer {

        // Property Server - 8001
        private static final String PROPERTY_SERVER = "http://localhost:8001";
        // Analytics Server - 8002
        private static final String ANALYTICS_SERVER = "http://localhost:8002";
        private static final HttpClient client = HttpClient.newHttpClient();

        public static void main(String[] args) {

            // start Javalin on port 8000
            var app = Javalin.create()
                    .get("/", ctx -> ctx.result("Real Estate server is running"))
                    .start(8000);


            // configure endpoint handlers to process HTTP requests
            JavalinConfig config = new JavalinConfig();
            config.router.apiBuilder(() -> {

                // return a sale by sale ID
                app.get("/sales/{saleID}", ctx -> {
                    String id = ctx.pathParam("saleID");
                    HttpRequest propReq = HttpRequest.newBuilder()
                        .uri(URI.create(PROPERTY_SERVER + "/sales/" + id))
                        .build();
                    HttpResponse<String> propResp = client.send(propReq, HttpResponse.BodyHandlers.ofString());

                    // Create JSON body using Gson
                    JsonObject body = new JsonObject();
                    body.addProperty("queryType", "GET");
                    body.addProperty("params", "property_id=" + id);
                    body.addProperty("status", propResp.statusCode());

                    HttpRequest analyticsReq = HttpRequest.newBuilder()
                        .uri(URI.create(ANALYTICS_SERVER + "/metrics")) 
                        .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                        .build();

                    client.sendAsync(analyticsReq, HttpResponse.BodyHandlers.ofString());
                    ctx.status(propResp.statusCode()).result(propResp.body());
                });


                app.get("/sales", ctx -> {
                    HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(PROPERTY_SERVER + "/sales")) 
                        .build();
                    HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                    ctx.status(resp.statusCode()).result(resp.body());
                });


                app.post("/sales", ctx -> {
                    HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(PROPERTY_SERVER + "/sales")) 
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(ctx.body()))
                        .build();
                    HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                    ctx.status(resp.statusCode()).result(resp.body());
                });



                // return a sale by sale ID
                app.get("/sales/postcode/{postcode}", ctx -> {
                    String postcode = ctx.pathParam("postcode");
                    HttpRequest propReq = HttpRequest.newBuilder()
                        .uri(URI.create(PROPERTY_SERVER + "/sales/postcode/" + postcode))
                        .build();
                    HttpResponse<String> propResp = client.send(propReq, HttpResponse.BodyHandlers.ofString());


                    // Create JSON body using Gson
                    JsonObject body = new JsonObject();
                    body.addProperty("queryType", "GET");
                    body.addProperty("params", "post_code=" + postcode);
                    body.addProperty("status", propResp.statusCode());

                    HttpRequest analyticsReq = HttpRequest.newBuilder()
                        .uri(URI.create(ANALYTICS_SERVER + "/metrics")) 
                        .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                        .build();
                    
                    client.sendAsync(analyticsReq, HttpResponse.BodyHandlers.ofString());
                    ctx.status(propResp.statusCode()).result(propResp.body());
                });

                app.get("/sort/price", ctx -> {
                    HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(PROPERTY_SERVER + "/sort/price")) 
                        .build();
                    HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                    ctx.status(resp.statusCode()).result(resp.body());
                });

                app.get("/sort/price-per-area", ctx -> {
                    HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(PROPERTY_SERVER + "/sort/price-per-area")) 
                        .build();
                    HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                    ctx.status(resp.statusCode()).result(resp.body());
                });

                app.get("/sales/postcode/{postcode}/average", ctx -> {
                    String postcode = ctx.pathParam("postcode");
                    HttpRequest propReq = HttpRequest.newBuilder()
                        .uri(URI.create(PROPERTY_SERVER + "/sales/postcode/" + postcode + "/average"))
                        .build();
                    HttpResponse<String> propResp = client.send(propReq, HttpResponse.BodyHandlers.ofString());

                    // Create JSON body using Gson
                    JsonObject body = new JsonObject();
                    body.addProperty("queryType", "GET");
                    body.addProperty("params", "post_code=" + postcode);
                    body.addProperty("status", propResp.statusCode());

                    HttpRequest analyticsReq = HttpRequest.newBuilder()
                        .uri(URI.create(ANALYTICS_SERVER + "/metrics")) 
                        .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                        .build();

                    client.sendAsync(analyticsReq, HttpResponse.BodyHandlers.ofString());
                    ctx.status(propResp.statusCode()).result(propResp.body());
                });

                app.get("/sales/price/{low}/{high}", ctx -> {
                    String low = ctx.pathParam("low");
                    String high = ctx.pathParam("high");
                    HttpRequest propReq = HttpRequest.newBuilder()
                        .uri(URI.create(PROPERTY_SERVER + "/sales/price/" + low + "/" + high))
                        .build();
                    HttpResponse<String> propResp = client.send(propReq, HttpResponse.BodyHandlers.ofString());
                    ctx.status(propResp.statusCode()).result(propResp.body());
                });

                app.get("/metrics/postcode-count/{postcode}", ctx -> {
                    String postcode = ctx.pathParam("postcode");
                    HttpRequest propReq = HttpRequest.newBuilder()
                        .uri(URI.create(ANALYTICS_SERVER + "/metrics/postcode-count/" + postcode))
                        .build();
                    HttpResponse<String> propResp = client.send(propReq, HttpResponse.BodyHandlers.ofString());
                    ctx.status(propResp.statusCode()).result(propResp.body());
                });

                app.get("/metrics/property-count/{saleID}", ctx -> {
                    String id = ctx.pathParam("saleID");
                    HttpRequest propReq = HttpRequest.newBuilder()
                        .uri(URI.create(ANALYTICS_SERVER + "/metrics/property-count/" + id))
                        .build();
                    HttpResponse<String> propResp = client.send(propReq, HttpResponse.BodyHandlers.ofString());
                    ctx.status(propResp.statusCode()).result(propResp.body());
                });
                });

        }
}


