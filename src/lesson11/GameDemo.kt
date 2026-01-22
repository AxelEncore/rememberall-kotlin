package lesson11

fun main(){
    val quest = Quest(
        "Kill_kirill",
        "Kirill"
    )

    val npc = Npc("Oleg")

    // Нужно зарегистрировать нпс и квест в системе событий (подписать их на события)
    quest.register()
    npc.register()

    EventBus.publish(
        GameEvent.CharacterDied(
            "Innokentiry"
        )
    )

    EventBus.publish(
        GameEvent.CharacterDied(
            "Kirill"
        )
    )
}

// Добавить событие:
// DialogStarted
// DialogChoiceSelected

// Сделать NPC, который:
// по событию QuestCompleted меняет своё состояние
// начинает другой диалог

// Связать с прошлым боем:
// при смерти персонажа в боевой системе → publish(CharacterDied)