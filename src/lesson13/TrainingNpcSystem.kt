package lesson13

import lesson12.GameEvent
import lesson12.EventBus

class TrainingNpcSystem {
    private val npc = TrainingNpc("Trainer")

    fun register(){
        EventBus.subscribe { event ->
            when(event){
                is GameEvent.DialogueStarted -> {
                    npc.onDialogueStarted(event.playerId)
                }
                is GameEvent.DialogueChoiceSelected -> {
                    npc.onDialogueChoiceSelected(
                        event.playerId,
                        event.choiceId
                    )
                }
                else->{}
            }
        }
    }
    fun playerApproaches(playerId: String){
        npc.onPlayerApproached(playerId)
    }
}