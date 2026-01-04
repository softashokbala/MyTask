# My Task

A modern Android Todo application built with Jetpack Compose and Clean Architecture.

## Features
- **Task Management**: Create, read, update, and delete tasks.
- **Local Persistence**: Data is stored locally using Room Database.
- **Reactive UI**: Built with Jetpack Compose for a modern, reactive user interface.

## Tech Stack
- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture**: Clean Architecture + MVVM (Model-View-ViewModel)
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Concurrency**: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)

## Setup
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle to download dependencies.
4. Run the application on an Android Emulator or physical device (Min SDK 27).

## Architecture
The application follows Clean Architecture principles, separating concerns into layers:
- **Presentation**: UI (Compose) and ViewModels.
- **Domain**: Use Cases and Repository interfaces.
- **Data**: Repository implementations, Room Database, and Data Sources.

## CI/CD
This project uses [GitHub Actions](https://github.com/features/actions) for Continuous Integration.
The workflow is defined in `.github/workflows/android.yml` and performs the following checks on every push and pull request to `main` or `master` branches:
- Sets up JDK 11.
- Builds the project using Gradle (`./gradlew build`).
- Runs unit tests.

### Status
![Android CI](https://github.com/USERNAME/REPO_NAME/actions/workflows/android.yml/badge.svg)
*(Replace `USERNAME` and `REPO_NAME` with your GitHub details)*
