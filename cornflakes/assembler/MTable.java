/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ramak
 */
public  class MTable
    {
        SimpleStringProperty Pc;
        SimpleStringProperty MachineCode;

        public MTable(String Pc, String MachineCode) {
            this.Pc = new SimpleStringProperty(Pc);
            this.MachineCode = new SimpleStringProperty(MachineCode);
        }

        public String getPc() {
            return Pc.get();
        }

        public void setPc(SimpleStringProperty Pc) {
            this.Pc = Pc;
        }

        public String getMachineCode() {
            return MachineCode.get();
        }

        public void setMachineCode(SimpleStringProperty MachineCode) {
            this.MachineCode = MachineCode;
        }
        
        
    }