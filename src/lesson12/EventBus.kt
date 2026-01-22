package lesson12

import kotlin.math.max

object EventBus {
    // Сохранение типа слушателя //
    typealias Listener = (GameEvent) -> Unit
    // "Переменная" но для типов данных
    // Создание нового более короткого псевдонима для типа данных
    // То есть вместо (GameEvent) -> Unit будем писать его псевдоним Listener

    // Список подписчиков(слушателей) хранить в Map
    // Чтобы можно было опираться на id подписчика в базе
    private val listeners = mutableMapOf<Int, Listener>()
    private var nextId: Int = 1
    // id следующего подписчика

    // очередь событий (пошаговой обработки событий)
    private val eventQueue = ArrayDeque<GameEvent>()
    // ArrayDeque - двусторонняя очередь
    // Здесь будут храниться события, которые мы захотим обработать позже

    fun subscribe(listener: (GameEvent) -> Unit): Int {
        val id = nextId
        nextId += 1

        listeners[id] = listener

        println("Подсписчик добавлен id=$id Всего подписчиков: ${listeners.size}")
        return id
    }

    fun unsubscribe(id: Int){
        val removed = listeners.remove(id)

        if (removed != null){
            println("Подписчик удален. id = $id")
        }else{
            println("Не удалось отписаться, не найден id=$id")
        }
    }

    fun subscribeOnce(listener: Listener): Int {
        // Одноразовая подписка на события
        // Слушатель сам отпишется после первого полученного события

        var id: Int = -1
        // Врем. переменная для id
        id = subscribe { event ->
            listener(event)

            unsubscribe(id)
            // Отреагировали и сразу отписались
        }

        return id
    }

    fun publish(event: GameEvent){
        // Сразу публирует и выполняет событие, мгновенно вызывая подписчиков
        println("Событие опубликовано: $event")
        for (listener in listeners.values){
            listener(event)
        }
    }

    fun post(event: GameEvent){
        // post - отложить события в очередь выполнения (выполнится не сразу)
        eventQueue.addLast(event)
        // addLast - добавление в конец очереди
        println("Событие $event добавлено в очередь (в очереди ${eventQueue.size}")
    }

    fun processQueue(maxEvent: Int = 10){
        var processed = 0

        while (processed < maxEvent && eventQueue.isNotEmpty()){
            val event = eventQueue.removeFirst()
            // Достает и удаляет первый элемент из очереди

            publish(event)

            processed++ // processed += 1 // processed = processed + 1
        }
    }
}