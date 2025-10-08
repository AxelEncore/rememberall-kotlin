import com.sun.jdi.Value
import kotlin.random.Random

// КЛАСС ПРЕДМЕТА
class Item(val name: String, val description: String, val useEffect: (Player) -> Unit){
    fun use(player: Player){
        println("Используется: $name")
        useEffect(player)
    }
}
// КЛАСС ЛОКАЦИИ
class Location(val name: String, val description: String){
    val enemies = mutableListOf<Character>()
    val items = mutableListOf<Item>()

    fun addEnemy(enemy: Character){
        enemies.add(enemy)
    }

    fun addItem(item: Item){
        items.add(item)
    }

    fun describe(){
        println("=== $name ===")
        println(description)

        if (enemies.isNotEmpty()){
            println("Враги в локации: ")
            // forEachIndexed - перебирает список(массив), предоставляя индексы и элементы под этими индексами
            enemies.forEachIndexed{ index, enemy ->
                println("${index + 1}. ${enemy.name}  (HP: ${enemy.health})")
            }
        }

        if (items.isNotEmpty()){
            println("Предметы в локации: ")
            // forEachIndexed - перебирает список(массив), предоставляя индексы и элементы под этими индексами
            items.forEachIndexed{ index, item ->
                println("${index + 1}. ${item.name} - ${item.description}")
            }
        }
    }
}

// КЛАСС ИГРОВОГО МИРА
class GameWorld{
    private val locations = mutableListOf<Location>()
    private var currentLocationIndex = 0
    val gameInput = GameInput()

    // Вычисляемое свойство для текущей локации
    val currentLocation: Location
        get() = locations[currentLocationIndex]

    fun createWorld(){
        val forest = Location("Темный лес", "Густой темный лес, здесь страшно, не ходи сюда")
        val cave = Location("Пещера темная", "Темная пещера, не такая страшная как лес, но я бы сюда не совался")
        val village = Location("Деревья", "Мирная зона, здесь кайфово, пока не придет голем го-го-го")


        // Создаем врагов
        val oleg = Character("Олег", 40, 8)
        val wolf = Character("Волк", 25, 10)
        val wildGolem = Character("Дикий кукуруз", 200, 15)

        // Создаем предметы локации
        val healPotion = Item("Зелье здоровья", "Восстанавливает от -1 до 1 HP", { player ->
            println("${player.name} восстанавливает ... HP")
        })
        val attackPotion = Item("Зелье силы", "Увеличивает атаку на 5", {player ->
            println("${player.name} усиливает себя на 5")
        })

        // РАСПРЕДЕЛЕНИЕ ОБЪЕКТОВ (ВРАГОВ И ПРЕДМЕТОВ) ПО ЛОКАЦИЯМ
        forest.addEnemy(wolf)
        forest.addEnemy(oleg)
        forest.addItem(healPotion)

        cave.addEnemy(wildGolem)
        cave.addEnemy(oleg)
        cave.addItem(attackPotion)

        village.addItem(healPotion)

        // ДОБАВИМ ЛОКАЦИИ В НАШ МИР
        locations.add(forest)
        locations.add(cave)
        locations.add(village)
    }

    fun startGame(player: Player){
        println("Добро пожаловать в игру! ${player.name}")

        var gameRunning = true

        while (gameRunning && player.isAlive){
            // Отображаем текущую локацию
            currentLocation.describe()

            // ПОКАЗЫВАЕМ ДОСТУПНЫЕ ДЕЙСТВИЯ ИГРОКА
            println("\n>>> ДОСТУПНЫЕ ДЕЙСТВИЯ <<<")
            println("1. Осмотреться")
            println("2. Атаковать врага")
            println("3. Взять предмет")
            println("4. Перейти на следующую локацию")
            println("5. Использовать зелье")
            println("6. Выйти из игры")

            val choice = gameInput.getNumberInput("Выбериете действие: ", 1,6)

            when(choice){
                1 -> currentLocation.describe()
                2 -> combatMenu(player)
                3 -> takeItemMenu(player)
                4 -> moveToNextLocation()
                5 -> player.usePotions()
                6 -> gameRunning = false
            }

            if (!player.isAlive){
                println("Игра окончена ${player.name} пал в бою, GG WP gl hf")
            }
        }
    }

    private fun combatMenu(player: Player){
        if (currentLocation.enemies.isEmpty()){
            println("Здесь нет врагов, чтобы с ними файтиться, остуди пыл")
            return
        }

        println("\n Выберите цель для атаки: ")
        currentLocation.enemies.forEachIndexed { index, enemy ->
            println("${index + 1}. ${enemy.name} (HP: ${enemy.health}")
        }
        println("${currentLocation.enemies.size + 1}. Отмена")

    }

}
























