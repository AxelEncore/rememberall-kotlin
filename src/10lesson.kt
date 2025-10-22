import kotlin.enums.EnumEntries
import kotlin.random.Random

class Item(
    val id: String,
    val name: String,
    val description: String,
    val value: Int = 0,
    val useEffect: (Player) -> Unit = {} // Функция использования
){
    fun use(player: Player){
        println("Используется: $name")
        useEffect(player)
    }

    fun displayInfo(){
        println("$name - $description (Ценность: $value)")
    }
}

class Inventory{
    // mutableListOf<Item>() - создает ПУСТОЙ изменяемый список в который можно положить только предметы (Items)
    // private - означает, что доступ к списку предметов, есть только внутри класса Инвентаря
    val items = mutableListOf<Item>()

    fun addItem(item: Item){
        // .add(элемент) - Метод для добавления элемента в конец списка
        items.add(item)
        println("Предмет '${item.name}' добавлен в инвентарь")
    }

    fun removeItem(index: Int): Boolean{
        // index in 0 until items.size - проверяет находится ли index в диапазоне от 0 до размера списка (НЕ ВКЛЮЧИТЕЛЬНО)
        if(index in 0 until items.size){
            val removedItem = items.removeAt(index) // Удаляем элемент по указанному индексу
            println("Предмет ${removedItem.name} удален из инвентаря")
            return true
        }
        println("Неверный индекс предмета! Такого в инвентаре нет")
        return false
    }

    fun useItem(index: Int, player: Player): Boolean{
        if (index in 0 until items.size){
            val item = items[index]
            item.use(player)
            items.removeAt(index)
            return true
        }
        println("Неверный индекс предмета! Такого в инвентаре нет")
        return false
    }

    fun display(){
        if (items.isEmpty()){
            println("Инвентарь пуст...")
        }else{
            println("\n=== ИНВЕНТАРЬ ===")
            // { index, item -> ....} - лямбда выражение с параметрами index и item (функция без названия)
            items.forEachIndexed { index, item ->
                println("${index + 1}. ${item.name} - ${item.description}")
            }
            println("Всего предметов ${items.size}")
        }
    }

    fun findItemById(itemId: String): Item? {
        // .find { } ищет первый элемент удовлетворяющий условию поиска
        // it - ключевое слово, обозначающее текущий элемент в поиске
        // ? - функция может вернуть null (ничего), если ничего не найдено
        return items.find { it.id == itemId}
    }

    fun hasItem(itemId: String): Boolean{
        // .any { } - вернет true, если хотя бы один элемент удовлетворяет поиску
        return items.any { it.id == itemId}
    }

    fun countItems(itemId: String): Int{
        return items.count {it.id == itemId}
    }
}

open class Character(val name: String, var health: Int, val attack: Int){
    val isAlive: Boolean get() = health > 0

    fun takeDamage(damage: Int){
        health -= damage
        println("$name получает $damage")
        if(health <= 0) println("$name пал в бою!")
    }

    open fun attack(target: Character){
        if (!isAlive || !target.isAlive) return
        val damage = Random.nextInt(attack - 3, attack + 4) // Случайный урон в диапазоне
        println("$name атакует ${target.name}!")
        target.takeDamage(damage)
    }
}

// КЛАСС КВЕСТА
class Quest(
    val id: String,
    val name: String,
    val description: String,
    val requiredItemId: String? = null, // ID предмета необходимого для выполнения квеста (может быть null - т.е. не требовать)
    val rewardGold: Int = 0,
    val rewardItem: Item? = null
){
    var isCompleted: Boolean = false
    var isActive: Boolean = false

    fun checkCompletion(player: Player): Boolean{
        if (!isCompleted && isActive){
            // Если квест требует предмет, проверяем его наличие у игрока
            if(requiredItemId != null && player.inventory.hasItem(requiredItemId)){
                completeQuest(player)
                return true
            }
        }
        return false
    }

    private fun completeQuest(player: Player){
        isCompleted = true
        isActive = false

        println("\n*** КВЕСТ $name ВЫПОЛНЕН  ***")
        println("Награда:")

        if (rewardGold > 0){
            println("- Золото: $rewardGold")
            // В реальной игре тут будет метод добавления золота нашему игроку
        }

        if (rewardItem != null){
            println("- Предмет: ${rewardItem.name}")
            player.inventory.addItem(rewardItem)
        }
    }

    fun displayInfo(){
        val status = when{
            isCompleted -> "ВЫПОЛНЕН"
            isActive -> "АКТИВЕН"
            else -> "НЕ АКТИВЕН"
        }
        println("[$status] $name: $description")
    }
}

class QuestManager{
    // mutableMapOf<String, Quest>() - создаем изменяемый словарь с квестами, где:
    // String - типом данных ключа (id квеста)
    // Quest - тип значения (объект Квеста)
    private val quests = mutableMapOf<String, Quest>()

    fun addQuest(quest: Quest){
        // quests[quest.id] = quest - добавляет в словарь по ключу quest.id
        quests[quest.id] = quest
    }

