fun main() {
    val list = List(3) {
        List(3) {
            List(3) { 0 }
        }
    }
    println(list)
}