/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import datapath.datapath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ramak
 */
public class SimulatorController implements Initializable {

    Scene original_scene;
    Stage window;
    final static String[] registers = new String[]{"x0", "x1", "x2", "x3", "x4", "x5", "x6", "x7", "x8", "x9", "x10", "x11", "x12", "x13", "x14", "x15",
        "x16", "x17", "x18", "x19", "x20", "x21", "x22", "x23", "x24", "x25", "x26", "x27", "x28", "x29",
        "x30", "x31"};
    String filename;
    primary_memory memory;
    ObservableList<MTable> Mlist = FXCollections.observableArrayList();
    ObservableList<RTable> Rlist = FXCollections.observableArrayList();
    ObservableList<memtab> memlist = FXCollections.observableArrayList();
    ArrayList<String> assemblyCode;
    Object[][] reg;
    boolean pipelined = false;
    boolean data_forwarding = false;
    boolean disable_writing_to_registers = false;
    boolean disable_writing_to_pipelined_regs = false;
    boolean watch_pipline_reg = false;
    boolean stall_decode = false;
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
    private Button run;
    @FXML
    private Button editor;
    @FXML
    private TableView<memtab> memoryTab;
    @FXML
    TableColumn<memtab, String> c1;
    @FXML
    TableColumn<memtab, String> c2;
    @FXML
    TableColumn<memtab, String> c3;
    @FXML
    TableColumn<memtab, String> c4;
    @FXML
    TableColumn<memtab, String> c5;

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
        c1.setCellValueFactory(new PropertyValueFactory<memtab, String>("address"));
        c2.setCellValueFactory(new PropertyValueFactory<memtab, String>("address_0"));
        c3.setCellValueFactory(new PropertyValueFactory<memtab, String>("address_1"));
        c4.setCellValueFactory(new PropertyValueFactory<memtab, String>("address_2"));
        c5.setCellValueFactory(new PropertyValueFactory<memtab, String>("address_3"));
        machineCodeTable.setItems(Mlist);
        registersTable.setItems(Rlist);
    }

    /**
     * this method will get information from main scene
     */
    void transfeBetweenScenes(String filename, ArrayList<String> assemblyCode, primary_memory memory, Scene scene, Stage window) throws IOException {
        this.filename = filename;
        this.memory = memory;
        this.assemblyCode = assemblyCode;
        this.original_scene = scene;
        this.window = window;
        getMTableList();
        getRTableList();
        this.getMemTableList(0);
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
        Rlist.removeAll();
        for (int i = 0; i < 31; i++) {
            Rlist.add(new RTable(registers[i], memory.register[i]));
        }
        return Rlist;
    }

    public ObservableList<memtab> getMemTableList(int index) {
        int id = index >> 2;
        id = id << 2;
        id = id - 28;//plus or minus seven addresses in a table;
        memlist.removeAll();
        for (int i = 0; i < 20; i++) {
            if (id >= 0 && id < 0x7FFFFFF0) {
                String s1 = String.format("0x%08X", id);
                String s2 = primary_memory.mem.getOrDefault(id + 0, "0");
                String s3 = primary_memory.mem.getOrDefault(id + 1, "0");
                String s4 = primary_memory.mem.getOrDefault(id + 2, "0");
                String s5 = primary_memory.mem.getOrDefault(id + 3, "0");
                memlist.add(new memtab(s1,s2,s3,s4,s5));
            } else {
                memlist.add(new memtab("","","","",""));
            }
            id = id + 4;
        }
        return memlist;
    }

    @FXML
    void runButtonAction() {
        File file = new File(filename);

        int mem_index = 0;
        try {

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            //System.out.println("No input");
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                long itemp = Long.parseLong(line.substring(2), 16);
                String bin_line = Long.toBinaryString(itemp);
                String temp = "";
                for (int i = 0; i < 32 - bin_line.length(); i++) {
                    temp = temp + "0";
                }
                memory.storewordstr(mem_index, temp + bin_line);
                //System.out.println(mem.loadword(mem_index)+"p");
                mem_index = mem_index + 4;

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        datapath dat = new datapath();

        dat.run(memory, pipelined,
                data_forwarding,
                disable_writing_to_registers,
                disable_writing_to_pipelined_regs,
                watch_pipline_reg,
                stall_decode
        );
        registersTable.getItems().clear();
        registersTable.setItems(getRTableList());
    }

    @FXML
    void editorButtonAction() {

        window.setScene(original_scene);

    }

    /**
     * class memory tab pagination
     */
    public class memtab {

        SimpleStringProperty address;
        SimpleStringProperty address_0;
        SimpleStringProperty address_1;
        SimpleStringProperty address_2;
        SimpleStringProperty address_3;

        public memtab(String address, String address_0, String address_1, String address_2, String address_3) {
            this.address = new SimpleStringProperty(address);
            this.address_0 = new SimpleStringProperty(address_0);
            this.address_1 = new SimpleStringProperty(address_1);
            this.address_2 = new SimpleStringProperty(address_2);
            this.address_3 = new SimpleStringProperty(address_3);
        }

    }

}