    fun getQuest(questId: String): Quest? {
        return quests[questId]
    }

    fun startQuest(questId: String): Boolean{
        val quest = quests[questId]
        if (quest != null && !quest.isCompleted){
            quest.isActive = true
            println("Квест активирован: ${quest.name}")
            return true
        }
        return false
    }

    // Функция проверки выполнения всех активных квестов
    fun checkAllQuests(player: Player){
        // .values - получает все значения словаря (все квесты)
        // .filter { } - фильтрует только активные квесты
        quests.values.filter{ it.isActive }.forEach { quest ->
            quest.checkCompletion(player)
        }
    }

    fun displayQuests(){
        if (quests.isEmpty()){
            println("Список квестов пуст")
        }else{
            println("\n=== ЖУРНАЛ КВЕСТОВ ===")
            // перебор всех значений словаря квестов
            quests.values.forEach { quest ->
                quest.displayInfo()
            }
        }
    }

    fun getActiveQuests(): List<Quest>{
        // .toList - преобразует в изменяемый список
        return quests.values.filter{ it.isActive }.toList()
    }
}

class NPC(val name:String, val description: String){
    // mutableMapOf<String, String> - словарь диалогов
    // Ключ - фраза игрока, Значение - ответ NPC
    private val dialogues = mutableMapOf<String, String>()

    fun addDialogue(playerPhrase: String, npcResponse: String){
        dialogues[playerPhrase] = npcResponse
    }

    fun talk(){
        println("\n=== ДИАЛОГ С $name ===")
        println("$name: $description")

        if (dialogues.isEmpty()){
            println("$name не о чем с вами говорить")
            return
        }

        // Показываем варианты ответов игрока
        println("\n Варианты ответов:")
        dialogues.keys.forEachIndexed { index, phrase ->
            println("${index + 1}. $phrase")
        }
        println("${dialogues.size + 1}. Уйти")
        // Обработка ввода игрока
        println("Выберите реплику: ")
        val choice = readln().toIntOrNull() ?: 0

        if (choice in 1..dialogues.size){
            // Преобразуем ключи в список и берем по индексу
            val playerPhrase = dialogues.keys.toList()[choice - 1]
            val npcResponse = dialogues[playerPhrase] // Получаем ответ NPC по ключу (фразе игрока)

            println("\nВы: $playerPhrase")
            println("$name: $npcResponse")
        }else{
            println("Вы прощаетесь с $name")
        }
    }
}


class Player(
    name: String,
    health: Int,
    attack: Int
) :Character(name,health,attack){
    val inventory = Inventory()
    val maxHealth: Int = 100
    var isPowered: Boolean = false

    val questManager = QuestManager()

    fun usePotion() {
        // Используем поиск по id зелья здоровья
        val potion = inventory.findItemById("health_potion")
        if (potion != null){
            potion.use(this) // this - ссылка на текущий объект Player
        }else{
            println("У вас нет зелий здоровья!")
        }
    }

    override fun attack(target: Character){
        if (isPowered == true){
            val damage = attack + 10
            target.takeDamage(damage)
        }else{
            val damage = Random.nextInt(attack - 3, attack + 4)
            target.takeDamage(damage)
        }
    }

    fun pickUpItem(item: Item){
        inventory.addItem(item)
    }

    fun showInventory(){
        inventory.display()
    }
}
class Shop(val name: String, val description: String){
    private val itemsForSale = mutableMapOf<Item, Int>()

    private val buyPrices = mutableMapOf<String, Int>()

    fun addItem(item: Item, price: Int){
        itemsForSale[item] = price
        buyPrices[item.id] = (price * 0.6).toInt()
    }
    fun openShop(player: Player){
        println("\n=== ДОБРО ПОЖАЛОВАТЬ В МАГАЗИН: $name ===")
        println(description)

        var shopping = true

        while(shopping){
            println("\n--- МЕНЮ МАГАЗИНА ---")
            println("1. Купить предметы")
            println("2. Продать предметы")
            println("3. Уйти")

            println("Выберите действие: ")
            when(readln().toIntOrNull() ?: 0){
                1 -> showItemsForSale(player)
                2 -> showBuyMenu(player)
                3 -> {
                    shopping = false
                    println(" Вы покидаете магазин.")
                }
                else -> println("Неправильно, попробуй еще раз")
            }
        }
    }

    private fun showItemsForSale(player: Player){
        if (itemsForSale.isEmpty()){
            println("Товаров сейчас нет")
            return
        }
        println("\n=== ТОВАРЫ НА ПРОДАЖУ ===")
        itemsForSale.forEach{ (item, price) ->
            println("${item.name} - ${item.description} | Цена: $price золотых")
        }
        println("${itemsForSale.size + 1}. Назад")

        println("Выберите товар для покупки: ")
        val choice = readln().toIntOrNull() ?: 0

        if (choice in 1..itemsForSale.size){
            val selectedItem = itemsForSale.keys.toList()[choice - 1]
            val price = itemsForSale[selectedItem] ?: 0

            // ДЗ РЕАЛИЗОВАТЬ ПРОВЕРКУ ЗОЛОТА У ИГРОКА (ХВАТАЕТ ИЛИ НЕТ)
            println("Вы покупаете ${selectedItem.name} за $price золотых")
            player.inventory.addItem(selectedItem)
            // ДЗ ЗДЕСЬ ВЫЧЕСТЬ У НЕГО ЗОЛОТЫЕ
        }
    }

