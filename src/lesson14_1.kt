import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class GameCharacter(val name: String){
    var health = 100
    var mana = 100
    var isAlive = true

    // ФЛАГИ ДЛЯ УПРАВЛЕНИЯ СОСТОЯНИЯМИ
    var canAttack = true
    var isUnderEffect = false

    // СПИСОК АКТИВНЫХ КОРУТИН ДЛЯ УПРАВЛЕНИЯ ИМИ
    private val activeJobs = mutableListOf<Job>()

    // Метод остановки всех корутин персонажа
    fun cancelAllJobs(){
        activeJobs.forEach { it.cancel() } // Прерывание всех корутин из списка
        activeJobs.clear() // Очистка списка
    }

    fun startAttackCooldown(cooldownTime: Long = 1000L){
        val job = GlobalScope.launch {
            canAttack = false
            println("! $name: Атака перезагружается...")

            // Таймер перезарядки
            repeat((cooldownTime / 200).toInt()){ second ->
                delay(200L)
                print(".") // Визуализация перезарядки
            }
            println()

            canAttack = true
            println("$name: Атака готова!")
        }
        activeJobs.add(job)
    }

    // Корутина регенерации маны
    fun startManaRegeneration(){
        val job = GlobalScope.launch {
            while(isAlive && mana < 100){
                delay(1000L) // Восстановление маны каждую секунду

                if (mana < 100){
                    mana += 5
                    if (mana > 100) mana = 100
                    println("$name: Мана восстановлена до $mana")
                }
            }
        }
        activeJobs.add(job)
    }

    // КОРУТИНА ЭФФЕКТА ЯДА (постепенное уменьшение здоровья)
    suspend fun applyPoisonEffect(duration: Long = 5000L, damagePerTick: Int = 5){
        if (isUnderEffect) return // Если уже есть какой-то эффект на игроке, выходим из метода
        isUnderEffect = true

        println("$name отрпавлен! Здоровье будет уменьшаться в течение ${duration/1000} секунд")

        val startTime = System.currentTimeMillis()
        val endTime = startTime + duration

        while (System.currentTimeMillis() < endTime && isAlive){
            delay(1000L) // Урон каждую секунду
            health -= damagePerTick

            println("$name отравлен! -$damagePerTick HP, Осталось $health")

            if (health <= 0){
                health = 0
                isAlive = false
                println("$name стал дедом от отравления")
                break
            }
        }

        if (isAlive){
            println("$name: эффект отравления прошел")
        }
        isUnderEffect = false
    }

    // suspend функцию, которая симулирует эффект лечения (постепенное восстановление)

}
























