/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author ramak
 */
public class FXMLController implements Initializable {

    String filename;
    String filedirectory;
    boolean firstsave = false;
    String outputfile;
    String dir;
    String file;
    @FXML
    private MenuItem UiNew;
    @FXML
    private MenuItem UiOpen;
    @FXML
    private MenuItem UiSave;
    @FXML
    private MenuItem UiSaveAs;
    @FXML
    private TextArea TextArea1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void New(ActionEvent event) {
    }

    @FXML
    private void Open(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filename = selectedFile.getAbsolutePath();
//                    fileDialog.getDirectory() + fileDialog.getFile();
            // setTitle(filename);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                TextArea1.setText(sb.toString());

            }
            reader.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    @FXML
    private void Save(ActionEvent event) {

        if (firstsave == false) {
            SaveAs(event);
            // firstsave = true;
        }
        try {
            FileWriter filewriter = new FileWriter(filename);
            String content = TextArea1.getText();
            content = content.replaceAll("(?!\\r)\\n", "\r\n");
            filewriter.write(content);
//filewriter.write(jTextArea1.getText());
            //setTitle(filename);
            filewriter.close();
            filewriter.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    @FXML
    private void SaveAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        //fileChooser.setInitialFileName();
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            filename = selectedFile.getAbsolutePath();
            file = selectedFile.getName();
            dir = selectedFile.getParent();
            //setTitle(filename);
        }
        try {
            FileWriter filewriter = new FileWriter(filename);
            String content = TextArea1.getText();
            content = content.replaceAll("(?!\\r)\\n", "\r\n");
            filewriter.write(content);
            // setTitle(filename);
            filewriter.flush();
            filewriter.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
        firstsave = true;
    }

}
