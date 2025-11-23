import SwiftUI
import Shared

// MARK: - UiState Helper Functions
func isIdle(_ state: Any) -> Bool {
    return state is UiStateIdle
}

func isLoading(_ state: Any) -> Bool {
    return state is UiStateLoading
}

func isSuccess(_ state: Any) -> Bool {
    return state is UiStateSuccess<AnyObject>
}

func isError(_ state: Any) -> Bool {
    return state is UiStateError
}