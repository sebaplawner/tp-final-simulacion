package tpFinalSimulacion

import java.util.*
import kotlin.math.roundToInt

object Main {
    var t = 0
    var borrado = false
    var i = 0
    var costoTotal = 0.0
    var imgs = 0
    var backups = emptyArray<Int>()
    var eau = 0
    var costoExtra = 0.0

    var ta = 0
    var n = 0
    var m = 0
    var eas = 0

    var tf = 0

    var costoMensual = 0
    val random = Random()

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size < 5) {
            println("java -jar tpFinalSimulacion.jar [TA] [N] [M] [EAS] [TF]")
            return
        }

        ta = args[0].toInt()
        n = args[1].toInt()
        m = args[2].toInt()
        eas = args[3].toInt()
        tf = args[4].toInt()
        backups = arrayOf(n)
        costoMensual = obtenerCostoMensual()

        while (t < tf)
            ciclar()

        val costoMensual2 = ((costoTotal + costoExtra) / t) * 30

        println("Plan elegido:\t%dGB %d USD".format(eas / 1000, costoMensual))
        println("Costo mensual:\t%.2f USD".format(costoMensual2))
        println("Costo extra:\t%.2f USD".format(costoExtra))
        println("Costo total:\t%.2f USD".format(costoExtra + costoMensual2))
        println()
        println("TA:\t%d".format(ta))
        println("N:\t%d".format(n))
        println("M:\t%d".format(m))
        println("EAS:\t%d".format(eas))
    }

    private fun ciclar() {
        t += 1

        val iss = obtenerIss()
        imgs = ta * iss

        if (i > n) {
            i = 0
            borrado = true
        }

        if (borrado)
            eau -= backups[i]

        backups[i] = (imgs * .8).roundToInt()
        eau += backups[i] + imgs

        if (eau > eas) {
            val gbExtras = eau - eas
            costoExtra += (gbExtras / 1000) * 0.5
        }

        if (t.rem(30) == 0)
            costoTotal += costoMensual

        if (t.rem(m) == 0) {
            if (eas < eau)
                println("GB excedidos: %dGB".format((eau - eas) / 1000))
            eau = 0
            borrado = false
            backups = arrayOf(n)
            i = 0
        }
    }

    private fun obtenerIss(): Int {
        val k = 14.786
        val a = 1.4989
        val b = 976.13
        val y = 1.5176

        val M = 0.00452

        var fxi = .0
        var x1 = 0
        var y1 = .0

        val a1 = 4
        val b1 = 578

        while (fxi >= y1) {
            val r1 = random.nextDouble()
            val r2 = random.nextDouble()

            x1 = (a1 + (b1 - a1) * r1).toInt()
            y1 = M * r2

            fxi = (a * k * Math.pow((x1 - y) / b, a - 1)) / (b * Math.pow(1 + Math.pow((x1 - y) / b, a), k + 1))
        }

        return x1
    }

    private fun obtenerCostoMensual(): Int {
        return when (eas) {
            25000 -> 5
            50000 -> 10
            80000 -> 20
            160000 -> 40
            320000 -> 80
            else -> throw Exception("EAS no permitido")
        }
    }

}