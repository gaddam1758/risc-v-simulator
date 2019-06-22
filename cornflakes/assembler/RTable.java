/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ramak
 */
public class RTable {
    SimpleStringProperty register;
    SimpleIntegerProperty value;

    public RTable(String register,int value) {
        this.register = new SimpleStringProperty(register);
        this.value = new SimpleIntegerProperty(value);
    }

    public String getRegister() {
        return register.get();
    }

    public void setRegister(SimpleStringProperty register) {
        this.register = register;
    }

    public int getValue() {
        return value.get();
    }

    public void setValue(SimpleIntegerProperty value) {
        this.value = value;
    }
    
    
}
