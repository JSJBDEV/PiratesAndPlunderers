package gd.rf.acro.pap.dimension

import net.minecraft.client.render.SkyProperties
import net.minecraft.util.math.Vec3d


class OceanPocketDimensionSkyProperties() : SkyProperties(Float.NaN, true, class_5401.field_25639, false, true) {

    override fun adjustSkyColor(color: Vec3d?, sunHeight: Float): Vec3d? {
        return color
    }

    override fun useThickFog(camX: Int, camY: Int): Boolean {
        return true
    }
}

