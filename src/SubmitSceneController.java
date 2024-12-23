import java.io.IOException;

import javax.imageio.plugins.tiff.TIFFField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SubmitSceneController {

    @FXML private Button compareOAEButton;
    @FXML private Button viewBTButton, viewEMButton, backButton;
    @FXML private Label wordsNumLabel, charNumLabel, bitsNumLabel;

    private BinaryTree myBT;
    private StringBuilder encodedMessage, originalMessage, decodedMessage;

    public void compareOrigAEnc(ActionEvent event) {

        Stage modalStage = new Stage();

        FlowPane pane = new FlowPane(5, 5);
        pane.setAlignment(Pos.CENTER);

        Label origLabel = new Label("Original message: ");
        origLabel.setStyle(

            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;"
        );

        Label myOrig = new Label();
        myOrig.setWrapText(true);
        myOrig.setPrefSize(400, 150);
        myOrig.setText(originalMessage.toString());
        myOrig.setAlignment(Pos.CENTER);

        Label encoLabel = new Label("Decoded message: ");
        encoLabel.setStyle(

            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;"
        );

        Label myEnco = new Label();
        myEnco.setWrapText(true);
        myEnco.setPrefSize(400, 150);
        myEnco.setText(decodedMessage.toString());
        myEnco.setAlignment(Pos.CENTER);

        Button backButton = new Button();
        backButton.setText("Back");
        backButton.setPrefSize(60, 30);
        backButton.setOnAction(e -> {

            modalStage.close();
        });

        pane.getChildren().addAll(origLabel, myOrig, encoLabel, myEnco, backButton);

        Scene scene = new Scene(pane, 450, 450);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("View Encoded Message");
        modalStage.setScene(scene);
        modalStage.setResizable(false);
        modalStage.show();
    }


    public void viewEncodedMessage(ActionEvent event) {

        Stage modalStage = new Stage();

        FlowPane pane = new FlowPane(5, 5);
        pane.setAlignment(Pos.CENTER);

        Label origLabel = new Label("Original message: ");
        origLabel.setStyle(

            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;"
        );

        Label myOrig = new Label();
        myOrig.setWrapText(true);
        myOrig.setPrefSize(400, 150);
        myOrig.setText(originalMessage.toString());
        myOrig.setAlignment(Pos.CENTER);

        Label encoLabel = new Label("Encoded message: ");
        encoLabel.setStyle(

            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;"
        );

        Label myEnco = new Label();
        myEnco.setWrapText(true);
        myEnco.setPrefSize(400, 150);
        myEnco.setText(encodedMessage.toString());
        myEnco.setAlignment(Pos.CENTER);

        Button backButton = new Button();
        backButton.setText("Back");
        backButton.setPrefSize(60, 30);
        backButton.setOnAction(e -> {

            modalStage.close();
        });

        pane.getChildren().addAll(origLabel, myOrig, encoLabel, myEnco, backButton);

        Scene scene = new Scene(pane, 450, 450);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("View Encoded Message");
        modalStage.setScene(scene);
        modalStage.setResizable(false);
        modalStage.show();
    }

    public void viewBinaryTree(ActionEvent event) {

        Stage modalStage = new Stage();

        BorderPane pane = new BorderPane();
        BTPane view = new BTPane(myBT);

        Button backButton = new Button();
        backButton.setText("Back");
        backButton.setPrefSize(60, 30);
        backButton.setOnAction(e -> {

            modalStage.close();
        });

        HBox bottomBox = new HBox(backButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 10, 10, 10));
        
        pane.setBottom(bottomBox);
        pane.setCenter(view);

        Scene scene = new Scene(pane, 600, 450);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Binary Tree (Huffman Tree)");
        modalStage.setScene(scene);
        modalStage.setResizable(false);
        modalStage.show();
        view.displayTree();
    }

    public void backToMainScene(ActionEvent event) throws IOException{

        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();

        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
    
        newStage.setScene(scene);
        newStage.show();
    }

    public void setBT(BinaryTree myBT) {

        this.myBT = myBT;
    }

    public void setStats(int wordsNum, int charNum, int bitsNum, StringBuilder originalMessage, StringBuilder encodedMessage, StringBuilder decodedMessage) {

        this.wordsNumLabel.setText(String.valueOf(wordsNum));
        this.charNumLabel.setText(String.valueOf(charNum));
        this.bitsNumLabel.setText(String.valueOf(bitsNum));
        this.originalMessage = originalMessage;
        this.encodedMessage = encodedMessage;
        this.decodedMessage = decodedMessage;
    }
}
