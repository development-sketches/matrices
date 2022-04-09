package processor

fun main() {
    do {
        val menu = """
            1. Add matrices
            2. Multiply matrix by a constant
            3. Multiply matrices
            4. Transpose matrix
            5. Calculate a determinant
            6. Inverse matrix
            0. Exit
        """.trimIndent()
        println(menu)
        print("Your choice: > ")
        try {
            when (readln().toInt()) {
                1 -> addMatrices()
                2 -> multiplyMatrixByConstant()
                3 -> multiplyMatrices()
                4 -> transposeMatrix()
                5 -> calculateDeterminant()
                6 -> inverseMatrix()
                0 -> break
            }
            println()
        } catch (ex: Exception) {
            println("The operation cannot be performed.")
        }
    } while (true)
}

fun inverseMatrix() {
    print("Enter size of matrix: > ")
    val size = readDimension()
    println("Enter matrix:")
    val matrix = readMatrix(size)
    println("The result is:")
    val inverse = matrix.inverse()
    output(inverse)
}

fun calculateDeterminant() {
    print("Enter size of matrix: > ")
    val size = readDimension()
    println("Enter matrix:")
    val matrix = readMatrix(size)
    println("The result is:")
    println(matrix.determinant())
}

fun transposeMatrix() {
    val menu = """
        1. Main diagonal
        2. Side diagonal
        3. Vertical line
        4. Horizontal line
    """.trimIndent()
    println(menu)
    print("Your choice: > ")
    val type = readln().toInt()
    print("Enter size of matrix: > ")
    val size = readDimension()
    println("Enter matrix:")
    val matrix = readMatrix(size)
    println("The result is:")
    when (type) {
        1 -> output(matrix.transpose(TranspositionType.MAIN))
        2 -> output(matrix.transpose(TranspositionType.SIDE))
        3 -> output(matrix.transpose(TranspositionType.VERTICAL))
        4 -> output(matrix.transpose(TranspositionType.HORIZONTAL))
    }
}

fun multiplyMatrices() {
    print("Enter size of first matrix: > ")
    val size1 = readDimension()
    println("Enter first matrix:")
    val matrix1 = readMatrix(size1)
    print("Enter size of second matrix: > ")
    val size2 = readDimension()
    println("Enter second matrix:")
    val matrix2 = readMatrix(size2)
    println("The result is:")
    output(matrix1 * matrix2)
}

fun multiplyMatrixByConstant() {
    print("Enter size of matrix: > ")
    val size = readDimension()
    println("Enter matrix:")
    val matrix = readMatrix(size)
    print("Enter constant: > ")
    val multiplier = readln().toDouble()
    println("The result is:")
    output(matrix * multiplier)
}

fun addMatrices() {
    print("Enter size of first matrix: > ")
    val size1 = readDimension()
    println("Enter first matrix:")
    val matrix1 = readMatrix(size1)
    print("Enter size of second matrix: > ")
    val size2 = readDimension()
    println("Enter second matrix:")
    val matrix2 = readMatrix(size2)
    println("The result is:")
    output(matrix1 + matrix2)
}

private fun <T : Any> output(matrix: Matrix<T>) {
    (0 until matrix.size.rows).forEach { row ->
        (0 until matrix.size.columns).map { col -> matrix[row, col] }
            .joinToString(" ").let { println(it) }
    }
}

private fun readDimension(): Dimension {
    val values = readln().split(Regex("\\s+")).map { it.toInt() }
    return Dimension(values[0], values[1])
}

private fun readMatrix(size: Dimension): Matrix<Double> =
    doubleMatrix(size.rows) {
        print("> ")
        readln().split(Regex("\\s+")).map { it.toDouble() }
    }
