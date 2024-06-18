
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

class Drug {
    String drugCode;
    String name;
    int stock;
    LinkedList<Supplier> suppliers;
    LinkedList<Purchase> purchaseHistory;

    public Drug(String drugCode, String name, int stock) {
        this.drugCode = drugCode;
        this.name = name;
        this.stock = stock;
        this.suppliers = new LinkedList<>();
        this.purchaseHistory = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "Drug{" +
                "drugCode='" + drugCode + '\'' +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                '}';
    }
}

class Supplier {
    String id;
    String name;
    String location;

    public Supplier(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

class Purchase {
    String buyer;
    Date date;
    int amount;

    public Purchase(String buyer, Date date, int amount) {
        this.buyer = buyer;
        this.date = date;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "buyer='" + buyer + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}

class PharmacyManagementSystem {
    LinkedList<Drug> drugs;
    HashMap<String, LinkedList<Supplier>> drugSuppliers;

    public PharmacyManagementSystem() {
        this.drugs = new LinkedList<>();
        this.drugSuppliers = new HashMap<>();
    }

    // Add Drug
    public void addDrug(Drug drug) {
        drugs.add(drug);
        drugSuppliers.put(drug.drugCode, drug.suppliers);
    }

    // Search for a Drug
    public Drug searchDrug(String drugName) {
        for (Drug drug : drugs) {
            if (drug.name.equalsIgnoreCase(drugName)) {
                return drug;
            }
        }
        return null;
    }

    // View All Drugs
    public String viewAllDrugs() {
        StringBuilder sb = new StringBuilder();
        for (Drug drug : drugs) {
            sb.append(drug).append("\n");
        }
        return sb.toString();
    }

    // View Purchase History for a Drug
    public String viewPurchaseHistory(String drugName) {
        Drug drug = searchDrug(drugName);
        if (drug != null) {
            StringBuilder sb = new StringBuilder();
            for (Purchase purchase : drug.purchaseHistory) {
                sb.append(purchase).append("\n");
            }
            return sb.toString();
        }
        return "No purchase history found for the drug.";
    }
}

public class PharmacyManagementUI extends JFrame {
    private PharmacyManagementSystem pms;
    private JTextArea drugsTextArea;
    private JTextArea purchaseHistoryTextArea;

    public PharmacyManagementUI() {
        pms = new PharmacyManagementSystem();
        setTitle("Pharmacy Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeUI();
    }

    private void initializeUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Drug Panel
        JPanel addDrugPanel = new JPanel();
        addDrugPanel.setLayout(new GridLayout(5, 2));

        JLabel drugCodeLabel = new JLabel("Drug Code:");
        JTextField drugCodeField = new JTextField();
        JLabel drugNameLabel = new JLabel("Drug Name:");
        JTextField drugNameField = new JTextField();
        JLabel stockLabel = new JLabel("Stock:");
        JTextField stockField = new JTextField();

        JButton addButton = new JButton("Add Drug");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String drugCode = drugCodeField.getText();
                String drugName = drugNameField.getText();
                int stock = Integer.parseInt(stockField.getText());
                Drug drug = new Drug(drugCode, drugName, stock);
                pms.addDrug(drug);
                JOptionPane.showMessageDialog(null, "Drug added successfully!");
                drugCodeField.setText("");
                drugNameField.setText("");
                stockField.setText("");
                updateDrugsTextArea();
            }
        });

        addDrugPanel.add(drugCodeLabel);
        addDrugPanel.add(drugCodeField);
        addDrugPanel.add(drugNameLabel);
        addDrugPanel.add(drugNameField);
        addDrugPanel.add(stockLabel);
        addDrugPanel.add(stockField);
        addDrugPanel.add(new JLabel());
        addDrugPanel.add(addButton);

        // View Drugs Panel
        JPanel viewDrugsPanel = new JPanel(new BorderLayout());
        drugsTextArea = new JTextArea(20, 50);
        drugsTextArea.setEditable(false);
        viewDrugsPanel.add(new JScrollPane(drugsTextArea), BorderLayout.CENTER);

        // Search Drug Panel
        JPanel searchDrugPanel = new JPanel();
        searchDrugPanel.setLayout(new GridLayout(3, 2));

        JLabel searchDrugNameLabel = new JLabel("Drug Name:");
        JTextField searchDrugNameField = new JTextField();
        JTextArea searchResultArea = new JTextArea(5, 20);
        searchResultArea.setEditable(false);
        JButton searchDrugButton = new JButton("Search Drug");
        searchDrugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String drugName = searchDrugNameField.getText();
                Drug drug = pms.searchDrug(drugName);
                if (drug != null) {
                    searchResultArea.setText(drug.toString());
                } else {
                    searchResultArea.setText("Drug not found.");
                }
            }
        });

        searchDrugPanel.add(searchDrugNameLabel);
        searchDrugPanel.add(searchDrugNameField);
        searchDrugPanel.add(new JLabel());
        searchDrugPanel.add(searchDrugButton);
        searchDrugPanel.add(new JScrollPane(searchResultArea));

        // View Purchase History Panel
        JPanel viewPurchaseHistoryPanel = new JPanel(new BorderLayout());
        JLabel purchaseHistoryDrugNameLabel = new JLabel("Drug Name:");
        JTextField purchaseHistoryDrugNameField = new JTextField();
        purchaseHistoryTextArea = new JTextArea(20, 50);
        purchaseHistoryTextArea.setEditable(false);
        JButton viewPurchaseHistoryButton = new JButton("View Purchase History");
        viewPurchaseHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String drugName = purchaseHistoryDrugNameField.getText();
                String history = pms.viewPurchaseHistory(drugName);
                purchaseHistoryTextArea.setText(history);
            }
        });

        JPanel inputPanel = new JPanel(new GridLayout(3, 1));
        inputPanel.add(purchaseHistoryDrugNameLabel);
        inputPanel.add(purchaseHistoryDrugNameField);
        inputPanel.add(viewPurchaseHistoryButton);

        viewPurchaseHistoryPanel.add(inputPanel, BorderLayout.NORTH);
        viewPurchaseHistoryPanel.add(new JScrollPane(purchaseHistoryTextArea), BorderLayout.CENTER);

        tabbedPane.addTab("Add Drug", addDrugPanel);
        tabbedPane.addTab("View All Drugs", viewDrugsPanel);
        tabbedPane.addTab("Search Drug", searchDrugPanel);
        tabbedPane.addTab("View Purchase History", viewPurchaseHistoryPanel);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 1) {  // "View All Drugs" tab is at index 1
                    updateDrugsTextArea();
                }
            }
        });

        add(tabbedPane);
    }

    private void updateDrugsTextArea() {
        drugsTextArea.setText(pms.viewAllDrugs());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PharmacyManagementUI().setVisible(true);
            }
        });
    }
}

