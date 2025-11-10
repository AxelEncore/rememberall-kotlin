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
    // НАДО ДУМАТЬ О Т 
    protected fun <T> executeSafely(operation: String, block: () -> T): T? {
        try{
            logger.debug("$systemName: Начало операции: $operation")
            val result = block() // Выполняем переданный блок кода
            logger.debug("$systemName: Операции $operation завершена успешно")
            // вернуть результат работы
        } // сделать catch исключения с выводом лог-строки с уровнем error и вернуть null
    }
}



























