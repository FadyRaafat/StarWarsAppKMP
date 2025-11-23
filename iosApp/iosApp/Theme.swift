import SwiftUI

// MARK: - App Flavor Enum
enum AppFlavor: String, CaseIterable {
    case flavorA = "FlavorA"
    case flavorB = "FlavorB"
    
    var displayName: String {
        switch self {
        case .flavorA:
            return "Star Wars A"
        case .flavorB:
            return "Star Wars B"
        }
    }
}

// MARK: - Theme Colors
extension Color {
    // FlavorA - Blue Theme (matching Android #2196F3, #1976D2)
    static let flavorAPrimary = Color(red: 0.129, green: 0.588, blue: 0.953)      // #2196F3
    static let flavorASecondary = Color(red: 0.098, green: 0.463, blue: 0.824)    // #1976D2
    
    // FlavorB - Purple Theme (matching Android #9C27B0, #7B1FA2)
    static let flavorBPrimary = Color(red: 0.612, green: 0.153, blue: 0.690)      // #9C27B0
    static let flavorBSecondary = Color(red: 0.482, green: 0.122, blue: 0.635)    // #7B1FA2
    
    // Common colors
    static let appBackground = Color(red: 0.961, green: 0.961, blue: 0.961)       // #F5F5F5
    static let appSurface = Color.white                                           // #FFFFFF
    static let appOnPrimary = Color.white                                         // #FFFFFF
    static let appOnBackground = Color(red: 0.129, green: 0.129, blue: 0.129)     // #212121
}

// MARK: - Theme Configuration
struct AppTheme {
    let primary: Color
    let secondary: Color
    let background: Color
    let surface: Color
    let onPrimary: Color
    let onBackground: Color
    
    static func theme(for flavor: AppFlavor) -> AppTheme {
        switch flavor {
        case .flavorA:
            return AppTheme(
                primary: .flavorAPrimary,
                secondary: .flavorASecondary,
                background: .appBackground,
                surface: .appSurface,
                onPrimary: .appOnPrimary,
                onBackground: .appOnBackground
            )
        case .flavorB:
            return AppTheme(
                primary: .flavorBPrimary,
                secondary: .flavorBSecondary,
                background: .appBackground,
                surface: .appSurface,
                onPrimary: .appOnPrimary,
                onBackground: .appOnBackground
            )
        }
    }
}

// MARK: - Theme Environment
class ThemeManager: ObservableObject {
    @Published var currentFlavor: AppFlavor
    
    init(flavor: AppFlavor = .flavorA) {
        self.currentFlavor = flavor
        updateAccentColor()
    }
    
    var currentTheme: AppTheme {
        return AppTheme.theme(for: currentFlavor)
    }
    
    func switchFlavor(_ flavor: AppFlavor) {
        currentFlavor = flavor
        updateAccentColor()
    }
    
    private func updateAccentColor() {
        // Update global accent color for the app
        UIView.appearance().tintColor = UIColor(currentTheme.primary)
    }
}

// MARK: - Theme Environment Key
struct ThemeEnvironmentKey: EnvironmentKey {
    static let defaultValue = ThemeManager()
}

extension EnvironmentValues {
    var themeManager: ThemeManager {
        get { self[ThemeEnvironmentKey.self] }
        set { self[ThemeEnvironmentKey.self] = newValue }
    }
}

// MARK: - Theme Modifier
struct ThemedView<Content: View>: View {
    @ObservedObject var themeManager: ThemeManager
    let content: Content
    
    init(themeManager: ThemeManager, @ViewBuilder content: () -> Content) {
        self.themeManager = themeManager
        self.content = content()
    }
    
    var body: some View {
        content
            .environment(\.themeManager, themeManager)
            .accentColor(themeManager.currentTheme.primary)
    }
}

// MARK: - Theme Preview Helper
#if DEBUG
extension AppFlavor {
    static let preview = AppFlavor.flavorA
}
#endif