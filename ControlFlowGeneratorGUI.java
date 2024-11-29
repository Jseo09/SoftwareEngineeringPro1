
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ControlFlowGeneratorGUI extends JFrame {
    private JTextArea codeInputArea, outputArea;
    private ControlFlowGenerator generator;
    private Flowchart flowchartPanel;
    public ControlFlowGeneratorGUI() {
        super("Control Flow Generator");
        this.flowchartPanel = new Flowchart();
        this.generator = new ControlFlowGenerator(flowchartPanel);
        setupGUI();
    }

    private void setupGUI() {
        setLayout(new BorderLayout());

        // Input area for code
        codeInputArea = new JTextArea();
        codeInputArea.setBorder(BorderFactory.createTitledBorder("Enter Java Code Here"));
        JScrollPane inputScroll = new JScrollPane(codeInputArea);

        // Output area for analysis
        outputArea = new JTextArea();
        outputArea.setBorder(BorderFactory.createTitledBorder("Output"));
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        // SplitPane for input and output areas
        JSplitPane inputOutputSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputScroll, outputScroll);
        inputOutputSplitPane.setResizeWeight(0.5);

        // Flowchart panel with scrolling
        JScrollPane flowchartScrollPane = new JScrollPane(flowchartPanel);
        flowchartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        flowchartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Main split pane to organize the GUI layout
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputOutputSplitPane, flowchartScrollPane);
        add(mainSplitPane, BorderLayout.CENTER);

        // Button panel for actions
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JButton analyzeButton = new JButton("Analyze Code");
        JButton generateButton = new JButton("Generate Flowchart");
        JButton exportButton = new JButton("Export Flowchart");

        // Analyze Code button logic
        analyzeButton.addActionListener(e -> {
            String code = codeInputArea.getText();
            if (!code.isEmpty()) {
                // Check for syntax errors using ErrorManager
                String syntaxErrors = ErrorManager.checkSyntaxErrors(code);
                if (!syntaxErrors.isEmpty()) {
                    // Show error popup and stop further analysis
                    JOptionPane.showMessageDialog(this, "Syntax Errors Detected:\n" + syntaxErrors, 
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Proceed with analysis if no errors are found
                generator.inputCode(code);
                String analysisResult = generator.analyzeCode();
                outputArea.setText(analysisResult);
            } else {
                outputArea.setText("Please enter code for analysis.");
            }
        });        

        // Generate Flowchart button logic
        generateButton.addActionListener(e -> {
            String analysisResult = outputArea.getText();
            if (!analysisResult.isEmpty() && !analysisResult.equals("Please enter code for analysis.")) {
                flowchartPanel.generateFlowchart(analysisResult);
                outputArea.append("\n[Flowchart updated]");
            } else {
                JOptionPane.showMessageDialog(this, "Analyze code before generating the flowchart.", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Export Flowchart button logic
        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Flowchart as PNG");
            int userSelection = fileChooser.showSaveDialog(ControlFlowGeneratorGUI.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                flowchartPanel.exportFlowchart(fileToSave.getAbsolutePath() + ".png");
            }
        });

        // Add buttons to button panel
        buttonPanel.add(analyzeButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(exportButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Frame settings
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ControlFlowGeneratorGUI::new);
    }
}
