package gd.rf.acro.blockwake.lib

import net.minecraft.util.math.Vec3i
import kotlin.math.pow
import kotlin.math.sqrt

object VectorHelper {

    fun getMidpoint(a: Vec3i, b: Vec3i): Vec3i {
        return Vec3i(
                (a.x+b.x)/2,
                (a.y+b.y)/2,
                (a.z+b.z)/2
        )
    }

    fun subtract(a: Vec3i, b: Vec3i): Vec3i {
        return Vec3i(
                (a.x-b.x),
                (a.y-b.y),
                (a.z-b.z)
        )
    }

    private infix fun Int.`**`(exponent: Int): Double = toDouble().pow(exponent)

    private fun sum(vararg values: Double): Double {
        var total: Double = 0.0
        for (v in values) {
            total += v
        }
        return total
    }

    fun findMagnitude(a: Vec3i): Double {
        return sqrt(sum(a.x `**` 2, a.y `**` 2, a.z `**` 2))
    }

    fun getUnitVector(a: Vec3i): Vec3i {
        val mag = findMagnitude(a)
        return Vec3i(
                a.x/mag,
                a.y/mag,
                a.z/mag
        )
    }

    fun multiplyVector(a: Vec3i, m: Double): Vec3i {
        return Vec3i(
                a.x*m,
                a.y*m,
                a.z*m
        )
    }
}