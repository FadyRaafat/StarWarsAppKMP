import SwiftUI

@main
struct iOSApp: App {
    // Theme manager for the entire app
    @StateObject private var themeManager = ThemeManager(flavor: .flavorA) // Default to FlavorA
    
    var body: some Scene {
        WindowGroup {
            ThemedView(themeManager: themeManager) {
                ContentView()
                    .environmentObject(themeManager)
            }
        }
    }
}