import com.sun.jdi.Value
import java.time.temporal.TemporalAmount
import kotlin.random.Random

// УЛУЧШЕННЫЙ КЛАСС ITEM С РАЗНЫТИ ТИПАМИ ПРЕДМЕТОВ
// sealed class - закрытый класс (позволяет создавать ограниченную иерархию классов)

sealed class Item(
    val id: String,
    val name: String,
    val description: String,
    val value: Int = 0, // Стоимость предмета
    val weight: Double = 0.0, // Вес для ограничения инвентаря
    val maxStackSize: Int = 1
){
    // Вложенный класс для зелий
    class Potion(
        id: String,
        name: String,
        description: String,
        val healAmount: Int = 0,
        val manaAmount: Int = 0,
        value: Int = 0,
        weight: Double = 0.5
    ): Item(id,name,description, value, weight, 5)

    class Weapon(
        id: String,
        name: String,
        description: String,
        val damage: Int,
        val weaponType: WeaponType,
        value: Int = 0,
        weight: Double = 3.0
    ): Item(id,name,description, value, weight, 1)

    class Armor(
        id: String,
        name: String,
        description: String,
        val defence: Int,
        val armorType: ArmorType,
        value: Int = 0,
        weight: Double = 5.0
    ): Item(id,name,description, value, weight, 1)
}

enum class WeaponType{
    SWORD, AXE, SPEAR, BOW
}

enum class ArmorType{
    HELMET, CHESTPLATE, LEGGINGS, BOOTS, SHIELD
}

// КЛАСС data - класс с полями и простыми методами с доступом к ним
data class InventorySlot(
    var item: Item?,  // (Предмет в ячейке МОЖЕТ равняться null если ячейка пустая)
    var quantity: Int = 1 // Количество предметов в ячейке инвентаря
){
    val isEmpty: Boolean get() = item == null
    val isFull: Boolean get() = item?.let{ quantity >= it.maxStackSize} ?: false
}

// КЛАСС ИНВЕНТОРЯ С ИСПОЛЬЗОВАНИЕМ КОЛЛЕКЦИЙ
class Inventory(val capacity: Int = 20){
    // MutableList - изменяем список ячеек инвентаря
    private val slots = MutableList(capacity){InventorySlot(null)}

    private val itemIndex = mutableMapOf<String, Int>()

    // Map для быстрого поиска предметов по их ID (ключ itemId, и значение: индекс в slots
    fun addItem(newItem: Item, quantity: Int = 1): Boolean{
        var remaining = quantity

        // 1. Пробуем добавить к существующим стекам
        for (slot in slots){
            // ? - безопасный вызов (оператор) "если значение слева НЕ равно null, то выполнить операцию справа
            if(slot.item?.id == newItem.id && !slot.isFull){
                // !! - утверждение НЕ null
                val spaceInSlot = slot.item!!.maxStackSize - slot.quantity

                val toAdd = minOf(spaceInSlot, remaining)

                slot.quantity += toAdd
                remaining -= toAdd
                updateItemIndex(newItem.id, slots.indexOf(slot))

                if(remaining <= 0) return true
            }
        }

        // 2. Добавляем в пустые ячейки
        for(i in slots.indices){
            if(slots[i].isEmpty && remaining > 0){
                slots[i].item = newItem
                val toAdd = minOf(newItem.maxStackSize, remaining)
                slots[i].quantity = toAdd
                remaining -= toAdd
                updateItemIndex(newItem.id, i)

                if(remaining <= 0) return true
            }
        }
        if(remaining > 0){
            println("Инвентарь полон, Не поместились: $remaining ${newItem.name}")
            return false
        }

        return true
    }

    fun removeItem(itemId: String, quantity: Int = 1): Boolean{
        if (!itemIndex.containsKey(itemId)){
            println("Премет $itemId не найден в инвентаре")
            return false
        }

        var remaining = quantity
        val slotsWithItem = slots.filter { it.item?.id == itemId }

        for(slot in slotsWithItem){
            val toRemove = minOf(slot.quantity, remaining)
            slot.quantity -= toRemove
            remaining -= toRemove

            if(slot.quantity <= 0){
                slot.item = null
                itemIndex.remove(itemId)
            }

            if (remaining <= 0) break
        }

        return remaining <= 0
    }
    fun findItem(itemId: String): InventorySlot? {
        return slots.find{ it.item?.id == itemId }
    }



}



























