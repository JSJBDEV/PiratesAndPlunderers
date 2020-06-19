package gd.rf.acro.blockwake.lib

import net.minecraft.util.math.Vec3i

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

}