Facial Detection Attendance System

This Java-based application leverages OpenCV for real-time face detection and recognition via webcam, designed primarily for automated attendance tracking. The system detects faces, extracts features, and attempts to identify individuals from a set of pre-registered face images.
Project Status

    Core modules for face detection and recognition using OpenCV are implemented.

    GUI includes webcam control, session timing, and visual feedback with light/dark modes.

    Attendance management features, including recording, storing, and reporting attendance, are currently under development and will be integrated soon.

Features (Implemented)

    Real-time face detection through webcam feed.

    Face feature extraction and matching with pre-stored images using ORB descriptors.

    Interactive GUI to start/stop webcam and select session duration.

    Log panel displaying face recognition results and status messages.

    UI supports light and dark themes for better user experience.

Planned Features (In Progress)

    Automated attendance marking based on recognized faces.

    Attendance record storage and management.

    Reporting and export functionality for attendance data.

    User management for registering new faces and editing attendance logs.

Technologies Used

    Java 8+

    OpenCV (Java bindings)

    Swing for GUI development

Getting Started
Prerequisites

    JDK 8 or higher installed

    OpenCV native binaries and Java bindings configured

    Connected webcam

    Haar Cascade XML (haarcascade_frontalface_alt.xml) in the project directory

    Pre-registered face images configured in the application (IMAGE_PATHS)

Running the Application

    Clone the repository.

    Set up OpenCV native libraries on your system or IDE.

    Add paths to pre-registered face images in the IMAGE_PATHS array.

    Run the main class (FaceRecognition).

    Use the GUI to start the webcam and select session timing.

Usage Notes

    The system currently detects and recognizes faces but does not yet mark or save attendance.

    Recognition accuracy depends on quality and variety of stored images.

    Future updates will introduce attendance recording and user management.
