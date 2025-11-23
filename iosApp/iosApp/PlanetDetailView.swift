import SwiftUI
import Shared

struct PlanetDetailView: View {
    @ObservedObject var viewModel: StarWarsViewModelWrapper
    let selectedPerson: Person
    @Binding var isPresented: Bool
    
    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                // Header
                HStack {
                    Text("Homeworld Details")
                        .font(.title)
                        .fontWeight(.bold)
                        .foregroundColor(.accentColor)
                    
                    Spacer()
                    
                    Button("Close") {
                        isPresented = false
                        viewModel.clearPlanetState()
                    }
                }
                .padding()
                
                // Content based on state
                if isLoading(viewModel.planetState) {
                    LoadingContentView()
                } else if let successState = viewModel.planetState as? UiStateSuccess<Planet> {
                    if let planet = successState.data {
                        PlanetContentView(planet: planet)
                    }
                } else if let errorState = viewModel.planetState as? UiStateError {
                    PlanetErrorContentView(
                        message: errorState.message,
                        onRetry: {
                            viewModel.retryPlanet(selectedPerson.planetId)
                        }
                    )
                }
                
                Spacer()
            }
            .background(Color(.systemGroupedBackground))
        }
    }
}

struct LoadingContentView: View {
    var body: some View {
        VStack(spacing: 16) {
            ProgressView()
                .scaleEffect(1.5)
            
            Text("Loading planet information...")
                .font(.body)
                .foregroundColor(.secondary)
        }
        .padding(32)
    }
}

struct PlanetContentView: View {
    let planet: Planet
    
    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                // Planet Icon
                ZStack {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(Color.accentColor.opacity(0.1))
                        .frame(width: 64, height: 64)
                    
                    Image(systemName: "globe")
                        .foregroundColor(.accentColor)
                        .font(.system(size: 36))
                }
                
                // Planet Details
                VStack(spacing: 16) {
                    PlanetDetailRow(label: "Homeworld:", value: planet.name)
                    PlanetDetailRow(label: "Gravity:", value: planet.gravity)
                    PlanetDetailRow(label: "Terrain:", value: planet.terrain)
                    PlanetDetailRow(label: "Population:", value: planet.population)
                }
                .padding(.horizontal)
            }
            .padding()
        }
    }
}

struct PlanetDetailRow: View {
    let label: String
    let value: String
    
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label)
                .font(.caption)
                .fontWeight(.medium)
                .foregroundColor(.accentColor)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            Text(value)
                .font(.body)
                .foregroundColor(.primary)
                .padding(12)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color(.systemBackground))
                )
        }
    }
}

struct PlanetErrorContentView: View {
    let message: String
    let onRetry: () -> Void
    
    var body: some View {
        VStack(spacing: 16) {
            Text("Failed to load planet")
                .font(.title2)
                .fontWeight(.medium)
                .foregroundColor(.red)
                .multilineTextAlignment(.center)
            
            Text(message)
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
            
            Button("Retry", action: onRetry)
                .buttonStyle(.borderedProminent)
        }
        .padding(32)
    }
}