import kotlin.random.Random
import kotlin.random.nextInt

fun calculateDamage(baseAttack: Int): Int{
    val variation = Random.nextInt(80,131)
    return (baseAttack * variation) / 100
}

// Функция для определения атаки одного существа другим
fun performAttack(attackerName: String, attackerAttack: Int, defenderName: String, defenderHealth: Int): Int{
    println("Ход существа $attackerName: ")
    val damage = calculateDamage(attackerAttack) // Вызов функции считающей урон от аттаки
    var newHealth = defenderHealth - damage

    println("$attackerName наносит $defenderName $damage урона!")
    println("У $defenderName осталось $newHealth HP")

    return newHealth
}

fun startBattle(){
    val playerName = "Рутаритилиус"
    var playerHealth = 100
    val playerAttack = 10

    val monsterName = "EminemZombie"
    var monsterHealth = 60
    val monsterAttack = 12

    var round = 1

    println(">>> Бой между $playerName и $monsterName начинается <<<")

    while (playerHealth > 0 && monsterHealth > 0){
        println(" ---- Раунд: $round ---- ")
        // ХОД ИГРОКА
        monsterHealth = performAttack(playerName, playerAttack, monsterName, monsterHealth)

        if (monsterHealth <= 0) break

        // ХОД МОНСТРА
        playerHealth = performAttack(monsterName, monsterAttack, playerName, playerHealth)

        if (playerHealth <= 0) break

        round++
    }

    // Определение победителя
    if (playerHealth > 0){
        println("\n Победа! $playerName одолел $monsterName!")
    } else {
        println("\n Поражение! $monsterName одолел $playerName!")
    }


}

fun main(){
    println("==== ЗАПУСК ДВИЖКА ИГРЫ ====")
    startBattle()
    println(" === ИГРА ЗАВЕРШИЛАСЬ === ")
}





















