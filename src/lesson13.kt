import jdk.dynalink.Operation
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// КЛАСС ДЛЯ ЛОГИРОВАНИЯ ОШИБОК И СОБЫТИЙ ИГРЫ
class GameLogger{
    private val logFile = "game_log.txt"
    private val fileManager = SafeFileManager()

    // МЕТОД ЗАПИСИ ЛОГОВ В ФАЙЛ
    fun log(message: String, level: String = "INFO"){
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val logEntry = "[$timestamp] [$level] $message\n"

        // Проверка на дублирование в консоль важных сообщений
        if (level == "ERROR" || level == "WARN"){
            println("LOG [$level]: $message")
        }

        // Запись в файл логирования
        fileManager.writeFileSafely(logFile, logEntry)
    }

    // МЕТОДЫ-ПОМОЩНИКИ ДЛЯ РАЗНЫХ УРОВНЕЙ ЛОГИРОВАНИЯ
    fun info(message: String) = log(message, "INFO")
    fun warn(message: String) = log(message, "WARN")
    fun error(message: String) = log(message, "ERROR")
    fun debug(message: String) = log(message, "DEBUG")
}

// БАЗОВЫЙ КЛАСС ДЛЯ ВСЕХ ИГРОВЫХ СИСТЕМ С ОБРАБОТКОЙ ОШИБОК
abstract class GameSystem(val systemName: String, protected val logger: GameLogger){

    // МЕТОД ДЛЯ БЕЗОПАСНОГО ВЫПОЛНЕНИЯ ОПЕРАЦИЙ СИСТЕМЫ
    // <T> - объявление обобщенного типа данных
    // T - ПЛЕЙСХОЛДЕР (заполнитель) для любого типа данных
    // НАДО ДУМАТЬ О Т КАК О "ЧЕМ-ТО ВРЕМЕННОМ ИЛИ АБСТРАКТНОМ" КАК ЯЧЕЙКА В КОТОРУЮ ПОДСТАВИМ КАКОЙ-ТО ОБЪЕКТ
    protected fun <T> executeSafely(operation: String, block: () -> T): T? {
        // Читаем верхнюю строку, как:
        // Функция executeSafely работает(выполняется) с каким-то типом T
        // Она принимает операцию (строку) и блок кода, который возвращает T
        // Сама функция после выполнения возвращает T? (T или null)
        try{
            logger.debug("$systemName: Начало операции: $operation")
            val result = block() // Выполняем переданный блок кода
            logger.debug("$systemName: Операции $operation завершена успешно")
            return result
            // вернуть результат работы
        } catch (e: Exception){
            logger.error("$systemName: Ошибка операции $operation - ${e.message}")
            return null
        }
    }
    // АБСТРАКТНЫЙ МЕТОД ДЛЯ ИНИЦИАЛИЗАЦИИ СИСТЕМЫ
    abstract fun initialize(): Boolean
    // АБСТРАКТНЫЙ МЕТОД ДЛЯ ЭКСТРЕННОЙ ОСТАНОВКИ СИСТЕМЫ
    abstract fun emergencyShutdown()

    val resultName: String? = executeSafely("Получение имени игрока"){
        // Здесь может быть неограниченное число кода, которое после вычисления должен вернуть String
        "Oleg"
    }
    val resultInt: Int? = executeSafely("Расчет урона"){
        42
    }
    val resultBool: Boolean? = executeSafely("Проверка жизни"){
        true
    }

}

// СИСТЕМА БОЯ С ОБРАБОТКОЙ ОШИБОК
class CombatSystem(logger: GameLogger) : GameSystem("CombatSystem", logger){
    private var isInitialized = false

    override fun initialize(): Boolean{
        return executeSafely("initialize"){
            // Имитация инициализации системы боя
            logger.info("Инициализация системы боя...")
            Thread.sleep(100) // Имитайия загрузки элементов и процессов
            isInitialized = true
            logger.info("Ситема  боя успешно инициализирована")
            true
        } ?: false
    }

    fun performAttack(attacker: String, target: String, damage: Int): Boolean{
        if(!isInitialized){
            logger.warn("Попытка атаки при неинициализированной системы боя")
            return false
        }

        return executeSafely("performAttack"){
            // Проверка корректности введнных параметров
            if(damage < 0){
                throw IllegalArgumentException("Урон не может быть отрицательным: $damage")
            }
            if (attacker.isBlank() || target.isBlank()){
                throw IllegalArgumentException("Имена персонажей не могут быть пустыми")
            }

            logger.info("$attacker атакует $target с уроном: $damage")
            true
        } ?: false
    }

    override fun emergencyShutdown() {
        logger.warn("Аварийное заверение системы боя")
        isInitialized = false
        // Здесь в будущем освобождение ресурсов, сохранение состояний и тд.
    }
}

class InventorySystem(logger: GameLogger): GameSystem("InventorySystem", logger){
    private val items = mutableListOf<String>()
    private var isInitialized = false

    override fun initialize(): Boolean {
        return executeSafely("initialize") {
            logger.info("Инит. системы инвентаря...")
            // Загрузка предметов по умолчанию, при создании игрока
            items.addAll(listOf("Старый меч", "Поношенный доспех"))
            isInitialized = true
            logger.info("Система инвентаря инит. успешно")
            true
        } ?: false
    }
    fun addItem(item: String): Boolean{
        if (!isInitialized){
            logger.warn("Попытка добавить предмет в инвентарь без инит системы")
            return false
        }

        return executeSafely("addItem"){
            if (item.isBlank()){
                throw IllegalArgumentException("Название предмета не может быть пустым")
            }
            if (items.size >= 20){
                throw IllegalArgumentException("Инвентарь переполен (максимум 20 предметов)")
            }

            items.add(item)
            logger.info("Предмет $item добавлен в инвентарь. Всего предметов: ${items.size}")

            true
        } ?: false
    }

    fun getItems(): List<String>{
        if (!isInitialized){
            logger.warn("Попытка получить предметы без инит системы")
            return emptyList()
        }

        return executeSafely("getItems"){
            items.toList() // Возвращение копии списка
        } ?: emptyList()
    }

    override fun emergencyShutdown() {
        // Логирование warn экстренного отключения системы инвентаря
        // Сохранение состояния инвентаря перед отключением
        // Создание бэкап-списка - использовать метод joinToString("\n")
        // Используя метод File в аттрибут которого мы кладем "название_файла_бэкапа.txt" + записать в файл созданный бэкап - writeText(бэкап-список)
        // проверка на инициализацию должна стать = false
    }
}


























