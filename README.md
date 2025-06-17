# Kotlin Showcase

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A modern Android application built with Kotlin that showcases best practices, modern Android development tools, and clean architecture principles.

## 📱 Features

- **Amiibo Browser**: Browse and search through a collection of Amiibo figures
- **Password Generator**: Generate secure passwords with customizable options
- **Text Utilities**: Various text manipulation tools (e.g., capitalize words, etc.)
- **Modern Architecture**: Built with MVVM, Clean Architecture, and Repository pattern
- **Jetpack Compose**: Modern declarative UI toolkit
- **Dependency Injection**: Using Koin for dependency injection
- **Pagination**: Efficient data loading with Paging 3

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: MVVM, Clean Architecture
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Networking**: [Ktor Client](https://ktor.io/)
- **Pagination**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Testing**: JUnit, MockK, Turbine

## 🚀 Getting Started

### Prerequisites

- Android Studio Flamingo (2022.2.1) or newer
- Android SDK 34
- JDK 17
- Gradle 8.0+

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/kotlinshowcase.git
   cd kotlinshowcase
   ```

## 🏗️ Project Structure

```
app/
├── src/
│   ├── androidTest/      # Instrumentation tests
│   ├── main/             # Main source code
│   │   ├── java/com/example/kotlinshowcase/
│   │   │   ├── di/                    # Dependency injection modules
│   │   │   ├── feature/                # Feature modules
│   │   │   │   ├── amiibo/             # Amiibo browser feature
│   │   │   │   ├── password/           # Password generator feature
│   │   │   │   └── textutils/          # Text utilities feature
│   │   │   └── util/                   # Utility classes
│   │   └── res/                        # Resources
│   └── test/                           # Unit tests
```

## 🧪 Testing

The project includes unit tests

- **Unit Tests**: Test business logic and data layer