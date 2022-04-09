package processor

import processor.TranspositionType.*
import java.math.BigDecimal

class MatrixImpl<T : Any>(
    private val buffer: Buffer<Element<T>>,
    private val factory: BufferFactory
) : Matrix<T> {

    override fun cofactor(): Matrix<T> {
        val newBuffer = factory.buffer(size.rows, size.columns) { row, col ->
            val minor = MatrixImpl(internalExclude(row, col), factory).internalDeterminant()
            if ((row + col) % 2 == 0) minor else -minor
        }
        return MatrixImpl(newBuffer, factory)
    }

    override fun determinant(): T = internalDeterminant().value

    override fun exclude(rowToExclude: Int, colToExclude: Int): Matrix<T> =
        MatrixImpl(internalExclude(rowToExclude, colToExclude), factory)

    private fun internalDeterminant(): Element<T> {
        require(size.columns == size.rows) {
            "Number of rows must be equal to the number of columns: ${size.rows} <> ${size.columns}"
        }
        val calculated = when (size.rows) {
            1 -> buffer[0, 0]
            2 -> buffer[0, 0] * buffer[1, 1] - buffer[1, 0] * buffer[0, 1]
            else -> {
                (0 until size.columns).map { col ->
                    val element = if (col % 2 == 0) buffer[0, col] else -buffer[0, col]
                    element * exclude(0, col).determinant()
                }.reduce { acc, element -> acc + element }
            }
        }
        return calculated
    }

    private fun internalExclude(
        rowToExclude: Int,
        colToExclude: Int
    ): Buffer<Element<T>> {
        require(size.rows > 1 && size.columns > 1) {
            "The matrix have at least two rows and at least two columns"
        }
        require(rowToExclude in 0 until size.rows) {
            "The row $rowToExclude is out of bounds"
        }
        require(colToExclude in 0 until size.columns) {
            "The column $colToExclude is out of bounds"
        }
        return factory.buffer(size.rows - 1, size.columns - 1) { row, col ->
            buffer[if (row < rowToExclude) row else row + 1, if (col < colToExclude) col else col + 1]
        }
    }

    override fun inverse(): Matrix<T> {
        val determinant = internalDeterminant()
        require(!determinant.isZero()) {
            "The inverse matrix can’t be found if determinant equals zero."
        }
        return adjoint() * determinant.inverse().value
    }

    override fun plus(other: Matrix<T>): Matrix<T> {
        require(size == other.size) {
            "Matrices must have an equal number of rows and columns."
        }
        return MatrixImpl(
            factory.buffer(size.rows, size.columns) { row, col -> buffer[row, col] + other[row, col] },
            factory
        )
    }

    override fun times(multiplier: T): Matrix<T> =
        MatrixImpl(
            factory.buffer(size.rows, size.columns) { row, col -> buffer[row, col] * multiplier },
            factory
        )

    private fun times(other: Matrix<T>, row: Int, col: Int): Element<T> =
        (0 until size.columns)
            .map { buffer[row, it] * other[it, col] }
            .reduce { acc, element -> acc + element }

    override fun times(other: Matrix<T>): Matrix<T> {
        require(size.columns == other.size.rows) {
            "Number of rows of the argument must be equal to the number of columns: ${other.size.rows} <> ${size.columns}"
        }
        return MatrixImpl(
            factory.buffer(size.rows, other.size.columns) { row, col -> times(other, row, col) },
            factory
        )
    }

    private fun transpose(transformation: (Int, Int) -> Element<T>): Buffer<Element<T>> =
        factory.buffer(size.rows, size.columns, transformation)

    override fun transpose(type: TranspositionType): Matrix<T> {
        require(size.columns == size.rows) {
            "Number of rows must be equal to the number of columns: ${size.rows} <> ${size.columns}"
        }
        val newBuffer = when (type) {
            MAIN -> transpose { row, col -> buffer[col, row] }
            SIDE -> transpose { row, col -> buffer[size.columns - col - 1, size.rows - row - 1] }
            VERTICAL -> transpose { row, col -> buffer[row, size.columns - col - 1] }
            HORIZONTAL -> transpose { row, col -> buffer[size.rows - row - 1, col] }
        }
        return MatrixImpl(newBuffer, factory)
    }

    override val size: Dimension
        get() = buffer.size

    override fun get(row: Int, col: Int): T = buffer[row, col].value

}

fun doubleMatrix(rowCount: Int, rows: (Int) -> List<Double>): Matrix<Double> =
    LinearBufferFactory().let { factory ->
        MatrixImpl(
            factory.buffer(rowCount) { row -> rows(row).map { DoubleElement(it) } },
            factory
        )
    }

fun doubleMatrix(rows: List<List<Double>>): Matrix<Double> =
    doubleMatrix(rows.size) { row -> rows[row] }

fun decimalMatrix(rows: List<List<BigDecimal>>): Matrix<BigDecimal> =
    LinearBufferFactory().let { factory ->
        MatrixImpl(
            factory.buffer(rows.map { row -> row.map { DecimalElement(it) } }),
            factory
        )
    }

fun intMatrix(rows: List<List<Int>>): Matrix<Int> =
    LinearBufferFactory().let { factory ->
        MatrixImpl(
            factory.buffer(rows.map { row -> row.map { IntElement(it) } }),
            factory
        )
    }