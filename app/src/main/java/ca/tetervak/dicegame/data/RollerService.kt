package ca.tetervak.dicegame.data

import ca.tetervak.dicegame.domain.RollData
import kotlin.random.Random

class RollerService(
    private val random: Random = Random.Default
) {

    fun getRollData(numberOfDice: Int): RollData {
        if (numberOfDice > 0) {
            val list: List<Int> =
                buildList(capacity = numberOfDice) {
                    repeat(numberOfDice) {
                        add(random.nextInt(from = 1, until = 7))
                    }
                }
            return RollData(list)
        } else {
            throw IllegalArgumentException("Illegal numberOfDice = $numberOfDice")
        }
    }
}