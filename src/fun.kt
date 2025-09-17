fun main(){
    val playerHealth = 100
    val playerName = "Oleg"

    println("$playerName начинает путешествие по Брейнротопии")
    println("Здоворье: $playerHealth")

    val updateHealth = attackMonster(playerHealth)
    println("После боя здоровье ваше: $updateHealth")

    val healingPosion = drinkHealingPivo(updateHealth)
    println("Вы восстановили здопровье до: $healingPosion")


}

fun attackMonster(health: Int): Int{
    println(">>>> Начинается битва! <<<<")
    // Имитация получения урона в бое с боссом
    val damage = 25
    val newHealth = health - damage
    println("Получено урона: $damage")

    // Возврат результата функции
    return newHealth; // Возвращаем обновленное здоровье
}

fun drinkHealingPivo(health: Int): Int{
    println(" >>>> Вы выпили пиво <<<<")
    val healing = 30
    val newHealth = health + healing
    println("Восстановили здоровья: $healing")
    return newHealth
}