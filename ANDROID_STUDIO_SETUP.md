# Android Studio Setup Guide

## Important: Avoiding Module Detection Issues

This is a Kotlin Multiplatform project that contains both Android and iOS code. Android Studio may sometimes detect the iOS Xcode project as an incompatible module.

### If you see "Unsupported Modules Detected" error:

1. **Close Android Studio completely**

2. **Remove the .idea directory**:
   ```bash
   rm -rf .idea
   ```

3. **Open only the Android project**:
   - In Android Studio, use "Open" (not "Import")
   - Select the root directory containing `build.gradle.kts`
   - Android Studio will detect this as a Gradle project

4. **If the error persists**:
   - Close Android Studio
   - Temporarily rename the iOS directory:
     ```bash
     mv iosApp iosApp_temp
     ```
   - Open the project in Android Studio
   - Once Android Studio finishes indexing, close it
   - Restore the iOS directory:
     ```bash
     mv iosApp_temp iosApp
     ```
   - Reopen Android Studio

### Project Structure:
- `androidApp/` - Android application module
- `shared/` - Kotlin Multiplatform shared code
- `iosApp/` - iOS application (for Xcode development only)

### For iOS Development:
- Use Xcode to open `iosApp/iosApp.xcodeproj`
- Never open the iOS project in Android Studio

### Building the Project:
```bash
# Android Debug builds
./gradlew :androidApp:assembleFlavorADebug
./gradlew :androidApp:assembleFlavorBDebug

# All Android builds
./gradlew :androidApp:assembleDebug
```

## Flavors Available:
- **FlavorA**: Blue theme Star Wars app
- **FlavorB**: Purple theme Star Wars app