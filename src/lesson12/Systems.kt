package lesson12

// Система логирования
class LogSystem{
    fun register(){

        EventBus.subscribe { event ->
            println("[INFO] Получено событие: $event")
        }
        // fun chel (GameEvent) { sfdsfsf } // Обычная функция
        // (GameEvent) -> {} // Лямбда
        // null - значение (переменные)
        // Unit - (структура, набор команд или тело функции)
    }
}

class AchievementSystem{
    private var killCount: Int = 0

    fun register(){
        EventBus.subscribe { event ->
            when(event){
                // when is это замена switch case - он проверяет в роли условия полученное событие
                is GameEvent.CharacterDied -> {
                    // Проверяем убийца игрок - то прибавить к счетчику
                    if (event.killerName == "Oleg"){
                        killCount++
                        println("Счетчик убийств Олега: $killCount")

                        if (killCount == 1){
                            EventBus.post(GameEvent.AchievementUnlocked("first_blood"))
                        }
                        if (killCount == 5){
                            EventBus.post(GameEvent.AchievementUnlocked("ti_cho_delaesh"))
                        }
                        // Важно нельзя все события грубо отправлять на выполнение здесь и сейчас через publish
                        // post нужен для строгой очереди выполнения игровых событий.
                        // Нужно это например, чтобы одно событие не выполнилось раньше другого, не перекрыло другое
                        // Не наложилось на другое или логически не сломало порядок событий
                    }
                }
                else -> {}
            }
        }
    }
}
