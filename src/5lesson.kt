import kotlin.random.Random

// УЛУЧШЕННЫЙ КЛАСС CHARACTER - С КОНСТУРКТОРОМ
// (...) Памаметры в скобках - парметры главного конструктора
class Character (
    // val - параметр автоматически становится свойствами класса
    val name: String,
    // Без val - это просто параметр конструктора (ЭТО НЕ СВОЙСТВО КЛАССА)
    maxHealth: Int,
    baseAttack: Int
){
    // ИНКАПСУЛЯЦИЯ - делает внутренние свойства приватными (private)
    // private - модификатор доступа. Означается что свойство доступно для использования только внутри класса
    private var _health = maxHealth.coerceAtLeast(1) // coerceAtLeast(1) - гарантирует мин. здоровье = 1

    // Публичное свойство health только для чления (val)
    // Другие классы могут узнать количество здоровья, НО НЕ МОГУТ изменить
    val health: Int
        get() = _health // get() - геттер, возвращает только значение приватного _health

    private val _maxHealth = maxHealth.coerceAtLeast(1)
    val maxHealth: Int
        get() = _maxHealth

    private val _attack = baseAttack.coerceAtLeast(1)
    val attack: Int
        get() = _attack

    // Вычисляемое свойство - Свойство которое не хранится, а вычесляется при каждом обращении
    val isAlive: Boolean
        get() = _health > 0

    // Блок init выполняется при создании объекта (при инициализации свойств)
    init {
        println("Создан новый персонаж: $name (HP: $_health/$_maxHealth, АТК: $_attack)")
    }

    // МЕТОДЫ С ПРОВЕРКАМИ (ИНКАПСУЛЯЦИЯ В ДЕЛЕ)
    fun takeDamage(damage: Int){
        if (!isAlive){
            println("$name уже мертв и не может получить дамаге!")
            return
        }

        val actualDamage = damage.coerceAtLeast(0) // Урон не может быть отрицательным
        _health -= actualDamage // _health = _health - actualDamage
        println("$name получает $actualDamage дамага! Осталось здоровья: $_health")

        if (_health <= 0){
            println("$name пал в бою за брейнрот утопию")
        }
    }

    fun heal(amount: Int){
        if (!isAlive){
            println("$name уже мертв и не может похилиться!")
            return
        }

        val healAmount = amount.coerceAtLeast(0)

        _health = (_health + healAmount).coerceAtMost(_maxHealth)
        println("$name восстанавливает себе $healAmount здоровья, теперь у него: $_health/$_maxHealth HP")
    }

    fun attack(target: Character){
        if (!isAlive){
            println("$name уже мертв и не может тыщ тыщ тыщ!")
            return
        }

        if (!target.isAlive){
            println("${target.name} уже мертв, хватит его бить")
            return
        }

        val damage = calculateDamage(_attack)
        println("$name атакует ${target.name}")
        target.takeDamage(damage)
    }

    fun printStatus(){
        val status = if(isAlive) "Жив" else "Нежив"
        println("$name: $_health/$_maxHealth HP, ATK: $_attack ($status)")
    }
}

fun main(){
    println("==== УЛУЧШЕННАЯ СИСТЕМА СОЗДАНИЯ НАЙШИХ ПЕРСОНАЖЕЙ ====")
    val player = Character("OLEG", 80, 5)
    val monster = Character("Tolik", 120, 10)

    player.attack(monster)
    monster.attack(player)

    println("--- Состояние после раунда 1---")
    player.printStatus()
    monster.printStatus()

    // КАК ДЕЛАТЬ НЕЛЬЗЯ (РАССТРЕЛ)
    // player.health = 100 // ОШИБКА!!! health - val (только для чтения)
    // player._health = 100 // ОШИБКА!!! _health - private
    // ИНКАПСУЛЯЦИЯ - защищает нас, чтобы не сделать что-то неправильное в программе

    val healer = Character("Жрец", 60, 3)
    val warrior = Character("Щитовидный железный", 95, 8)

    warrior.attack(monster)
    healer.heal(warrior.health)
}













