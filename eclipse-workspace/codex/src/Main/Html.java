package Main;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Html extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel; 
    private JProgressBar progressBar;
    private int currentProgress = 0; 
    private int score = 0;

        public Html() {
        	loadProgress();
            setTitle("CODEX");
            setSize(816, 590);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            add(createTopPanel(), BorderLayout.NORTH);

            mainPanel = new JPanel(new BorderLayout());
            add(mainPanel, BorderLayout.CENTER);

            switchToPanel(introduction());

            applyHandCursor(introduction());
            applyHandCursor(HtmlQuizPanel1());
            applyHandCursor(HtmlQuizPanel2());
            applyHandCursor(HtmlQuizPanel3());
            applyHandCursor(HtmlQuizPanel4());
            applyHandCursor(HtmlQuizPanel5());
            applyHandCursor(HtmlQuizPanel6());
            applyHandCursor(finishPanel(score));
        }

        private JPanel createTopPanel() {
            JPanel topPanel = new JPanel(new BorderLayout());
            JPanel topBar = new JPanel(new BorderLayout());
            topBar.setBackground(new Color(245, 245, 255));

            JButton closeButton = new JButton("\u274C");
            closeButton.setBorderPainted(false);
            closeButton.setFocusable(false);
            closeButton.setContentAreaFilled(false);

            closeButton.addActionListener(e -> {
                Object[] options = {"OK", "Cancel"};
                
                JOptionPane pane = new JOptionPane(
                    "Are you sure you want to quit?",
                    JOptionPane.WARNING_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION,
                    null,
                    options,
                    options[1]
                );

                JDialog dialog = pane.createDialog("Confirm Exit");

                SwingUtilities.invokeLater(() -> {
                    for (Component c : pane.getComponents()) {
                        if (c instanceof JButton) {
                            ((JButton) c).setFocusable(false);
                        }
                    }
                });

                dialog.setVisible(true);

                Object selectedValue = pane.getValue();

                if (selectedValue != null && selectedValue.equals("OK")) {
                    try {
                        home main = new home(); 
                        main.setVisible(true);
                        dispose(); 
                    } catch (Exception e1) {
                        e1.printStackTrace(); 
                    }
                }
            });


            topBar.add(closeButton, BorderLayout.WEST);

            progressBar = new JProgressBar(0, 8);
            progressBar.setValue(currentProgress);
            progressBar.setStringPainted(false);
            progressBar.setForeground(new Color(50, 150, 255));
            progressBar.setPreferredSize(new Dimension(816, 10));

            topPanel.add(topBar, BorderLayout.NORTH);
            topPanel.add(progressBar, BorderLayout.SOUTH);

            return topPanel;
        }
        
        
    

    private void switchToPanel1(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        
        updateProgress();
    }

    
    private void updateProgress() {
        if (currentProgress < 10) {
            currentProgress++;
            progressBar.setValue(currentProgress);
            
            
            SwingUtilities.invokeLater(() -> {
                progressBar.repaint();
                progressBar.revalidate();
            });
        }
    }
    
    private void applyHandCursor(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                component.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else if (component instanceof Container) {
                applyHandCursor((Container) component); 
            }
        }
    }

    

    
    @SuppressWarnings("deprecation")
	private JPanel introduction() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(255, 255, 255));

        try {
            URL gifUrl = new URL("https://gifdb.com/images/high/walking-duck-animated-happy-waddle-swnffm726l0qmw5j.gif");
            ImageIcon gifIcon = new ImageIcon(gifUrl); 
            JLabel illustration = new JLabel(gifIcon);
            illustration.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(illustration);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        JLabel welcomeText = new JLabel(
                "<html><center><h1>『 Discovering HTML and Tags 』</h1>"
                + "Welcome! You're about to learn technologies like HTML, CSS, JavaScript, React, and everything else we'll need to build for the web.<br><br>Before you realize it, we'll be creating real-life projects. Let's start with <b>HTML.</b></center></html>",
                SwingConstants.CENTER
            );

        welcomeText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(welcomeText);

        panel.add(centerPanel, BorderLayout.CENTER);

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setBackground(new Color(114, 46, 209));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        continueButton.setFocusPainted(false);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        continueButton.addActionListener(e -> switchToPanel1(guessTheLetterPanel()));

        panel.add(continueButton, BorderLayout.SOUTH);
        applyHandCursor(panel);
        
        return panel;
    }


    private JPanel guessTheLetterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 250, 255));

        JLabel htmlText = new JLabel("<html>Hypertext Markup Language, or <b>HTML</b>, is the computer language that structures the web pages on the internet.<br><br>On top of HTML, you can build stunning web pages with buttons, images, and lots more.</html>");
        htmlText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        htmlText.setAlignmentX(Component.CENTER_ALIGNMENT);
        htmlText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel guessPanel = new JPanel();
        guessPanel.setBackground(new Color(200, 200, 230));
        guessPanel.setLayout(new BoxLayout(guessPanel, BoxLayout.Y_AXIS));
        guessPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel guessText = new JLabel("Guess the letter", SwingConstants.CENTER);
        guessText.setFont(new Font("SansSerif", Font.BOLD, 20));
        guessText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel wordLabel = new JLabel("_ O G", SwingConstants.CENTER);
        wordLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBackground(new Color(200, 200, 230));

        JButton letterQ = createLetterButton("Q");
        JButton letterR = createLetterButton("L");
        JButton letterD = createLetterButton("X");

        buttonPanel.add(letterQ);
        buttonPanel.add(letterR);
        buttonPanel.add(letterD);

        guessPanel.add(guessText);
        guessPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        guessPanel.add(wordLabel);
        guessPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        guessPanel.add(buttonPanel);

        contentPanel.add(htmlText);
        contentPanel.add(guessPanel);

        panel.add(contentPanel, BorderLayout.CENTER);

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setBackground(new Color(114, 46, 209));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        continueButton.setFocusPainted(false);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setEnabled(false);

        panel.add(continueButton, BorderLayout.SOUTH);

        letterQ.addActionListener(e -> JOptionPane.showMessageDialog(this, "Wrong choice! Try again."));
        letterR.addActionListener(e -> {
            wordLabel.setText("L O G");
            continueButton.setEnabled(true);
        });
        letterD.addActionListener(e -> JOptionPane.showMessageDialog(this, "Wrong choice! Try again."));

        continueButton.addActionListener(e -> switchToPanel1(HtmlQuizPanel1()));

        applyHandCursor(panel);
        
        return panel;
    }

    
    

    private JPanel HtmlQuizPanel1() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 250));

        // Question Label
        JLabel questionLabel = new JLabel(
            "<html>"
            + "The <b style='font-size:11px;'>&lt;button&gt;</b> tag creates a clickable button labeled <b>\\\"Like\\\"</b> on a webpage, with <b style='font-size:12px;'>&lt;button&gt;</b> being the opening tag, 'Like' being the text on the button,"
            + " and <b style='font-size:11px;'>&lt;/button&gt;</b> being the closing tag.<br>"
            + "By adding the HTML code <span style='font-family:monospace'>&lt;button&gt;Like&lt;/button&gt;</span>, " +
            "you can create a button with the label 'Like'.<br><br>" +
            "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the snippets below</span></html>"
        );
        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 

        // Create a panel to contain both the label and the separator
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); 
        headerPanel.add(questionLabel);
        headerPanel.add(separator);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Answer Box (HTML Code Editor)
        JTextArea answerBox = new JTextArea();
        answerBox.setFont(new Font("Courier New", Font.PLAIN, 14));
        answerBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        answerBox.setLineWrap(true);
        answerBox.setWrapStyleWord(true);
        JScrollPane answerScrollPane = new JScrollPane(answerBox);
        answerScrollPane.setPreferredSize(new Dimension(350, 100));

        // "index.html" Tab
        JPanel indexHtmlPanel = new JPanel(new BorderLayout());
        indexHtmlPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        indexHtmlPanel.add(answerScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("index.html", indexHtmlPanel);

        // "Browser" Tab (Live Preview)
        JEditorPane browserPane = new JEditorPane();
        browserPane.setContentType("text/html");  
        browserPane.setEditable(false);  

        JPanel browserPanel = new JPanel(new BorderLayout());
        browserPanel.add(new JScrollPane(browserPane), BorderLayout.CENTER);
        tabbedPane.addTab("Browser", browserPanel);

        // HTML Snippet Button
        JButton htmlButton = new JButton("<html>&lt;button&gt;Like&lt;/button&gt;</html>");
        htmlButton.setFont(new Font("Courier New", Font.BOLD, 14));
        htmlButton.setBackground(Color.WHITE);
        htmlButton.setFocusPainted(false);
        htmlButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(htmlButton);

        // Continue Button
        JButton continueButton = new JButton("CONTINUE");
        continueButton.setBackground(new Color(114, 46, 209));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        continueButton.setFocusPainted(false);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setEnabled(false);

        // Flag to track browser state
        final boolean[] browserOpened = {false};

        // Button Click to Auto-Fill Answer
        htmlButton.addActionListener(e -> {
            answerBox.setText("<button>Like</button>");
            continueButton.setEnabled(true);
        });

        // Answer Box Click Reset
        answerBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!answerBox.getText().isEmpty()) {
                    continueButton.setEnabled(false);
                }
            }
        });

        // Enable Continue Button when Text is Typed
        answerBox.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                continueButton.setEnabled(!answerBox.getText().trim().isEmpty());
            }
        });

        // Handle Continue Button Click
        continueButton.addActionListener(e -> {
            String userAnswer = answerBox.getText().trim();

            if (userAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in the blank!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!userAnswer.equals("<button>Like</button>")) {
                JOptionPane.showMessageDialog(panel, "Your answer is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (!browserOpened[0]) {
                    // Show preview in browser tab with style
                    String htmlContent = "<html><body>" +
                        "<button style='font-size:18px; padding:10px 20px; background-color:#4CAF50; color:white; border:none; border-radius:5px;'>Like</button>" +
                        "</body></html>";
                    browserPane.setText(htmlContent);  
                    tabbedPane.setSelectedIndex(1); 

                    // Set flag to true indicating the browser is opened
                    browserOpened[0] = true;
                } else {
                    // Switch to HtmlQuizPanel2 after opening browser
                    switchToPanel1(HtmlQuizPanel2());
                }
            }
        });

        // Add Components to Main Panel
        panel.add(headerPanel, BorderLayout.NORTH);  
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(continueButton, BorderLayout.PAGE_END);

        applyHandCursor(panel);
        
        return panel;
    }


    
    
    
    private JPanel HtmlQuizPanel2() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 250));

        // Question Label
        JLabel questionLabel = new JLabel(
            "<html>The text between the opening and closing tags of a button becomes the button's label, " +
            "you can create a button with the label 'Like'.<br><br>" +
            "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the snippets below</span><br><br>" +
            "</html>"
        );
        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Answer Box (TextField for user input with "<button>" as default)
        JPanel answerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));  
        answerPanel.setBackground(new Color(245, 248, 250));
        
        // Static part of the button tag (start tag)
        JLabel startButtonTag = new JLabel("<html>&lt;<font size='5'><b>button</b></font>&gt;</html>");
        startButtonTag.setFont(new Font("Courier New", Font.PLAIN, 20)); 

        // Text Field for user to input the button label (empty initially)
        JTextField answerBox = new JTextField();
        answerBox.setFont(new Font("Courier New", Font.PLAIN, 20)); 
        answerBox.setPreferredSize(new Dimension(180, 40));  
        answerBox.setEditable(true);  

        // Static part of the button tag (end tag)
        JLabel endButtonTag = new JLabel("<html>&lt;/<font size='5'><b>button</b></font>&gt;</html>");
        endButtonTag.setFont(new Font("Courier New", Font.PLAIN, 20)); 

        // Add the static parts and the text field together
        answerPanel.add(startButtonTag);
        answerPanel.add(answerBox);
        answerPanel.add(endButtonTag);

        // Continue Button
        JButton continueButton = new JButton("CONTINUE");
        continueButton.setBackground(new Color(114, 46, 209));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        continueButton.setFocusPainted(false);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setEnabled(false);  

        answerBox.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                String userAnswer = answerBox.getText().trim();
                continueButton.setEnabled(userAnswer.equals("Like"));
            }
        });

        continueButton.addActionListener(e -> {
            String userAnswer = answerBox.getText().trim();

            if (userAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please type the correct text in the field!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!userAnswer.equals("Like")) {
                JOptionPane.showMessageDialog(panel, "Your answer is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                switchToPanel1(HtmlQuizPanel3());
            }
        });

        // Layout setup
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 248, 250));
        topPanel.add(questionLabel, BorderLayout.NORTH);
        topPanel.add(separator, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 248, 250));
        centerPanel.add(answerPanel, BorderLayout.CENTER);
        centerPanel.add(continueButton, BorderLayout.SOUTH);

        // Add Components to Main Panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        applyHandCursor(panel);
        
        return panel;
    }
    
    
    




    class RoundedBorder extends AbstractBorder {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int radius;
        private Color color;
        private int thickness;

        RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = this.radius + 1;
            return insets;
        }
    }

    private JPanel HtmlQuizPanel3() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 250));

        JLabel questionLabel = new JLabel(
            "<html>Time to test what you've learned so far!<br>" +
            "What type of tag is this? <span style='font-family:monospace; font-size:14px;'>&lt;/button&gt;</span><br><br>" +
            "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the correct answer</span><br><br>" +
            "</html>"
        );
        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        RoundedBorder defaultBorder = new RoundedBorder(15, Color.LIGHT_GRAY, 2);
        RoundedBorder selectedBorder = new RoundedBorder(15, Color.BLUE, 2);
        RoundedBorder correctBorder = new RoundedBorder(15, Color.GREEN, 2);
        RoundedBorder incorrectBorder = new RoundedBorder(15, Color.RED, 2);

        JButton openingTagButton = new JButton("<html><b> An Opening Tag </b></html>");
        openingTagButton.setBackground(Color.WHITE);
        openingTagButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        openingTagButton.setFocusPainted(false);
        openingTagButton.setBorder(defaultBorder);

        JButton closingTagButton = new JButton("<html><b> A Closing Tag </b></html>");
        closingTagButton.setBackground(Color.WHITE);
        closingTagButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        closingTagButton.setFocusPainted(false);
        closingTagButton.setBorder(defaultBorder);

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); 
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); 

        // Hover effect
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(130, 56, 230)); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(114, 46, 217)); 
            }
        });

        // State variables
        final boolean[] isClosingTagSelected = {false};
        final boolean[] answerRevealed = {false}; 
        final JButton[] selectedButton = {null}; 

        // Action listener for answer selection
        ActionListener selectionListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            selectedButton[0] = clickedButton;
            isClosingTagSelected[0] = (clickedButton == closingTagButton);

            // Reset borders and highlight selection
            openingTagButton.setBorder(defaultBorder);
            closingTagButton.setBorder(defaultBorder);
            clickedButton.setBorder(selectedBorder);

            continueButton.setEnabled(true); 
            answerRevealed[0] = false; 
        };

        openingTagButton.addActionListener(selectionListener);
        closingTagButton.addActionListener(selectionListener);

        // Continue button action
        continueButton.addActionListener(e -> {
            if (!answerRevealed[0]) { 
                // First click reveals the answer
                if (selectedButton[0] != null) {
                    if (isClosingTagSelected[0]) {
                        selectedButton[0].setBorder(correctBorder);
                        score++;
                    } else {
                        selectedButton[0].setBorder(incorrectBorder);
                    }
                    answerRevealed[0] = true; 
                    
                    openingTagButton.setEnabled(false);
                    closingTagButton.setEnabled(false);
                }
            } else {
                // Second click proceeds to the next panel
                  switchToPanel1(HtmlQuizPanel4());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 248, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.add(openingTagButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(closingTagButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 248, 250));
        topPanel.add(questionLabel, BorderLayout.NORTH);
        topPanel.add(separator, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(continueButton, BorderLayout.SOUTH);

        applyHandCursor(panel);
        
        return panel;
    }
    
    
    private JPanel HtmlQuizPanel4() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 250));

        JLabel questionLabel = new JLabel(
        	    "<html><p>Which of these tags is spelled out incorrectly?</p><br>" +
        	    "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the correct answer</span></html>"
        	);

        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        RoundedBorder defaultBorder = new RoundedBorder(15, Color.LIGHT_GRAY, 2);
        RoundedBorder selectedBorder = new RoundedBorder(15, Color.BLUE, 2);
        RoundedBorder correctBorder = new RoundedBorder(15, Color.GREEN, 2);
        RoundedBorder incorrectBorder = new RoundedBorder(15, Color.RED, 2);

        JButton firstAnswerButton = new JButton("<html><center><b style='font-size:12px;'>&lt;button&gt;</b></center</html>");
        firstAnswerButton.setBackground(Color.WHITE);
        firstAnswerButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        firstAnswerButton.setFocusPainted(false);
        firstAnswerButton.setBorder(defaultBorder);

        JButton secondAnswerButton = new JButton("<html><center><b style='font-size:12px;'>&lt;/button&gt;</b></center></html>");
        secondAnswerButton.setBackground(Color.WHITE);
        secondAnswerButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        secondAnswerButton.setFocusPainted(false);
        secondAnswerButton.setBorder(defaultBorder);

        JButton thirdAnswerButton = new JButton("<html><center><b style='font-size:12px;'>&lt;button//&gt;</b></center></html>");
        thirdAnswerButton.setBackground(Color.WHITE);
        thirdAnswerButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        thirdAnswerButton.setFocusPainted(false);
        thirdAnswerButton.setBorder(defaultBorder);

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); 
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); 

        // Hover effect
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(130, 56, 230)); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(114, 46, 217)); 
            }
        });
        
        final boolean[] isClosingTagSelected = {false};
        final boolean[] answerRevealed = {false}; 
        final JButton[] selectedButton = {null}; 

        // Action listener for answer selection
        ActionListener selectionListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            selectedButton[0] = clickedButton;
            isClosingTagSelected[0] = (clickedButton == thirdAnswerButton);

            // Reset borders and highlight selection
            firstAnswerButton.setBorder(defaultBorder);
            secondAnswerButton.setBorder(defaultBorder);
            thirdAnswerButton.setBorder(defaultBorder);
            clickedButton.setBorder(selectedBorder);

            continueButton.setEnabled(true); 
            answerRevealed[0] = false; 
        };

        firstAnswerButton.addActionListener(selectionListener);
        secondAnswerButton.addActionListener(selectionListener);
        thirdAnswerButton.addActionListener(selectionListener);

        // Continue button action
        continueButton.addActionListener(e -> {
            if (!answerRevealed[0]) { 
                // First click reveals the answer
                if (selectedButton[0] != null) {
                    if (isClosingTagSelected[0]) {
                        selectedButton[0].setBorder(correctBorder);
                        score++;
                    } else {
                        selectedButton[0].setBorder(incorrectBorder);
                    }
                    answerRevealed[0] = true; 
                    
                    firstAnswerButton.setEnabled(false);
                    secondAnswerButton.setEnabled(false);
                    thirdAnswerButton.setEnabled(false);
                }
            } else {
                // Second click proceeds to the next panel
                  switchToPanel1(HtmlQuizPanel5());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 248, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.add(firstAnswerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(secondAnswerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(thirdAnswerButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 248, 250));
        topPanel.add(questionLabel, BorderLayout.NORTH);
        topPanel.add(separator, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(continueButton, BorderLayout.SOUTH);

        applyHandCursor(panel);
        
        return panel;
    }


    private JPanel HtmlQuizPanel5() {
    	 JPanel panel = new JPanel(new BorderLayout());
         panel.setBackground(new Color(245, 248, 250));

        JLabel questionLabel = new JLabel(
                "<html>" +
                "What type of tag is this? <span style='font-family:monospace; font-size:14px;'>&lt;button&gt;</span><br><br>" +
                "<span style='color:#5865F2; font-size:10px;'>ⓘ Tap the correct answer</span><br><br>" +
                "</html>"
            );
            questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBounds(20, 60, 300, 10);
        
        RoundedBorder defaultBorder = new RoundedBorder(15, Color.LIGHT_GRAY, 2);
        RoundedBorder selectedBorder = new RoundedBorder(15, Color.BLUE, 2);
        RoundedBorder correctBorder = new RoundedBorder(15, Color.GREEN, 2);
        RoundedBorder incorrectBorder = new RoundedBorder(15, Color.RED, 2);

        JButton openingTagButton = new JButton("<html><b> An Opening Tag </b></html>");
        openingTagButton.setBackground(Color.WHITE);
        openingTagButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        openingTagButton.setFocusPainted(false);
        openingTagButton.setBorder(defaultBorder);

        JButton closingTagButton = new JButton("<html><b> A Closing Tag </b></html>");
        closingTagButton.setBackground(Color.WHITE);
        closingTagButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        closingTagButton.setFocusPainted(false);
        closingTagButton.setBorder(defaultBorder);

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); 
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); 

        // Hover effect
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(130, 56, 230)); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(114, 46, 217)); 
            }
        });

        // State variables
        final boolean[] isClosingTagSelected = {false};
        final boolean[] answerRevealed = {false}; 
        final JButton[] selectedButton = {null}; 

        ActionListener selectionListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            selectedButton[0] = clickedButton;
            isClosingTagSelected[0] = (clickedButton == openingTagButton);

            openingTagButton.setBorder(defaultBorder);
            closingTagButton.setBorder(defaultBorder);
            clickedButton.setBorder(selectedBorder);

            continueButton.setEnabled(true); 
            answerRevealed[0] = false; 
        };

        openingTagButton.addActionListener(selectionListener);
        closingTagButton.addActionListener(selectionListener);

        continueButton.addActionListener(e -> {
            if (!answerRevealed[0]) { 
                if (selectedButton[0] != null) {
                    if (isClosingTagSelected[0]) {
                        selectedButton[0].setBorder(correctBorder);
                        score++;
                    } else {
                        selectedButton[0].setBorder(incorrectBorder);
                    }
                    answerRevealed[0] = true; 
                    
                    openingTagButton.setEnabled(false);
                    closingTagButton.setEnabled(false);
                }
            } else {
                  switchToPanel1(HtmlQuizPanel6());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 248, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.add(openingTagButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(closingTagButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 248, 250));
        topPanel.add(questionLabel, BorderLayout.NORTH);
        topPanel.add(separator, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(continueButton, BorderLayout.SOUTH);
        
        applyHandCursor(panel);
        
        return panel;
    }
    
    
    
    private JPanel HtmlQuizPanel6() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 250));
        JButton continueButton = new JButton("CONTINUE");

        JLabel questionLabel = new JLabel(
            "<html>Arrange the HTML code below to create a button with the label 'Like'.<br><br>"
            + "<span style='color:#5865F2; font-size:10px;'>ⓘ Tap the snippets below in order</span></html>"
        );
        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JTextField answerBox = new JTextField();
        answerBox.setFont(new Font("Courier New", Font.BOLD, 16));
        answerBox.setBackground(new Color(240, 240, 240));
        answerBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        answerBox.setEditable(false);
        answerBox.setPreferredSize(new Dimension(200, 30));

        JLabel fileLabel = new JLabel("index.html");
        fileLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        fileLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JPanel answerPanel = new JPanel(new BorderLayout());
        answerPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        answerPanel.add(fileLabel, BorderLayout.NORTH);
        answerPanel.add(answerBox, BorderLayout.CENTER);

        JButton closeTagButton = new JButton("</button>");
        JButton openTagButton = new JButton("<button>");
        JButton textButton = new JButton("Like");

        List<JButton> buttons = Arrays.asList(closeTagButton, openTagButton, textButton);
        for (JButton button : buttons) {
            button.setFont(new Font("Courier New", Font.BOLD, 14));
            button.setBackground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.addActionListener(e -> {
                answerBox.setText(answerBox.getText() + button.getText());
                button.setEnabled(false);
                if (answerBox.getText().trim().length() > 0) {
                    continueButton.setEnabled(true);
                }
            });
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(closeTagButton);
        buttonPanel.add(openTagButton);
        buttonPanel.add(textButton);

        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); 
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); 

        answerBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                answerBox.setText("");
                answerBox.setBackground(new Color(240, 240, 240));
                buttons.forEach(b -> b.setEnabled(true));
                continueButton.setEnabled(false);
            }
        });

        final boolean[] answerChecked = {false};

        continueButton.addActionListener(e -> {
            if (!answerChecked[0]) {
                String userAnswer = answerBox.getText().trim();
                if (userAnswer.equals("<button>Like</button>")) {
                    answerBox.setBackground(new Color(144, 238, 144)); 
                    answerChecked[0] = true;
                    score++;
                    continueButton.setText("Continue");
                } else {
                    answerBox.setBackground(new Color(255, 182, 193)); 
                }
            } else {
                switchToPanel1(finishPanel(score)); 
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(continueButton, BorderLayout.SOUTH);

        panel.add(questionLabel, BorderLayout.NORTH);
        panel.add(answerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        applyHandCursor(panel);
        
        return panel;
    }



    private JPanel finishPanel(int score) {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalStrut(10));

        // Determine feedback message, color, and icon based on the score
        String titleText;
        Color titleColor;
        if (score == 4) {
            titleText = "🏆 100% - Perfect Lesson!";
            titleColor = new Color(34, 177, 76); // Green
        } else if (score >= 3) {
            titleText = "🌟 98% Great Job! Almost Perfect!";
            titleColor = new Color(0, 102, 204); // Blue
        } else if (score >= 2) {
            titleText = "👍 95% Well Done! Keep Going!";
            titleColor = new Color(255, 140, 0); // Orange
        } else {
            titleText = "💡 88% Good Effort! Try Again for a Higher Score!";
            titleColor = new Color(200, 0, 0); // Red
        }

        JLabel welcomeText = new JLabel("<html><center><h1><b>" + titleText + "</b></h1></center></html>", SwingConstants.CENTER);
        welcomeText.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcomeText.setForeground(titleColor);
        welcomeText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(welcomeText);

        panel.add(centerPanel, BorderLayout.CENTER);

        JButton continueButton = new JButton("Continue");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217));
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Make the button fill the width of the panel
        continueButton.setPreferredSize(new Dimension(panel.getWidth(), 50));

        continueButton.addActionListener(e -> {
            try {
                home mainHome = new home();
                mainHome.unlockNextLesson();
                mainHome.setVisible(true);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }

            Window window = SwingUtilities.getWindowAncestor(panel);
            if (window != null) {
                window.dispose();
            }
        });

        // Add directly to SOUTH of the panel
        panel.add(continueButton, BorderLayout.SOUTH);

        applyHandCursor(panel);

        return panel;
    }

    
    private void loadProgress() {
        try (BufferedReader reader = new BufferedReader(new FileReader("progress.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                currentProgress = Integer.parseInt(line);
            }
            String scoreLine = reader.readLine();
            if (scoreLine != null) {
                score = Integer.parseInt(scoreLine);
            }
        } catch (IOException e) {
            currentProgress = 0;
            score = 0; 
        }
    }

    private JButton createLetterButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(60, 60));
        return button;
    }

    private void switchToPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Html().setVisible(true));
    }
}
