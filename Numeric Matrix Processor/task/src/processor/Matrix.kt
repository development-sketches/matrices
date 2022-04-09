package processor

enum class TranspositionType {
    MAIN, SIDE, VERTICAL, HORIZONTAL
}

interface Matrix<T : Any> {

    val size: Dimension

    fun adjoint(): Matrix<T> = cofactor().transpose()

    fun cofactor(): Matrix<T>

    fun determinant(): T

    fun exclude(rowToExclude: Int, colToExclude: Int): Matrix<T>

    operator fun get(row: Int, col: Int): T

    fun inverse(): Matrix<T>

    operator fun plus(other: Matrix<T>): Matrix<T>

    operator fun times(multiplier: T): Matrix<T>

    operator fun times(other: Matrix<T>): Matrix<T>

    fun transpose(type: TranspositionType = TranspositionType.MAIN): Matrix<T>

}
