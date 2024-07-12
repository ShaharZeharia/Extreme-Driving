# Extreme-Driving
Welcome to **Extreme-Driving**, an exciting game where players navigate through challenging scenarios, avoiding collisions and managing their resources. This game is designed to provide an engaging experience with real-time feedback and a dynamic user interface.



## Features

- **Collision Detection**: The game detects collisions and updates the user interface accordingly.

- **Real-Time Feedback**: The device vibrates, plays a sound, and displays a toast message when a collision occurs.

- **Game State Management**: The game tracks the number of collisions and resets the game when necessary.

- **Dynamic UI**: The display of hearts and other UI elements are updated based on the game state.

- **Highscores Table**: View the top scores with an integrated map showing the locations.

- **Return to Menu Button**: Easily navigate back to the main menu from the highscores screen.


## Installation

  

 - Clone the repository:
git clone https://github.com/ShaharZeharia/Extreme-Driving.git

 -  Open the project in your preferred IDE (e.g., Android Studio).

 -  Build and run the project on your device or emulator.


**API Key Setup**
To enable the Google Maps functionality, you need to set up an API key for your project.

 - Go to the [Google Cloud Console](https://console.cloud.google.com/).

 - Create a new project or select an existing one.

 - Navigate to the **APIs & Services** dashboard.

 - Click on “+ ENABLE APIS AND SERVICES” at the top.

 - Search for “Google Maps” in the search bar. Enable the necessary APIs (e.g., Maps SDK for Android).

 - Go to the **Credentials** page and click on “Create credentials” > “API key”.

 - Restrict the API key to your app by specifying the package name and SHA-1 certificate fingerprint.

 - Copy the API key and add it to your project’s AndroidManifest.xml file: 

> <application>
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_API_KEY_HERE"/>
</application>



# Contact
  

If you have any questions or feedback, feel free to contact me at shacharz2000@gmail.com.

 
Thank you for playing **Extreme-Driving**! Enjoy the game and feel free to contribute to its development.


