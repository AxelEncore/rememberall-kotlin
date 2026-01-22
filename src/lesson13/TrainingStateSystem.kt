package lesson13

import lesson12.EventBus
import lesson12.GameEvent

class TrainingStateSystem{
    private val progress = TrainingProgress()

    fun register(){
        EventBus.subscribe { event ->
            when(event){
                is GameEvent.DialogueStarted,
                is GameEvent.DialogueChoiceSelected,
                is GameEvent.CharacterDied -> {
                    progress.handleEvent(event.playerId, event)
                }
                else -> {}
            }
        }
    }
}