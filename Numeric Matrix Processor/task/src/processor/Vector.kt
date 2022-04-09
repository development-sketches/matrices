package processor

import kotlin.math.sqrt

class Vector(private val values: DoubleArray) {

    fun angle(other: Vector): Double = dot(other) / (norm() * other.norm())

    fun dot(other: Vector): Double = values.zip(other.values).sumOf { (value1, value2) -> value1 * value2 }

    fun norm(): Double = sqrt(values.sumOf { it * it })

    fun projectionOn(other: Vector): Vector {
        val unit = other.unit()
        return unit * dot(unit)
    }

    operator fun times(scalar: Double): Vector = Vector(values.map { it * scalar }.toDoubleArray())

    fun unit(): Vector {
        val norm = norm()
        return Vector(values.map { it / norm }.toDoubleArray())
    }

}