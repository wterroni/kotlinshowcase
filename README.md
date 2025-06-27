# Linky

Linky is a URL shortener Android application built with Kotlin and modern Android development practices. The app allows users to shorten URLs and view a history of recently shortened URLs.

## Features

- Shorten any URL using the [URL Shortener API](https://url-shortener-nu.herokuapp.com)
- View history of recently shortened URLs
- Copy shortened URLs to clipboard with a single tap
- Clean and intuitive user interface built with Jetpack Compose

## Architecture

The application follows Clean Architecture principles with a clear separation of concerns:

### Layers

1. **Presentation Layer**
   - Screens built with Jetpack Compose
   - ViewModels that manage UI state
   - UI state representation with data classes

2. **Domain Layer**
   - Use cases that encapsulate business logic
   - Domain models
   - Repository interfaces

3. **Data Layer**
   - Repository implementations
   - Remote data sources (API services)
   - In-memory data storage

### Technologies Used

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit for building native UI
- **Coroutines & Flow** - For asynchronous programming
- **Koin** - For dependency injection
- **Ktor Client** - For network requests
- **MockK & Turbine** - For testing

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/linky/
│   │   │   ├── core/ - Core components and utilities
│   │   │   │   ├── app/ - Application setup
│   │   │   │   ├── di/ - Dependency injection modules
│   │   │   │   ├── network/ - Network configuration
│   │   │   │   └── theme/ - UI theme
│   │   │   └── feature/ - Feature modules
│   │   │       ├── shortener/ - URL shortener feature
│   │   │       │   ├── data/ - Data layer
│   │   │       │   ├── domain/ - Domain layer
│   │   │       │   └── presentation/ - UI layer
│   │   │       └── MainActivity.kt - Main activity
│   ├── test/ - Unit tests
│   └── androidTest/ - UI tests
```

## Testing

The project includes:

- **Unit Tests** for:
  - Use cases
  - Repositories
  - ViewModels
  - API services

- **UI Tests** for:
  - Main screen functionality
  - URL shortening flow
  - Component rendering

## Getting Started

1. Clone the repository
2. Open the project in Android Studio
3. Build and run the application

## API

The application uses the following API endpoints:

- `POST /api/alias` - Create a shortened URL
- `GET /api/alias/:id` - Retrieve the original URL by alias

## License

This project is for demonstration purposes only.