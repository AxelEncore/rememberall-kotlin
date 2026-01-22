package lesson13

import lesson12.GameEvent

// Узлы графа (State Node)
// Будем явно описывать каждый переход, а не прятать их за if else

class StateNode(
    val state:  TrainingState
    // Какое состояние представляет этот узел
){
    // Все возможные переходы ИЗ этого состояния
    private val transitions = mutableMapOf<Class<out GameEvent>, TrainingState>()
    // Класс GameEvent ИЛИ ЛЮБОЙ класс, который от него наследуется
    // GameEvent - это иерархия событий (а события от нее наследуются)
    // out - модификатор ковариантности, используется в обобщенных generic типах данных (классах)
    // Используется для указания, что параметризованный тип может быть использован в иерархии
    // Если пишем класс, который должен вернуть строго тип данных GameEvent, то используем out
    // Словарь: ключ - тип события (например DialogueStarted)
    // Значение - в какое состояние перейдем при данном событии

    fun addTransition(
        eventType: Class<out GameEvent>,
        nextState: TrainingState
    ){
        transitions[eventType] = nextState
    }

    fun getNextState(event: GameEvent): TrainingState? {
        // Достаем класс события и ищем куда он может перейти в случае этого события
        return transitions[event::class.java]
        // СОБЫТИЕ -> СОСТОЯНИЕ
        // event - Kotlin
        // event::class - Язык программирования
        // event::class - скажи мне, КАКОГО ТИПА данный объект
        // :: - оператор ссылки
        // Он НЕ ИСПОЛЬЗУЕТ ОБЪЕКТ, а только ссылается на информацию о нем
        // ссылка на класс объекта event
        // .java - зачем?
        // Kotlin и java существуют вместе одновременно
        // Kotlin работает поверх JVM (Java Virtual Machine)
        // И создаваемый нами Map существует как Java-класс
        // event::class      - Kotlin класс
        // event::class.java - Java класс
        // ВАЖНО ЭТО НЕ 2 РАЗНЫХ КЛАССА - это 2 разных формы записи одного и того же типа
        // Java тип здесь нам нужен, потому что Map<Class<>,..> - использует именно Class из Java
        // return transitions[event::class.java] - по-человечески
        // "Взять тип события, которое пришло, найти в таблице переходов(map), то в какое состояние
        // мы должны перейти и верни его нам"
        // Пример: Если приходит событие DialogueStarted то код сделает:
        // transitions[DialogueStarted] -> TALKING
        // Если события в таблице не найдется -> вернет null
    }
}

