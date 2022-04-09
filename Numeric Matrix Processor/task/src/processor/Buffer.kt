package processor

data class Dimension(val rows: Int, val columns: Int)

interface Buffer<out T> {

    val size: Dimension

    operator fun get(row: Int, col: Int): T

}