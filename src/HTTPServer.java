import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.TimerTask;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;

public class HTTPServer {
    private static final int PORTNO =8000;
    public static int getPortno(){return PORTNO;}
    public static void run() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(PORTNO), 0);
            RootHandler root = new RootHandler();
            server.createContext("/", root);
            server.createContext("/webapp", new MyHandler());
            server.createContext("/webapp/Index",new MyHandler1());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
//            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException
        {
//            String response=he.getRequestURI().toString();
//            response =response.substring(0,response.length()-2);//the last 2 chars are /> for some reason
//            response= "<img src=\"http://findicons.com/files/icons/727/leopard/128/folder.png\" height=\"12\" width=\"12>"+
//                    "<a href=\">"+response+"/webapp\"> Go to the folder </a>";

//            else if (!response.endsWith("/"))
//                response= "<a href=\">"+he.getRequestURI()+"/webapp\"> Go to the folder </a>";
            //try {
//                he.sendResponseHeaders(200, response.length());
//                OutputStream os = he.getResponseBody();
//                os.write(response.getBytes());
//                os.close();
//            }
//            catch(IOException e){
//                System.out.println(e.getMessage());
//            }
        }
    }

    static class MyHandler implements HttpHandler {
        String url;
        @Override
        public void handle(HttpExchange t)
        {
            try {
               url = new String(
                       //"http://localhost:" + Integer.toString(PORTNO) +
                        t.getRequestURI() + "/Index"

               );
                System.out.println("Opening URL: " + url);

                String header="<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js\"></script>" +
                        "<script type=\"text/javascript\">" +
                        "$( document ).ready(function() {" +
                            "$('.link').click( function(){" +
                                "alert(\""+url+"\");" +
                            "});" +
                        "});</script>"
                        ;

                String response =
                        "<html>"+"<head>"+header+"</head>"+"<body>"+
                                "<div>"+
                                "<img src=\"" +
                                "http://findicons.com/files/icons/727/leopard/128/folder.png" +
                                "\" alt=\"Some dude\" height=\"12\" width=\"12\">"+
                                "<a href=\""+url  +"\" class=\"link\" onclick=\"myEvent()\"> View Picture Album</a>"
                                +"</div>"+"</body>"+"</html>";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } // end try
            catch (IOException e){
                System.out.println(e.getMessage());
            }// end catch
        }
    }

    static class MyHandler1 implements  HttpHandler{
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            StringBuilder contentBuilder = new StringBuilder();
            String response;
            try{
                BufferedReader in = new BufferedReader(new FileReader("Index.jsp"));
                String str;
                while ((str = in.readLine()) != null) {
                    contentBuilder.append(str);
                }
                in.close();
                response = "<html>"+"<body>"+
                        "<div>"+contentBuilder.toString()+"</div>"+"</body>"+"</html>";
            }
            catch(IOException e){
                    response = "<html>"+"<body>"+
                            "<div>"+e.getMessage()+"</div>"+"</body>"+"</html>";
                    System.out.println(e);
            }
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
