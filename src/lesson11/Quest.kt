package lesson11

class Quest(
    val id: String,
    val targetCharacter: String
){
    var isCompleted: Boolean = false

    fun register(){
        // Регистрация квеста в системе событий

        EventBus.subscribe { event ->
            // Эта функция будет вызываться каждый раз, когда в игре случается каое-то событие

            when(event){
                is GameEvent.CharacterDied -> {
                    if (event.characterName == targetCharacter && !isCompleted){
                        // Проверили соответсвует ли убитый моб цели квеста и не выполнен ли еще квест
                        isCompleted = true
                        println("Квест $id выполен! Моб ${event.characterName} повержен")

                        // Вызываем метод рассылки события выполнения квеста
                        EventBus.publish(
                            GameEvent.QuestCompleted(id)
                        )
                    }
                }
                else -> {
                    // else - все остальные события будут проигнорированы
                }
            }
        }
    }
}
