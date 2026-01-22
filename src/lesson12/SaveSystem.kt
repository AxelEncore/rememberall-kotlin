package lesson12

class SaveSystem{
    private val progress: MutableMap<String, MutableMap<String, MutableSet<String>>> = mutableMapOf()
    // progress[playerId][questId] = Набор выполненных шагов игрока
    // MutableMap<String, ... > - ключ playerId (словарь ключем которого будет игрок, а значения - его прогресс)
    // MutableMap<String, MutableSet<String>> - ключ questId - все квесты игрока со всеми его шагами квеста
    // MutableSet<String> - набор шагов (stepId) которые уже выполнены игроком в квесте

    fun register(){
        EventBus.subscribe { event ->
            when(event) {
                is GameEvent.PlayerProgressSaved -> {
                    saveStep(event.playerId, event.questId, event.stepId)
                }
                else -> {}
            }
        }
    }

    fun saveStep(playerId: String, questId: String, stepId: String){
        val playerData = progress.getOrPut(playerId) {mutableMapOf()}
        // getOrPut(key) {...} - ищет ключ, если находит его достает его значение
        // Если не находит, то задает ему значение которое мы положили в { ... }

        val questSteps = playerData.getOrPut(questId) {mutableSetOf()}

        val wasAdded = questSteps.add(stepId)
        // Если ша впервые добавлен вернет true, если шаг уже сохранен - false

        if (wasAdded){
            println("[SAVE] Сохранено: игрок=$playerId квест=$questId шаг=$stepId")
        }else{
            println("[SAVE] Шаг уже был сохранен ранее: игрок=$playerId квест=$questId шаг=$stepId")
        }
    }
    fun printProgress(playerId: String){
        println("=== ПРОГРЕСС ИГРЫ ДЛЯ playerId=$playerId ===")

        val playerData = progress[playerId]
        if (playerData == null){
            println("Прогресса нет")
            return
        }
        for ((questId, steps) in playerData){
            println("Квест: $questId")
            println("Шаги: $steps")
            println("================")
        }
    }
}