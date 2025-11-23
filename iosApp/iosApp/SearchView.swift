import SwiftUI
import Shared

struct SearchView: View {
    @ObservedObject var viewModel: StarWarsViewModelWrapper
    @Binding var selectedPerson: Person?
    @Binding var showPlanetModal: Bool
    @EnvironmentObject var themeManager: ThemeManager
    
    var body: some View {
        VStack(spacing: 16) {
            // Header
            Text("Star Wars Character Search")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(themeManager.currentTheme.primary)
                .multilineTextAlignment(.center)
                .padding(.top, 20)
            
            // Search Row (matching Android side-by-side layout)
            HStack(spacing: 8) {
                // Search Field
                HStack {
                    Image(systemName: "magnifyingglass")
                        .foregroundColor(.secondary)
                        .padding(.leading, 8)
                    
                    TextField("Enter character name...", text: $viewModel.searchQuery)
                        .padding(.vertical, 12)
                        .onSubmit {
                            performSearch()
                        }
                }
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(Color.secondary.opacity(0.3), lineWidth: 1)
                )
                .frame(minHeight: 56)
                
                // Search Button
                Button(action: {
                    performSearch()
                }) {
                    HStack {
                        if isLoading(viewModel.searchState) {
                            ProgressView()
                                .scaleEffect(0.8)
                                .progressViewStyle(CircularProgressViewStyle(tint: .white))
                        } else {
                            Text("Search")
                                .fontWeight(.medium)
                        }
                    }
                    .frame(width: 80, height: 56)
                    .background(themeManager.currentTheme.primary)
                    .foregroundColor(themeManager.currentTheme.onPrimary)
                    .cornerRadius(12)
                }
                .disabled(viewModel.searchQuery.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty || isLoading(viewModel.searchState))
            }
            .padding(.horizontal)
            .padding(.bottom, 8)
            
            // Content based on state
            if isIdle(viewModel.searchState) {
                EmptySearchStateView()
            } else if let successState = viewModel.searchState as? UiStateSuccess<NSArray>,
                      let peopleArray = successState.data as? [Person] {
                PeopleListView(
                    people: peopleArray,
                    onPersonTap: { person in
                        selectedPerson = person
                        showPlanetModal = true
                        viewModel.loadPlanet(person.planetId)
                    }
                )
            } else if let errorState = viewModel.searchState as? UiStateError {
                ErrorStateView(
                    message: errorState.message,
                    onRetry: {
                        viewModel.retrySearch()
                    }
                )
            }
            
            Spacer()
        }
        .padding()
    }
    
    // MARK: - Helper Methods
    private func performSearch() {
        let trimmedQuery = viewModel.searchQuery.trimmingCharacters(in: .whitespacesAndNewlines)
        if !trimmedQuery.isEmpty {
            viewModel.searchPeople(trimmedQuery)
        }
    }
}

struct EmptySearchStateView: View {
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "magnifyingglass")
                .font(.system(size: 64))
                .foregroundColor(.secondary)
            
            Text("Search for Star Wars characters")
                .font(.title2)
                .fontWeight(.medium)
                .multilineTextAlignment(.center)
            
            Text("Enter a character name to discover their homeworld")
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
        }
        .padding(32)
    }
}

struct ErrorStateView: View {
    let message: String
    let onRetry: () -> Void
    
    var body: some View {
        VStack(spacing: 16) {
            Text("Oops! Something went wrong")
                .font(.title2)
                .fontWeight(.medium)
                .foregroundColor(.red)
                .multilineTextAlignment(.center)
            
            Text(message)
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
            
            Button("Try Again", action: onRetry)
                .buttonStyle(.bordered)
        }
        .padding(32)
    }
}