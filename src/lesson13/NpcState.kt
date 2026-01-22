package lesson13

enum class NpcState{
    // enum - перечисление всех возможных состояний NPC
    IDLE,           // Стоит ничего не делает
    WAITING,        // Ждет игрока
    TALKING,
    REWARDED        // Уже выдал награду
}