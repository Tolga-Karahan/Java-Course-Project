import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.table.AbstractTableModel;

public class GUI extends JFrame
{
     // GUI bileşenleri için tanımlamalar
     private final JTable table; // Verilerin tutulacağı tablo
     private final JScrollPane pane; // Tablo üzerinde kaydırma işlevi
     private final Box parent; // Bileşenlerin tutulacağı parent Box
     private final Box child1; // textField ve buton un tutulduğu Box
     private final JPanel panel1; // label ve child Box'ın tutulduğu panel
     private final JPanel panel2; // JRadioButton ların tutulduğu panel
     private final JPanel panel3; // İsim bilgisinin tutulduğu panel
     private final JRadioButton borcSorgu;
     private final JRadioButton tahsilatSorgu;
     private final ButtonGroup buttons;
     private final JLabel label;
     private final JLabel isimIcerik;
     private final JTextField textField;
     private final JButton buton;
     private String refValue; // Referans numarası
     private String sorgu = "BorcSorgu"; // İşlem tipi

     public GUI()
     {
        /*ParseXML parser = new ParseXML("30295872165");
        parser.parse();*/

        setTitle("Borc Sorgulama");

        // Kullanıcıdan referans no yu isteyen ve alan bileşenler
        label = new JLabel("Referans No");
        textField = new JTextField(11);
        buton = new JButton("Sorgula");

        // Referans no yu alan bileşenlerin tutulduğu Box
        child1 = Box.createHorizontalBox();
        child1.add(textField);
        child1.add(buton);
        panel1 = new JPanel();
        panel1.add(label);
        panel1.add(child1);

        // RadioButtonların gösterileceğin panel
        panel2 = new JPanel();
        buttons = new ButtonGroup();
        borcSorgu = new JRadioButton("Borc Sorgu", true);
        tahsilatSorgu = new JRadioButton("Tahsilat Sorgu", false);
        buttons.add(borcSorgu);
        buttons.add(tahsilatSorgu);
        panel2.add(borcSorgu);
        panel2.add(tahsilatSorgu);

        // İsim bilgisinin gösterileceği panel
        panel3 = new JPanel();
        isimIcerik = new JLabel();
        panel3.add(isimIcerik);

        // Verilerin gösterileceği tablo ve ilgili setlemeler
        table = new JTable();
        pane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Tüm bileşenleri tutacak dikey Box ve Box a bileşenlerin eklenmesi
        parent = Box.createVerticalBox();
        parent.add(panel1);
        parent.add(panel2);
        parent.add(panel3);
        parent.add(pane);

        add(parent);

        // borcSorgu butonu için event handler
        borcSorgu.addItemListener(new ItemListener()
            {
                @Override
                public void itemStateChanged(ItemEvent e)
                {
                    sorgu = "BorcSorgu";
                }
            });

        // tahsilatSorgu butonu için event handler
        tahsilatSorgu.addItemListener(new ItemListener()
            {
                @Override
                public void itemStateChanged(ItemEvent e)
                {
                    sorgu = "TahsilatSorgu";
                }
            });

        // Buton için event handler
        buton.addActionListener(new ActionListener()
           {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                 ParseXML parser = new ParseXML(textField.getText(), sorgu);
                 parser.parse();
                 isimIcerik.setText(parser.getName());
                 table.setModel(
                    new MyTable(parser.getColumns(), parser.getTableData()));
               }
           });
     }

     // Değişen verileri tutan TableModel
     private class MyTable extends AbstractTableModel
     {
         private final String[] columns;
         private Object[][] data;

         public MyTable(String[] columns, Object[][] data)
         {
             this.columns = columns;
             this.data = data;
         }

         public int getColumnCount()
         {
             return columns.length;
         }

         public int getRowCount()
         {
             return data.length;
         }

         public String getColumnName(int col)
         {
             return columns[col];
         }

         public Object getValueAt(int row, int col)
         {
             return data[row][col];
         }
     }


     public static void main(String[] args)
     {
        GUI gui = new GUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(700, 500);
        gui.setVisible(true);
     }
}
