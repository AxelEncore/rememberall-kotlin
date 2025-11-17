import kotlinx.coroutines.*
// * - значит импортировать все методы и классы из Корутин
import kotlin.random.Random
// Корутины - Легкие потоки, они позволяют выполнять несколько задач одновременно, без блокировки основного потока
// Global Scope - область видимости для корутин. Где будут жить "жить" корутины во время работы всего приложения
// launch - запуск новой корутины
// delay - приостановка корутины, на указанное нами время, не блокируя другие потоки(корутины)

// ТЕСТОВЫЙ КЛАСС ДЛЯ ДЕМОНСТРАЦИИ ПРИМИТИВНЫХ КОРУТИН
class BasicCoroutinesDemo{
    // ФУНКЦИИ С ЗАДЕРЖКОЙ
    // suspend - ключевое слово, что отмечает функцию, которую можно остановить
    // !!! suspend функции можно вызвать только из других suspend функцией или корутин
    suspend fun simpleDelayDemo(){
        println("=== ДЕМО ЗАДЕРЖКИ КОРУТИНЫ ===")
        println(" Начало выполнения: ${System.currentTimeMillis()} ")

        // 1000L - время в миллисекундах (1000мс - 1 сек)
        delay(1000L) // Приостановка корутины на 1 сек

        println("Прошла 1 секунда: ${System.currentTimeMillis()}")

        delay(500L)

        println("Прошло ещё 0.5 сек: ${System.currentTimeMillis()}")
    }

    fun multipleCoroutinesDemo(){
        println("=== Demo запуска нескольких корутин ===")

        // GlobalScope.launch - запускает новую корутину в глобальной области видимости
        // внутри GlobalScope {} - лямбда функция или же тело запускаемой корутины
        GlobalScope.launch {
            // Это 1 корутина
            println("Корутина 1 - начала работу")
            delay(1000L)
            println("Корутина 1 - завершила свое выполнение")
        }
        GlobalScope.launch {
            // Это 2 корутина
            println("Корутина 2 - начала работу")
            delay(500L)
            println("Корутина 2 - завершила свое выполнение")
        }
        GlobalScope.launch {
            // Это 3 корутина
            println("Корутина 3 - начала работу")
            delay(1000L)
            println("Корутина 3 - завершила свое выполнение")
        }

        println("Все корутины завершены! Основной поток продолжает свою работу...")
    }

    suspend fun animationDemo(){
        println("\n=== DEMO анимации ===")

        var playerName= "Oleg"

        // Анимация появления текста по буквам
        for (i in 1..4){
            print(".")
            delay(200L)
        }
        println()

        val message = "Добро пожаловать, $playerName!"
        for (char in message){
            print(char)
            delay(50L)
        }
        println()

        println("\nЗагрузка игры...")
        val loadingFrames = listOf("⣾", "⣽", "⣻", "⢿", "⡿", "⣟", "⣯", "⣷")

        repeat(17) { frame ->
            // % - оператор остатка от деления, позволяет циклически перебирать frames
            val frameChar = loadingFrames[frame % loadingFrames.size]
            print("\r$frameChar Загрузка... ${frame * 6}%") // \r - возврат каретки (перезаписывает строку)
            delay(200L)
        }
        println("\nЗагрузка завершена!")
    }

    // ОЖИДАНИЕ ЗАВЕРШЕНИЯ КОРУТИН
    suspend fun waitingForCoroutines(){
        // job - это объект, который представляет в себе запущенную корутину
        // job нужен для управления корутинами (например отменой выполнения или ожиданием)
        val job1 = GlobalScope.launch {
            println("JOB1: Пример долгой корутины... (задачи)")
            delay(2500L)
            println("JOB1: Быстрая задача завершена")
        }
        val job2 = GlobalScope.launch {
            println("JOB2: Пример долгой корутины... (задачи)")
            delay(500L)
            println("JOB2: Быстрая задача завершена")
        }

        println("Ожидаем завершения обеих задач...")

        // job.join() - Приостанавливает текущую корутину до завершения job
        job1.join() // Ждем завершения первой корутины
        job2.join() // Ждем завершения второй корутины

        println("Все задачи завершены")


    }
}

// ПРИМЕР РАБОТЫ С КОРУТИНАМИ
// runBlocking - специальная функция, которая создает корутину и БЛОКИРУЕТ текущий поток до её завершения


fun main() = runBlocking {
    // main сейчас - это основная корутина, которая блокирует поток программы до завершения всех корутин внутри неё
    val demo = BasicCoroutinesDemo()

    // Последовательный запуск демонстраций
    demo.simpleDelayDemo()
    demo.multipleCoroutinesDemo()

    // Ждать нужно, чтобы асинхронные корутины успели выполниться
    delay(2000L)


    demo.animationDemo()
    demo.waitingForCoroutines()


    println("=== ЗАВЕРШЕНИЕ ТЕСТА КОРУТИН ===")
}