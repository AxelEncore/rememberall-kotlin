package lesson13

import lesson12.GameEvent

class TrainingStateGraph{
    private val nodes = mutableMapOf<TrainingState, StateNode>()

    init{
        // init - блок, который выполнится при первом создании объекта

        // Создаем узлы
        val start = StateNode(TrainingState.START)
        val approached = StateNode(TrainingState.APPROACHED)
        val talking = StateNode(TrainingState.TALKING)
        val accepted = StateNode(TrainingState.ACCEPTED)
        val dummy_killed = StateNode(TrainingState.DUMMY_KILLED)
        val completed = StateNode(TrainingState.COMPLETED)

        // Описываем переходы (ЭТО САМ ГРАФ)
        start.addTransition(
            GameEvent.DialogueStarted::class.java,
            TrainingState.APPROACHED
        )
        approached.addTransition(
            GameEvent.DialogueStarted::class.java,
            TrainingState.TALKING
        )

        talking.addTransition(
            GameEvent.DialogueChoiceSelected::class.java,
            TrainingState.ACCEPTED
        )

        accepted.addTransition(
            GameEvent.CharacterDied::class.java,
            TrainingState.DUMMY_KILLED
        )

        dummy_killed.addTransition(
            GameEvent.DialogueChoiceSelected::class.java,
            TrainingState.COMPLETED
        )

        // Кладем ноды на карту
        nodes[start.state] = start
        nodes[approached.state] = approached
        nodes[talking.state] = talking
        nodes[accepted.state] = accepted
        nodes[dummy_killed.state] = dummy_killed
        nodes[completed.state] = completed
    }

    fun getNode(state: TrainingState): StateNode{
        return nodes[state]!!
        // !! - мы уверены в существовании искомого узла
    }
}
