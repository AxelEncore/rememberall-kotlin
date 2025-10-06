//import kotlin.random.Random
//
//// БАЗОВЫЙ КЛАСС - его важно сделать open чтобы его смогли наследовать
//open class Character(
//    val name: String,
//    maxHealth: Int,
//    baseAttack: Int
//){
//    // protected - доступен для использования в классе и наследниках этого класса
//    protected var _health = maxHealth.coerceAtLeast(1)
//    protected val _maxHealth = maxHealth.coerceAtLeast(1)
//    protected val _attack = baseAttack.coerceAtLeast(1)
//
//    // open - разрешаем переопределение метода в наследниках класса
//    open val health: Int
//        get() = _health
//
//    open val maxHealth: Int
//        get() = _maxHealth
//
//    open val attack: Int
//        get() = _attack
//
//    open val isAlive: Boolean
//        get() = _health > 0
//
//    init {
//        println("Создан персонаж: $name")
//    }
//
//    open fun takeDamage(damage: Int){
//        if (!isAlive) return
//
//        val actualDamage = damage.coerceAtLeast(0)
//        _health -= actualDamage
//        println("$name получает $actualDamage урона! Осталось: ${_health} HP")
//
//        if (_health <= 0){
//            println("$name погиб")
//        }
//    }
//
//    open fun heal(amount: Int){
//        if (!isAlive) return
//
//        val healAmount = amount.coerceAtLeast(0)
//        _health = (_health + healAmount).coerceAtMost(_maxHealth)
//        println("$name похилился на $healAmount")
//    }
//
//    open fun attack(target: Character){
//        if (!isAlive || !target.isAlive) return
//
//        val damage = calculateDamage(_attack)
//        println("$name атакует ${target.name}!")
//        target.takeDamage(damage)
//    }
//
//    open fun printStatus(){
//        val status = if(isAlive) "Жив" else "Нежив"
//        println("$name: $_health/$_maxHealth HP, ATK: $_attack ($status)")
//    }
//}
//
//// КЛАСС-НАСЛЕДНИК WARRIOR (ДОЧЕРНИЙ КЛАСС)
//// : Character(name, maxHealth, baseAttack) - наследование и вызов конструктора родителя
//class Warrior(name: String, maxHealth: Int, baseAttack: Int) : Character(name, maxHealth, baseAttack){
//
//    // Дополнительное свойство, специфичное для Класса Воина
//    var armor: Int = 5
//
//    // Переопределение метода получения урона с учетом брони
//    override fun takeDamage(damage: Int) {
//        if(!isAlive) return
//
//        val reducedDamage = (damage - armor).coerceAtLeast(0)
//        println("Броня $name поглащает $armor урона")
//        // super - вызов метода родительского класса
//        super.takeDamage(reducedDamage)
//    }
//
//    // Уникальная способность воина (уникальный метод)
//    fun powerfullStrike(target: Character){
//        if (!isAlive) return
//
//        val cost = 10 // Стоимость способности в очках
//        if (_health > cost){
//            _health -= cost // Тратим здоровье для усиленной атаки
//            val damage = calculateDamage(_attack * 2) // Удвоенный урон
//            println("$name использует усиленную атаку")
//            target.takeDamage(damage)
//        }else{
//            println("у $name недостаточно HP для мощной атаки")
//            attack(target)
//        }
//    }
//}
//
//class Mage(name: String, maxHealth: Int, baseAttack: Int) : Character(name, maxHealth, baseAttack){
//
//    var mana: Int = 100
//    val maxMana: Int = 100
//
//    override fun attack(target: Character) {
//        if (!isAlive || !target.isAlive) return
//
//        if (mana >= 10){
//            mana -= 10
//            val damage = calculateDamage(_attack + 5) // Бонус к базовой атаке
//            println("$name атакует магическим посохом и тратит 10 маны")
//            target.takeDamage(damage)
//            println("Осталось маны: $mana/$maxMana")
//        }else{
//            // Обычная атака, если маны
//            println("У $name недостаточно маны")
//            super.attack(target) // Вызов базовой (не измененной) реализации атаки
//        }
//    }
//
//    fun castFireball(target: Character){
//        if (!isAlive) return
//
//        val cost = 30
//
//        if (mana >= 30){
//            mana -= cost
//            val damage = calculateDamage(_attack * 3) // Тройной урона FireBALLS
//            println("$name кастует fireball и тратит $cost маны")
//            target.takeDamage(damage)
//        }else{
//            attack(target)
//        }
//    }
//
//    override fun printStatus(){
//        val status = if (isAlive) "живчик" else "мертвунчик"
//        println("$name: $_health/$_maxHealth HP, MANA: $mana/$maxMana, АТК: $_attack ($status)")
//    }
//}
//
//fun main(){
//    println(" === БОЙ С СИТЕМОЙ КЛАССОВ ПЕРСОНАЖЕЙ ЁУ ===")
//
//    val warrior = Warrior("Сеньер Помидор", 120, 16)
//    val mage = Mage("Баба зина калдун", 80, 10)
//    val enemy = Warrior("Полурак, полуОрк", 100, 14)
//
//    println(">>> НАЧАЛО БОЯ <<<")
//    warrior.powerfullStrike(enemy)
//    mage.castFireball(enemy)
//    enemy.attack(warrior)
//
//    println(" --- Статусы персонажей --- ")
//    warrior.printStatus()
//    mage.printStatus()
//    enemy.printStatus()
//
//
//    warrior.takeDamage(20)
//    mage.attack(enemy)
//}
//
//
////1. Создайте класс Archer (Лучник) с дополнительным свойством agility (ловкость).
////  Переопределите метод attack так, чтобы ловкость добавляла шанс
////  на критический удар
////  (удвоенный урон с вероятностью 30%).
////
////2. Добавьте в класс Character свойство level (уровень) и метод levelUp(),
////  который увеличивает уровень и немного повышает характеристики.
////  Создайте простую систему боя, где персонажи получают опыт за победу.
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
