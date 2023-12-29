package com.github.gallvp.sincmaths

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
        "Transform should result in a column vector of length equal to the number of rows in parent matrix."
    }
    tVal.asRowMajorArray().toList()
}.flatten().asSincMatrix(this.numCols(), this.numRows()).t

fun SincMatrix.mapRows(transform: (rowVector: SincMatrix) -> SincMatrix) = this.rows.map {
    val tVal = transform(it)
    require((this.numCols() == tVal.numCols()) && tVal.isrow()) {
        "Transform should result in a row vector of length equal to the number of columns in parent matrix."
    }
    tVal.asRowMajorArray().toList()
}.flatten().asSincMatrix(this.numRows(), this.numCols())

fun SincMatrix.mapColumnsToDouble(transform: (colVector: SincMatrix) -> Double) : SincMatrix = this.columns.map {
    transform(it)
}.asSincMatrix()

fun SincMatrix.mapRowsToDouble(transform: (rowVector: SincMatrix) -> Double) = this.rows.map {
    transform(it)
}.asSincMatrix(false)

fun SincMatrix.mapColumns(resultLen:Int, transform: (colVector: SincMatrix) -> SincMatrix) = this.columns.map {
    val tVal = transform(it)
    require((resultLen == tVal.numRows()) && tVal.iscolumn()) {
        "Transform should result in a column vector of length equal to $resultLen."
    }
    tVal.asRowMajorArray().toList()
}.flatten().asSincMatrix(this.numCols(), resultLen).t

fun SincMatrix.mapRows(resultLen:Int, transform: (rowVector: SincMatrix) -> SincMatrix) = this.rows.map {
    val tVal = transform(it)
    require((resultLen == tVal.numCols()) && tVal.isrow()) {
        "Transform should result in a row vector of length equal to $resultLen."
    }
    tVal.asRowMajorArray().toList()
}.flatten().asSincMatrix(this.numRows(), resultLen)

fun SincMatrix.mapColumnsToList(transform: (colVector: SincMatrix) -> SincMatrix) : List<SincMatrix> = this.columns.map {
    val tVal = transform(it)
    require(tVal.iscolumn()) {
        "Transform should result in a column vector."
    }
    tVal
}

fun SincMatrix.mapRowsToList(transform: (rowVector: SincMatrix) -> SincMatrix) : List<SincMatrix> = this.rows.map {
    val tVal = transform(it)
    require(tVal.isrow()) {
        "Transform should result in a row vector."
    }
    tVal
}