/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ramak
 */
public class SimulatorController implements Initializable {

    final static String[] registers = new String[]{"x0", "x1", "x2", "x3", "x4", "x5", "x6", "x7", "x8", "x9", "x10", "x11", "x12", "x13", "x14", "x15",
        "x16", "x17", "x18", "x19", "x20", "x21", "x22", "x23", "x24", "x25", "x26", "x27", "x28", "x29",
        "x30", "x31"};
    String filename;
    primary_memory memory;
    ObservableList<MTable> Mlist = FXCollections.observableArrayList();
    ObservableList<RTable> Rlist = FXCollections.observableArrayList();
    ArrayList<String> assemblyCode;
    Object[][] reg;
    @FXML
    private TableView registersTable;
    @FXML
    private TableColumn<RTable, String> RegistersColumn;
    @FXML
    private TableColumn<RTable, String> RegisterValueColumn;
    @FXML
    private TableView<MTable> machineCodeTable;
    @FXML
    private TableColumn<MTable, String> PCColumn;
    @FXML
    private TableColumn<MTable, String> MCColumn;
    @FXML
    private TableColumn<MTable, String> ACColumn;
    @FXML
    private Button assemble;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        machineCodeTable.setPlaceholder(new Label(""));
        registersTable.setPlaceholder(new Label(""));

        PCColumn.setCellValueFactory(new PropertyValueFactory<MTable, String>("Pc"));
        MCColumn.setCellValueFactory(new PropertyValueFactory<MTable, String>("MachineCode"));
        ACColumn.setCellValueFactory(new PropertyValueFactory<MTable, String>("AssemblyCode"));
        RegistersColumn.setCellValueFactory(new PropertyValueFactory<RTable, String>("register"));
        RegisterValueColumn.setCellValueFactory(new PropertyValueFactory<RTable, String>("value"));

        machineCodeTable.setItems(Mlist);
        registersTable.setItems(Rlist);
    }

    /**
     * this method will get information from main scene
     */
    void transfeBetweenScenes(String filename, ArrayList<String> assemblyCode, primary_memory memory) throws IOException {
        this.filename = filename;
        this.memory = memory;
        this.assemblyCode = assemblyCode;
        getMTableList();
        getRTableList();
    }

    /**
     * this method will return pc and machine code values as a ObservableList
     * For Table
     *
     * @return
     */
    public ObservableList<MTable> getMTableList() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int pc = 0;
        //String.format("0x%08X", decimal)
        String line = null;
        while ((line = reader.readLine()) != null) {
            String temp = String.format("0x%08X", pc);
            Mlist.add(new MTable(temp, line, assemblyCode.get(pc / 4)));
            pc = pc + 4;
        }
        if (Mlist.isEmpty()) {
            Mlist.add(new MTable("", "", ""));
        }
        return Mlist;
    }

    /**
     * this method will return register values list
     *
     * @return
     */
    public ObservableList<RTable> getRTableList() {
        for (int i = 0; i < 31; i++) {
            Rlist.add(new RTable(registers[i], memory.register[i]));
        }
        return Rlist;
    }

    @FXML
    void assembleButtonAction() {
        //registersTable.getItems().add(0x1,0);
    }

}
