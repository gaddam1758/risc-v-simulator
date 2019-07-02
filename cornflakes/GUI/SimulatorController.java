/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import assembler.*;
import datapath.datapath;
import datapath.instructions;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    ArrayList<String> OriginalCode;
    Object[][] reg;
    boolean pipelined = false;
    boolean data_forwarding = false;
    boolean disable_writing_to_registers = false;
    boolean disable_writing_to_pipelined_regs = false;
    boolean watch_pipline_reg = false;
    boolean stall_decode = false;
    int current_index = 0;
    Stack<primary_memory> memStack = new Stack<>();
    Stack<datapath> datStack = new Stack<>();
    datapath dat;
    instructions[] instr_que;
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
    private TableColumn<MTable,String> OCColumn;
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
    @FXML
    private ComboBox<?> memChoiceBox;
    @FXML
    private Button step;
    @FXML
    private Button prev;
    @FXML
    private TextField memTextfield;
    @FXML
    private Button memUpButton;
    @FXML
    private Button memDownButton;

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
        OCColumn.setCellValueFactory(new PropertyValueFactory<MTable,String>("OriginalCode"));
        RegistersColumn.setCellValueFactory(new PropertyValueFactory<RTable, String>("register"));
        RegisterValueColumn.setCellValueFactory(new PropertyValueFactory<RTable, String>("value"));
        c1.setCellValueFactory(new PropertyValueFactory<memtab, String>("address"));
        c2.setCellValueFactory(new PropertyValueFactory<memtab, String>("address_0"));
        c3.setCellValueFactory(new PropertyValueFactory<memtab, String>("address_1"));
        c4.setCellValueFactory(new PropertyValueFactory<memtab, String>("address_2"));
        c5.setCellValueFactory(new PropertyValueFactory<memtab, String>("address_3"));
        machineCodeTable.setItems(Mlist);
        registersTable.setItems(Rlist);
        memoryTab.setItems(this.memlist);
