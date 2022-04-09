package processor

class LinearBufferFactory : BufferFactory {

    override fun <T> buffer(rows: List<List<T>>): Buffer<T> {
        val numOfRows = rows.size
        if (numOfRows == 0) {
            return LinearBuffer(Dimension(0, 0), listOf())
        }
        val numOfCols = rows[0].size
        for (i in 1..rows.lastIndex) {
            require(rows[i].size == numOfCols) {
                "Number of columns mismatch: row 0 has $numOfCols columns and row $i has ${rows[i].size} columns."
            }
        }
        val buffer = rows.flatMap { it.asIterable() }.toList()
        return LinearBuffer(Dimension(numOfRows, numOfCols), buffer)
    }

    private class LinearBuffer<T>(
        override val size: Dimension,
        private val buffer: List<T>
    ) : Buffer<T> {

        init {
            require(size.columns * size.rows == buffer.size) {
                "Size mismatch: expected buffer size - ${size.columns * size.rows}; actual buffer size - ${buffer.size}"
            }
        }

        override fun get(row: Int, col: Int): T {
            val index = row * size.columns + col
            require(index in buffer.indices) {
                "Row must be within 0 till ${size.rows} and column within 0 until ${size.columns}"
            }
            return buffer[index]
        }

    }

}