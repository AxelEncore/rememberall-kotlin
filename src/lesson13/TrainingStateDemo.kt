package lesson13

import lesson12.EventBus
import lesson12.GameEvent

fun main(){
    val system = TrainingStateSystem()
    system.register()

    val player = "Oleg"

    EventBus.post(GameEvent.DialogueStarted(player, "Trainer", player))
    EventBus.processQueue()

    EventBus.post(GameEvent.DialogueStarted(player, "Trainer", player))
    EventBus.processQueue()

    EventBus.post(GameEvent.DialogueChoiceSelected(player, "Trainer", player, "accept"))
    EventBus.processQueue()

    EventBus.post(GameEvent.CharacterDied(player, "Dummy", player))
    EventBus.processQueue()

    EventBus.post(GameEvent.DialogueChoiceSelected(player, "Trainer", player, "complete"))
    EventBus.processQueue()
}