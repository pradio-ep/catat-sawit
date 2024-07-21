package pradio.ep.catatsawit.util.state

sealed class SortingState {
    object Date: SortingState()
    object Driver: SortingState()
    object License: SortingState()
}