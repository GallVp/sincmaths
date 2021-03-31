import org.ejml.simple.SimpleMatrix

internal fun SimpleMatrix.asSincMatrix() = SincMatrix(this.ddrm.data, this.numRows(), this.numCols())
