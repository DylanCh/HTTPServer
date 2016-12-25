import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HTTPServerAppGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

//    @Override
//    public void stop(){
//
//    }

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Start server "+HTTPServer.getPortno()),
                launchBrowser = new Button("view in browser");
        Pane root = new StackPane();
        Label label=new Label();
        ContextMenu contextMenu = new ContextMenu();
        Hyperlink link = new Hyperlink();
//        link.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
//            if (event.getButton() == MouseButton.SECONDARY) {
//
//            }
//        });
        javafx.scene.control.MenuItem copy = new javafx.scene.control.MenuItem("copy");
        contextMenu.getItems().add(copy);
        copy.setOnAction((ActionEvent event)->{
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(
                    new StringSelection(
                        link.getText()
                    ),
                    new StringSelection(
                            link.getText()
                    )
            );
            JOptionPane.showMessageDialog(null,"Server url copied!");
        });
        link.setContextMenu(contextMenu);
        //label.setVisible(false);

        link.setVisible(false);
        link.setOnAction(
                (ActionEvent event)->{
                    HostServicesDelegate hostServices = HostServicesFactory.getInstance(this);
                    hostServices.showDocument(link.getText());
                }
        );

        btn.setOnAction(
                (ActionEvent event)-> {
                HTTPServer.run();
                //launchBrowser.setVisible(true);
                launchBrowser.setDisable(false);
                label.setText("Please copy to browser: ");
                //label.setVisible(true);
                link.setVisible(true);
                link.setText("http://localhost:"+HTTPServer.getPortno()+"/webapp");
            }
        );

        launchBrowser.setOnAction(
                (ActionEvent event) -> {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI(
                                    "localhost:" + Integer.toString(HTTPServer.getPortno()) + "/webapp")
                            );
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null,e.getMessage());
                        } catch (URISyntaxException e) {
                            JOptionPane.showMessageDialog(null,e.getMessage());
                        }
                    }
//                    HostServicesDelegate hostServices = HostServicesFactory.getInstance(this);
//                    hostServices.showDocument("localhost:" + Integer.toString(HTTPServer.getPortno()) + "/webapp");
                }
        );

        launchBrowser.setVisible(false);
        launchBrowser.setDisable(true);

        VBox vbox = new VBox(8);

        //if(label!=null)
            vbox.getChildren().addAll(btn,launchBrowser,label,link);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }
}
