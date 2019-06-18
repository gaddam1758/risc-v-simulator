/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 *
 * @author ramak
 */
public class NewFXMain extends Application {

    StyleClassedTextArea codeArea = new StyleClassedTextArea();
    String filename;
    String filedirectory;
    boolean firstsave = false;
    String outputfile;
    String dir;
    String file;
    MenuBar menu = new MenuBar();
    Menu File = new Menu("File");
    MenuItem New = new MenuItem("New");
    MenuItem Open = new MenuItem("Open");
    MenuItem Save = new MenuItem("Save");
    MenuItem SaveAs = new MenuItem("Save As");

    ToolBar toolbar = new ToolBar();
    Button build = new Button();

    void setMenuItemsEvents() {
        //setting key combinations
        New.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        Open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        Save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        SaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        //New Action
        New.setOnAction((ActionEvent e) -> {
            System.out.println("working");
        });
        //Open Action
        Open.setOnAction((ActionEvent e)
                -> {
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
                    codeArea.replaceText(sb.toString());

                }
                reader.close();
            } catch (IOException c) {
                System.out.println("File not found");
            }
        });
        SaveAs.setOnAction((ActionEvent e)
                -> {
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
                String content = codeArea.getText();
                content = content.replaceAll("(?!\\r)\\n", "\r\n");
                filewriter.write(content);
                // setTitle(filename);
                filewriter.flush();
                filewriter.close();
            } catch (IOException c) {
                System.out.println("File not found");
            }
            firstsave = true;
        });
        Save.setOnAction((ActionEvent event)
                -> {
            if (firstsave == false) {
                SaveAs.setOnAction((ActionEvent e)
                        -> {
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
                        String content = codeArea.getText();
                        content = content.replaceAll("(?!\\r)\\n", "\r\n");
                        filewriter.write(content);
                        // setTitle(filename);
                        filewriter.flush();
                        filewriter.close();
                    } catch (IOException c) {
                        System.out.println("File not found");
                    }
                    firstsave = true;
                });
            }
            try {
                FileWriter filewriter = new FileWriter(filename);
                String content = codeArea.getText();
                content = content.replaceAll("(?!\\r)\\n", "\r\n");
                filewriter.write(content);
//filewriter.write(jTextArea1.getText());
                //setTitle(filename);
                filewriter.close();
                filewriter.close();
            } catch (IOException e) {
                System.out.println("File not found");
            }
        });

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //filemenu actions;
        System.out.println("Asas");
        setMenuItemsEvents();

        File.getItems().addAll(New, Open, Save, SaveAs);
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        menu.getMenus().add(File);
        toolbar.getItems().add(build);
        VBox root = new VBox();

        VirtualizedScrollPane pane = new VirtualizedScrollPane<>(codeArea);
        VBox.setVgrow(pane, Priority.ALWAYS);
        root.getChildren().addAll(menu, toolbar, pane);

//        Scene scene = new Scene(new AnchorPane(new VirtualizedScrollPane<>(codeArea)), 600, 400); 
        //  root.getChildren().addAll(menu,toolbar,new VirtualizedScrollPane<>(codeArea));
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
