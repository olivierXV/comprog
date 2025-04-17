package Main;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

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

public class SecondStage extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel; 
    private JProgressBar progressBar;
    private int currentProgress = 0; 
    private int score = 0;


        public SecondStage() {
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
                int response = JOptionPane.showConfirmDialog(
                    null, "Are you sure you want to quit?",
                    "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE
                );
                if (response == JOptionPane.OK_OPTION) {
                    home main;
					try {
						main = new home();
						main.setVisible(true);
						dispose();
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
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
    
    private void switchToPanel1(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        updateProgress();
    }

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
                "<html><center><h1>『 Understanding HTML Structures 』</h1>",
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

        continueButton.addActionListener(e -> switchToPanel1(Explanation()));

        panel.add(continueButton, BorderLayout.SOUTH);
        applyHandCursor(panel);
        
        return panel;
    }

    private JPanel Explanation() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Center Panel (Text Content)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); 

        // Explanation Text
        JLabel welcomeText = new JLabel(
            "<html><div style='text-align: justify; width: 400px;'>"
            + "<b>HTML</b>, or <b>HyperText Markup Language</b>, is the standard language used to create web pages. "
            + "It structures content by using a series of elements, or <b>\"tags,\"</b> that tell the browser how to display the text, images, and other media on a webpage.<br><br>"
            + "<b>&lt;html&gt;</b>: This is the root element of an HTML document. All other elements are nested within this.<br><br>"
            + "<b>&lt;head&gt;</b>: This contains meta-information such as the title, character set, styles, and scripts. It is not displayed on the webpage but is essential for proper page rendering.<br><br>"
            + "<b>&lt;body&gt;</b>: This contains the main content of the webpage, including text, images, links, and other media.<br><br>"
            + "<b>Closing Tags:</b> Always close elements properly (e.g., <b>&lt;/html&gt;</b>) to maintain a structured document."
            + "</div></html>",
            SwingConstants.CENTER
        );
        welcomeText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add Explanation Text to Center Panel
        centerPanel.add(welcomeText);

        // Continue Button
        JButton continueButton = new JButton("CONTINUE");
        continueButton.setBackground(new Color(114, 46, 209));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        continueButton.setFocusPainted(false);
        continueButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        continueButton.addActionListener(e -> switchToPanel1(ExamplePanel()));

        // Add Components
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(continueButton, BorderLayout.SOUTH);
        
        applyHandCursor(panel);
        
        return panel;
    }


    private JPanel ExamplePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 250, 255));

        JLabel lblTitle = new JLabel("Here's how a complete HTML document might look:", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lblTitle, BorderLayout.NORTH);

        JTextArea txtExample = new JTextArea("<!DOCTYPE html>\r\n"
        		+ "<html>\r\n"
        		+ "<head>\r\n"
        		+ "    <meta charset=\"UTF-8\">\r\n"
        		+ "    <title>My First HTML Page</title>\r\n"
        		+ "    <link rel=\"stylesheet\" href=\"styles.css\">\r\n"
        		+ "</head>\r\n"
        		+ "<body>\r\n"
        		+ "    <h1>Welcome to My Website</h1>\r\n"
        		+ "    <p>This is my first paragraph.</p>\r\n"
        		+ "    <img src=\"image.jpg\" alt=\"A beautiful view\">\r\n"
        		+ "    <a href=\"https://example.com\">Visit Example</a>\r\n"
        		+ "</body>\r\n"
        		+ "</html>");
        txtExample.setFont(new Font("Monospaced", Font.PLAIN, 16));
        txtExample.setEditable(false);
        txtExample.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panel.add(txtExample, BorderLayout.CENTER);

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setBackground(new Color(114, 46, 209));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        continueButton.setFocusPainted(false);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        panel.add(continueButton, BorderLayout.SOUTH);

        continueButton.addActionListener(e -> switchToPanel1(HtmlQuizPanel1()));

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

    private JPanel HtmlQuizPanel1() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 250));

        JLabel questionLabel = new JLabel(
            "<html><center>What <b>HTML</b> stands for?</center></html>"
        );
        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Define borders
        RoundedBorder defaultBorder = new RoundedBorder(15, Color.LIGHT_GRAY, 2);
        RoundedBorder selectedBorder = new RoundedBorder(15, Color.BLUE, 2);
        RoundedBorder correctBorder = new RoundedBorder(15, Color.GREEN, 2);
        RoundedBorder incorrectBorder = new RoundedBorder(15, Color.RED, 2);

        // Define buttons
        JButton hTagButton = new JButton("<html><b> Hyperlink Markup Language </b></html>");
        JButton pTagButton = new JButton("<html><b> HyperText Markup Language </b></html>");
        JButton iTagButton = new JButton("<html><b> HyperTool Markup Language </b></html>");
        JButton hlTagButton = new JButton("<html><b> HyperText Machine Language </b></html>");

        JButton[] answerButtons = { hTagButton, pTagButton, iTagButton, hlTagButton };

        for (JButton button : answerButtons) {
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Monospaced", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorder(defaultBorder);
        }

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); // Vibrant purple
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); // Initially disabled

        // Hover effect
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(130, 56, 230)); // Slightly lighter purple
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(114, 46, 217)); // Default purple
            }
        });

        // State variables
        final boolean[] isCorrectAnswer = { false };
        final boolean[] answerRevealed = { false }; 
        final JButton[] selectedButton = { null };

        // Action listener for answer selection
        ActionListener selectionListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            selectedButton[0] = clickedButton;
            isCorrectAnswer[0] = (clickedButton == pTagButton);

            // Reset borders and highlight selection
            for (JButton button : answerButtons) {
                button.setBorder(defaultBorder);
            }
            clickedButton.setBorder(selectedBorder);

            continueButton.setEnabled(true); // Enable continue button
            answerRevealed[0] = false; // Reset answer reveal state
        };

        // Attach listener to all buttons
        for (JButton button : answerButtons) {
            button.addActionListener(selectionListener);
        }

        // Continue button action
        continueButton.addActionListener(e -> {
            if (!answerRevealed[0]) { 
                // First click reveals the answer
                if (selectedButton[0] != null) {
                    if (isCorrectAnswer[0]) {
                        selectedButton[0].setBorder(correctBorder);
                        score++;
                    } else {
                        selectedButton[0].setBorder(incorrectBorder);
                    }
                    answerRevealed[0] = true; // Mark as revealed
                    
                    hTagButton.setEnabled(false);
                    pTagButton.setEnabled(false);
                    iTagButton.setEnabled(false);
                    hlTagButton.setEnabled(false);
                }
            } else {
                // Second click proceeds to the next panel
                switchToPanel1(HtmlQuizPanel2());
            }
        });

        // Organize layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 248, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (JButton button : answerButtons) {
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

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

    
    
    private JPanel HtmlQuizPanel2() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 250));

        JLabel questionLabel = new JLabel(
        	    "<html><p>What is the root element in HTML?</p><br>" +
        	    "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the correct answer</span></html>"
        	);

        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Define borders
        RoundedBorder defaultBorder = new RoundedBorder(15, Color.LIGHT_GRAY, 2);
        RoundedBorder selectedBorder = new RoundedBorder(15, Color.BLUE, 2);
        RoundedBorder correctBorder = new RoundedBorder(15, Color.GREEN, 2);
        RoundedBorder incorrectBorder = new RoundedBorder(15, Color.RED, 2);

        // Define buttons
        JButton hTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;html&gt;</b></center</html>");
        JButton pTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;head&gt;</b></center</html>");
        JButton iTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;body&gt;</b></center</html>");
        JButton hlTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;div&gt;</b></center</html>");

        JButton[] answerButtons = { hTagButton, pTagButton, iTagButton, hlTagButton };

        for (JButton button : answerButtons) {
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Monospaced", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorder(defaultBorder);
        }

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); // Vibrant purple
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); // Initially disabled

        // Hover effect
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(130, 56, 230)); // Slightly lighter purple
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(114, 46, 217)); // Default purple
            }
        });

        // State variables
        final boolean[] isCorrectAnswer = { false };
        final boolean[] answerRevealed = { false }; 
        final JButton[] selectedButton = { null };

        // Action listener for answer selection
        ActionListener selectionListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            selectedButton[0] = clickedButton;
            isCorrectAnswer[0] = (clickedButton == hTagButton);

            // Reset borders and highlight selection
            for (JButton button : answerButtons) {
                button.setBorder(defaultBorder);
            }
            clickedButton.setBorder(selectedBorder);

            continueButton.setEnabled(true); // Enable continue button
            answerRevealed[0] = false; // Reset answer reveal state
        };

        // Attach listener to all buttons
        for (JButton button : answerButtons) {
            button.addActionListener(selectionListener);
        }

        // Continue button action
        continueButton.addActionListener(e -> {
            if (!answerRevealed[0]) { 
                // First click reveals the answer
                if (selectedButton[0] != null) {
                    if (isCorrectAnswer[0]) {
                        selectedButton[0].setBorder(correctBorder);
                        score++;
                    } else {
                        selectedButton[0].setBorder(incorrectBorder);
                    }
                    answerRevealed[0] = true; // Mark as revealed
                    
                    hTagButton.setEnabled(false);
                    pTagButton.setEnabled(false);
                    iTagButton.setEnabled(false);
                    hlTagButton.setEnabled(false);
                }
            } else {
                // Second click proceeds to the next panel
                switchToPanel1(HtmlQuizPanel3());
            }
        });

        // Organize layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 248, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (JButton button : answerButtons) {
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

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


    private JPanel HtmlQuizPanel3() {
    	JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 250));

        JLabel questionLabel = new JLabel(
        	    "<html><p>What tag is used to close a html document</p><br>" +
        	    "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the correct answer</span></html>"
        	);

        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Define borders
        RoundedBorder defaultBorder = new RoundedBorder(15, Color.LIGHT_GRAY, 2);
        RoundedBorder selectedBorder = new RoundedBorder(15, Color.BLUE, 2);
        RoundedBorder correctBorder = new RoundedBorder(15, Color.GREEN, 2);
        RoundedBorder incorrectBorder = new RoundedBorder(15, Color.RED, 2);

        // Define buttons
        JButton hTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;finish&gt;</b></center</html>");
        JButton pTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;close&gt;</b></center</html>");
        JButton iTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;/html&gt;</b></center</html>");
        JButton hlTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;end&gt;</b></center</html>");

        JButton[] answerButtons = { hTagButton, pTagButton, iTagButton, hlTagButton };

        for (JButton button : answerButtons) {
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Monospaced", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorder(defaultBorder);
        }

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); // Vibrant purple
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); // Initially disabled

        // Hover effect
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(130, 56, 230)); // Slightly lighter purple
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(114, 46, 217)); // Default purple
            }
        });

        // State variables
        final boolean[] isCorrectAnswer = { false };
        final boolean[] answerRevealed = { false }; 
        final JButton[] selectedButton = { null };

        // Action listener for answer selection
        ActionListener selectionListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            selectedButton[0] = clickedButton;
            isCorrectAnswer[0] = (clickedButton == iTagButton);

            // Reset borders and highlight selection
            for (JButton button : answerButtons) {
                button.setBorder(defaultBorder);
            }
            clickedButton.setBorder(selectedBorder);

            continueButton.setEnabled(true); // Enable continue button
            answerRevealed[0] = false; // Reset answer reveal state
        };

        // Attach listener to all buttons
        for (JButton button : answerButtons) {
            button.addActionListener(selectionListener);
        }

        // Continue button action
        continueButton.addActionListener(e -> {
            if (!answerRevealed[0]) { 
                // First click reveals the answer
                if (selectedButton[0] != null) {
                    if (isCorrectAnswer[0]) {
                        selectedButton[0].setBorder(correctBorder);
                        score++;
                    } else {
                        selectedButton[0].setBorder(incorrectBorder);
                    }
                    answerRevealed[0] = true; // Mark as revealed
                    
                    hTagButton.setEnabled(false);
                    pTagButton.setEnabled(false);
                    iTagButton.setEnabled(false);
                    hlTagButton.setEnabled(false);
                }
            } else {
                // Second click proceeds to the next panel
                switchToPanel1(HtmlQuizPanel4());
            }
        });

        // Organize layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 248, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (JButton button : answerButtons) {
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

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
        	    "<html><p>Which tag is used for inserting images?</p><br>" +
        	    "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the correct answer</span></html>"
        	);

        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Define borders
        RoundedBorder defaultBorder = new RoundedBorder(15, Color.LIGHT_GRAY, 2);
        RoundedBorder selectedBorder = new RoundedBorder(15, Color.BLUE, 2);
        RoundedBorder correctBorder = new RoundedBorder(15, Color.GREEN, 2);
        RoundedBorder incorrectBorder = new RoundedBorder(15, Color.RED, 2);

        // Define buttons
        JButton hTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;img&gt;</b></center</html>");
        JButton pTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;link&gt;</b></center</html>");
        JButton iTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;src&gt;</b></center</html>");
        JButton hlTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;meta&gt;</b></center</html>");

        JButton[] answerButtons = { hTagButton, pTagButton, iTagButton, hlTagButton };

        for (JButton button : answerButtons) {
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Monospaced", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorder(defaultBorder);
        }

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); // Vibrant purple
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); // Initially disabled

        // Hover effect
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(130, 56, 230)); // Slightly lighter purple
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(114, 46, 217)); // Default purple
            }
        });

        // State variables
        final boolean[] isCorrectAnswer = { false };
        final boolean[] answerRevealed = { false }; 
        final JButton[] selectedButton = { null };

        // Action listener for answer selection
        ActionListener selectionListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            selectedButton[0] = clickedButton;
            isCorrectAnswer[0] = (clickedButton == hTagButton);

            // Reset borders and highlight selection
            for (JButton button : answerButtons) {
                button.setBorder(defaultBorder);
            }
            clickedButton.setBorder(selectedBorder);

            continueButton.setEnabled(true); // Enable continue button
            answerRevealed[0] = false; // Reset answer reveal state
        };

        // Attach listener to all buttons
        for (JButton button : answerButtons) {
            button.addActionListener(selectionListener);
        }

        // Continue button action
        continueButton.addActionListener(e -> {
            if (!answerRevealed[0]) { 
                // First click reveals the answer
                if (selectedButton[0] != null) {
                    if (isCorrectAnswer[0]) {
                        selectedButton[0].setBorder(correctBorder);
                        score++;
                    } else {
                        selectedButton[0].setBorder(incorrectBorder);
                    }
                    answerRevealed[0] = true; // Mark as revealed
                    
                    hTagButton.setEnabled(false);
                    pTagButton.setEnabled(false);
                    iTagButton.setEnabled(false);
                    hlTagButton.setEnabled(false);
                }
            } else {
                // Second click proceeds to the next panel
                switchToPanel1(HtmlQuizPanel5());
            }
        });

        // Organize layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 248, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (JButton button : answerButtons) {
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

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
        	    "<html><p>What does <b style='font-size:12px;'>&lt;head&gt;</b> contain in an HTML document?</p><br>" +
        	    "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the correct answer</span></html>"
        	);

        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Define borders
        RoundedBorder defaultBorder = new RoundedBorder(15, Color.LIGHT_GRAY, 2);
        RoundedBorder selectedBorder = new RoundedBorder(15, Color.BLUE, 2);
        RoundedBorder correctBorder = new RoundedBorder(15, Color.GREEN, 2);
        RoundedBorder incorrectBorder = new RoundedBorder(15, Color.RED, 2);

        // Define buttons
        JButton hTagButton = new JButton("<html><b> Metadata </b></html>");
        JButton pTagButton = new JButton("<html><b> Main Content </b></html>");
        JButton iTagButton = new JButton("<html><b> Footer </b></html>");
        JButton hlTagButton = new JButton("<html><b> Hyperlinks </b></html>");

        JButton[] answerButtons = { hTagButton, pTagButton, iTagButton, hlTagButton };

        for (JButton button : answerButtons) {
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Monospaced", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorder(defaultBorder);
        }

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); // Vibrant purple
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); // Initially disabled

        // Hover effect
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(130, 56, 230)); // Slightly lighter purple
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(114, 46, 217)); // Default purple
            }
        });

        // State variables
        final boolean[] isCorrectAnswer = { false };
        final boolean[] answerRevealed = { false }; 
        final JButton[] selectedButton = { null };

        // Action listener for answer selection
        ActionListener selectionListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            selectedButton[0] = clickedButton;
            isCorrectAnswer[0] = (clickedButton == hTagButton);

            // Reset borders and highlight selection
            for (JButton button : answerButtons) {
                button.setBorder(defaultBorder);
            }
            clickedButton.setBorder(selectedBorder);

            continueButton.setEnabled(true); // Enable continue button
            answerRevealed[0] = false; // Reset answer reveal state
        };

        // Attach listener to all buttons
        for (JButton button : answerButtons) {
            button.addActionListener(selectionListener);
        }

        // Continue button action
        continueButton.addActionListener(e -> {
            if (!answerRevealed[0]) { 
                // First click reveals the answer
                if (selectedButton[0] != null) {
                    if (isCorrectAnswer[0]) {
                        selectedButton[0].setBorder(correctBorder);
                        score++;
                    } else {
                        selectedButton[0].setBorder(incorrectBorder);
                    }
                    answerRevealed[0] = true; // Mark as revealed
                    
                    hTagButton.setEnabled(false);
                    pTagButton.setEnabled(false);
                    iTagButton.setEnabled(false);
                    hlTagButton.setEnabled(false);
                }
            } else {
                // Second click proceeds to the next panel
                switchToPanel1(HtmlQuizPanel6());
            }
        });

        // Organize layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 248, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (JButton button : answerButtons) {
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

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

        JLabel questionLabel = new JLabel(
        	    "<html><p>Which tag contains the main content of the webpage?</p><br>" +
        	    "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the correct answer</span></html>"
        	);

        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Define borders
        RoundedBorder defaultBorder = new RoundedBorder(15, Color.LIGHT_GRAY, 2);
        RoundedBorder selectedBorder = new RoundedBorder(15, Color.BLUE, 2);
        RoundedBorder correctBorder = new RoundedBorder(15, Color.GREEN, 2);
        RoundedBorder incorrectBorder = new RoundedBorder(15, Color.RED, 2);

        // Define buttons
        JButton hTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;header&gt;</b></center</html>");
        JButton pTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;main&gt;</b></center</html>");
        JButton iTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;footer&gt;</b></center</html>");
        JButton hlTagButton = new JButton("<html><center><b style='font-size:12px;'>&lt;body&gt;</b></center</html>");

        JButton[] answerButtons = { hTagButton, pTagButton, iTagButton, hlTagButton };

        for (JButton button : answerButtons) {
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Monospaced", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorder(defaultBorder);
        }

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(114, 46, 217)); // Vibrant purple
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        continueButton.setFocusPainted(false);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setEnabled(false); // Initially disabled

        // Hover effect
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(130, 56, 230)); // Slightly lighter purple
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(114, 46, 217)); // Default purple
            }
        });

        // State variables
        final boolean[] isCorrectAnswer = { false };
        final boolean[] answerRevealed = { false }; 
        final JButton[] selectedButton = { null };

        // Action listener for answer selection
        ActionListener selectionListener = e -> {
            JButton clickedButton = (JButton) e.getSource();
            selectedButton[0] = clickedButton;
            isCorrectAnswer[0] = (clickedButton == hlTagButton);

            // Reset borders and highlight selection
            for (JButton button : answerButtons) {
                button.setBorder(defaultBorder);
            }
            clickedButton.setBorder(selectedBorder);

            continueButton.setEnabled(true); 
            answerRevealed[0] = false; 
        };
        // Attach listener to all buttons
        for (JButton button : answerButtons) {
            button.addActionListener(selectionListener);
        }

        // Continue button action
        continueButton.addActionListener(e -> {
            if (!answerRevealed[0]) { 
                // First click reveals the answer
                if (selectedButton[0] != null) {
                    if (isCorrectAnswer[0]) {
                        selectedButton[0].setBorder(correctBorder);
                        score++;
                    } else {
                        selectedButton[0].setBorder(incorrectBorder);
                    }
                    answerRevealed[0] = true; // Mark as revealed
                    
                    hTagButton.setEnabled(false);
                    pTagButton.setEnabled(false);
                    iTagButton.setEnabled(false);
                    hlTagButton.setEnabled(false);
                }
            } else {
                // Second click proceeds to the next panel
                switchToPanel1(HtmlQuizPanel8());
            }
        });

        // Organize layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 248, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (JButton button : answerButtons) {
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

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
    
    private JPanel HtmlQuizPanel8() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 250));

        // Question Label
        JLabel questionLabel = new JLabel(
            "<html> Recreate the <b>HTML</b> structures below" +
            "<span style='color:#5865F2; font-size:12px;'>ⓘ Tap the snippets below</span></html>"
        );
        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Optional padding around the separator

        // Create a panel to contain both the label and the separator
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); // Stack vertically
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
        browserPane.setContentType("text/html");  // This ensures the content is rendered as HTML
        browserPane.setEditable(false);  // Disable editing in the preview

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

            String regexPattern = "(?s)(<!DOCTYPE html>\\s*)?<html>\\s*<head>\\s*<title>.*?</title>\\s*</head>\\s*<body>\\s*</body>\\s*</html>";

            if (userAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter the complete default HTML structure!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!userAnswer.matches(regexPattern)) {
                JOptionPane.showMessageDialog(panel, "Incorrect HTML structure! Ensure you include <html>, <head>, <title>, <body>, and </body>.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Allow them to proceed
                switchToPanel1(finishPanel(score));
            }
        });



        // Add Components to Main Panel
        panel.add(headerPanel, BorderLayout.NORTH);  // Add the headerPanel containing both the question and the separator
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(continueButton, BorderLayout.PAGE_END);

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
        if (score == 6) {
            titleText = "🏆 100% - Perfect Lesson!";
            titleColor = new Color(34, 177, 76); // Green
        } else if (score >= 5) {
            titleText = "🌟 98% Great Job! Almost Perfect!";
            titleColor = new Color(0, 102, 204); // Blue
        } else if (score >= 3) {
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



    private void switchToPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SecondStage().setVisible(true));
    }
}
