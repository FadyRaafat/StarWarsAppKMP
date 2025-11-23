import SwiftUI
import Shared

@MainActor
class StarWarsViewModelWrapper: ObservableObject {
    private let viewModel: StarWarsViewModel
    
    @Published var searchState: Any = UiStateIdle()
    @Published var planetState: Any = UiStateIdle()
    @Published var searchQuery: String = "" {
        didSet {
            // Update the ViewModel immediately when the search query changes
            viewModel.updateSearchQuery(query: searchQuery)
        }
    }
    
    // Computed property to get people from search state
    var searchPeople: [Person] {
        if let successState = searchState as? UiStateSuccess<AnyObject>,
           let peopleArray = successState.data as? NSArray {
            return peopleArray.compactMap { $0 as? Person }
        }
        return []
    }
    
    init() {
        // Use the factory function from ViewModelFactoryKt
        viewModel = ViewModelFactoryKt.createStarWarsViewModel()
        setupInitialState()
    }
    
    deinit {
        viewModel.onCleared()
    }
    
    private func setupInitialState() {
        // Set initial states
        searchState = UiStateIdle()
        planetState = UiStateIdle()
        searchQuery = ""
    }
    
    private func updateSearchState() {
        // Get current search state from ViewModel and update UI
        let currentSearchState = viewModel.searchState.value
        if let kotlinSearchState = currentSearchState {
            let swiftSearchState = convertKotlinUiStateToSwift(kotlinSearchState)
            DispatchQueue.main.async { [weak self] in
                self?.searchState = swiftSearchState
            }
        }
    }
    
    private func updatePlanetState() {
        // Get current planet state from ViewModel and update UI
        let currentPlanetState = viewModel.planetState.value
        if let kotlinPlanetState = currentPlanetState {
            DispatchQueue.main.async { [weak self] in
                self?.planetState = kotlinPlanetState
            }
        }
    }
    
    private func startStateMonitoring() {
        // Monitor search state changes for a limited time
        var attempts = 0
        let maxAttempts = 50 // 5 seconds max
        
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] timer in
            guard let self = self else {
                timer.invalidate()
                return
            }
            
            attempts += 1
            self.updateSearchState()
            
            // Stop monitoring if we're no longer loading or max attempts reached
            if !isLoading(self.searchState) || attempts >= maxAttempts {
                timer.invalidate()
            }
        }
    }
    
    private func startPlanetStateMonitoring() {
        // Monitor planet state changes for a limited time
        var attempts = 0
        let maxAttempts = 50 // 5 seconds max
        
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] timer in
            guard let self = self else {
                timer.invalidate()
                return
            }
            
            attempts += 1
            self.updatePlanetState()
            
            // Stop monitoring if we're no longer loading or max attempts reached
            if !isLoading(self.planetState) || attempts >= maxAttempts {
                timer.invalidate()
            }
        }
    }
    
    private func convertKotlinUiStateToSwift(_ kotlinState: Any) -> Any {
        if kotlinState is UiStateIdle {
            return UiStateIdle()
        } else if kotlinState is UiStateLoading {
            return UiStateLoading()
        } else if let successState = kotlinState as? UiStateSuccess<AnyObject> {
            if let peopleArray = successState.data as? [Person] {
                let nsArray = NSArray(array: peopleArray)
                return UiStateSuccess(data: nsArray as AnyObject)
            }
            return successState
        } else if let errorState = kotlinState as? UiStateError {
            return errorState
        }
        
        return UiStateIdle()
    }
    
    private func areStatesEqual(_ state1: Any, _ state2: Any) -> Bool {
        switch (state1, state2) {
        case (is UiStateIdle, is UiStateIdle):
            return true
        case (is UiStateLoading, is UiStateLoading):
            return true
        case (let s1 as UiStateSuccess<AnyObject>, let s2 as UiStateSuccess<AnyObject>):
            if let arr1 = s1.data as? NSArray, let arr2 = s2.data as? NSArray {
                return arr1.count == arr2.count
            }
            return false
        case (let e1 as UiStateError, let e2 as UiStateError):
            return e1.message == e2.message
        default:
            return false
        }
    }
    
    func updateSearchQuery(_ query: String) {
        searchQuery = query
    }
    
    func searchPeople(_ query: String) {
        // Set loading state immediately
        searchState = UiStateLoading()
        
        viewModel.searchPeople(query: query)
        
        // Start monitoring for state changes
        startStateMonitoring()
    }
    
    func loadPlanet(_ planetId: String) {
        // Set loading state immediately
        planetState = UiStateLoading()
        
        viewModel.loadPlanet(planetId: planetId)
        
        // Monitor for planet state changes
        startPlanetStateMonitoring()
    }
    
    func clearPlanetState() {
        viewModel.clearPlanetState()
        planetState = UiStateIdle()
    }
    
    func retrySearch() {
        if !searchQuery.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            searchPeople(searchQuery)
        }
    }
    
    func retryPlanet(_ planetId: String) {
        loadPlanet(planetId)
    }
}