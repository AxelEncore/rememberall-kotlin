package lesson12

fun main(){
    // Регистрация систем
    val logSystem = LogSystem()
    logSystem.register()

    val achievementSystem = AchievementSystem()
    achievementSystem.register()

    val questSystem = QuestSystem()
    questSystem.register()

    val npcSystem = NpcSystem()
    npcSystem.register()

    println("=== СЦЕНА 1: Игрок начинаем диалог с неписем")

    EventBus.post(GameEvent.DialogueStarted("Старый", "Oleg"))

    EventBus.processQueue(50)

    println("=== СЦЕНА 2: Процесс боя")
    val combat = CombatSystemDemo()
    combat.simulateFight()

    EventBus.processQueue(50)

    println("=== СЦЕНА 3: игрок возвращается с докладом и головой Кирилла")

    EventBus.post(
        GameEvent.DialogueChoiceSelected(
            "Старый",
            "Oleg",
            "report_done"
        )
    )
    EventBus.processQueue(50)
}


//1. подписка “один раз”
//В main :
//создать один слушатель subscribeOnce, который реагирует только на первое событие.
//Например:
//«Первое событие в игре!»

//2. сделать “персональный прогресс игрока”
//Создать событие:
//PlayerProgressSaved(playerName, questId, stepId)
//В QuestSystem после каждого QuestStepCompleted публиковать это событие.

//3. разные игроки
//Сделай во всех событиях поле:
//playerId: String
//Чтобы квест выполнялся не глобально, а отдельно для каждого игрока.
//Проверяй конкретного игрока

////////////////////////////////////////////////////////
// Квест с двумя вариантами завершения
// Сделать квест: kill_kirill_or_pay_gold

// Квест стартует.
// Игрок может:

// убить Кирилла -> событие CharacterDied
// ИЛИ выбрать диалог pay_gold

// В QuestSystem:
// при убийстве -> квест завершён «убийством»
// при оплате -> квест завершён «деньгами»
// В консоли должно быть видно, какой путь выбран.
////////////////////////////////////////////////////////////////