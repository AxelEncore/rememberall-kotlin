import kotlin.random.Random

open class Character(val name: String, var health: Int, val attack: Int){
    val isAlive: Boolean get() = health > 0

    fun takeDamage(damage: Int){
        health -= damage
        println("$name получает $damage")
        if(health <= 0) println("$name пал в бою!")
    }

    fun attack(target: Character){
        if (!isAlive || !target.isAlive) return
        val damage = Random.nextInt(attack - 3, attack + 4) // Случайный урон в диапазоне
        println("$name атакует ${target.name}!")
        target.takeDamage(damage)
    }
}

class Player(name: String, health: Int, attack: Int) : Character(name, health, attack){
    var potions = 3 //Количество зелий здоровья (индивидуальный аттрибут(параметр) дочернего класса)

    fun usePotions(){
        if (potions > 0){
            val healAmount = 30
            health += healAmount
            potions--
            println("$name использует зелье, +${healAmount} теперь у него $health HP")
        }else{
            println("У $name нет зелий, БЕГИ ХИЛА НЕТ")
        }
    }

    fun printStatus(){
        println("=== $name ===")
        println("HP: $health")
        println("АТК: $attack")
        println("Зелья: $potions")
        println("=============")
    }
}

class GameInput{
    // Функция получения числа от пользователя
    fun getNumberInput(prompt: String, min: Int = 1, max: Int = 10): Int{
        while (true){ // Бесконечный цикл - пока мы его не прервем (не получим от пользователя верный ввод)
            print(prompt)

            try {
                // readln() - читаем ввод пользователя (ждет пока пользователь нажмет Enter)
                val input = readln()
                // .toInt() - пытается перевести ввод в число
                val number = input.toInt()

                // Проверка, попадает ли число в допустимый диапазон min max
                if (number in min..max){
                    return number // Возвращает корректное число
                }else{
                    println("ПЖ, введи норм число от $min до $max")
                }
            }catch (e: NumberFormatException){
                // try - catch механизм обработки ошибок и исключений в коде
                // Если toInt не смог преобразовать строку в число (например введены буквы) то сработает это исключение
                // NumberFormatException - неверный формат числа
                println("Ошибка! Ниче тот факт, что нужно ввести число")
            }
        }
    }

    fun getYesNoInput(prompt: String): Boolean{
        while (true){
            print("$prompt (д/н): ")
            val input = readln().lowercase() // Приводит строку к нижнему регистру

            when(input){
                "д", "да", "y", "yes" -> return true
                "н", "нет", "n", "no" -> return false
                else -> print("Пожалуйста введите 'да' или 'нет' или катись отсюда")
            }
        }
    }
}

fun main(){
    println("=== СИСТЕМА ВВОДА ДАННЫХ ===")

    val gameInput = GameInput()
    val player = Player("Игрок", 100, 15)

    println("Создайте вашего персонажа: ")
    println("Введите имя игрока: ")
    val playerName = readln()
    // Пересоздаем игрока с введенным именем
    val customPLayer = Player(if (playerName.isBlank()) "Безымянный" else playerName, 100, 15)
    val playerLevel = gameInput.getNumberInput("Введите уровень сложности (1-легко, 5-сложно): ", 1, 5)
    println("Выбран уровень сложности: $playerLevel, нет пути обратно! >:)")

    var gameRunning = true

    while (gameRunning){
        println("=== ГЛАВНОЕ МЕНЮ ===")
        println("1. Посмотреть статус")
        println("2. Использовать зелье")
        println("3. Выйти из игры")

        val choice = gameInput.getNumberInput("Введите действие: 1, 2, 3")
        when(choice){
            1 -> customPLayer.printStatus()
            2 -> customPLayer.usePotions()
            3 -> {
                gameRunning = false
                println("Выход из игры... бб")
            }
        }
    }

    println("Спасибо за невероятную игру, ты же вернешься?")
}






























