Facial Detection Attendance System

This Java-based application leverages OpenCV for real-time face detection and recognition via webcam, designed primarily for automated attendance tracking. The system detects faces, extracts features, and attempts to identify individuals from a set of pre-registered face images.

Project Status

Core modules for face detection and recognition using OpenCV are implemented.

    1. GUI includes webcam control, session timing, and visual feedback with light/dark modes.
    2. Attendance management features, including recording, storing, and reporting attendance, are currently under development and will be integrated soon.

Features (Implemented)

    1. Real-time face detection through webcam feed.
    2. Face feature extraction and matching with pre-stored images using ORB descriptors.
    3. Interactive GUI to start/stop webcam and select session duration.
    4. Log panel displaying face recognition results and status messages.
    5. UI supports light and dark themes for better user experience.

Planned Features (In Progress)

    1. Automated attendance marking based on recognized faces.
    2. Attendance record storage and management.
    3. Reporting and export functionality for attendance data.
    4. User management for registering new faces and editing attendance logs.

Technologies Used

    1. Java 8+
    2. OpenCV (Java bindings)
    3. Swing for GUI development

Getting Started

Prerequisites

    1. JDK 8 or higher installed
    2. OpenCV native binaries and Java bindings configured
    3. Connected webcam
    4. Haar Cascade XML (haarcascade_frontalface_alt.xml) in the project directory
    5. Pre-registered face images configured in the application (IMAGE_PATHS)

Running the Application

    1. Clone the repository.
    2. Set up OpenCV native libraries on your system or IDE.
    3. Add paths to pre-registered face images in the IMAGE_PATHS array.
    4. Run the main class (FaceRecognition).
    5. Use the GUI to start the webcam and select session timing.

Usage Notes

    1. The system currently detects and recognizes faces but does not yet mark or save attendance.
    2. Recognition accuracy depends on quality and variety of stored images.
    3. Future updates will introduce attendance recording and user management.
