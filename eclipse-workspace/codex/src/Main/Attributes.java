package Main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Attributes {
	
	public void applyRoundedBorder(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                ((JComponent) comp).setBorder(new RoundedBorder(15)); 
            }
            if (comp instanceof Container) {
                applyRoundedBorder((Container) comp); 
            }
        }
    }
    
	public void applyHandCursor(Container container) {
	    for (Component component : container.getComponents()) {
	        if (component instanceof JButton) {
	            JButton button = (JButton) component;
	            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

	            // Store original color
	            Color originalColor = button.getBackground();
	            Color hoverColor = new Color(100, 50, 150); 

	            button.addMouseListener(new MouseAdapter() {
	                @Override
	                public void mouseEntered(MouseEvent e) {
	                    button.setBackground(hoverColor);
	                }

	                @Override
	                public void mouseExited(MouseEvent e) {
	                    button.setBackground(originalColor);
	                }
	            });
	        } else if (component instanceof Container) {
	            applyHandCursor((Container) component); 
	        }
	    }
	}
    

    
    public JPanel createStatLabelWithBorder(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new RoundedBorder(20));
        panel.setPreferredSize(new Dimension(400, 100));
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        return panel;
    }

    @SuppressWarnings("deprecation")
	public JButton createPlayButton() throws MalformedURLException {
        URL imageUrl = new URL("https://cdn-icons-png.flaticon.com/512/727/727245.png");
        ImageIcon playIcon = new ImageIcon(new ImageIcon(imageUrl).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        
        
        
        return new JButton(playIcon);
    }
    


	@SuppressWarnings("deprecation")
	public JPanel createStyledButton(int lessonNumber, String lessonTitle, String iconPath) {
	    JPanel buttonPanel = new JPanel(new BorderLayout()) {
	        private static final long serialVersionUID = 1L;

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            Graphics2D g2 = (Graphics2D) g;
	            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            // Draw rounded border
	            g2.setColor(new Color(88, 101, 242));
	            g2.setStroke(new BasicStroke(2));
	            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
	        }
	    };

	    buttonPanel.setPreferredSize(new Dimension(700, 80));
	    buttonPanel.setMaximumSize(new Dimension(700, 80));
	    buttonPanel.setBackground(Color.WHITE);

	    // Number label (left-aligned)
	    JLabel numberLabel = new JLabel(lessonNumber + ".");
	    numberLabel.setFont(new Font("Arial", Font.BOLD, 9));
	    numberLabel.setForeground(Color.LIGHT_GRAY);
	    numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    numberLabel.setPreferredSize(new Dimension(40, 80));

	    // Load icon from source folder
	    ImageIcon icon = null;
	    URL iconURL = getClass().getResource(iconPath);
	    if (iconURL != null) {
	        icon = new ImageIcon(iconURL);
	        Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
	        icon = new ImageIcon(scaledImage);
	    } else {
	        System.err.println("Failed to load icon: " + iconPath);
	    }

	    // Button (centered text)
	    JButton button = new JButton(lessonTitle, icon);
	    button.setFont(new Font("Arial", Font.BOLD, 18));
	    button.setBackground(new Color(250, 250, 250));
	    button.setForeground(Color.BLACK);
	    button.setFocusPainted(false);
	    button.setBorder(BorderFactory.createEmptyBorder());
	    button.setContentAreaFilled(false);
	    button.setHorizontalAlignment(SwingConstants.LEFT);
	    button.setIconTextGap(10);

	    // Add hover effect
	    button.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            buttonPanel.setBackground(new Color(220, 220, 255)); 
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            buttonPanel.setBackground(Color.WHITE);
	        }
	    });

	    // Add components to the panel
	    buttonPanel.add(numberLabel, BorderLayout.WEST);
	    buttonPanel.add(button, BorderLayout.CENTER);

	    applyHandCursor(buttonPanel);

	    return buttonPanel;
	}

	
	private boolean darkMode = false;
	
	 public void applyTheme(JPanel profilePanel, JPanel topPanel) {
	        Color bgColor = darkMode ? new Color(40, 40, 40) : new Color(240, 240, 240);
	        Color textColor = darkMode ? Color.WHITE : Color.BLACK;

	        profilePanel.setBackground(bgColor);
	        topPanel.setBackground(bgColor);

	        // Apply background and foreground colors to all components
	        updateComponentColors(profilePanel, bgColor, textColor);
	    }

	    // Recursively update all child components
	    public void updateComponentColors(Container container, Color bgColor, Color textColor) {
	        for (Component c : container.getComponents()) {
	            if (c instanceof JPanel) {
	                c.setBackground(bgColor);
	                updateComponentColors((JPanel) c, bgColor, textColor); // Recursively update children
	            } else if (c instanceof JLabel) {
	                ((JLabel) c).setForeground(textColor);
	            } else if (c instanceof JButton) {
	                ((JButton) c).setForeground(textColor);
	                if (!c.getBackground().equals(new Color(255, 69, 58))) { // Skip logout button
	                    c.setBackground(bgColor);
	                }
	            } else if (c instanceof JCheckBox) {
	                ((JCheckBox) c).setForeground(textColor);
	            }
	        }
	    }

		public Object showSettingsDialog(JPanel profilePanel, JPanel topPanel) {
	        JDialog settingsDialog = new JDialog((Frame) null, "Settings", true);
	        settingsDialog.setSize(250, 150);
	        settingsDialog.getContentPane().setLayout(new FlowLayout());

	        JCheckBox darkModeToggle = new JCheckBox("Enable Dark Mode", darkMode);
	        darkModeToggle.setForeground(darkMode ? Color.WHITE : Color.BLACK); // Adjust checkbox text color
	        darkModeToggle.addActionListener(e -> {
	            darkMode = darkModeToggle.isSelected();
	            applyTheme(profilePanel, topPanel);
	            darkModeToggle.setForeground(darkMode ? Color.WHITE : Color.BLACK); // Update text color dynamically
	        });

	        settingsDialog.getContentPane().add(darkModeToggle);
	        JButton closeButton = new JButton("Close");
	        closeButton.addActionListener(e -> settingsDialog.dispose());
	        settingsDialog.getContentPane().add(closeButton);

	        settingsDialog.setLocationRelativeTo(null);
	        settingsDialog.setVisible(true);
			return null;
		}




}
