package file.encryptor;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class Main extends javax.swing.JFrame {

  private static Main GUI;

  private int c1;
  private int c2;
  private int c3;
  private int c4;

  private Thread pokazThread;
  private Thread opravThread;

  private File subor;
  private String heslo;

  public Main() {
    initComponents();
    listGui.setDragEnabled(true);
    listGui.setTransferHandler(new FileListTransferHandler(listGui));
  }

  // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    passwordLabel = new javax.swing.JLabel();
    passField = new javax.swing.JTextField();
    jScrollPane1 = new javax.swing.JScrollPane();
    listGui = new javax.swing.JList < > ();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    passwordLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
    passwordLabel.setText("Password: ");

    passField.setText("enter password here....");

    listGui.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
    listGui.setModel(new javax.swing.AbstractListModel < String > () {
      String[] strings = {
        "\tDrag n Drop file to encrypt here...."
      };
      public int getSize() {
        return strings.length;
      }
      public String getElementAt(int i) {
        return strings[i];
      }
    });
    jScrollPane1.setViewportView(listGui);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(6, 6, 6)
            .addComponent(jScrollPane1))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(passwordLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(14, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(15, 15, 15)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(passwordLabel)
          .addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        .addGap(12, 12, 12))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    pack();
  } // </editor-fold>                        

  class FileListTransferHandler extends TransferHandler {
    public FileListTransferHandler(JList list) {
      list = list;
    }

    public int getSourceActions(JComponent c) {
      return 3;
    }

    public boolean canImport(TransferHandler.TransferSupport ts) {
      return ts.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    public boolean importData(TransferHandler.TransferSupport ts) {
      try {
        List data = (List) ts.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
        DefaultListModel < String > listModel = new DefaultListModel();
        String filePath = "";

        for (Object item: data) {
          subor = (File) item;
          filePath = subor.getAbsoluteFile().getPath();
          System.out.println(filePath);
          citajBytes(filePath);
          listModel.addElement(filePath);

          if (c1 == 82 && c2 == 97 && c3 == 114 && c4 == 33) {
            pokazThread(filePath);
          } else {
            opravThread(filePath);
          }
        }
        listGui.setModel(listModel);

        return true;
      } catch (UnsupportedFlavorException e) {
        return false;
      } catch (IOException iOException) {
        return false;
      }
    }

    public void pokazThread(final String s) {
      pokazThread = new Thread() {
        public void run() {
          try {
            String temp = "XXXX";

            heslo = passField.getText();

            byte[] fileBytes = NullbeansFileManager.readFile(s);
            byte[] buzz = AESEncryptionManager.encryptData(heslo, fileBytes);
            NullbeansFileManager.writeFile(s, buzz);
            renameFileExtension(subor.getAbsolutePath(), "subX");
            pokazThread.interrupt();
          } catch (Exception ex) {
            System.out.println("Chyba:" + ex.getLocalizedMessage());
            ex.printStackTrace();
          }
        }
      };
      pokazThread.start();
    }

    public void opravThread(final String s) {
      opravThread = new Thread() {

        public void run() {
          try {
            System.out.println("==================================");
            heslo = passField.getText();
            byte[] x = AESEncryptionManager.decryptData(heslo, NullbeansFileManager.readFile(s));
            NullbeansFileManager.writeFile(s, x);
            renameFileExtension(s, "rar");
            opravThread.interrupt();
          } catch (Exception ex) {
            System.out.println("Chyba:" + ex.getLocalizedMessage());
            ex.printStackTrace();
          }
        }
      };

      opravThread.start();
    }

    public void oprav(String s) throws FileNotFoundException, IOException {
      RandomAccessFile raf = new RandomAccessFile(s, "rw");
      raf.seek(0L);
      raf.write(82);
      raf.seek(1L);
      raf.write(97);
      raf.seek(2L);
      raf.write(114);
      raf.seek(3L);
      raf.write(33);
      raf.close();
    }

    public void citajBytes(String s) throws FileNotFoundException, IOException {
      RandomAccessFile raf = new RandomAccessFile(s, "rw");
      raf.seek(0L);
      c1 = raf.read();
      raf.seek(1L);
      c2 = raf.read();
      raf.seek(2L);
      c3 = raf.read();
      raf.seek(3L);
      c4 = raf.read();
      System.out.println(c1 + " " + c2 + " " + c3 + " " + c4);
      raf.close();
    }
  }

  public static boolean renameFileExtension(String source, String newExtension) {
    String target, currentExtension = getFileExtension(source);

    if (currentExtension.equals("")) {
      target = source + "." + newExtension;
    } else {
      target = source.replaceFirst(Pattern.quote("." + currentExtension) + "$", Matcher.quoteReplacement("." + newExtension));
    }

    return (new File(source)).renameTo(new File(target));
  }

  public static String getFileExtension(String f) {
    String ext = "";
    int i = f.lastIndexOf('.');

    if (i > 0 && i < f.length() - 1) {
      ext = f.substring(i + 1);
    }
    return ext;
  }

  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        Main GUI = new Main();
        GUI.setTitle("");
        GUI.setDefaultCloseOperation(3);
        GUI.setLocationByPlatform(true);
        GUI.setVisible(true);
        GUI.pack();
      }
    });
  }

  // Variables declaration - do not modify                     
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JList < String > listGui;
  private javax.swing.JTextField passField;
  private javax.swing.JLabel passwordLabel;
  // End of variables declaration                   
}