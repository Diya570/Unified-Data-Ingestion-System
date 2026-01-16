import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class SwingUI extends JFrame {

    JTextField filePathField;
    JLabel statusLabel;

    public SwingUI() {

        setTitle("Unified Data Ingestion System");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // -------- TOP PANEL --------
JPanel topPanel = new JPanel(new BorderLayout(5, 5));

JLabel pathLabel = new JLabel("Enter File Path:");

filePathField = new JTextField();
filePathField.getDocument().addDocumentListener(
    new javax.swing.event.DocumentListener() {

        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            statusLabel.setText(" ");
        }

        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            statusLabel.setText(" ");
        }

        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            statusLabel.setText(" ");
        }
    }
);

JButton browseButton = new JButton("Browse");

// Panel for textfield + browse button
JPanel pathPanel = new JPanel(new BorderLayout(5, 5));
pathPanel.add(filePathField, BorderLayout.CENTER);
pathPanel.add(browseButton, BorderLayout.EAST);

topPanel.add(pathLabel, BorderLayout.NORTH);
topPanel.add(pathPanel, BorderLayout.CENTER);

add(topPanel, BorderLayout.NORTH);


        //Browse
        browseButton.addActionListener(e ->{
            JFileChooser fileChooser = new JFileChooser();
            int result=fileChooser.showOpenDialog(this);

            if(result == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            statusLabel.setText("");
        }
        });

        // Button
        JButton ingestButton = new JButton("Ingest File");
        ingestButton.setPreferredSize(new Dimension(120, 30));
        ingestButton.addActionListener(e -> ingestFile());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ingestButton);
        add(buttonPanel, BorderLayout.CENTER);

        // Status
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private void ingestFile() {
    try {
        String filePath = filePathField.getText().trim();

        // 1️⃣ Empty path check
        if (filePath.isEmpty()) {
            statusLabel.setText("❌ Please select a file");
            return;
        }

        Path path = Path.of(filePath);

        // 2️⃣ File existence check
        if (!Files.exists(path)) {
            statusLabel.setText("❌ File not found");
            return;
        }

        String fileName = path.getFileName().toString();

        // 3️⃣ Extension existence check
        if (!fileName.contains(".")) {
            statusLabel.setText("❌ File has no extension");
            return;
        }

        String extension =
                fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();

        String content;

        // 4️⃣ EXTENSION VALIDATION (MAIN REQUIREMENT)
        switch (extension) {

            case "TXT":
                content = TXTReader.readTXT(filePath);
                break;

            case "XML":
                content = XMLReader.readXML(filePath);
                break;

            case "XLS":
            case "XLSX":
                content = XLSReader.readXLS(filePath);
                break;

            default:
                statusLabel.setText(
                    "❌ Invalid file type. Only TXT, XML, XLS files are allowed."
                );
                return;
        }

        // 5️⃣ Store in database (ONLY if valid)
        RawDataDAO.insertRawData(extension, fileName, content);

        statusLabel.setText("✅ Data stored in PostgreSQL successfully");

    } catch (Exception e) {
        e.printStackTrace();
        statusLabel.setText("❌ Error occurred while processing file");
    }
}
}