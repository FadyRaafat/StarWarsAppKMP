import SwiftUI
import Shared

struct ContentView: View {
    @StateObject private var viewModel = StarWarsViewModelWrapper()
    @State private var selectedPerson: Person?
    @State private var showPlanetModal = false
    @EnvironmentObject var themeManager: ThemeManager
    
    var body: some View {
        NavigationView {
            VStack {
                // Theme Switcher - Simple Toggle
                HStack {
                    Spacer()
                    
                    Picker("Theme", selection: $themeManager.currentFlavor) {
                        ForEach(AppFlavor.allCases, id: \.self) { flavor in
                            Text(flavor.displayName).tag(flavor)
                        }
                    }
                    .pickerStyle(.segmented)
                    .frame(width: 200)
                }
                .padding(.horizontal)
                .padding(.top, 10)
                
                SearchView(
                    viewModel: viewModel,
                    selectedPerson: $selectedPerson,
                    showPlanetModal: $showPlanetModal
                )
            }
        }
        .sheet(isPresented: $showPlanetModal) {
            if let person = selectedPerson {
                PlanetDetailView(
                    viewModel: viewModel,
                    selectedPerson: person,
                    isPresented: $showPlanetModal
                )
                .environmentObject(themeManager)
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
