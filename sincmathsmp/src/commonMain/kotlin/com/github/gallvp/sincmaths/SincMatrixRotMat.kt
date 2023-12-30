package com.github.gallvp.sincmaths

import kotlin.math.sqrt

fun SincMatrix.quat2rotm(): SincMatrix = SincMatrix.quat2rotm(quat = this.asArray()).asSincMatrix(m = 3, n = 3)

fun SincMatrix.rotm2quat(): SincMatrix = SincMatrix.rotm2quat(rotm = this.asRowMajorArray()).asSincMatrix()

fun SincMatrix.Companion.eul2rotm(
    xyzRadianAngles: DoubleArray,
    sequence: AngleSequence,
): SincMatrix {
    val rowOne: DoubleArray
    val rowTwo: DoubleArray
    val rowThree: DoubleArray
    val cosTheta: DoubleArray =
        (doubleArrayOf(0.0) + xyzRadianAngles).map {
            kotlin.math.cos(it)
        }.toDoubleArray()
    // For Octave like indexing
    val sinTheta: DoubleArray =
        (doubleArrayOf(0.0) + xyzRadianAngles).map {
            kotlin.math.sin(it)
        }.toDoubleArray()
    // For Octave like indexing
    when (sequence) {
        AngleSequence.XYZ -> {
            rowOne =
                doubleArrayOf(
                    cosTheta[2] * cosTheta[3],
                    -cosTheta[2] * sinTheta[3],
                    sinTheta[2],
                )
            rowTwo =
                doubleArrayOf(
                    cosTheta[1] * sinTheta[3] + cosTheta[3] * sinTheta[1] * sinTheta[2],
                    cosTheta[1] * cosTheta[3] - sinTheta[1] * sinTheta[2] * sinTheta[3],
                    -cosTheta[2] * sinTheta[1],
                )
            rowThree =
                doubleArrayOf(
                    sinTheta[1] * sinTheta[3] - cosTheta[1] * cosTheta[3] * sinTheta[2],
                    cosTheta[3] * sinTheta[1] + cosTheta[1] * sinTheta[2] * sinTheta[3],
                    cosTheta[1] * cosTheta[2],
                )
        }
        AngleSequence.ZYX -> {
            rowOne =
                doubleArrayOf(
                    cosTheta[2] * cosTheta[1],
                    sinTheta[3] * sinTheta[2] * cosTheta[1] - cosTheta[3] * sinTheta[1],
                    cosTheta[3] * sinTheta[2] * cosTheta[1] + sinTheta[3] * sinTheta[1],
                )
            rowTwo =
                doubleArrayOf(
                    cosTheta[2] * sinTheta[1],
                    sinTheta[3] * sinTheta[2] * sinTheta[1] + cosTheta[3] * cosTheta[1],
                    cosTheta[3] * sinTheta[2] * sinTheta[1] - sinTheta[3] * cosTheta[1],
                )
            rowThree =
                doubleArrayOf(
                    -sinTheta[2],
                    sinTheta[3] * cosTheta[2],
                    cosTheta[3] * cosTheta[2],
                )
        }
        AngleSequence.ZYZ -> {
            rowOne =
                doubleArrayOf(
                    cosTheta[1] * cosTheta[3] * cosTheta[2] - sinTheta[1] * sinTheta[3],
                    -cosTheta[1] * cosTheta[2] * sinTheta[3] - sinTheta[1] * cosTheta[3],
                    cosTheta[1] * sinTheta[2],
                )
            rowTwo =
                doubleArrayOf(
                    sinTheta[1] * cosTheta[3] * cosTheta[2] + cosTheta[1] * sinTheta[3],
                    -sinTheta[1] * cosTheta[2] * sinTheta[3] + cosTheta[1] * cosTheta[3],
                    sinTheta[1] * sinTheta[2],
                )
            rowThree =
                doubleArrayOf(
                    -sinTheta[2] * cosTheta[3],
                    sinTheta[2] * sinTheta[3],
                    cosTheta[2],
                )
        }
    }
    return SincMatrix(rowMajArray = rowOne + rowTwo + rowThree, m = 3, n = 3)
}

/**
 * @return A row major pre-multiplying 3 x 3 matrix. quat<w, x, y, z>
 */
fun SincMatrix.Companion.quat2rotm(quat: DoubleArray): DoubleArray {
    val quatNormVal = sqrt(quat.map { it * it }.sum())
    val quatNorm = quat.map { it / quatNormVal }
    val qw: Double = quatNorm[0]
    val qx: Double = quatNorm[1]
    val qy: Double = quatNorm[2]
    val qz: Double = quatNorm[3]
    return doubleArrayOf(
        1 - 2 * (qy * qy + qz * qz),
        2 * (qx * qy - qw * qz),
        2 * (qx * qz + qw * qy),
        2 * (qx * qy + qw * qz),
        1 - 2 * (qx * qx + qz * qz),
        2 * (qy * qz - qw * qx),
        2 * (qx * qz - qw * qy),
        2 * (qy * qz + qw * qx),
        1 - 2 * (qx * qx + qy * qy),
    )
}

/**
 * Citation: Mike Day, Insomniac Games, "Converting a Rotation Matrix to a Quaternion".
 * The code in this document is for post-multiplying rotation matrix.
 *
 * @param rotm A row major pre-multiplying 3 x 3 matrix. quat<w, x, y, z>
 *
 */
fun SincMatrix.Companion.rotm2quat(rotm: DoubleArray): DoubleArray {
    // Citation:
    //
    val m00 = rotm[0]
    val m10 = rotm[1]
    val m20 = rotm[2]
    val m01 = rotm[3]
    val m11 = rotm[4]
    val m21 = rotm[5]
    val m02 = rotm[6]
    val m12 = rotm[7]
    val m22 = rotm[8]
    val t: Double
    var q: DoubleArray
    // <w:Double, x:Double, y:Double, z: Double>
    if ((m22 < 0)) {
        if ((m00 > m11)) {
            t = 1 + m00 - m11 - m22
            q = doubleArrayOf(m12 - m21, t, m01 + m10, m20 + m02)
        } else {
            t = 1 - m00 + m11 - m22
            q = doubleArrayOf(m20 - m02, m01 + m10, t, m12 + m21)
        }
    } else {
        if ((m00 < -m11)) {
            t = 1 - m00 - m11 + m22
            q = doubleArrayOf(m01 - m10, m20 + m02, m12 + m21, t)
        } else {
            t = 1 + m00 + m11 + m22
            q = doubleArrayOf(t, m12 - m21, m20 - m02, m01 - m10)
        }
    }
    q = q.map { it * 0.5 / sqrt(t) }.toDoubleArray()
    if (q[0] < 0.0) {
        // Keep scaler as positive
        q = q.map { -it }.toDoubleArray()
    }
    return q
}