    fun showBuyMenu(player: Player){
        val sellableItems = player.inventory.items.filter{ item ->
            // containsKey - содержит ключи (по id)
            buyPrices.containsKey(item.id)
        }

        if (sellableItems.isEmpty()){
            println("У вас нет предметов на продажу")
            return
        }

        println("\n=== ВАШИ ПРЕДМЕТЫ ДЛЯ ПРОДАЖИ ===")
        sellableItems.forEachIndexed { index, item ->
            val price = buyPrices[item.id] ?: 0
            println("${index + 1}. ${item.name} - Цена продажи: $price золотых")
        }
        println("${sellableItems.size + 1}. Назад")

        println("Выберите предмет для продажи: ")
        val choice = readln().toIntOrNull() ?: 0

        if (choice in 1..sellableItems.size){
            val selectedItem = sellableItems[choice - 1]
            val price = buyPrices[selectedItem.id] ?: 0
            println("Вы продаете: ${selectedItem.name} за $price золотых")
            player.inventory.removeItem(choice - 1)
            // Реализация добавление золота игроку на price
        }
    }

    fun getBuyPrice(itemId: String): Int{
        return buyPrices[itemId] ?: 0
    }

}

class  Location(val name: String, val description: String){
    val items = mutableListOf<Item>()
    val enemies = mutableListOf<Character>()
    // может быть null если на локации нет магазина
    var shop: Shop? = null

    fun setShop(shop: Shop){
        this.shop = shop
    }

    fun describe(){
        println("\n=== $name ===")
        println(description)

        if (enemies.isNotEmpty()){
            println("\nВраги на локации:")
            enemies.forEachIndexed { index, enemy ->
                println("${index + 1}. ${enemy.name} (HP: ${enemy.health})")
            }
        }

        if (items.isNotEmpty()){
            println("\nПредметы в локации:")
            items.forEachIndexed { index, item ->
                println("${index + 1}. ${item.name} - ${item.description}")
            }
        }

        if (shop != null){
            // !! - Это строгое утверждение, что shop не null
            println("\nМагазин: ${shop!!.name}")
        }
    }
}




fun main(){
    println("=== СИСТЕМА КВЕСТОВ И NPC ===")

    val player = Player("Олег", 100, 15)

    val healthPotion = Item(
        "health_potion",
        "Зелье здоровья",
        "Восстанавливает 30 ХП",
        30,
        useEffect = { player ->
            // minOf(a,b) - a это округляемое значение, b это то, до которого его надо округлить
            player.health = minOf((player.health + 30), player.maxHealth)
            println("${player.name} восстанавливает себе 30 HP")
        }
    )

    val strengthPotion = Item(
        "strength_potion",
        "Зелье силы",
        "Усиливает вас на 1 ход (наносите на 10 больше урона)",
        50,
        useEffect = { player ->
            player.
        }
    )

    // Предметы для квестов (создание)
    val mysteryHerb = Item(
        "mystery_herb",
        "Таинственная тарва",
        "Редкое растение с целебными свойствами",
        15
    )

    val ancientAmulet = Item(
        "ancient_amulet",
        "Древний амулет",
        "Старинный амулет, с магическими свойсвами",
        100
    )

    // СОЗДАНИЕ КВЕСТОВ
    val herbQuest = Quest(
        "find_herbs",
        "Сбор целебных трав",
        "Найдите таинственную траву в лесу",
        "mystery_herb",
        50,
        ancientAmulet
    )

    val monsterQuest = Quest(
        "kill_monsters",
        "Очистка леса",
        "Убейте 3 гремленов в лесу",
        rewardGold = 100
    )

    val villageElder = NPC("Старейшина деревни", "Мудрый старик, знающий много секретного")

    villageElder.addDialogue("Поздороваться", "Добро пожаловать путник")
    villageElder.addDialogue("Спросить о работе", "Лес кишит монстрами, поможешь с этой работенкой?")
    villageElder.addDialogue("Спросить о траве", "В глубине леса растет дикая трава, драться умеет, собери немного для меня")

    player.questManager.addQuest(herbQuest)
    player.questManager.addQuest(monsterQuest)

    player.questManager.startQuest("find_herbs")

    // ИГРА С ДЕМОНСТРАЦИЕЙ РАБОТЫ
    println("--- Взаимодействие с NPC ---")
    villageElder.talk()

    println("\n--- Проверка квестов ---")
    player.questManager.displayQuests()

    println("\n--- Игрок нашел траву ---")
    player.inventory.addItem(mysteryHerb)

    println("\n--- Проверка на выполнение квеста ---")
    player.questManager.checkAllQuests(player)

    println("\n--- Финальный статус квестов ---")
    player.questManager.displayQuests()
}

























