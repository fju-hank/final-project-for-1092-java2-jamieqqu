package com.fju;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Tester extends JFrame {

    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final long serialVersionUID = 1L;

    public static List<List<String>> sourcelist; //來源清單
    private JPanel contentPane;
    private JTextField textField;
    private JTable table_1;
    private JScrollPane scroll;
    private DefaultTableModel dm;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Tester frame = new Tester();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Tester() throws ParseException {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 322, 392);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textField = new JTextField();
        textField.setBounds(10, 22, 179, 21);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("\u641C\u5C0B");
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                List<List<String>> newSource = new ArrayList<List<String>>();
                if(textField.getText() != null && textField.getText().length() > 0) {
                    for(List<String> strList : sourcelist) {

                        if(strList.get(2).indexOf(textField.getText()) != -1) {
                            newSource.add(strList);
                        }
                    }
                }else {
                    newSource = sourcelist;
                }

                String[][] array = new String[newSource.size()][];
                for (int a = 0; a < newSource.size(); a++) {
                    List<String> row = newSource.get(a);
                    array[a] = new String[]{"",row.get(2), row.get(3)};
                }
                dm.setDataVector(array, new Object[] { "Button", "餐點", "價格"});
                dm.fireTableDataChanged();
                table_1.getColumn("Button").setCellRenderer(new ButtonRenderer());
                table_1.getColumn("Button").setCellEditor(new ButtonEditor(new JCheckBox()));

                int opt=JOptionPane.showConfirmDialog(contentPane,"是否要再次搜尋","確認",
                        JOptionPane.YES_NO_OPTION);

                if (opt==JOptionPane.YES_OPTION) {
                }
                else if (opt==JOptionPane.NO_OPTION) {
                    textField.setText("");
                }
            }
        });
        btnNewButton.setBounds(199, 21, 97, 23);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("\u7D71\u8A08");
        btnNewButton_1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("請選擇匯出的檔案");
                int userSelection = fileChooser.showSaveDialog(fileChooser);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToSave = fileChooser.getSelectedFile();

                    Runnable task = () -> {
                        try {
                            Parse.exportToCSV(fileToSave);
                            OrderCache.order.clear();
                            JOptionPane.showMessageDialog(fileChooser, "匯出成功","INFORMATION",JOptionPane.INFORMATION_MESSAGE);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    };

                    Thread thread = new Thread(task);
                    thread.start();

                }

            }

        });
        btnNewButton_1.setBounds(10, 304, 286, 39);
        contentPane.add(btnNewButton_1);

        Parse parse = new Parse();
        ArrayList<Map<String, String>> source = parse.parse();
        int i = 0;
        sourcelist = new ArrayList<List<String>>();
        for(Map<String, String> map : source) {
            if(i == 0) {
                //跳過第一航
                i++;
                continue;
            }
            List<String> temp = new ArrayList<String>();
            temp.add("");
            Date date = null;
            for(String key : map.keySet()) {
                if("PurchaseDate".equals(key)) {
                    date = formatter.parse(map.get(key));
                    temp.add(map.get(key));
                }
                if("Name".equals(key)) {

                    Long hours = null;
                    if(date != null) {
                        long diff = new Date().getTime() - date.getTime();
                        hours = diff / (1000 * 60 * 60);
                    }

                    if("Hamburger".equals(map.get(key))) {
                        Hamburger hamburger = new Hamburger();
                        temp.add(hamburger.getName());
                        if(hours != null && hours > hamburger.getExpireHour()) {
                            temp.add(String.valueOf(hamburger.getPrice() -  hamburger.getDiscount()));
                        }else {
                            temp.add(String.valueOf(hamburger.getPrice()));
                        }
                        continue;
                    }
                    if("Sub".equals(map.get(key))) {
                        Sub sub = new Sub();
                        temp.add(sub.getName());
                        if(hours != null && hours > sub.getExpireHour()) {
                            temp.add(String.valueOf(sub.getPrice() -  sub.getDiscount()));
                        }else {
                            temp.add(String.valueOf(sub.getPrice()));
                        }
                        continue;
                    }
                    if("Sausage".equals(map.get(key))) {
                        Sausage sausage = new Sausage();
                        temp.add(sausage.getName());
                        if(hours != null && hours > sausage.getExpireHour()) {
                            temp.add(String.valueOf(sausage.getPrice() -  sausage.getDiscount()));
                        }else {
                            temp.add(String.valueOf(sausage.getPrice()));
                        }
                        continue;
                    }
                }
            }
            sourcelist.add(temp);
        }

        String[][] array = new String[sourcelist.size()][];
        for (int a = 0; a < sourcelist.size(); a++) {
            List<String> row = sourcelist.get(a);
            array[a] = new String[]{"",row.get(2), row.get(3)};
        }

        dm = new DefaultTableModel();
        dm.setDataVector(array, new Object[] { "Button", "餐點", "價格"});

        table_1 = new JTable(dm);
        table_1.setDefaultEditor(Object.class, null);

        table_1.getColumn("Button").setCellRenderer(new ButtonRenderer());
        table_1.getColumn("Button").setCellEditor(new ButtonEditor(new JCheckBox()));
        scroll = new JScrollPane(table_1);
        scroll.setBounds(10, 78, 286, 207);
        contentPane.add(scroll);

    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {

    private static final long serialVersionUID = 5557037409700072773L;

    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText("加入");
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {

    private static final long serialVersionUID = 7812076562323016759L;

    protected JButton button;

    private String label;

    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.putClientProperty("table", table);
        button.putClientProperty("rowIndex", row);
        button.putClientProperty("columnIndex", column);
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            JTable table = (JTable)button.getClientProperty("table");
            Object obj = table.getModel().getValueAt(Integer.valueOf(String.valueOf(button.getClientProperty("rowIndex"))), 1);

            for(List<String> strList : Tester.sourcelist) {
                if(strList.get(2).indexOf(obj.toString()) != -1) {
                    OrderCache.order.add(strList);
                    break;
                }
            }
        }
        isPushed = false;
        return new String(label);
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}