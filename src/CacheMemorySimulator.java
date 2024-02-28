import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CacheMemorySimulator extends JFrame {
    private JTextField inputField;
    private JTextField valueField;
    private JTextArea outputArea;
    private JButton requestButton;
    private JButton updateButton;
    private JButton downloadButton;
    private JButton showHitRatioButton;

    private Map<String, String> cache;
    private int cacheHits = 0;
    private int cacheMisses = 0;
    private double currentHitMissRatio = 0.0;

    public CacheMemorySimulator() {
        inputField = new JTextField(10);
        valueField = new JTextField(10);
        outputArea = new JTextArea(10, 30);
        requestButton = new JButton("Make Request");
        updateButton = new JButton("Update Cache");
        downloadButton = new JButton("Download");
        showHitRatioButton = new JButton("Show Hit Ratio");
        cache = new HashMap<>();

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(inputField);
        inputPanel.add(new JLabel("Value:"));
        inputPanel.add(valueField);
        add(inputPanel, BorderLayout.NORTH);

        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(requestButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(downloadButton);
        buttonPanel.add(showHitRatioButton);
        add(buttonPanel, BorderLayout.SOUTH);

        requestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeRequest();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCache();
            }
        });

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDownload();
            }
        });

        showHitRatioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHitRatio();
            }
        });

        setTitle("Cache Memory Simulator");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean isValidInput(String input){
        try{
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void updateHitMissRatio(){
        int totalRequests = cacheHits + cacheMisses;
        currentHitMissRatio = totalRequests > 0 ? (double) cacheHits/totalRequests : 0;
    }

    private void makeRequest() {
        String address = inputField.getText();
        if(!isValidInput(address))
            return;
        if (cache.containsKey(address)) {
            cacheHits++;
            JOptionPane.showMessageDialog(this, "Cache Hit: " + cache.get(address), "Cache Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            cacheMisses++;
            String data = fetchDataFromMemory(  address);
            cache.put(address, data);
            JOptionPane.showMessageDialog(this, "Cache Miss: " + data + " (data fetched from memory)", "Cache Status", JOptionPane.WARNING_MESSAGE);
        }
        updateHitMissRatio();
    }

    private void updateCache() {
        String address = inputField.getText();
        String value = valueField.getText();
        if (!isValidInput(address) || !isValidInput(value))
            return;
        cache.put(address, value);
        outputArea.append("Cache Updated at address " + address + " with the value: " + value+ "\n");
    }

    private void performDownload() {
        String address = inputField.getText();
        String value = valueField.getText();
        if (!isValidInput(address))
            return;
        if (cache.containsKey(address)) {
            JOptionPane.showMessageDialog(this, "Download successful", "Download Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Data not found in cache", "Download Status", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String fetchDataFromMemory(String address) {
        return "Data for " + address;
    }

    private void showHitRatio(){
        String ratioPercentage = String.format("%.2f", currentHitMissRatio * 100);
        JOptionPane.showMessageDialog(this,"Hit/Miss Ratio: " + ratioPercentage + "%\n", "Hit/Miss", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CacheMemorySimulator();
            }
        });
    }
}