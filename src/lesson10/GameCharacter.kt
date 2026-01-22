package lesson10

import kotlinx.coroutines.*
import kotlin.math.max

// (*) - остаем все методы и классы из библиотеки - чтобы не выбирать
// Подключаем корутины типа: launch, delay, Job и тд.

class GameCharacter(
    val name: String,
    val maxHealth: Int,
    val maxMana: Int,
    val baseAttack: Int
){
    var currentHealth: Int = maxHealth

    var currentMana: Int = maxMana

    var canAttack: Boolean = true
    // Флаг - точка, на которую мы будем опираться, с целью проверки состояния

    // Храним ссылку на корутину, чтобы можно было в любой момент ее отменить.
    private var manaRegenJob: Job? = null
    // Job? - "работа" корутины, на нее можно ссылаться, запускать, отменять и проверять ее статус
    // ? - возможность вернуть null

    private var potionJob: Job? = null
    // Ссылка на корутину, которая будет отвечать за эффект яда

    fun printStatus(){
        println("=== Статус персонажа: $name ===")
        println("HP: $currentHealth / $maxHealth")
        println("MP: $currentMana / $maxMana")
        println("ATK: $baseAttack")
        println("Can Attack? $canAttack")
        println("===============================")
    }

    fun attack(target: GameCharacter){
        if (!canAttack){
            println("[!] $name не может сейчас атаковать! Кулдаун еще не закончился")
            return
        }

        if (currentHealth <= 0){
            println("[!] $name уже погиб и не может атаковать!")
            return
        }

        val damage = baseAttack
        // Берем базовый урон - к нему будем добавлять криты, баффы и тд.

        println("$name атакует ${target.name} и наносит $damage урона!")
        target.takeDamage(damage)

        // Нужно запустить кулдаун атаки, чтобы ее нельзя было спамить
        // Обычно с кулдауном прописывается метод анимации
        startAttackCooldown(2000L)
        // 2000L - время в миллисекундах. L - означает тип данных Long
    }
    fun takeDamage(amount: Int){
        if (amount <= 0){
            println("[!] $name не получает урона (повезло повезло)")
            return
        }

        currentHealth -= amount
        // Отнимаем число получаемого урона от нынешнего хп
        println("[-] $name получает $amount урона  |  HP: $currentHealth")

        if (currentHealth <= 0){
            println("[!] $name повержен!")
        }
    }

    // Запуск кулдауна атаки в корутине
    private fun startAttackCooldown(cooldownMillis: Long){
        canAttack = false
        println("[*] $name получает кулдаун на ${cooldownMillis} мс")

        // Запуск новой корутины в "глобальной области" (главным потоком программы)
        GlobalScope.launch {
            // GlobalScope - глобальная область для корутин
            // Корутины внутри этой области живут пока жив процесс
            // launch - запускает новую корутину (фоновую задачу)

            delay(cooldownMillis)
            // delay - "задержать поток" или усыпить корутину на указанное время
            // ВАЖНО: delay НЕ БЛОКИРУЕТ ВЕСЬ ПОТОК, только данную запущенную корутину
            // То есть пока кулдаун способности перезаряжается - игра продолжается
            // Без корутин - пока перезаряжалась бы способность, все игровые события
            // Ждали бы пока это не произойдет. (то есть это многопоточность)
            canAttack = true
            println("[*] Кулдаун атаки для $name прошел")
        }
    }

    fun startManaRegeneration(
        amountPerTick: Int,  // Чисто восстановления маны за 1 тик
        intervalMillis: Long
    ){
        // Проверка - если регенерацию уже идет, отменить ее и запустить заново
        if (manaRegenJob != null){
            println("[!] Мана уже регенерируется")
            manaRegenJob?.cancel()
            // Отмена корутины
        }

        manaRegenJob = GlobalScope.launch{
            println("Регенерация маны для $name запущена")

            while (true){
                delay(intervalMillis)

                if (currentHealth <= 0){
                    println("[!] $name повержен, регенерация прервана")
                    break
                }

                if (currentMana >= maxMana){
                    println("[!] Мана $name уже полная")
                    continue
                    // Переход к следующей итерации цикла ( с пропуском оставшихся строк кода ниже)
                }

                currentMana += amountPerTick

                if (currentMana > maxMana){
                    currentMana = maxMana
                }

                println("[+] $name регенерирует $amountPerTick маны ($currentMana/$maxMana)")
            }
            println("Корутина регенрации маны для $name завершена")
        }
    }

    fun stopManaRegen(){

        if (manaRegenJob == null){
            println("[!] Регенерация маны для $name не запущена")
            return
        }

        manaRegenJob?.cancel()
        // cancel - завершаем выполнение корутины

        println("[!] Регенерация маны для $name остановлена")
        manaRegenJob = null
    }

    fun applyPoisonEffect(
        damagePerTick: Int,  // Урон за 1 тик
        ticks: Int,          // Сколько будет действовать эффект
        intervalMillis: Long
    ){
        if(potionJob != null){
            // Если эффект уже действует, отменяем его и запускаем по новой (обновляем)
            println("[!] Ян уже действует на $name - Эффект перезапущен")
            potionJob?.cancel()
            // Отменяем уже ранее запущенную корутину эффекта
        }

        potionJob = GlobalScope.launch {
            println("[-] $name отравлен! Эффект яда действует $ticks тиков")

            var remainingTicks = ticks
            // Считаем сколько тиков осталось до конца эффекта

            while (remainingTicks > 0){
                delay(intervalMillis)
                // Ждем интервал между тиками

                if (currentHealth <= 0){
                    println("$name уже погиб. Яд больше не действует")
                    break
                }

                println("Ян наносит $damagePerTick уронa $name")
                takeDamage(damagePerTick)
                // Получение урона от яда за 1 тик (вызов внутреннего метода)

                remainingTicks -= 1
                // После получения урона отнимаем 1 тик от оставшегося времени наложения эффекта
            }
            println("[!] Эффект яда на $name завершился")
        }
    }

    fun clearPoison(){
        // Принудительное снятие эффекта яда
        if (potionJob == null){
            println("[!] На $name нет эффектов")
            return
        }

        potionJob?.cancel()
        println("[!] Эффект с $name снят")
        potionJob = null
    }
}