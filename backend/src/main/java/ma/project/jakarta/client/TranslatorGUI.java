package ma.project.jakarta.client;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class TranslatorGUI extends JFrame {
    
    private static final String API_URL = "http://localhost:8080/darija-translator/api/translator/translate";
    
    // Couleurs modernes
    private static final Color PRIMARY_COLOR = new Color(79, 70, 229);
    private static final Color SECONDARY_COLOR = new Color(139, 92, 246);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color BG_COLOR = new Color(249, 250, 251);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton translateButton;
    private JButton clearButton;
    private JButton copyButton;
    private JButton swapButton;
    private JLabel statusLabel;
    private JLabel charCountLabel;
    private JComboBox<String> languageCombo;
    private boolean isEnglishToDarija = true;

    public TranslatorGUI() {
        setTitle("Traducteur Intelligent - English ↔ Darija");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        
        // Titre
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Traducteur Intelligent");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        
        // Options
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        optionsPanel.setOpaque(false);
        
        JLabel modeLabel = new JLabel("Mode:");
        modeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        optionsPanel.add(modeLabel);
        
        languageCombo = new JComboBox<>(new String[]{"English → Darija", "Darija → English"});
        languageCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        languageCombo.addActionListener(e -> updateLanguageMode());
        styleComboBox(languageCombo);
        optionsPanel.add(languageCombo);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(optionsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);
        
        JPanel inputPanel = createTextPanel(
            "Texte source", 
            true, 
            "Entrez votre texte ici...",
            PRIMARY_COLOR
        );
        
        JPanel outputPanel = createTextPanel(
            "Traduction", 
            false, 
            "La traduction apparaîtra ici...",
            SECONDARY_COLOR
        );
        
        centerPanel.add(inputPanel);
        centerPanel.add(outputPanel);
        
        return centerPanel;
    }

    private JPanel createTextPanel(String title, boolean isInput, String placeholder, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(229, 231, 235), 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(accentColor);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        if (isInput) {
            charCountLabel = new JLabel("0 caractères");
            charCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            charCountLabel.setForeground(Color.GRAY);
            headerPanel.add(charCountLabel, BorderLayout.EAST);
        }
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JTextArea textArea;
        if (isInput) {
            inputArea = new JTextArea();
            textArea = inputArea;
            
            inputArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void changedUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
                public void insertUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
            });
        } else {
            outputArea = new JTextArea();
            textArea = outputArea;
            textArea.setEditable(false);
        }
        
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setBackground(isInput ? Color.WHITE : new Color(249, 250, 251));
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        if (!isInput) {
            JPanel copyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            copyPanel.setOpaque(false);
            
            copyButton = createStyledButton("Copier", SECONDARY_COLOR);
            copyButton.setEnabled(false);
            copyButton.addActionListener(e -> copyToClipboard());
            copyPanel.add(copyButton);
            
            panel.add(copyPanel, BorderLayout.SOUTH);
        }
        
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setOpaque(false);
        
        // Boutons avec texte uniquement (pas d'emoji)
        translateButton = createStyledButton("Traduire", PRIMARY_COLOR);
        translateButton.setPreferredSize(new Dimension(180, 50));
        translateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        translateButton.addActionListener(e -> translateText());
        
        clearButton = createStyledButton("Effacer", new Color(107, 114, 128));
        clearButton.addActionListener(e -> clearAll());
        
        swapButton = createStyledButton("Inverser", new Color(59, 130, 246));
        swapButton.addActionListener(e -> swapLanguages());
        
        actionPanel.add(clearButton);
        actionPanel.add(translateButton);
        actionPanel.add(swapButton);
        
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        statusLabel = new JLabel("Prêt à traduire", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setForeground(new Color(107, 114, 128));
        
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        
        bottomPanel.add(actionPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);
        
        return bottomPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(140, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXT_COLOR);
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void updateCharCount() {
        int count = inputArea.getText().length();
        charCountLabel.setText(count + " caractères");
        if (count > 1000) {
            charCountLabel.setForeground(ERROR_COLOR);
        } else {
            charCountLabel.setForeground(Color.GRAY);
        }
    }

    private void updateLanguageMode() {
        isEnglishToDarija = languageCombo.getSelectedIndex() == 0;
        
        // Mettre à jour l'orientation du texte
        if (isEnglishToDarija) {
            inputArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            outputArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        } else {
            inputArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            outputArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
        
        statusLabel.setText("Mode changé: " + (isEnglishToDarija ? "English → Darija" : "Darija → English"));
        statusLabel.setForeground(PRIMARY_COLOR);
    }

    private void translateText() {
        String text = inputArea.getText().trim();
        
        if (text.isEmpty()) {
            showNotification("Veuillez entrer un texte à traduire", ERROR_COLOR);
            return;
        }
        
        setButtonsEnabled(false);
        statusLabel.setText("Traduction en cours...");
        statusLabel.setForeground(PRIMARY_COLOR);
        outputArea.setText("");
        copyButton.setEnabled(false);
        
        Timer loadingTimer = new Timer(500, null);
        final int[] dots = {0};
        loadingTimer.addActionListener(e -> {
            dots[0] = (dots[0] + 1) % 4;
            statusLabel.setText("Traduction en cours" + ".".repeat(dots[0]));
        });
        loadingTimer.start();
        
        new Thread(() -> {
            try {
                // ✅ MODIFICATION ICI - Prompts améliorés pour forcer l'écriture arabe
                String prompt;
                if (isEnglishToDarija) {
                    // Pour English → Darija : FORCER l'écriture en arabe
                    prompt = "Translate the following English text to Moroccan Darija. " +
                             "IMPORTANT: Write the translation ONLY in Arabic script (not Latin letters). " +
                             "Use Arabic letters like: ا، ب، ت، ث، etc. " +
                             "Text to translate: " + text;
                } else {
                    // Pour Darija → English
                    prompt = "Translate the following Moroccan Darija text (written in Arabic script) to English. " +
                             "Provide only the English translation: " + text;
                }
                
                JSONObject requestBody = new JSONObject();
                requestBody.put("text", prompt);
                
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                        .build();
                
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                loadingTimer.stop();
                
                if (response.statusCode() == 200) {
                    JSONObject responseJson = new JSONObject(response.body());
                    String translation = responseJson.getString("translation");
                    String mode = responseJson.optString("mode", "unknown");
                    
                    SwingUtilities.invokeLater(() -> {
                        outputArea.setText(translation);
                        copyButton.setEnabled(true);
                        statusLabel.setText("Traduction réussie (Mode: " + mode + ")");
                        statusLabel.setForeground(SUCCESS_COLOR);
                        setButtonsEnabled(true);
                        
                        flashButton(translateButton, SUCCESS_COLOR);
                    });
                } else {
                    throw new Exception("Erreur HTTP " + response.statusCode());
                }
                
            } catch (Exception e) {
                loadingTimer.stop();
                SwingUtilities.invokeLater(() -> {
                    showNotification("Erreur: " + e.getMessage(), ERROR_COLOR);
                    statusLabel.setText("Échec de la traduction");
                    statusLabel.setForeground(ERROR_COLOR);
                    setButtonsEnabled(true);
                });
            }
        }).start();
    }
    private void copyToClipboard() {
        String text = outputArea.getText();
        if (!text.isEmpty()) {
            StringSelection selection = new StringSelection(text);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            
            showNotification("Texte copié dans le presse-papier", SUCCESS_COLOR);
            flashButton(copyButton, SUCCESS_COLOR);
        }
    }

    private void clearAll() {
        inputArea.setText("");
        outputArea.setText("");
        statusLabel.setText("Prêt à traduire");
        statusLabel.setForeground(new Color(107, 114, 128));
        copyButton.setEnabled(false);
    }

    private void swapLanguages() {
        String temp = inputArea.getText();
        inputArea.setText(outputArea.getText());
        outputArea.setText(temp);
        
        int selectedIndex = languageCombo.getSelectedIndex();
        languageCombo.setSelectedIndex(selectedIndex == 0 ? 1 : 0);
        
        showNotification("Langues inversées", PRIMARY_COLOR);
    }

    private void setButtonsEnabled(boolean enabled) {
        translateButton.setEnabled(enabled);
        clearButton.setEnabled(enabled);
        swapButton.setEnabled(enabled);
    }

    private void showNotification(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private void flashButton(JButton button, Color color) {
        Color originalColor = button.getBackground();
        Timer timer = new Timer(100, null);
        final int[] count = {0};
        
        timer.addActionListener(e -> {
            if (count[0] % 2 == 0) {
                button.setBackground(color);
            } else {
                button.setBackground(originalColor);
            }
            count[0]++;
            if (count[0] >= 4) {
                ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(243, 244, 246),
                0, h, new Color(229, 231, 235)
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            TranslatorGUI gui = new TranslatorGUI();
            gui.setVisible(true);
        });
    }
}