//        machineCodeTable.setRowFactory(tv -> new TableRow<MTable>() {
//            @Override
//            public void updateItem(MTable item, boolean empty) {
//                super.updateItem(item, empty);
//                if (item == null) {
//                    setStyle("");
//                } else if (item.getPc().equals(String.format("0x%08X", 0))) {
//                    setStyle("-fx-background-color: tomato;");
//                } else {
//                    setStyle("");
//                }
//            }
//        });

    }

    /**
     * this method will get information from main scene
     */
    void transfeBetweenScenes(String filename, ArrayList<String> assemblyCode,ArrayList<String> OriginalCode, primary_memory memory, Scene scene, Stage window) throws IOException {
        this.filename = filename;
        this.memory = memory;
        this.assemblyCode = assemblyCode;
        this.OriginalCode=OriginalCode;
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
            Mlist.add(new MTable(temp, line, assemblyCode.get(pc / 4),OriginalCode.get(pc/4)));
            pc = pc + 4;
        }
        if (Mlist.isEmpty()) {
            Mlist.add(new MTable("", "", ""," "));
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
        if(index<0)
            index=0;
        if(index>0x7FFFFFF0)
            index=0x7FFFFFF0-1;
        int id = index >> 2;
        id = id << 2;
        this.current_index=id;
        id = id + 28;//plus or minus seven addresses in a table;
        memlist.removeAll();
        for (int i = 0; i < 20; i++) {
            if (id >= 0 && id < 0x7FFFFFF0) {
                String s1 = String.format("0x%08X", id);
                String s2 = Integer.toString((Integer.parseInt(memory.mem.getOrDefault(id + 0, "0"), 2)));
                String s3 = Integer.toString((Integer.parseInt(memory.mem.getOrDefault(id + 1, "0"), 2)));
                String s4 = Integer.toString((Integer.parseInt(memory.mem.getOrDefault(id + 2, "0"), 2)));
                String s5 = Integer.toString((Integer.parseInt(memory.mem.getOrDefault(id + 3, "0"), 2)));;
                memlist.add(new memtab(s1, s2, s3, s4, s5));
            } else {
                memlist.add(new memtab("", "", "", "", ""));
            }
            id = id - 4;
        }
        return memlist;
    }

    @FXML
    void runButtonAction() {
        setRun();
        boolean flag;
        while (true) {
            //n--;

            dat.print_reg(memory);
            dat.prev_pc = dat.cur_pc;
            flag = dat.fetch(memory, instr_que);
            dat.write(memory, instr_que);
            dat.print_reg(memory);
            dat.decode(memory, instr_que);
            dat.execute(memory, instr_que);
            dat.no_of_cycles++;
            this.highlightingRow(dat.cur_pc);
            //print_que(instr_que);
            if (dat.memory(memory, instr_que) && !flag) {
                break;
            }

        }
        dat.calculate_data();
        dat.print_summary();
//
        registersTable.getItems().clear();
        registersTable.setItems(getRTableList());
        this.memoryTab.getItems().clear();
        this.memoryTab.setItems(this.getMemTableList(current_index));
    }

    @FXML
    void stepButtonAction() {
        if (memStack.empty() && this.datStack.empty()) {
            setRun();
        }
        memStack.push((primary_memory) deepCopy(memory));
        datStack.push((datapath) deepCopy(dat));
        this.highlightingRow(dat.cur_pc);
        boolean flag;
        for (int i = 0; i < 5; i++) {
            dat.print_reg(memory);
            dat.prev_pc = dat.cur_pc;
            flag = dat.fetch(memory, instr_que);
            dat.write(memory, instr_que);
            dat.print_reg(memory);
            dat.decode(memory, instr_que);
            dat.execute(memory, instr_que);
            dat.no_of_cycles++;
            dat.print_que(instr_que);
            dat.memory(memory, instr_que);
        }
        dat.calculate_data();
        dat.print_summary();
//      
        registersTable.getItems().clear();
        registersTable.setItems(getRTableList());
        this.memoryTab.getItems().clear();
        this.memoryTab.setItems(this.getMemTableList(current_index));
    }

    @FXML
    void prevButtonAction() {
        if (!memStack.empty() && !this.datStack.empty()) {
            memory = memStack.pop();
            dat = datStack.pop();
        }
        registersTable.getItems().clear();
        registersTable.setItems(getRTableList());
        this.memoryTab.getItems().clear();
        this.memoryTab.setItems(this.getMemTableList(current_index));
    }

    /*
    go to method for memory table
     */
    @FXML
    void gotoLocation() {
        try{
        String temp=this.memTextfield.getText().strip();
        String temp1=temp.substring(2,temp.length());
        int loc=Integer.parseInt(temp1,16);
        
        this.memoryTab.getItems().clear();
        this.memoryTab.setItems(this.getMemTableList(loc));
        
        }
        catch(Exception e)
        {
            
        }
       
    }

    @FXML
    void editorButtonAction() {

        window.setScene(original_scene);

    }

    /**
     * preprocess function for step
     */
    void setRun() {
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
        dat = new datapath();
//
        dat.cur_pc = 0;
        memory.pc = 0;
        instr_que = new instructions[5];
        for (instructions i : instr_que) {
            i = null;
        }
        dat.pipelined = pipelined;
        dat.data_forwarding = data_forwarding;
        dat.disable_writing_to_registers = disable_writing_to_registers;
        dat.disable_writing_to_pipelined_regs = disable_writing_to_pipelined_regs;
        dat.watch_pipline_reg = watch_pipline_reg;
        dat.stall_decode = stall_decode;
    }

    @FXML
    private void memUpButtonAction(ActionEvent event) {
        this.memoryTab.getItems().clear();
        this.memoryTab.setItems(this.getMemTableList(current_index+28));//+7 addresses
    }

    @FXML
    private void memDownButtonAction(ActionEvent event) {
        this.memoryTab.getItems().clear();
        this.memoryTab.setItems(this.getMemTableList(current_index-7));//-7 addresses
    }

    /**
     * class memory tab pagination
     */
    public static class memtab {

        SimpleStringProperty address;
        SimpleStringProperty address_0;
        SimpleStringProperty address_1;
        SimpleStringProperty address_2;
        SimpleStringProperty address_3;

        public String getAddress() {
            return address.get();
        }

        public void setAddress(SimpleStringProperty address) {
            this.address = address;
        }

        public String getAddress_0() {
            return address_0.get();
        }

        public void setAddress_0(SimpleStringProperty address_0) {
            this.address_0 = address_0;
        }

        public String getAddress_1() {
            return address_1.get();
        }

        public void setAddress_1(SimpleStringProperty address_1) {
            this.address_1 = address_1;
        }

        public String getAddress_2() {
            return address_2.get();
        }

        public void setAddress_2(SimpleStringProperty address_2) {
            this.address_2 = address_2;
        }

        public String getAddress_3() {
            return address_3.get();
        }

        public void setAddress_3(SimpleStringProperty address_3) {
            this.address_3 = address_3;
        }

        public memtab(String address, String address_0, String address_1, String address_2, String address_3) {
            this.address = new SimpleStringProperty(address);
            this.address_0 = new SimpleStringProperty(address_0);
            this.address_1 = new SimpleStringProperty(address_1);
            this.address_2 = new SimpleStringProperty(address_2);
            this.address_3 = new SimpleStringProperty(address_3);
        }

    }

    public void highlightingRow(int row) {
        machineCodeTable.setRowFactory(tv -> new TableRow<MTable>() {
            @Override
            public void updateItem(MTable item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getPc().equals(String.format("0x%08X", row))) {
                    setStyle("-fx-background-color: tomato;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    /**
     * Makes a deep copy of any Java object that is passed.
     */
    private static Object deepCopy(Object object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
            outputStrm.writeObject(object);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
            return objInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
