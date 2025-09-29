package cz.mendelu.project.extensions

fun Double.toPercentage(): String{
    val value = (this * 100.0).round()
    return "${value}%"
}

fun Double.round(): String {
    return String.format("%.2f", this)
}