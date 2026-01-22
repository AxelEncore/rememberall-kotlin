package lesson11

// EventBus - это основы игровой системы событий
// С его помощью обрабатываются и рассылаются все события в игре

// object - это Singleton
// Это объект в единственном экземпляре на всю программу
object EventBus{
    // Список слушателей (подписчиков)
    private val listeners = mutableListOf<(GameEvent) -> Unit>()
    // (GameEvent) -> Unit Это лямбда функция, она принимает в роли параметра событие
    // И возвращает Unit (ничего - пустоту)

    fun subscribe(listener: (GameEvent) -> Unit){
        // Метод подписки на событие
        listeners.add(listener)
        // add - добавить нового подписчика в спискок подписанных на события

        println("Новый подписчик добавлен. Всего ${listeners.size}")
    }

    fun publish(event: GameEvent){
        // Рассылка событий слушателям
        println("Событие $event разослано")

        for (listener in listeners){
            listener(event)
            // Вызов функции слушателя
        }
    }
}