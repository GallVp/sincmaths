package sincmaths.sincmatrix.helpers

import org.ejml.simple.SimpleMatrix
import sincmaths.SincMatrix

internal fun SimpleMatrix.asSincMatrix() = SincMatrix(this.ddrm.data, this.numRows(), this.numCols())
