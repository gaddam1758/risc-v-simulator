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
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ramak
 */
public class SimulatorController implements Initializable {
    ObservableList<MTable> list=FXCollections.observableArrayList();
    @FXML
    private TableView registersTable;
    @FXML
    private TableColumn<String,Integer> RegistersColumn;
    @FXML
    private TableColumn<String,Integer> RegisterValueColumn;
    @FXML
    private TableView<MTable> machineCodeTable;
    @FXML
    private TableColumn<MTable,String>PCColumn;
    @FXML
    private TableColumn<MTable,String>MCColumn;
    @FXML
    private Button assemble;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        machineCodeTable.setPlaceholder(null);
        PCColumn.setCellValueFactory(new PropertyValueFactory<MTable,String>("Pc"));
        MCColumn.setCellValueFactory(new PropertyValueFactory<MTable,String>("MachineCode"));
       machineCodeTable.setItems(list);
       
    } 
    //contents in machine code Table;
            
    /**
     * this method will return pc and machine code values as a ObservableList For Table
     * @return 
     */
    public ObservableList<MTable> getMTableList(String filename) throws IOException
    {
        
     BufferedReader reader = new BufferedReader(new FileReader(filename));
            int pc=0;
           //String.format("0x%08X", decimal)
            String line = null;
            while ((line = reader.readLine()) != null) {
                String temp=String.format("0x%08X", pc);
                list.add(new MTable(temp,line));
                pc=pc+4;
            }
            if(list.size()==0)
                list.add(new MTable("",""));
        return list;
    }
    @FXML
    void assembleButtonAction()
    {
        //registersTable.getItems().add(0x1,0);
    }    
    
}
