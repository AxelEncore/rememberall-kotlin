import java.io.File
import kotlin.random.Random

class SafeCharacter(val name: String, var health: Int){

    // Функция с возможной ошибкой расчета урона (потому может поделиться на 0)
    fun calculateDamageRatio(defence: Int): Double{
        // В теории - если defence будет = 0 произойдет деление на 0 (ArithmeticException)
        return 100.0 / defence
    }

    fun demonstrateCommonErrors(){
        try {
            // try - ключевое слово для перехвата ошибок (обозначает блок "попытки")
            // Код внутри try выполняется нормально
            println("Input number of attack power: ")
            val input = readln()
            // toInt() - преобразование строки в число
            // Он выбросит ошибку NumberFormatException - если введены не цифры
            val attackPower = input.toInt()
            println("Attack power is setup to: $attackPower")
        }catch (e: NumberFormatException){
            // catch - ключевое слово для перехвата ошибки в случае возникновения (ловил ошибку)
            // e: NumberFormatException - переменная e типа NumberFormatException
            // Этот блок catch выполнится только, если произошла ошибка указанного типа
            println("Input is not a number! Using default number: 10")
            val attackPower = 10
            println("Attack power is setup to: $attackPower")
        }

        try {
            val items = arrayOf("Sword", "Shield", "Potion")
            println("Choose number of Item (1-3): ")
            val index = readln().toInt() - 1 // Преобразуем в индекс (0-2), потому что пользователь вводит (1-3)
            // [index] может выбросить ArrayIndexOutOfBoundsException,
            val selectedItem = items[index]
            println("You choose: $selectedItem")
        }catch (e: ArrayIndexOutOfBoundsException){
            println("Wrong number of item, chosen default item")
            val selectedItem = "Shield"
            println("You choose: $selectedItem")
        }catch (e: NumberFormatException){
            println("Input is not a number! chosen default item")
            val selectedItem = "Shield"
            println("You choose: $selectedItem")
        }

        try{
            println("Input defend number (NOT A ZERO PLS!)")
            val defence = readln().toInt()
            val ratio = calculateDamageRatio(defence) // Он может вызвать ошибку деления на 0
            println("Damage Ratio: $ratio")
        }catch (e: ArithmeticException){
            println("DUDE! YOU DIVIDED BY ZERO, WTF? Used default ratio: 1.0")
            val ratio = 1.0
            println("Damage Ratio: $ratio")
        }

        // Общий обработчик для любых исключений
        try {
            println("Input game difficult (1-easy, 2-medium, 3-INSANE): ")
            val difficulty = readln().toInt()
            val enemyHealth = when(difficulty){
                1 -> 50
                2 -> 100
                3 -> 300
                else -> throw IllegalArgumentException("Unknown Difficult") // throw - сами бросаем исключение
            }
            println("Enemy Health: $enemyHealth")
        }catch (e: Exception){
            // Exception - базовый класс для ВСЕХ возможных исключений (поймает любую ошибку)
            println("Error! ${e.message}. Used default difficult")
            val enemyHealth = 100
            println("Enemy Health: $enemyHealth")
        }
    }

    fun demonstateFinally(){
        println("\n--- Finally block ---")

        try {
            println("Input attack bonus")
            val bonus = readln().toInt()
            println("Bonus Used: $bonus")
        }catch (e: NumberFormatException){
            println("Unknown number format. Bonus not used")
        }finally{
            // finally - блок который выполняется ВСЕГДА, независимо от того, была ошибка или нет
            // Обычно используется для завершения или очистки ресурсов (закрытие файлов, соединений и тд.)
            println("Block Finally is completed: resources cleared successfully")
        }
    }
}

class SafeInput{
    // ФУНКЦИЯ ДЛЯ БЕЗОПАСНОГО ПОЛУЧЕНИЯ ЧИСЛА
    fun getSafeInput(prompt: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int{
        while (true){ // Бесконечный цикл, пока не получим корректный ввод
            try {
                println(prompt)
                val input = readln()
                val number = input.toInt() // Может выдать ошибку

                // Проверка диапазона
                if(number in min..max){
                    return number
                }else{
                    println("Number need to be in range of $min to $max. Try Again!")
                }
            }catch (e: NumberFormatException){
                println("ERROR PLEASE INPUT INTEGER NUMBER MATHER F%$#ER. Try again bitch")
            }
        }
    }
}




























