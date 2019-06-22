/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 *
 * @author ramak
 */
public class NewFXMain extends Application {

    String filename;
    String filedirectory;
    boolean firstsave = false;
    String outputfile;
    String dir;
    String file;
    public primary_memory memory = new primary_memory();
    boolean pipelined = false;
    boolean data_forwarding = false;
    boolean disable_writing_to_registers = false;
    boolean disable_writing_to_pipelined_regs = false;
    boolean watch_pipline_reg = false;
    boolean stall_decode = false;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    int current_address = 0x00000000;
    StyleClassedTextArea codeArea = new StyleClassedTextArea();
    Scene editor_scene;
    Scene simulator_scene;
    MenuBar menu = new MenuBar();
    Menu File = new Menu("File");
    MenuItem New = new MenuItem("New");
    MenuItem Open = new MenuItem("Open");
    MenuItem Save = new MenuItem("Save");
    MenuItem SaveAs = new MenuItem("Save As");

    ToolBar toolbar = new ToolBar();
    Button simulator = new Button("Simulator");

    @Override
    public void start(Stage primaryStage) throws Exception {

        //filemenu actions;
        setMenuItemsEvents();

        File.getItems().addAll(New, Open, Save, SaveAs);
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        menu.getMenus().add(File);
        toolbar.getItems().add(simulator);
        VBox root = new VBox();
        VirtualizedScrollPane pane = new VirtualizedScrollPane<>(codeArea);
        VBox.setVgrow(pane, Priority.ALWAYS);
        root.getChildren().addAll(menu, toolbar, pane);
        //simulator_scene = new Scene(), 1000, 500);
        editor_scene=new Scene(root,1000,500);
        primaryStage.setScene(editor_scene);
        primaryStage.show();
        //all button functions start
        simulator.setOnAction(e
                -> {
            Save.fire();
            int a1 = 2;
            int a2 = 2;
            int a3 = 2;
            int a4 = 2;
            int a5 = 0;

            memory.set_primary_memory(a1, a2, a3, a4, a5);
            assembler obj = new assembler();
            try {
                obj.assemble(filename, memory);
            } catch (IOException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }

            // PrintWriter p=null;
            //creating output filename
            int temp = filename.indexOf('.');
            outputfile =filename.substring(0, temp) + ".mc";
            PrintWriter p;

            try {
                p = new PrintWriter(new FileOutputStream(outputfile, false));
                for (int i = 0; i < obj.instructions_temp.length; i++) {
                    Long decimal = Long.parseLong(obj.instructions_temp[i].binary, 2);
                    // String hexStr = Integer.toString(decimal, 16);
                    // System.out.println(decimal.toHexString());
                    // System.out.println(String.format("0x%08X", decimal));
                    // PrintWriter writer = nul
                    p.println(String.format("0x%08X", decimal));

                    // p.flush();
                }
                // p.println(assembler.instructions_temp.length);
                p.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(getClass().getResource("Simulator.fxml"));
            try {
                Parent parent=loader.load();
                simulator_scene=new Scene(parent,1000,500);
                SimulatorController controller=loader.getController();
                controller.transfeBetweenScenes(outputfile,obj.instruction_list,memory);
                primaryStage.setScene(simulator_scene);
            } catch (IOException ex) {
                Logger.getLogger(NewFXMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        });
        //all button function end
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

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
        Save.setOnAction((ActionEvent e)
                -> {
            if (firstsave == false) {
                SaveAs.fire();
                       
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
            } catch (IOException ex) {
                System.out.println("File not found");
            }
        });

    }

}
