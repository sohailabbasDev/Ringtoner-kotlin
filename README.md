# Ringtoner 🎵

Ringtoner is a simple and intuitive Android application that allows users to browse, play, and set ringtones on their devices. The app is entirely built in Kotlin, leveraging the modern Jetpack Compose toolkit for a sleek and responsive UI. 

## Features ✨

- **Browse Ringtones**: Explore a variety of ringtones directly within the app.
- **Play Ringtones**: Preview ringtones before setting them.
- **Set as Ringtone**: Easily set any ringtone as your default or for specific contacts.
- **Favorite Ringtones**: Mark ringtones as favorites for quick access.

## Tech Stack 🛠️

- **Kotlin**: The programming language used for all functionalities in the app.
- **Jetpack Compose**: A modern Android UI toolkit used to build the app’s user interface.
- **ViewModel**: Part of Android Jetpack, used to manage UI-related data in a lifecycle-conscious way.
- **Kotlin Flows**: For observing data and updating the UI reactively.
- **Dependency Injection**: Utilizes Dagger Hilt for managing dependencies across the app.
- **Coroutines**: Employed for handling asynchronous tasks smoothly.

## Project Structure 📂

The project follows a clean architecture approach with separation of concerns:

- **`app/`**: Contains all the source code, divided into the following layers:
  - **UI Layer**: Composed of Composables and ViewModels.
  - **Data Layer**: Handles data fetching and storage.
  - **Domain Layer**: Contains use cases and business logic.

## Getting Started 🚀

### Prerequisites

- Android Studio Flamingo or later.
- JDK 11 or later.
- An Android device or emulator running Android 5.0 (Lollipop) or higher.

### Installation

1. **Clone the repository**:
   ```sh
   git clone https://github.com/sohailabbasDev/Ringtoner2.git
