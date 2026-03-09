import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class SwingUI extends JFrame {

    private JTextField filePathField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel xmlCountLabel, xlsCountLabel, txtCountLabel;

    public SwingUI() {

        // ===== FRAME =====
        setTitle("Unified Data Ingestion System");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("Unified Data Ingestion System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel fileLabel = new JLabel("Enter File Path:");
        filePathField = new JTextField(35);

        JButton browseBtn = new JButton("Browse");
        JButton ingestBtn = new JButton("Ingest File");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        topPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        topPanel.add(fileLabel, gbc);

        gbc.gridx = 1;
        topPanel.add(filePathField, gbc);

        gbc.gridx = 2;
        topPanel.add(browseBtn, gbc);

        gbc.gridy = 2; gbc.gridx = 1;
        topPanel.add(ingestBtn, gbc);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER TABLE (SCROLLBAR WORKS) =====
        String[] columns = {"ID", "File Type", "File Name"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        add(scrollPane, BorderLayout.CENTER);

        // ===== COUNTS PANEL =====
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        xmlCountLabel = new JLabel("XML Files: 0");
        xlsCountLabel = new JLabel("XLS Files: 0");
        txtCountLabel = new JLabel("TXT Files: 0");

        countPanel.add(xmlCountLabel);
        countPanel.add(xlsCountLabel);
        countPanel.add(txtCountLabel);

        // ===== STATUS PANEL =====
        statusLabel = new JLabel("Select a file to ingest", SwingConstants.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(countPanel, BorderLayout.NORTH);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                filePathField.setText(file.getAbsolutePath());
                statusLabel.setText("File selected");
            }
        });

        ingestBtn.addActionListener(e -> ingestFile());

        // ===== INITIAL LOAD =====
        loadTableData();
        updateCounts();
        setVisible(true);
    }

    private void ingestFile() {
        try {
            String path = filePathField.getText().trim();
            if (path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a file");
                return;
            }

            String fileType = path.substring(path.lastIndexOf('.') + 1).toUpperCase();
            String fileName = new File(path).getName();
            File file= new File(path);
            

            String extension = fileType.toLowerCase();
            switch (extension) {

    case "txt": 
        
        String txtContent = TXTReader.readTXT(file.getAbsolutePath());
        RawDataDAO.insertRawData("TXT", fileName, txtContent);
        break;
    
    
    case "xml": 
        String xmlContent = XMLReader.readXML(file.getAbsolutePath());
        RawDataDAO.insertRawData("XML", fileName, xmlContent);
        break;
    

    case "xls":
    case "xlsx": 
        String xlsContent = XLSReader.readXLS(file.getAbsolutePath());
        RawDataDAO.insertRawData("XLS", fileName, xlsContent);
        break;
    

    default:
        JOptionPane.showMessageDialog(this, "Unsupported file type");
        return;
}

            loadTableData();
            updateCounts();
            statusLabel.setText("Data stored successfully");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error ingesting file");
        }
    }
    

    private void loadTableData() {
        try {
            tableModel.setRowCount(0);
            Object[][] data = RawDataDAO.fetchPreviewData();
            for (Object[] row : data) {
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCounts() {
        try {
            xmlCountLabel.setText("XML Files: " + RawDataDAO.countByType("XML"));
            xlsCountLabel.setText("XLS Files: " + RawDataDAO.countByType("XLS"));
            txtCountLabel.setText("TXT Files: " + RawDataDAO.countByType("TXT"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}