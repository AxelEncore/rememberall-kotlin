//import jdk.jfr.DataAmount
//
//class Player{
//    var name: String = "Безымянный герой" // Свойства, аттрибуты, параметры - который определяют функцию
//    var health: Int = 100
//    var damage: Int = 1
//    var isAlive: Boolean = true
//
//    // Методы = Это функции принадлежащие классу
//    // Они описывают поведение объекта
//
//    // damage - параметр метода takeDamage
//    fun takeDamage(damage: Int){
//        health -= damage
//        println("$name получает $damage урона! Осталось здоровья: $health")
//
//        // This - сылка на текущий объект внутри метода.
//        if(this.health <= 0){
//            isAlive = false
//            println("$name пал в 1000 битве с брейнротом")
//        }
//    }
//
//    fun heal(amount: Int){
//        if (isAlive) {
//            health += amount // health = health + amount
//            println("$name восстанавливает $amount здоровья. Теперь у него $health HP")
//        } else {
//            println("$name уже мертв и не может быт исцелен")
//        }
//    }
//}
//
//fun main(){
//    // Создание объекта (экземпляр класса) Player
//    val warrior = Player()
//
//    warrior.name = "Oleg"
//    println("Игрок: ${warrior.name} появился в мире, здоровье: ${warrior.health}")
//
//    // точка - это вывод метода объекта
//    warrior.takeDamage(30) // Герой получает 30 урона
//    warrior.heal(10)
//}