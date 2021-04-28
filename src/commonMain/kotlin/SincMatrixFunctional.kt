package sincmaths.sincmatrix

import sincmaths.SincMatrix
import sincmaths.asSincMatrix

fun SincMatrix.map(transform: (value: Double) -> Double) = this.asRowMajorArray().map {
    transform(it)
}.asSincMatrix(this.numRows(), this.numCols())

val SincMatrix.columns: List<SincMatrix>
    get() = this.colIndices.map {
        this.getCol(it)
    }

val SincMatrix.rows: List<SincMatrix>
    get() = this.rowIndices.map {
        this.getRow(it)
    }

fun SincMatrix.mapColumns(transform: (colVector: SincMatrix) -> SincMatrix) = this.columns.map {
    val tVal = transform(it)
    require((this.numRows() == tVal.numRows()) && tVal.iscolumn()) {
        "SMError: Transform should result in a column vector of length equal to the number of rows in parent matrix."
    }
    tVal.asRowMajorArray().toList()
}.flatten().asSincMatrix(this.numCols(), this.numRows()).t

fun SincMatrix.mapRows(transform: (rowVector: SincMatrix) -> SincMatrix) = this.rows.map {
    val tVal = transform(it)
    require((this.numCols() == tVal.numCols()) && tVal.isrow()) {
        "SMError: Transform should result in a row vector of length equal to the number of columns in parent matrix."
    }
    tVal.asRowMajorArray().toList()
}.flatten().asSincMatrix(this.numRows(), this.numCols())