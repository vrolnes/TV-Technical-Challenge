# TV Technical Challenge Android App

This project is an Android application developed for the TV Technical Challenge. It displays movie information fetched from The Movie Database (TMDb) API and includes a video player using ExoPlayer.
Please enter your API Key into NetworkModule.kt: TMDB_API_KEY_PLACEHOLDER 
## Features

*   **Movie List Screen:**
    *   Displays movies in three horizontally scrolling carousels based on different sorting criteria (e.g., Popularity, Top Rated, Revenue).
    *   Implements infinite scrolling to load more movies as the user scrolls.
    *   Supports both portrait and landscape orientations.
    *   Handles configuration changes gracefully.
    *   Navigates to the Movie Detail screen on item click.
*   **Movie Detail Screen:**
    *   Shows movie details including a backdrop banner, title, rating, and overview.
    *   Supports both portrait and landscape orientations.
    *   Handles configuration changes gracefully.
    *   Navigates to the Movie Player screen when the banner is clicked.
*   **Movie Player Screen:**
    *   Uses ExoPlayer (Media3) to play movie content.
    *   Supports Widevine DRM for protected content using the provided test DASH stream.
    *   Adapts layout for portrait (player + description) and landscape (fullscreen player) modes.
    *   Includes basic player controls (play, pause, seek) provided by ExoPlayer UI components.

## Tech Stack & Libraries

*   **Language:** Kotlin
*   **UI:** Jetpack Compose
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Dependency Injection:** Hilt
*   **Networking:** Retrofit & OkHttp
*   **Image Loading:** Coil
*   **Video Playback:** ExoPlayer (Media3)
*   **Navigation:** Jetpack Navigation Compose
*   **Asynchronous Programming:** Kotlin Coroutines & Flow

## Project Structure

*   `app/src/main/java/com/vrolnes/tvtechnicalchallenge/`: Root package.
    *   `data/`: Data layer components (Repositories, DataSources, Network API definitions, DTOs).
    *   `domain/`: Domain layer components (Use Cases, Models).
    *   `presentation/`: Presentation layer components (UI Composables, ViewModels, Navigation).
    *   `di/`: Hilt dependency injection modules.
    *   `util/`: Utility classes.