/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import assembler.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;
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
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

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
    Button under = new Button("underline");

    @Override
    public void start(Stage primaryStage) throws Exception {

        //filemenu actions;
        setMenuItemsEvents();

        File.getItems().addAll(New, Open, Save, SaveAs);
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        menu.getMenus().add(File);
        toolbar.getItems().addAll(simulator,under);
        VBox root = new VBox();
        VirtualizedScrollPane pane = new VirtualizedScrollPane<>(codeArea);
        VBox.setVgrow(pane, Priority.ALWAYS);
        root.getChildren().addAll(menu, toolbar, pane);
        //simulator_scene = new Scene(), 1000, 500);
        editor_scene=new Scene(root,1000,500);
        editor_scene.getStylesheets().add(getClass().getResource("/CSS/spellchecking.css").toExternalForm());
        primaryStage.setScene(editor_scene);
        primaryStage.show();
        //all button functions start
        simulator.setOnAction(e
                -> {
            Save.fire();
            int a1 = 2;
            int a2 = 2;
            int a3 = 0;
            int a4 = 0;
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
                Logger.getLogger(NewFXMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            FXMLLoader loader= new FXMLLoader();
            String s=getClass().getResource("Simulator.fxml").toString();
            System.out.println(s);
            loader.setLocation(getClass().getResource("Simulator.fxml"));
            System.out.println(loader.getLocation().toString());
            try {
                Parent parent=loader.load();
                simulator_scene=new Scene(parent,1000,500);
                SimulatorController controller=loader.getController();
                controller.transfeBetweenScenes(outputfile,obj.instruction_list,memory,editor_scene,primaryStage);
                primaryStage.setScene(simulator_scene);
            } catch (IOException ex) {
                Logger.getLogger(NewFXMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        });
        
        under.setOnAction( e->{
    		under.getStyleClass().add("pressed");
    		//updateStyleInSelection(spans -> TextStyle.underline(!spans.styleStream().allMatch(style -> style.underline.orElse(false))));
    		System.out.println("underline");
    		File file = new File("new.txt");
    		try {
                FileWriter filewriter = new FileWriter(file);
                String content = codeArea.getText();
                content = content.replaceAll("(?!\\r)\\n", "\r\n");
                filewriter.write(content);
                // setTitle(filename);
                filewriter.flush();
                filewriter.close();
            } catch (IOException c) {
                System.out.println("File not found");
            }
    		
                        try {
							codeArea.setStyleSpans(0, computeHighlighting());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
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
    
private static StyleSpans<Collection<String>> computeHighlighting() throws IOException {
	 	
	 	
        
        StyleSpansBuilder<Collection<String>> spansBuilder = null ;
        File file = new File("new.txt");   
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int end=0;
        while ((line = br.readLine()) != null)
        {
        	end = line.length() + 1 + end;
        }

        //System.out.println(text);
        int lastIndex = 0;
        //System.out.println(lastIndex);
        int lastKwEnd = 0;
        //primary_memory p1 = new primary_memory();
        //st = br.readLine();
        primary_memory p1 = new primary_memory();
        p1.set_primary_memory(2,2,2, 2, 0);
        error e = new error();
        Vector <Integer> V1 = e.assemble("new.txt", p1);
        	int s = V1.size();
        	if(s == 0)
        	{
        		System.out.println("pop");
        		spansBuilder  = new StyleSpansBuilder<>();
        		spansBuilder.add(Collections.emptyList(),end-1);
        	}
        	else
        	{
        		spansBuilder = new StyleSpansBuilder<>();
        		for(int i=0;i<(s-2);i = i+3)
        		{
        			
        			System.out.println(V1.get(i)+"-"+V1.get(i+1)+"-"+V1.get(i+2));
        			spansBuilder.add(Collections.emptyList(), V1.get(i) - V1.get(i+2));
        			spansBuilder.add(Collections.singleton("underlined"), V1.get(i+1) - V1.get(i)-1);
        		
        		}
        	}
        
        spansBuilder.add(Collections.emptyList(), lastIndex - lastKwEnd);

        return spansBuilder.create();
    }

}
