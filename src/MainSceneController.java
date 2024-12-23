import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileReader;

public class MainSceneController implements Initializable{

    @FXML Button exitButton, submitButton, clearButton;
    @FXML TextArea myTextArea;
    @FXML Label attachLabel;

    private BinaryTree tree;
    private File selectedFile;
    private StringBuilder txtFile = new StringBuilder();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        myTextArea.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                
                if(!myTextArea.getText().matches("^[a-zA-Z .,!?'/\\\\\"\\-\\n\\t]+$") && !myTextArea.getText().isEmpty()) {

                    myTextArea.setStyle(

                        "-fx-background-color: red;"+
                        "-fx-text-fill: red;" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-color: red;"
                    );

                    submitButton.setDisable(true);
                }

                else {

                    myTextArea.setStyle("");
                    submitButton.setDisable(false);
                }
            }
        });

        attachLabel.setOnMouseEntered(event -> {
            attachLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold; -fx-cursor: hand;");
        });

        attachLabel.setOnMouseExited(event -> {
            attachLabel.setStyle("");
        });
    }

    public void submitFunc(ActionEvent event) throws IOException {

        ArrayList<TreeNode<CharacterF>> treeNodes = new ArrayList<>();

        for (CharacterF charac : readText()) {

            treeNodes.add(new TreeNode<CharacterF>(charac));
        }

        toBinaryTree(treeNodes);

        tree = new BinaryTree(treeNodes.remove(0));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("submitScene.fxml"));
        Parent root = loader.load();

        StringBuilder encodedMessage = toBinaryCode();

        StringBuilder originalMessage = null;

        if(myTextArea.isDisabled()) {

            originalMessage = txtFile;
        }

        else {

            originalMessage = new StringBuilder(myTextArea.getText());
        }

        SubmitSceneController sController = loader.getController();
        sController.setBT(tree);
        sController.setStats(getWordCount(), getCharCount(), getBitsCount(encodedMessage), originalMessage, encodedMessage, decodeMessage(encodedMessage));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Alert ifSaveTxt = new Alert(AlertType.CONFIRMATION);
        ifSaveTxt.setTitle("Save Txt File?");
        ifSaveTxt.setHeaderText("Do you want to save the encoded txt file?");
        ifSaveTxt.setContentText("Click Ok to proceed or Cancel");

        ButtonType result = ifSaveTxt.showAndWait().orElse(ButtonType.NO);

        if(result == ButtonType.OK) {

            saveTxtFile(encodedMessage, stage);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Results");
        stage.show();
    }

    public void exitFunc(ActionEvent event) {

        System.exit(0);
    }

    public void clearFunc(ActionEvent event) {

        myTextArea.setText("");
    }

    public void attachTxtFile(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        File projectDir = new File(System.getProperty("user.dior"), "EncodedMessage");
        if(!projectDir.exists()) projectDir.mkdirs();

        fileChooser.setInitialDirectory(projectDir);
        fileChooser.setTitle("Attach Txt File");

        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        selectedFile = fileChooser.showOpenDialog(new Stage());
        
        if(selectedFile != null) {

            attachLabel.setText(selectedFile.getName());
            myTextArea.setDisable(true);
        }

        else {

            attachLabel.setText("--Attach a Txt File--");
            myTextArea.setDisable(false);
        }

        txtFile = new StringBuilder();
        try {
                
            FileReader reader = new FileReader(selectedFile);
            int data = reader.read();

            while(data != -1) {

                txtFile.append((char)data); 
                data = reader.read();
            }

            reader.close();
        }

        catch (FileNotFoundException e) {
      
            e.printStackTrace();
        }
            
        catch (IOException e) {
            
            e.printStackTrace();
        }

        String text = txtFile.toString().toLowerCase();

        if(!text.matches("^[a-zA-Z .,!?'/\\\\\"\\-\\n\\t]+$")) {

            Alert infoAlert = new Alert(AlertType.ERROR);
            infoAlert.setTitle("ERROR");
            infoAlert.setHeaderText("Invalid Txt File");
            infoAlert.setContentText("Content of the txt file is invalid");
            infoAlert.showAndWait();

            return;
        }
    }

    public ArrayList<CharacterF> readText() {

        char [] txtCharacs = null;

        if(myTextArea.isDisabled()) {

            StringBuilder txtFile = new StringBuilder();
            try {
                
                FileReader reader = new FileReader(selectedFile);
                int data = reader.read();

                while(data != -1) {

                    txtFile.append((char)data);
                    data = reader.read();
                }

                reader.close();
            }

            catch (FileNotFoundException e) {
      
                e.printStackTrace();
            }
            
            catch (IOException e) {
            
                e.printStackTrace();
            }

            txtCharacs = txtFile.toString().trim().toLowerCase().toCharArray();
        }

        else {

            txtCharacs = myTextArea.getText().trim().toLowerCase().toCharArray();
        }

        ArrayList<CharacterF> lettersCount = new ArrayList<>();

        for (int i = 0 ; i < txtCharacs.length ; i++) {

            if(txtCharacs[i] >= 97 && txtCharacs[i] <= 122) {

                String charac = Character.toString(txtCharacs[i]);
                boolean flag = false;

                for(CharacterF character : lettersCount) {

                    if(character != null) {

                        if(charac.equals(character.getCharacter())) {

                            flag = true;
                            break;
                        }
                    }
                }

                if(flag) {

                    continue;
                }

                int frequency = 0;

                for(char character : txtCharacs) {

                    if(charac.equals(Character.toString(character))) {

                        frequency++;
                    }
                }

                lettersCount.add(new CharacterF(charac, frequency));
            }
        }

        Collections.sort(lettersCount, new Comparator<CharacterF>(){

            @Override
            public int compare(CharacterF o1, CharacterF o2) {
                
                return Integer.compare(o1.getFrequency(), o2.getFrequency());
            }
        });

        return lettersCount;
    }


    public void toBinaryTree(ArrayList<TreeNode<CharacterF>> nodes) {
        
        if (nodes.size() <= 1) {
            return;
        }
    
        ArrayList<TreeNode<CharacterF>> tempNodes = new ArrayList<>();
        
        for (int i = 0; i < nodes.size() - 1; i += 2) {
            TreeNode<CharacterF> node1 = nodes.get(i);
            TreeNode<CharacterF> node2 = nodes.get(i + 1);
            
            node1.setBinaryValue(0);
            node2.setBinaryValue(1);
           
            int frequency = node1.getData().getFrequency() + node2.getData().getFrequency();
            CharacterF parentNode = new CharacterF(String.valueOf(frequency), frequency);
            
            TreeNode<CharacterF> treeNode = new TreeNode<>(parentNode, node1, node2);
            tempNodes.add(treeNode);
        }
    
        if (nodes.size() % 2 != 0) {
            tempNodes.add(nodes.get(nodes.size() - 1));
        }
    
        nodes.clear();
        nodes.addAll(tempNodes);
        toBinaryTree(nodes);
    }
    

    public StringBuilder toBinaryCode() {

        String text = "";

        if(myTextArea.isDisabled()){

            StringBuilder txtFile = new StringBuilder();
            try {
                
                FileReader reader = new FileReader(selectedFile);
                int data = reader.read();

                while(data != -1) {

                    txtFile.append((char)data); 
                    data = reader.read();
                }

                reader.close();
            }

            catch (FileNotFoundException e) {
      
                e.printStackTrace();
            }
            
            catch (IOException e) {
            
                e.printStackTrace();
            }

            text = txtFile.toString().toLowerCase();
        }

        else {

            text = myTextArea.getText().toLowerCase();
        }

        StringBuilder binary = new StringBuilder();
        

        for (int i = 0 ; i < text.length() ; i++) {

            if(text.charAt(i) >= 97 && text.charAt(i) <= 122) {

                binary.append(tree.getPath(String.valueOf(text.charAt(i))).toString());
            }

            else if(text.charAt(i) == ' ') {

                binary.append(" | ");
            }

            else if(text.charAt(i) == '\t' || text.charAt(i) == '\n') {

                binary.append(" ");
                continue;
            }

            else
                continue;

            binary.append(" ");
        }

        return binary;
    }

    public StringBuilder decodeMessage(StringBuilder encodedMessage) {

        StringBuilder decodedMessage = new StringBuilder();
        String [] text = encodedMessage.toString().split(" ");

        for(int i = 0 ; i < text.length ; i++) {

            if(text[i].trim().equals("|")) {

                decodedMessage.append(" ");
                continue;
            }

            else if (text[i].trim().matches("^[01]+$")) {

                decodedMessage.append(tree.getLetter(text[i].trim()));
            }
        }

        return decodedMessage;
    }

    public int getWordCount() {

        if(myTextArea.isDisabled()) {

            return txtFile.toString().trim().split("\\s+").length;
        }

        else {

            return myTextArea.getText().trim().split("\\s+").length;
        }
    }

    public int getCharCount() {
        
        int num = 0;
        String text = "";

        if(myTextArea.isDisabled()) {

            text = txtFile.toString().trim().toLowerCase();
        }

        else {

            text = myTextArea.getText().trim().toLowerCase();
        }
        
        for (int i = 0 ; i < text.length(); i++) {

            if(text.charAt(i) >= 97 && text.charAt(i) <= 122) {

                num++;
            }
        }

        return num;
    }

    public int getBitsCount(StringBuilder sb) {

        int num = 0;

        for (int i = 0 ; i < sb.length() ; i++) {

            if(sb.toString().toLowerCase().charAt(i) == '1' || sb.toString().toLowerCase().charAt(i) == '0') {

                num++;
            }
        }

        return num;
    }

    public void saveTxtFile(StringBuilder encodedMessage, Stage stage) {

        FileChooser fileChooser = new FileChooser();

        File projectDir = new File(System.getProperty("user.dior"), "EncodedMessage");
        if(!projectDir.exists()) projectDir.mkdirs();

        fileChooser.setInitialDirectory(projectDir);
        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showSaveDialog(stage);

        if(file != null) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

                writer.write(encodedMessage.toString());
            }

            catch (Exception e) {

                System.out.println("Save Error: " + e.getMessage());
            }

            Alert yesAlert = new Alert(AlertType.INFORMATION);
            yesAlert.setTitle("Txt Saved!");
            yesAlert.setHeaderText("Txt File Saved");
            yesAlert.setContentText("The encoded txt file has been saved");
            yesAlert.showAndWait();
        }

        else {

            Alert infoAlert = new Alert(AlertType.ERROR);
            infoAlert.setTitle("Txt Unsuccessfully Saved");
            infoAlert.setHeaderText("Txt File Unsaved");
            infoAlert.setContentText("The encoded txt file has not been saved");
            infoAlert.showAndWait();
        }
    }
}
