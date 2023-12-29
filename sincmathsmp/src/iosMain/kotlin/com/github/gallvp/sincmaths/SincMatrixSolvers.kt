package com.github.gallvp.sincmaths

actual fun SincMatrix.solve(b: SincMatrix): SincMatrix = solveNonsymSquareSystem(
    this.matrixData,
    this.numRows(),
    this.numCols(),
    b.matrixData
).asSincMatrix(b.numRows(), b.numCols())