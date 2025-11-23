# Star Wars KMP App

A production-ready Kotlin Multiplatform (KMP) application that searches Star Wars characters and displays their homeworld information using the SWAPI (Star Wars API). The app features two product flavors with different branding and supports both Android and iOS platforms.

## 🚀 Features

- **Character Search**: Search for Star Wars characters with real-time API calls
- **Automatic Pagination**: Fetches all results across multiple pages automatically
- **Homeworld Details**: View detailed planet information in an elegant modal
- **Product Flavors**: Two distinct themes (Blue and Purple) with separate branding
- **Cross-Platform**: Shared business logic with native UI for Android and iOS
- **Error Handling**: Comprehensive error handling with user-friendly messages
- **Offline-Ready**: Graceful handling of network issues with retry functionality

## 🏗️ Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:

```
┌─────────────────────────────────────────────┐
│         Presentation Layer                  │
│   Android Compose & iOS SwiftUI             │
├─────────────────────────────────────────────┤
│         Shared Business Logic               │
│    ViewModels, UseCases, Repository         │
├─────────────────────────────────────────────┤
│           Data Layer                        │
│  API Client, DTOs, Domain Models            │
└─────────────────────────────────────────────┘
```

### Key Components

- **Shared Module**: Contains all business logic, networking, and data models
- **Android App**: Jetpack Compose UI with Material 3 design
- **iOS App**: SwiftUI interface with native iOS patterns
- **Clean Architecture**: Repository pattern, Use Cases, and MVVM
- **Ktor Client**: Cross-platform HTTP client with proper error handling

## 📱 Screenshots

### FlavorA (Blue Theme)
- Clean blue color scheme (`#2196F3`, `#1976D2`)
- App name: "Star Wars A"

### FlavorB (Purple Theme)
- Elegant purple color scheme (`#9C27B0`, `#7B1FA2`)
- App name: "Star Wars B"

## 🛠️ Technical Stack

### Shared (Kotlin Multiplatform)
- **Kotlin**: 2.2.20
- **Ktor**: 2.3.7 (HTTP client with content negotiation)
- **Kotlinx Serialization**: 1.6.2
- **Kotlinx Coroutines**: 1.7.3

### Android
- **Jetpack Compose**: Latest stable
- **Material 3**: Modern design system
- **Compose Navigation**: For screen navigation
- **Android SDK**: Compile SDK 35, Min SDK 24

### iOS
- **SwiftUI**: Native iOS UI framework
- **iOS Deployment Target**: iOS 14+

### Build System
- **Gradle**: 8.11.2

## ⚠️ Important: Android Studio Setup

**If you encounter "Unsupported Modules Detected" error in Android Studio:**

1. Close Android Studio completely
2. Delete the `.idea` directory: `rm -rf .idea`
3. Reopen the project in Android Studio (it will regenerate proper configuration)

For detailed setup instructions, see [ANDROID_STUDIO_SETUP.md](ANDROID_STUDIO_SETUP.md)
- **Android Gradle Plugin**: 8.11.2
- **Product Flavors**: FlavorA (Blue) and FlavorB (Purple)

## 📦 Project Structure

```
StarWarsApp/
├── shared/                           # Shared KMP module
│   ├── commonMain/
│   │   ├── data/
│   │   │   ├── remote/
│   │   │   │   ├── StarWarsApi.kt          # API interface
│   │   │   │   └── dto/                    # Data transfer objects
│   │   │   └── repository/
│   │   │       └── StarWarsRepository.kt   # Repository implementation
│   │   ├── domain/
│   │   │   ├── model/                      # Domain models
│   │   │   ├── repository/                 # Repository interface
│   │   │   └── usecase/                    # Business logic use cases
│   │   └── presentation/
│   │       └── viewmodel/
│   │           └── StarWarsViewModel.kt    # Shared ViewModel
│   ├── androidMain/                  # Android-specific code
│   │   └── platform/
│   │       └── HttpClientFactory.android.kt
│   └── iosMain/                      # iOS-specific code
│       └── platform/
│           └── HttpClientFactory.ios.kt
├── androidApp/                       # Android application
│   ├── flavorA/                      # Blue theme resources
│   ├── flavorB/                      # Purple theme resources
│   └── src/main/
│       └── java/com/fady/starwars/
│           ├── ui/
│           │   ├── screen/            # Compose screens
│           │   └── theme/             # Material 3 theming
│           └── MainActivity.kt
└── iosApp/                          # iOS SwiftUI application
    └── iosApp/
        ├── SearchView.swift
        ├── PeopleListView.swift
        ├── PlanetDetailView.swift
        └── StarWarsViewModelWrapper.swift
```

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Arctic Fox (2020.3.1) or later
- **Xcode**: 13.0 or later (for iOS development)
- **Kotlin**: 2.2.20
- **Java**: 11 or later

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/star-wars-kmp-app.git
   cd star-wars-kmp-app
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory and select it

3. **Sync Gradle files**
   - Android Studio should automatically prompt to sync
   - If not, click "Sync Now" in the notification bar

## 🏗️ Building the Project

### Android Builds

#### Build FlavorA (Blue Theme)
```bash
./gradlew assembleFlavorADebug
./gradlew assembleFlavorARelease
```

#### Build FlavorB (Purple Theme)
```bash
./gradlew assembleFlavorBDebug
./gradlew assembleFlavorBRelease
```

#### Install on Device
```bash
# FlavorA
./gradlew installFlavorADebug

# FlavorB
./gradlew installFlavorBDebug
```

### iOS Build

1. **Generate iOS Framework**
   ```bash
   ./gradlew :shared:embedAndSignAppleFrameworkForXcode
   ```

2. **Open Xcode**
   ```bash
   open iosApp/iosApp.xcodeproj
   ```

