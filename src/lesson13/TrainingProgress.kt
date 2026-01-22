package lesson13

import lesson12.GameEvent

// Персональные состояния каждого игрока

class TrainingProgress{
    private val graph = TrainingStateGraph()

    private val currentStateByPlayer = mutableMapOf<String, TrainingState>()

    fun getState(playerId: String): TrainingState{
        return currentStateByPlayer.getOrPut(playerId) { TrainingState.START }
        // Если не находит активного состояния игрока - вернуть начальное состояние
    }

    fun handleEvent(playerId: String, event: GameEvent){
        val currentState = getState(playerId)
        val node = graph.getNode(currentState)

        val nextState = node.getNextState(event)

        if (nextState != null){
            println("[STATE GRAPH] $playerId перешел из состояния ($currentState) -> в ($nextState)")
            currentStateByPlayer[playerId] = nextState
            // Обновляем (сохраняем) новое состояние или новый этап прогресса для конкретного игрока
        }else{
            println("[STATE GRAPH] $playerId проигнорировал событие ${event::class.simpleName} состояние ($currentState) не изменено")
        }
    }
}