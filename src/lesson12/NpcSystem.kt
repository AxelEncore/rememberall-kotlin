package lesson12

import kotlin.math.E

class NpcSystem{
    fun register(){
        EventBus.subscribe { event ->
            when(event){
                is GameEvent.QuestStarted -> {
                    println("[NPCSystem] Старый ждет результата")
                    EventBus.post(GameEvent.DialogueLineUnlocked("Старый", "remind_kill_kirill"))
                }

                is GameEvent.QuestStepCompleted -> {
                    if(event.stedId == "kill_kirill"){
                        println("[NPCSystem] Открыта новая реплика 'О, ну шо там с квестом, Кирилл готов?'")
                        EventBus.post(GameEvent.DialogueLineUnlocked("Старый","ask_report"))
                    }
                }

                is GameEvent.QuestCompleted -> {
                    println("[NPCSystem] Квест выполнен, открываем реплику с поздравлением")
                    EventBus.post(GameEvent.DialogueLineUnlocked("Старый", "congrats"))
                }
                else -> {}
            }
        }
    }
}