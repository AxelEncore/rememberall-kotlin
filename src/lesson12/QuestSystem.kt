package lesson12

import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import java.awt.print.Printable
import kotlin.math.E

// Квест = шаги выполения (mini State Graph)
class QuestSystem{
    // Флаги контроля состояния квеста
    data class QuestProgressState(
        var questStarted: Boolean = false,
        var stepTalked: Boolean = false,
        var killKirill: Boolean = false,
        var reportedBack: Boolean = false,
        var questCompleted: Boolean = false
    )

    private val questId = "simple_dimple_quest_001"
    private val progressByPlayer: MutableMap<String, QuestProgressState> = mutableMapOf()
    // progressByPlayer[playerId] - проверка состояния прохождения квеста конкретным игроком

    fun register(){
        EventBus.subscribe { event ->
            when(event){
                is GameEvent.DialogueStarted -> {
                    if (event.npcName == "Старый"){
                        val state = getState(event.playerId)

                        if (!state.questStarted) {
                            state.questStarted = true
                            state.stepTalked = true
                            println("Квест $questId начат игроком ${event.playerId} через диалог с ${event.npcName}")

                            EventBus.post(GameEvent.QuestStarted(event.playerId, questId))
                            completeStep(event.playerId, "talk_to_npc")
                        }
                    }
                }

                is GameEvent.CharacterDied -> {
                    val state = getState(event.playerId)
                    if(state.questStarted &&
                       !state.killKirill &&
                        event.characterName == "Kirill" &&
                        event.killerName == "Oleg"
                        ){
                        state.killKirill = true
                        println("Игрок ${event.playerId} выполнил шаг квеста $questId: убить Кирилла")
                        completeStep(event.playerId, "kill_kirill")
                    }
                }

                is GameEvent.DialogueChoiceSelected -> {
                    if (event.npcName == "Старый" && event.choiceId == "report_done") {
                        val state = getState(event.playerId)
                        if (state.questStarted && state.killKirill && !state.reportedBack){
                            state.reportedBack = true
                            println("Игрок ${event.playerId} сдал квест")
                            completeStep(event.playerId, "report_back")
                        }
                    }
                }
                else -> {}
            }
            checkQuestCompletionForAllPlayers()
        }
    }

    private fun getState(playerId: String): QuestProgressState{
        return progressByPlayer.getOrPut(playerId) { QuestProgressState() }
    }

    private fun completeStep(playerId: String, stepId: String){
        EventBus.post(GameEvent.QuestStepCompleted(playerId, questId, stepId))

        EventBus.post(GameEvent.PlayerProgressSaved(playerId, questId, stepId))
    }

    private fun checkQuestCompletionForAllPlayers(){
        for((playerId, state) in progressByPlayer){

            if(!state.questCompleted &&
                state.questStarted &&
                state.stepTalked &&
                state.killKirill &&
                state.reportedBack
                ){
                    state.questCompleted = true
                    println("Квест ${questId} завершен для игрока $playerId")
                EventBus.post(GameEvent.QuestCompleted(playerId, questId))
                EventBus.post(GameEvent.PlayerProgressSaved(playerId, questId, "QUEST_COMPLETED"))
            }
        }
    }
}