3. **Build and Run**
   - Select your target device or simulator
   - Press `Cmd + R` to build and run

## 🧪 Testing

### Unit Tests
```bash
# Run shared module tests
./gradlew :shared:testDebugUnitTest

# Run Android tests
./gradlew :androidApp:testFlavorADebugUnitTest
./gradlew :androidApp:testFlavorBDebugUnitTest
```

### API Testing Checklist

- [ ] Search with "an" returns 12+ results (all pages fetched)
- [ ] Search with "sky" returns all Skywalker characters
- [ ] Search with empty query shows validation error
- [ ] Network error displays retry option
- [ ] Planet modal shows correct homeworld data
- [ ] Modal closes properly when tapping close button
- [ ] List scrolls smoothly with 50+ items
- [ ] Loading indicators appear during API calls

## 🔧 Configuration

### Switching Between Flavors

#### Android Studio
1. Go to **Build Variants** panel (View → Tool Windows → Build Variants)
2. Select the desired flavor:
   - **flavorADebug**: Blue theme in debug mode
   - **flavorBDebug**: Purple theme in debug mode
   - **flavorARelease**: Blue theme optimized for release
   - **flavorBRelease**: Purple theme optimized for release

#### Command Line
```bash
# Build specific flavor
./gradlew assembleFlavorADebug  # Blue theme
./gradlew assembleFlavorBDebug  # Purple theme
```

### Environment Configuration

#### Debug Mode
- Full logging enabled
- Network request/response logging
- Development-friendly error messages

#### Release Mode
- Optimized builds with ProGuard/R8
- Minimal logging
- User-friendly error messages

## 🌐 API Documentation

### Star Wars API (SWAPI)

The app uses the [Star Wars API (SWAPI)](https://swapi.dev/) for all data:

#### People Search
- **Endpoint**: `GET https://swapi.dev/api/people/?search={query}`
- **Pagination**: Automatic (fetches all pages)
- **Response**: List of characters matching the search query

#### Planet Details
- **Endpoint**: `GET https://swapi.dev/api/planets/{id}/`
- **Parameters**: Planet ID extracted from character's homeworld URL
- **Response**: Detailed planet information

### API Features Implemented

✅ **Automatic Pagination**: Fetches ALL results across multiple pages  
✅ **Error Handling**: Network, timeout, and parsing errors  
✅ **Retry Logic**: User-initiated retry on failures  
✅ **Input Validation**: Query sanitization and validation  
✅ **Edge Cases**: Malformed URLs, missing data, null values  

## 🔍 Key Features Deep Dive

### 1. Automatic Pagination
```kotlin
suspend fun searchPeople(query: String): List<PersonDto> {
    val allPeople = mutableListOf<PersonDto>()
    var nextUrl: String? = "${BASE_URL}people/?search=${query.trim()}"
    
    while (nextUrl != null && pageCount < maxPages) {
        val response = httpClient.get { url(nextUrl) }
        val peopleResponse: PeopleResponse = response.body()
        allPeople.addAll(peopleResponse.results)
        nextUrl = peopleResponse.next  // Continue to next page
    }
    
    return allPeople
}
```

### 2. Robust Error Handling
- **Network Errors**: "No internet connection. Please check your network."
- **Timeout Errors**: "Request timed out. Please try again."
- **Server Errors**: Specific HTTP status code handling
- **Parse Errors**: JSON deserialization error handling
- **Edge Cases**: Empty responses, malformed data

### 3. State Management
```kotlin
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

### 4. Product Flavors
Each flavor includes:
- **Unique App Name**: "Star Wars A" vs "Star Wars B"
- **Color Schemes**: Blue (#2196F3) vs Purple (#9C27B0)
- **Separate APKs**: Different application IDs
- **Branded Resources**: Flavor-specific colors and strings

## 🐛 Troubleshooting

### Common Issues

#### Build Errors
```bash
# Clean and rebuild
./gradlew clean
./gradlew build
```

#### iOS Framework Issues
```bash
# Regenerate iOS framework
./gradlew :shared:clean
./gradlew :shared:embedAndSignAppleFrameworkForXcode
```

#### Network Issues
- Ensure device has internet connection
- Check if SWAPI (https://swapi.dev/) is accessible
- Verify network permissions in AndroidManifest.xml

#### Flavor Not Found
- Sync Gradle files
- Invalidate caches: File → Invalidate Caches and Restart

### Performance Tips

1. **Enable R8/ProGuard** for release builds
2. **Use Build Cache**: Add `org.gradle.caching=true` to gradle.properties
3. **Optimize Images**: Use vector drawables when possible
4. **Lazy Loading**: Lists use LazyColumn/LazyVStack for performance

## 🤝 Contributing

### Development Workflow

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/amazing-feature`
3. **Commit** your changes: `git commit -m 'Add some amazing feature'`
4. **Push** to the branch: `git push origin feature/amazing-feature`
5. **Open** a Pull Request

### Code Style

- **Kotlin**: Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Swift**: Follow [Swift Style Guide](https://swift.org/documentation/api-design-guidelines/)
- **Compose**: Use Material 3 design principles
- **Architecture**: Maintain Clean Architecture patterns

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Star Wars API (SWAPI)**: For providing free access to Star Wars data
- **JetBrains**: For Kotlin Multiplatform technology
- **Google**: For Jetpack Compose and Material Design
- **Apple**: For SwiftUI framework

## 📞 Support

For support and questions:
- **Issues**: [GitHub Issues](https://github.com/yourusername/star-wars-kmp-app/issues)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/star-wars-kmp-app/discussions)

---

**Star Wars KMP App** - Bringing the galaxy to your mobile device with modern cross-platform technology! 🌌📱