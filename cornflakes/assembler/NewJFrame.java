/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import datapath.*;
import java.awt.*;
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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ramak
 */
public class NewJFrame extends javax.swing.JFrame {

    String filename;
    String filedirectory;
    boolean firstsave = false;
    String outputfile;
    String dir;
    String file;
    public primary_memory memory = new primary_memory();
    boolean pip=false;
    boolean b1=false;
    boolean b2=false;
    boolean b3=false;
    boolean b4=false;
Object[][] reg=new Object [][] {
        {"x0", 0},
        {"x1", 0},
        {"x2", memory.register[2]},
        {"x3", memory.register[3]},
        {"x4", 0},
        {"x5", 0},
        {"x6", 0},
        {"x7", 0},
        {"x8", 0},
        {"x9", 0},
        {"x10", 0},
        {"x11", 0},
        {"x12", 0},
        {"x13", 0},
        {"x14", 0},
        {"x15", 0},
        {"x16", 0},
        {"x17", 0},
        {"x18", 0},
        {"x19", 0},
        {"x20", 0},
        {"x21", 0},
        {"x22", 0},
        {"x23", 0},
        {"x24", 0},
        {"x25", 0},
        {"x26", 0},
        {"x27", 0},
        {"x28", 0},
        {"x29", 0},
        {"x30", 0},
        {"x31", 0},};
    String[] column=new String [] {
        "Register", "Value"
    };
    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cache_size = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        instruction_cache_type = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        block_size = new javax.swing.JLabel();
        no_sets = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        c_access = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        Build = new javax.swing.JButton();
        Run = new javax.swing.JButton();
        pipeline = new javax.swing.JToggleButton();
        MenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        New = new javax.swing.JMenuItem();
        Open = new javax.swing.JMenuItem();
        Save = new javax.swing.JMenuItem();
        SaveAs = new javax.swing.JMenuItem();
        Edit = new javax.swing.JMenu();
        Cut = new javax.swing.JMenuItem();
        Paste = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextArea1.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(jTextArea1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            reg,column
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        //jTable1.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        jTable1.setRowHeight(25);
        jScrollPane2.setViewportView(jTable1);

        cache_size.setText("Cache Size");

        jTextField1.setText("2");

        instruction_cache_type.setText("Instr Cache Type");

        jLabel4.setText("Data Cache type");

        block_size.setText("Block Size");

        no_sets.setText("No. Sets");

        jTextField2.setText("2");

        jTextField3.setText("0");

        jTextField4.setText("0");

        jTextField5.setText("0");

        c_access.setText("Total access");

        jTextField6.setText("0");

        jLabel2.setText("Total Miss");

        jLabel3.setText("Cold Miss");

        jTextField7.setText("0");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Cache Information");

        jTextField8.setText("0");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jLabel5.setText("Conflict Miss");

        jTextField9.setText("0");

        jLabel6.setText("Capacity Miss");

        jTextField10.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(instruction_cache_type, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(block_size, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(cache_size, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(c_access))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(no_sets, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField10))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(150, 150, 150)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cache_size, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_access, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(block_size, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(instruction_cache_type, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(no_sets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField5)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 460, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jToolBar1.setRollover(true);

        Build.setText("Build");
        Build.setFocusable(false);
        Build.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Build.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Build.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuildActionPerformed(evt);
            }
        });
        jToolBar1.add(Build);

        Run.setText("Run");
        Run.setFocusable(false);
        Run.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Run.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunActionPerformed(evt);
            }
        });
        jToolBar1.add(Run);

        pipeline.setText("pipeline");
        pipeline.setFocusable(false);
        pipeline.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pipeline.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pipeline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pipelineActionPerformed(evt);
            }
        });
        jToolBar1.add(pipeline);

        jMenu1.setText("File");

        New.setText("New");
        New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewActionPerformed(evt);
            }
        });
        jMenu1.add(New);

        Open.setText("Open");
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });
        jMenu1.add(Open);

        Save.setText("Save");
        Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveActionPerformed(evt);
            }
        });
        jMenu1.add(Save);

        SaveAs.setText("Save As");
        SaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveAsActionPerformed(evt);
            }
        });
        jMenu1.add(SaveAs);

        MenuBar.add(jMenu1);

        Edit.setText("Edit");

        Cut.setText("Cut");
        Edit.add(Cut);

        Paste.setText("Paste");
        Edit.add(Paste);

        MenuBar.add(Edit);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // MenuBar.add(jButton1);
    private void NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewActionPerformed
        // TODO add your handling code here:
        jTextArea1.setText("");
        setTitle(filename);

    }//GEN-LAST:event_NewActionPerformed

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
        // TODO add your handling code here:
        FileDialog fileDialog = new FileDialog(NewJFrame.this, "Open File", FileDialog.LOAD);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() != null) {
            filename = fileDialog.getDirectory() + fileDialog.getFile();
            setTitle(filename);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                jTextArea1.setText(sb.toString());

            }
            reader.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }//GEN-LAST:event_OpenActionPerformed

    private void SaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveAsActionPerformed
        FileDialog fileDialog = new FileDialog(NewJFrame.this, "Save File", FileDialog.SAVE);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() != null) {
            filename = fileDialog.getDirectory() + fileDialog.getFile();
            file = fileDialog.getFile();
            dir = fileDialog.getDirectory();
            setTitle(filename);
        }
        try {
            FileWriter filewriter = new FileWriter(filename);
            String content = jTextArea1.getText();
            content = content.replaceAll("(?!\\r)\\n", "\r\n");
            filewriter.write(content);
            setTitle(filename);
            filewriter.flush();
            filewriter.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
        firstsave = true;
    }//GEN-LAST:event_SaveAsActionPerformed

    private void SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveActionPerformed
        // TODO add your handling code here:
        if (firstsave == false) {
            SaveAsActionPerformed(evt);
            // firstsave = true;
        }
        try {
            FileWriter filewriter = new FileWriter(filename);
            String content = jTextArea1.getText();
            content = content.replaceAll("(?!\\r)\\n", "\r\n");
            filewriter.write(content);
//filewriter.write(jTextArea1.getText());
            setTitle(filename);
            filewriter.close();
            filewriter.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }


    }//GEN-LAST:event_SaveActionPerformed

    private void BuildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuildActionPerformed
        // TODO add your handling code here:
        SaveActionPerformed(evt);
        assembler obj = new assembler();
        try {
            obj.assemble(filename,memory);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // PrintWriter p=null;
        //creating output filename
        int temp = file.indexOf('.');
        outputfile = dir + file.substring(0, temp) + ".mc";
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
    }//GEN-LAST:event_BuildActionPerformed

    private void RunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunActionPerformed
        // TODO add your handling code here:
        //String a=jTextField1.toString();
        int a1=Integer.parseInt(jTextField1.getText());
        int a2=Integer.parseInt(jTextField2.getText());
        int a3=Integer.parseInt(jTextField3.getText());
        int a4=Integer.parseInt(jTextField4.getText());
        int a5=Integer.parseInt(jTextField5.getText());
        
        memory.set_primary_memory(a1,a2,a3,a4,a5);
        BuildActionPerformed(evt);
        File file = new File(outputfile);
        
        int mem_index=0;
                try
                {

                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    //System.out.println("No input");
                    while ((line = br.readLine()) != null)
                        {
                            //System.out.println(line);
                            long itemp = Long.parseLong(line.substring(2), 16);
                            String bin_line = Long.toBinaryString(itemp);
                            String temp="";
                            for (int i = 0; i < 32 - bin_line.length(); i++)
                            {
                                    temp = temp + "0";
                            }
                            memory.storewordstr(mem_index,temp+bin_line);
                            //System.out.println(mem.loadword(mem_index)+"p");
                            mem_index=mem_index+4;

                        }
                    for(int i=0;i<10;i++)
                    {
                        memory.storewordstr(mem_index,"00000000000000000000000000000000");
                            //System.out.println(mem.loadword(mem_index)+"p");
                            mem_index=mem_index+4;

                    }
                }
                catch(Exception e){System.out.println(e);}
        datapath dat=new datapath();
        
        dat.run(memory,pip);
        jTextField6.setText(Integer.toString(memory.access));
        jTextField7.setText(Integer.toString(memory.capacity_miss+memory.cold_miss+memory.conflict_miss));
        jTextField8.setText(Integer.toString(memory.cold_miss));
        jTextField9.setText(Integer.toString(memory.conflict_miss));
        jTextField10.setText(Integer.toString(memory.capacity_miss));
//System.out.println(memory.access);
        for(int i=0;i<32;i++)
        {
            if(i==0)
                reg[i][1]=0;
            reg[i][1]=memory.register[i];
        }
        jTable1.setModel(new DefaultTableModel(reg, column));
    }//GEN-LAST:event_RunActionPerformed

    private void pipelineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pipelineActionPerformed
        // TODO add your handling code here:
        pip=!pip;
    }//GEN-LAST:event_pipelineActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Build;
    private javax.swing.JMenuItem Cut;
    private javax.swing.JMenu Edit;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenuItem New;
    private javax.swing.JMenuItem Open;
    private javax.swing.JMenuItem Paste;
    private javax.swing.JButton Run;
    private javax.swing.JMenuItem Save;
    private javax.swing.JMenuItem SaveAs;
    private javax.swing.JLabel block_size;
    private javax.swing.JLabel c_access;
    private javax.swing.JLabel cache_size;
    private javax.swing.JLabel instruction_cache_type;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel no_sets;
    private javax.swing.JToggleButton pipeline;
    // End of variables declaration//GEN-END:variables
}