
package Main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class home extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private JPanel fixedTopPanel, bottomNav, slidingPanel;
    private JButton exitButton;
    private Timer slideUpTimer, slideDownTimer;
    private int panelY;
    private boolean isPanelVisible = false;
    private static int currentProgress = 0; 
    Attributes attributes = new Attributes();
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new home().setVisible(true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    public home() throws MalformedURLException {
        setTitle("Codex");
        setSize(816, 590);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        JPanel homePanel = HomePanel();
        JPanel pythonAIPanel = PythonAIPanel(); 
        JPanel libraryPanel = createLibraryPanel();

        mainContentPanel.add(homePanel, "Home");
        mainContentPanel.add(pythonAIPanel, "PythonAI"); 
        mainContentPanel.add(libraryPanel, "Library");

        getContentPane().add(mainContentPanel, BorderLayout.CENTER);

        bottomNav = BottomNav();
        bottomNav.setPreferredSize(new Dimension(getWidth(), 50));
        getContentPane().add(bottomNav, BorderLayout.SOUTH);

        slidingPanel = new JPanel();
        slidingPanel.setBounds(0, 590, 816, 590);
        slidingPanel.setBackground(Color.WHITE);
        slidingPanel.setLayout(null);
        slidingPanel.setVisible(false);
        mainContentPanel.add(slidingPanel);

        getLayeredPane().add(slidingPanel, JLayeredPane.POPUP_LAYER);

        attributes.applyRoundedBorder(mainContentPanel);
        attributes.applyHandCursor(homePanel);
        attributes.applyHandCursor(BurgerPanel());

        setupSlidingPanel();

        setVisible(true);
    }

    public void unlockNextLesson() {
        if (currentProgress < 9) { 
            currentProgress++; 
            System.out.println("Unlocked lesson: " + currentProgress);
        }
        refreshHomePanel(); // Refresh UI
    }


    private JPanel HomePanel() throws MalformedURLException {
        JPanel fixedTopPanel = new JPanel();
        fixedTopPanel.setLayout(new BoxLayout(fixedTopPanel, BoxLayout.Y_AXIS));
        fixedTopPanel.setPreferredSize(new Dimension(800, 112));
        fixedTopPanel.setOpaque(false);

        JPanel topBar = createTopBar();
        JPanel burgerPanel = BurgerPanel();

        fixedTopPanel.add(topBar);
        fixedTopPanel.add(new JSeparator());
        fixedTopPanel.add(burgerPanel);

        getContentPane().add(fixedTopPanel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        // First Course Panel
        JPanel coursePanel = attributes.createStatLabelWithBorder("Intro to Web Development");
        coursePanel.setPreferredSize(new Dimension(700, 80));
        coursePanel.setMaximumSize(new Dimension(700, 80));
        coursePanel.setBackground(new Color(240, 240, 240)); // Default color
        coursePanel.setOpaque(true);

        mainContent.add(Box.createVerticalStrut(10));
        mainContent.add(coursePanel);
        mainContent.add(Box.createVerticalStrut(10));

        String[][] lessons = {
            {"01", "Discovering HTML and Tags"},
            {"02", "Understanding HTML Structure"},
            {"03", "Adding Text & Headings"},
            {"04", "Working with Links"},
            {"05", "Lists in HTML"},
            {"06", "Forms and Input Elements"},
            {"07", "HTML Tables"},
            {"08", "Semantic HTML"},
            {"09", "Multimedia Elements"},
            {"10", "Introduction to CSS"}
        };

        for (int i = 0; i < lessons.length; i++) {
            boolean isUnlocked = (i <= currentProgress); 

            String lessonTitle = isUnlocked ? lessons[i][1] : "Locked Lesson";
            String iconUrl = isUnlocked ?
                "/html.png" :
                "/lock.png";

            JPanel buttonPanel = attributes.createStyledButton(Integer.parseInt(lessons[i][0]), lessonTitle, iconUrl);
            buttonPanel.setName("lesson_" + lessons[i][0]);
            buttonPanel.setBackground(new Color(240, 240, 240)); 
            buttonPanel.setOpaque(true);

            buttonPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    buttonPanel.setBackground(new Color(200, 170, 255)); 
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    buttonPanel.setBackground(new Color(240, 240, 240)); 
                }
            });

            for (Component comp : buttonPanel.getComponents()) {
                if (comp instanceof JButton button) {
                    button.setEnabled(isUnlocked);
                    final int lessonIndex = i + 1;

                    for (ActionListener al : button.getActionListeners()) {
                        button.removeActionListener(al);
                    }
                    button.addActionListener(e -> openLesson(lessonIndex));
                }
            }
            mainContent.add(buttonPanel);
            mainContent.add(Box.createVerticalStrut(10));

            if (i == 9) { 
                JPanel coursePanel2 = attributes.createStatLabelWithBorder(""); 
                coursePanel2.setPreferredSize(new Dimension(700, 80));
                coursePanel2.setMaximumSize(new Dimension(700, 80));
                coursePanel2.setBackground(new Color(240, 240, 240)); 
                coursePanel2.setOpaque(true);
                coursePanel2.setCursor(new Cursor(Cursor.HAND_CURSOR));

                coursePanel2.setLayout(new BoxLayout(coursePanel2, BoxLayout.X_AXIS));

                JLabel textLabel = new JLabel("Next Section");
                textLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); 

                try {
                    ImageIcon originalIcon = new ImageIcon(getClass().getResource("/rightArrow.png"));
                    Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); 
                    JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

                    coursePanel2.add(Box.createHorizontalGlue()); 
                    coursePanel2.add(textLabel);
                    coursePanel2.add(Box.createRigidArea(new Dimension(8, 0))); 
                    coursePanel2.add(iconLabel);
                    coursePanel2.add(Box.createHorizontalGlue()); 

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error loading rightArrow.png");
                }

                coursePanel2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        coursePanel2.setBackground(new Color(200, 170, 255)); 
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        coursePanel2.setBackground(new Color(240, 240, 240)); 
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // openNextSection();  
                    }
                });

                mainContent.add(coursePanel2);
                mainContent.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        attributes.applyHandCursor(mainPanel);
        return mainPanel;
    }
    
    private void openLesson(int lessonNumber) {
        System.out.println("Opening lesson: " + lessonNumber);
        
        JFrame newStage = switch (lessonNumber) {
            case 1 -> new Html(); 
            case 2 -> new SecondStage();
            case 3 -> new ThirdStage();
            default -> null;
        };

        if (newStage != null) {
            newStage.setVisible(true);
            this.setVisible(false); 
        }
    }



    
    private JPanel PythonAIPanel() throws MalformedURLException {
        JPanel fixedTopPanel = new JPanel();
        fixedTopPanel.setLayout(new BoxLayout(fixedTopPanel, BoxLayout.Y_AXIS));
        fixedTopPanel.setPreferredSize(new Dimension(800, 112));
        fixedTopPanel.setOpaque(false);

        JPanel topBar = createTopBar();
        JPanel burgerPanel = BurgerPanel();

        fixedTopPanel.add(topBar);
        fixedTopPanel.add(new JSeparator());
        fixedTopPanel.add(burgerPanel);

        getContentPane().add(fixedTopPanel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        // First Course Panel
        JPanel coursePanel = attributes.createStatLabelWithBorder("Intro to Python");
        coursePanel.setPreferredSize(new Dimension(700, 80));
        coursePanel.setMaximumSize(new Dimension(700, 80));
        coursePanel.setBackground(new Color(240, 240, 240)); 
        coursePanel.setOpaque(true);

        mainContent.add(Box.createVerticalStrut(10));
        mainContent.add(coursePanel);
        mainContent.add(Box.createVerticalStrut(10));

        String[][] lessons = {
        		{"01", "Getting Started with Python"},
                {"02", "Python Variables & Data Types"},
                {"03", "Control Flow & Loops"},
                {"04", "Functions & Modules"},
                {"05", "Object-Oriented Programming"},
                {"06", "Working with Libraries"},
                {"07", "Data Science Basics"},
                {"08", "Machine Learning Fundamentals"},
                {"09", "Deep Learning Overview"},
                {"10", "Building AI Projects"}
        };

        for (int i = 0; i < lessons.length; i++) {
            boolean isUnlocked = (i <= currentProgress); 

            String lessonTitle = isUnlocked ? lessons[i][1] : "Locked Lesson";
            String iconUrl = isUnlocked ?
                "/py.png" :
                "/lock.png";

            JPanel buttonPanel = attributes.createStyledButton(Integer.parseInt(lessons[i][0]), lessonTitle, iconUrl);
            buttonPanel.setName("lesson_" + lessons[i][0]);
            buttonPanel.setBackground(new Color(240, 240, 240)); 
            buttonPanel.setOpaque(true);

            buttonPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    buttonPanel.setBackground(new Color(200, 170, 255)); 
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    buttonPanel.setBackground(new Color(240, 240, 240)); 
                }
            });

            for (Component comp : buttonPanel.getComponents()) {
                if (comp instanceof JButton button) {
                    button.setEnabled(isUnlocked);
                    final int lessonIndex = i + 1;

                    for (ActionListener al : button.getActionListeners()) {
                        button.removeActionListener(al);
                    }
                    button.addActionListener(e -> openLesson2(lessonIndex));

                }
            }
            mainContent.add(buttonPanel);
            mainContent.add(Box.createVerticalStrut(10));

            if (i == 9) { 
                JPanel coursePanel2 = attributes.createStatLabelWithBorder(""); 
                coursePanel2.setPreferredSize(new Dimension(700, 80));
                coursePanel2.setMaximumSize(new Dimension(700, 80));
                coursePanel2.setBackground(new Color(240, 240, 240)); 
                coursePanel2.setOpaque(true);
                coursePanel2.setCursor(new Cursor(Cursor.HAND_CURSOR));

                coursePanel2.setLayout(new BoxLayout(coursePanel2, BoxLayout.X_AXIS));

                JLabel textLabel = new JLabel("Next Section");
                textLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); 

                try {
                    ImageIcon originalIcon = new ImageIcon(getClass().getResource("/rightArrow.png"));
                    Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); 
                    JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

                    coursePanel2.add(Box.createHorizontalGlue()); 
                    coursePanel2.add(textLabel);
                    coursePanel2.add(Box.createRigidArea(new Dimension(8, 0))); 
                    coursePanel2.add(iconLabel);
                    coursePanel2.add(Box.createHorizontalGlue()); 

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error loading rightArrow.png");
                }

                coursePanel2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        coursePanel2.setBackground(new Color(200, 170, 255)); 
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        coursePanel2.setBackground(new Color(240, 240, 240)); 
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // openNextSection();  
                    }
                });

                mainContent.add(coursePanel2);
                mainContent.add(Box.createVerticalStrut(10));
            }

        }

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        attributes.applyHandCursor(mainPanel);
        return mainPanel;
    }
    
    private void openLesson2(int lessonNumber) {
        System.out.println("Opening lesson: " + lessonNumber); 
        JFrame newStage = null;

        switch (lessonNumber) {
//            case 1 -> newStage = new FirstPythonStage(); 
//            case 2 -> newStage = new SecondStage();
//            case 3 -> newStage = new ThirdStage();
        }

        if (newStage != null) {
            newStage.setVisible(true);
            dispose();

            if (lessonNumber == currentProgress + 1) {
                unlockNextLesson();
            }
        }
    }

    private void refreshHomePanel() {
        mainContentPanel.removeAll();  
        try {
            mainContentPanel.add(HomePanel(), "Home");  
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        cardLayout.show(mainContentPanel, "Home"); 
        revalidate();
        repaint();
    }




    private JPanel createTopBar() throws MalformedURLException {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topBar.setPreferredSize(new Dimension(800, 50));

        JLabel hearts = new JLabel("C  O  D  E  X");
        hearts.setFont(new Font("Arial", Font.BOLD, 20));

        topBar.add(hearts);

        Timer timer = new Timer(85, new ActionListener() { 
            private final String[] letters = {"C", "O", "D", "E", "X"};
            private final String[] fullWords = {"Creative", "Optimization", "Development", "Enganging", "eXploration"};
            private String[] displayText = {"C", "O", "D", "E", "X"};
            private int index = 0;
            private boolean transforming = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (transforming) {
                    if (index < fullWords.length) {
                        if (displayText[index].length() < fullWords[index].length()) {
                            displayText[index] = fullWords[index].substring(0, displayText[index].length() + 1);
                        } else {
                            index++; 
                        }
                    }

                    if (index >= fullWords.length) {
                        transforming = false; 
                        index = fullWords.length - 1; 
                    }
                } else {
                    if (index >= 0) {
                        if (displayText[index].length() > 1) {
                            displayText[index] = displayText[index].substring(0, displayText[index].length() - 1);
                        } else {
                            displayText[index] = letters[index]; 
                            index--;
                        }
                    }

                    if (index < 0) {
                        index = 0; 
                        transforming = true; 
                    }
                }

                hearts.setText(String.join("  ", displayText));
            }
        });

        timer.start(); 

        return topBar;
    }



    private JButton fullStackButton;
    private JButton PythonButton;
    private JButton BackEndButton;
    private JButton FrontEndButton;

    private JLabel titleLabel; 

    private JPanel BurgerPanel() {
        JPanel burgerPanel = new JPanel(new BorderLayout());
        burgerPanel.setPreferredSize(new Dimension(800, 60));
        burgerPanel.setBackground(new Color(240, 240, 240));

        titleLabel = new JLabel("Select a Role", SwingConstants.CENTER); 
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK); 

        burgerPanel.add(titleLabel, BorderLayout.CENTER);

        burgerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                burgerPanel.setBackground(new Color(200, 170, 255)); 
                titleLabel.setForeground(Color.WHITE); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                burgerPanel.setBackground(new Color(240, 240, 240)); 
                titleLabel.setForeground(Color.BLACK); 
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSlidingPanel();
            }
        });

        return burgerPanel;
    }



    private boolean isAnimating = false;

    private void toggleSlidingPanel() {
        if (isAnimating) return; 

        isAnimating = true; 

        if (isPanelVisible) {
            slideDownLibraryAnimation(() -> {
                isPanelVisible = false; 
                isAnimating = false; 
            });
        } else {
            slideUpLibraryAnimation(() -> {
                isPanelVisible = true; 
                isAnimating = false; 
            });
        }
    }
    
    @SuppressWarnings("deprecation")
    private void setupSlidingPanel() {
        slidingPanel.setBackground(Color.WHITE);
        slidingPanel.setLayout(null);
        attributes.applyHandCursor(slidingPanel);

        try {
            URL exitIconUrl = new URL("https://pluspng.com/img-png/exit-button-png--1600.png"); // Replace with your chosen 
            ImageIcon exitIcon = new ImageIcon(new ImageIcon(exitIconUrl).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            exitButton = new JButton(exitIcon);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            exitButton = new JButton("X");
            exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        }
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(Color.WHITE);
        exitButton.setBorder(null);
        exitButton.setFocusable(false);
        exitButton.setBounds(10, 10, 30, 30);
        exitButton.addActionListener(e -> slideDownLibraryAnimation(() -> {
            isPanelVisible = false;
            isAnimating = false;
        }));
        slidingPanel.add(exitButton);

        fullStackButton = createRoleButton("Full Stack Developer",
        	    new String[]{
        	        "/html.png",
        	        "/css.png",
        	        "/js.png",
        	        "/react.png",
        	        "/mysql.png"
        	    },
        	    () -> {
        	    	 updateLabels("Full Stack Developer"); 
        	         showPanel("Home"); 
        	         slideDownLibraryAnimation(() -> {}); 
        	    });

        PythonButton = createRoleButton("Python AI Developer",
        	    new String[]{"/py.png"},
        	    () -> {
        	        updateLabels("Python AI Developer"); 
        	        showPanel("PythonAI"); 
        	        slideDownLibraryAnimation(() -> {}); 
        	    });




        BackEndButton = createRoleButton("Back-End Developer",
                new String[]{"/js.png", "/mysql.png", 
                		 "/css.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        FrontEndButton = createRoleButton("Front-End Developer",
                new String[]{"/html.png", "/css.png", "/js.png",
                		"/react.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        int buttonWidth = 776;
        int buttonHeight = 80;
        int gap = 10;

        fullStackButton.setBounds(20, 80, buttonWidth, buttonHeight);
        PythonButton.setBounds(20, 160 + gap, buttonWidth, buttonHeight);
        BackEndButton.setBounds(20, 240 + 2 * gap, buttonWidth, buttonHeight);
        FrontEndButton.setBounds(20, 320 + 3 * gap, buttonWidth, buttonHeight);

        slidingPanel.add(fullStackButton);
        slidingPanel.add(PythonButton);
        slidingPanel.add(BackEndButton);
        slidingPanel.add(FrontEndButton);

        slidingPanel.setBounds(0, 590, 816, 590); 
        slidingPanel.setVisible(false);
    }


    
    private JButton createRoleButton(String role, String[] iconUrls, Runnable action) {
        JButton button = new JButton();
        button.setFont(new Font("Arial", Font.BOLD, 16)); 
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(40),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setLayout(new GridBagLayout());

        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0)); 
        iconsPanel.setOpaque(false);

        for (String iconUrl : iconUrls) {
            ImageIcon icon = loadImage(iconUrl, 28, 28);
            JLabel iconLabel = new JLabel(icon);
            iconsPanel.add(iconLabel);
        }

        JLabel textLabel = new JLabel(role, SwingConstants.CENTER);
        textLabel.setFont(new Font("Verdana", Font.BOLD, 15));
        textLabel.setForeground(Color.BLACK);
        textLabel.setPreferredSize(new Dimension(180, 30));  
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.add(iconsPanel);
        panel.add(Box.createVerticalStrut(5)); 
        panel.add(textLabel);

        button.setPreferredSize(new Dimension(220, 60)); 
        button.add(panel);
        
        // Add Hover Effect
        Color defaultColor = Color.WHITE;
        Color hoverColor = new Color(200, 170, 255); 

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
            }
        });

        button.addActionListener(e -> action.run());

        attributes.applyHandCursor(button);
        
        return button;
    }


    private void slideUpLibraryAnimation(Runnable onComplete) {
        slidingPanel.setVisible(true);
        exitButton.setVisible(true); 
        panelY = slidingPanel.getY();
        int targetY = 0;

        slideUpTimer = new Timer(2, e -> { 
            if (panelY > targetY) {
                panelY -= 90; 
                slidingPanel.setBounds(0, panelY, 816, 590);
                slidingPanel.repaint();
            } else {
                slidingPanel.setBounds(0, 0, 816, 590);
                slideUpTimer.stop();
                isAnimating = false;
                if (onComplete != null) onComplete.run();
            }
        });

        slideUpTimer.start();
    }


    private void slideDownLibraryAnimation(Runnable onComplete) {
        panelY = slidingPanel.getY();
        int offScreenY = 590;

        slideDownTimer = new Timer(2, e -> { 
            if (panelY < offScreenY) {
                panelY += 100; // Increased movement per step for faster animation
                slidingPanel.setBounds(0, panelY, 816, 590);
                slidingPanel.repaint();
            } else {
                slidingPanel.setVisible(false);
                exitButton.setVisible(false);
                slideDownTimer.stop();
                isAnimating = false; 
                onComplete.run();
            }
        });
        slideDownTimer.start();
    }

    private void updateLabels(String selectedRole) {
        SwingUtilities.invokeLater(() -> {
            titleLabel.setText(selectedRole);
            titleLabel.revalidate();
            titleLabel.repaint();
            titleLabel.getParent().revalidate(); 
            titleLabel.getParent().repaint();
        });
    }

    private Color defaultColor = new Color(240, 240, 240); 
    private Color hoverColor = new Color(200, 170, 255); 
    private Color activeColor = new Color(200, 200, 200); 
    private JButton activeButton = null;

    private JPanel BottomNav() {
        JPanel bottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        bottomNav.setPreferredSize(new Dimension(800, 50));
        bottomNav.setBackground(defaultColor);

        bottomNav.add(createNavButton("Home", "/home.png", () -> showPanel("Home"), true));
        bottomNav.add(createNavButton("Practice", "/practice.png", () -> JOptionPane.showMessageDialog(this, "Practice Coming Soon"), false));
        bottomNav.add(createNavButton("Leaderboard", "/lead.png", () -> JOptionPane.showMessageDialog(this, "Leaderboard Coming Soon"), false));
        bottomNav.add(createNavButton("Library", "/library.png", () -> showPanel("Library"), true));
        bottomNav.add(createNavButton("Profile", "/profile.png", () -> JOptionPane.showMessageDialog(this, "Profile Coming Soon"), false));

        attributes.applyHandCursor(bottomNav);

        return bottomNav;
    }

    private JButton createNavButton(String text, String iconPath, Runnable action, boolean isPanelSwitch) {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBackground(defaultColor);
        button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS));

        ImageIcon icon = loadImage(iconPath, 18, 18);
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        textLabel.setForeground(Color.BLACK); 

        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.add(iconLabel);
        button.add(textLabel);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != activeButton) {
                    button.setBackground(hoverColor);
                    textLabel.setForeground(Color.WHITE); 
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != activeButton) {
                    button.setBackground(defaultColor);
                    textLabel.setForeground(Color.BLACK); 
                }
            }
        });

        // Button click event
        button.addActionListener(e -> {
            if (isPanelSwitch) {
                action.run();
                if (activeButton != null) {
                    activeButton.setBackground(defaultColor);
                    ((JLabel) activeButton.getComponent(1)).setForeground(Color.BLACK); 
                }

                activeButton = button;
                activeButton.setBackground(activeColor);
                textLabel.setForeground(Color.WHITE); 
            } else {
                JOptionPane.showMessageDialog(null, text + " Coming Soon!");
            }
        });

        attributes.applyHandCursor(button);
        return button;
    }


    // Helper method to resize images
    private ImageIcon loadImage(String path, int width, int height) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            System.err.println("Icon not found: " + path);
            return new ImageIcon(); 
        }
    }





    private void showPanel(String panelName) {
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(mainContentPanel, panelName);
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
        });
    }

    private void activeButton(JButton button) {
        if (activeButton != null) {
            activeButton.setBackground(defaultColor);
            activeButton.setForeground(Color.BLACK);
        }

        activeButton = button;
        activeButton.setBackground(activeColor);
        activeButton.setForeground(Color.BLACK);
    }

    

    
    private JButton HtmlBtn, CssBtn, JavaScriptBtn, PythonBtn;
    private JButton CppBtn, JavaBtn, CSharpBtn, SwiftBtn, PhpBtn, MySQLBtn;

    public JPanel createLibraryPanel() throws MalformedURLException {
        JPanel LibraryPanel = new JPanel(new GridLayout(5, 2, 20, 10)); 
        LibraryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        LibraryPanel.setBackground(Color.WHITE);

        HtmlBtn = libraryBtn("HTML",
                new String[]{"/html.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        CssBtn = libraryBtn("CSS",
                new String[]{"/css.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        JavaScriptBtn = libraryBtn("JAVASCRIPT",
                new String[]{"/js.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        PythonBtn = libraryBtn("PYTHON",
                new String[]{"/py.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        CppBtn = libraryBtn("C++",
                new String[]{"/cplus.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        JavaBtn = libraryBtn("JAVA",
                new String[]{"/java.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        CSharpBtn = libraryBtn("C#",
                new String[]{"/csharp.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        SwiftBtn = libraryBtn("SWIFT",
                new String[]{"/swift.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        PhpBtn = libraryBtn("PHP",
                new String[]{"/php.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        MySQLBtn = libraryBtn("MYSQL",
                new String[]{"/mysql.png"},
                () -> JOptionPane.showMessageDialog(null, "Coming Soon"));

        JButton[] buttons = {HtmlBtn, CssBtn, JavaScriptBtn, PythonBtn, CppBtn, JavaBtn, CSharpBtn, SwiftBtn, PhpBtn, MySQLBtn};

        for (JButton btn : buttons) {
            btn.setPreferredSize(new Dimension(350, 80)); 
            LibraryPanel.add(btn);
        }

        JPanel container = new JPanel(new BorderLayout());
        container.add(LibraryPanel, BorderLayout.CENTER);

        return container;
    }

        
    private JButton libraryBtn(String role, String[] iconUrls, Runnable action) {
        JButton button = new JButton();
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(40),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setLayout(new BorderLayout(10, 0)); 

        JLabel textLabel = new JLabel(role, SwingConstants.CENTER);
        textLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        textLabel.setForeground(Color.BLACK);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        iconPanel.setOpaque(false);

        for (String iconUrl : iconUrls) {
            ImageIcon icon = loadImage(iconUrl, 30, 30);
            JLabel iconLabel = new JLabel(icon);
            iconPanel.add(iconLabel);
        }

        button.add(textLabel, BorderLayout.CENTER); 
        button.add(iconPanel, BorderLayout.WEST); 

        button.addActionListener(e -> action.run());

        Color hoverColor = new Color(100, 50, 150); 
        Color defaultColor = Color.WHITE;

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                textLabel.setForeground(Color.WHITE); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
                textLabel.setForeground(Color.BLACK); 
            }
        });

        return button;
    }

    
    }
