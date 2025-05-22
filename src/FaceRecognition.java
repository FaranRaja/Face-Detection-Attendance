import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class FaceRecognition {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";
    private static final String[] IMAGE_PATHS = {"p1.jpg"}; // Add all 10 image paths here
    private VideoCapture webcam;
    private volatile boolean isRunning = true;
    private Map<String, Mat> storedDescriptors = new HashMap<>();
    private JLabel videoLabel;
    private JComboBox<String> timeSelectionBox;
    private JTextArea logArea;
    private Timer timer;
    private boolean isDarkMode = false; // Flag for dark mode



    public FaceRecognition() {
        loadStoredImages();
        setupGUI();
    }

    private void loadStoredImages() {
        CascadeClassifier faceDetector = new CascadeClassifier(CASCADE_FILE);

        for (String path : IMAGE_PATHS) {
            Mat image = Imgcodecs.imread(path);
            if (image.empty()) {
                System.out.println("Error: Unable to load image " + path);
                continue;
            }

            Mat face = detectFace(image, faceDetector);
            if (face.empty()) {
                System.out.println("No face detected in image: " + path);
                continue;
            }

            MatOfKeyPoint keypoints = new MatOfKeyPoint();
            Mat descriptors = new Mat();
            extractKeypointsAndDescriptors(face, keypoints, descriptors);
            if (!descriptors.empty()) {
                storedDescriptors.put(path, descriptors);
            }
        }
    }

    private void setupGUI() {
        JFrame frame = new JFrame("Webcam Face Recognition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);

        // --- Video Display Panel ---
        JPanel videoPanel = new JPanel();
        videoPanel.setLayout(new BorderLayout());
        videoPanel.setBorder(new TitledBorder("Webcam Feed"));

        videoLabel = new JLabel();
        videoLabel.setHorizontalAlignment(JLabel.CENTER);
        videoPanel.add(videoLabel, BorderLayout.CENTER);

        // --- Control Panel ---
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBorder(new TitledBorder("Controls"));

        // Time selection dropdown
        String[] timeOptions = {"Select Time", "1 Minute", "5 Minutes", "10 Minutes"};
        timeSelectionBox = new JComboBox<>(timeOptions);
        controlPanel.add(new JLabel("Session Duration:"));
        controlPanel.add(timeSelectionBox);

        // Start button
        JButton startButton = new JButton("Start Webcam");
        startButton.addActionListener(e -> startWebcamStream());
        controlPanel.add(startButton);

        // Stop button
        JButton stopButton = new JButton("Stop Webcam");
        stopButton.addActionListener(e -> stopWebcam());
        controlPanel.add(stopButton);

        // Toggle button for light/dark mode
        JToggleButton toggleButton = new JToggleButton("Dark Mode: OFF");
        toggleButton.addItemListener(e -> {
            isDarkMode = toggleButton.isSelected();
            toggleButton.setText(isDarkMode ? "Dark Mode: ON" : "Dark Mode: OFF");
            applyTheme(frame);
        });
        controlPanel.add(toggleButton);

        // --- Log Panel ---
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        logPanel.setBorder(new TitledBorder("Logs / Status"));

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Adding Panels to Frame ---
        frame.add(videoPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(10, 10));
        rightPanel.add(controlPanel, BorderLayout.NORTH);
        rightPanel.add(logPanel, BorderLayout.CENTER);

        frame.add(rightPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void applyTheme(JFrame frame) {
        // Colors for Dark and Light modes
        Color backgroundColor = isDarkMode ? Color.DARK_GRAY : Color.WHITE;
        Color foregroundColor = isDarkMode ? Color.WHITE : Color.BLACK;
        Color buttonBackgroundColor = isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200);
        Color buttonForegroundColor = isDarkMode ? Color.WHITE : Color.BLACK;

        // Set the frame background
        frame.getContentPane().setBackground(backgroundColor);

        // Update all components recursively
        for (Component component : frame.getContentPane().getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                panel.setBackground(backgroundColor);
                // Apply theme to all child components
                for (Component child : panel.getComponents()) {
                    updateComponentTheme(child, backgroundColor, foregroundColor, buttonBackgroundColor, buttonForegroundColor);
                }
            }
        }

        // Update JTextArea background and foreground
        logArea.setBackground(backgroundColor);
        logArea.setForeground(foregroundColor);
        timeSelectionBox.setBackground(backgroundColor);
        timeSelectionBox.setForeground(foregroundColor);

        // Update border and title for the TitledBorder
        for (Component component : frame.getContentPane().getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getBorder() instanceof TitledBorder) {
                    TitledBorder titleBorder = (TitledBorder) panel.getBorder();
                    titleBorder.setTitleColor(foregroundColor);
                }
            }
        }
    }

    private void updateComponentTheme(Component component, Color backgroundColor, Color foregroundColor, Color buttonBackgroundColor, Color buttonForegroundColor) {
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setBackground(backgroundColor);
            label.setForeground(foregroundColor);
        } else if (component instanceof JTextArea) {
            JTextArea textArea = (JTextArea) component;
            textArea.setBackground(backgroundColor);
            textArea.setForeground(foregroundColor);
        } else if (component instanceof JButton) {
            JButton button = (JButton) component;
            button.setBackground(buttonBackgroundColor);
            button.setForeground(buttonForegroundColor);
        } else if (component instanceof JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) component;
            toggleButton.setBackground(buttonBackgroundColor);
            toggleButton.setForeground(buttonForegroundColor);
        } else if (component instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) component;
            comboBox.setBackground(backgroundColor);
            comboBox.setForeground(foregroundColor);
        } else if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
            scrollBar.setBackground(backgroundColor);
            scrollBar.setForeground(foregroundColor);
        }
    }

    public void startWebcamStream() {
        String selectedTime = (String) timeSelectionBox.getSelectedItem();
        if (selectedTime == null || selectedTime.equals("Select Time")) {
            JOptionPane.showMessageDialog(null, "Please select a valid time duration", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int durationMinutes = 0;
        switch (selectedTime) {
            case "1 Minute":
                durationMinutes = 1;
                break;
            case "5 Minutes":
                durationMinutes = 5;
                break;
            case "10 Minutes":
                durationMinutes = 10;
                break;
        }

        webcam = new VideoCapture(0);
        if (!webcam.isOpened()) {
            JOptionPane.showMessageDialog(null, "Error: Unable to access the webcam", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        appendLog("Webcam started.");

        int durationMilliseconds = durationMinutes * 60 * 1000;
        timer = new Timer(durationMilliseconds, e -> stopWebcam());
        timer.setRepeats(false);
        timer.start();

        isRunning = true;

        new Thread(() -> {
            Mat frame = new Mat();
            CascadeClassifier faceDetector = new CascadeClassifier(CASCADE_FILE);
            ORB orb = ORB.create();

            while (isRunning) {
                if (!webcam.read(frame)) {
                    appendLog("Failed to read frame from webcam.");
                    continue;
                }

                Mat face = detectFace(frame, faceDetector);
                if (!face.empty()) {
                    MatOfKeyPoint keypoints = new MatOfKeyPoint();
                    Mat descriptors = new Mat();
                    extractKeypointsAndDescriptors(face, keypoints, descriptors);

                    if (!descriptors.empty()) {
                        String matchedImage = matchFace(descriptors);
                        if (matchedImage != null) {
                            appendLog("Face matched with image: " + matchedImage);
                        } else {
                            appendLog("Face not recognized.");
                        }
                    }
                }

                BufferedImage img = matToBufferedImage(frame);
                videoLabel.setIcon(new ImageIcon(img));
            }
        }).start();
    }

    public void stopWebcam() {
        isRunning = false;
        if (webcam != null && webcam.isOpened()) {
            webcam.release();
        }
        appendLog("Webcam stopped.");
    }

    private void appendLog(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    private Mat detectFace(Mat image, CascadeClassifier faceDetector) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale(grayImage, faces);

        for (Rect rect : faces.toArray()) {
            return new Mat(image, rect);
        }

        return new Mat(); // No face found
    }

    private void extractKeypointsAndDescriptors(Mat image, MatOfKeyPoint keypoints, Mat descriptors) {
        ORB orb = ORB.create();
        orb.detectAndCompute(image, new Mat(), keypoints, descriptors);
    }

    private String matchFace(Mat descriptors) {
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        for (Map.Entry<String, Mat> entry : storedDescriptors.entrySet()) {
            Mat storedDescriptor = entry.getValue();

            // Using k-nearest neighbors (k=2) to find the best match
            List<MatOfDMatch> matchesList = new ArrayList<>();
            matcher.knnMatch(descriptors, storedDescriptor, matchesList, 2);

            // Assuming the first match is the best
            if (!matchesList.isEmpty()) {
                List<DMatch> matches = matchesList.get(0).toList();
                if (matches.size() >= 2) {
                    DMatch firstMatch = matches.get(0);
                    DMatch secondMatch = matches.get(1);

                    // Apply Lowe's ratio test
                    if (firstMatch.distance < 0.75 * secondMatch.distance) {
                        return entry.getKey(); // A match is found
                    }
                }
            }
        }
        return null; // No match found
    }


    private double calculateDescriptionDistance(Mat descriptors1, Mat descriptors2) {
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        // Perform knn matching with k=2
        List<MatOfDMatch> matchesList = new ArrayList<>();
        matcher.knnMatch(descriptors1, descriptors2, matchesList, 2); // k = 2

        double totalDistance = 0;
        int validMatchesCount = 0;

        // Iterate through matches and apply Lowe's ratio test
        for (MatOfDMatch matOfDMatch : matchesList) {
            List<DMatch> matches = matOfDMatch.toList();
            if (matches.size() >= 2) {
                DMatch firstMatch = matches.get(0);
                DMatch secondMatch = matches.get(1);

                // Lowe's ratio test to ensure good matches
                if (firstMatch.distance < 0.75 * secondMatch.distance) {
                    totalDistance += firstMatch.distance;
                    validMatchesCount++;
                }
            }
        }

        // Return average distance if valid matches are found
        if (validMatchesCount > 0) {
            return totalDistance / validMatchesCount;
        } else {
            return Double.MAX_VALUE; // No valid matches found
        }
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        int width = mat.width();
        int height = mat.height();
        int channels = mat.channels();
        byte[] data = new byte[width * height * channels];
        mat.get(0, 0, data);

        int bufferSize = width * height * 3;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, width, height, data);
        return image;
    }
}

