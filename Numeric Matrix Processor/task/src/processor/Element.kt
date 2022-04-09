package processor

import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO

interface Element<T> {

    fun inverse(): Element<T>

    fun isZero(): Boolean

    operator fun minus(other: Element<T>): Element<T> = this + (-other)

    operator fun plus(other: Element<T>): Element<T> = plus(other.value)

    operator fun plus(other: T): Element<T>

    operator fun times(other: Element<T>): Element<T> = times(other.value)

    operator fun times(other: T): Element<T>

    operator fun unaryMinus(): Element<T>

    val value: T

}

class DoubleElement(override val value: Double) : Element<Double> {

    override fun inverse(): Element<Double> = DoubleElement(1.0 / value)

    override fun isZero(): Boolean = value == 0.0

    override fun plus(other: Double): Element<Double> = DoubleElement(value + other)

    override fun times(other: Double): Element<Double> = DoubleElement(value * other)

    override fun toString(): String = value.toString()

    override fun unaryMinus(): Element<Double> = DoubleElement(-value)

}

class IntElement(override val value: Int) : Element<Int> {

    override fun inverse(): Element<Int> = IntElement(0)

    override fun isZero(): Boolean = value == 0

    override fun plus(other: Int): Element<Int> = IntElement(value + other)

    override fun times(other: Int): Element<Int> = IntElement(value * other)

    override fun toString(): String = value.toString()

    override fun unaryMinus(): Element<Int> = IntElement(-value)

}

class DecimalElement(override val value: BigDecimal) : Element<BigDecimal> {

    override fun inverse(): Element<BigDecimal> = DecimalElement(ONE / value)

    override fun isZero(): Boolean = ZERO.compareTo(value) == 0

    override fun plus(other: BigDecimal): Element<BigDecimal> = DecimalElement(value + other)

    override fun times(other: BigDecimal): Element<BigDecimal> = DecimalElement(value + other)

    override fun toString(): String = value.toString()

    override fun unaryMinus(): Element<BigDecimal> = DecimalElement(-value)

}