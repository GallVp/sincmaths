package com.github.gallvp.sincmaths

actual fun SincMatrix.solve(b: SincMatrix): SincMatrix = this.asSimpleMatrix().solve(b.asSimpleMatrix()).asSincMatrix()
