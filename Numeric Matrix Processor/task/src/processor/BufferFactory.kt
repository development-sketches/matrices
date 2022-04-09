package processor

interface BufferFactory {

    fun <T> buffer(rows: List<List<T>>): Buffer<T>

    fun <T> buffer(rowCount: Int, rows: (Int) -> List<T>): Buffer<T> = buffer(List(rowCount, rows))

    fun <T> buffer(rowCount: Int, colCount: Int, cells: (Int, Int) -> T): Buffer<T> =
        buffer(List(rowCount) { row -> List(colCount) { col -> cells(row, col) } })

}