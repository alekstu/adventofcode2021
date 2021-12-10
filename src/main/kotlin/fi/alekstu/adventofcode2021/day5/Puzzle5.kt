package fi.alekstu.adventofcode2021.day5

import fi.alekstu.adventofcode2021.day4.Puzzle4

class Puzzle5 {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val input = getInput()
            val data = convertInputToPairs(input)
            val gridSize = 1000
            val grid = Array(gridSize) { Array(gridSize) { Cell() } }

            for (i in data) {
                drawToGrid(i.first, i.second, grid)
            }

            val count = grid.flatten().count { it.value >= 2 }
            print(count)
        }

        private fun convertInputToPairs(input: String): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
            return input.split("\n")
                .map { it.split(" -> ") }
                .chunked(1)
                .flatMap { it.map { that ->
                    Pair(that[0].split(",").map { val1 -> val1.split(",", ", ")}.map { k -> Integer.valueOf(k[0]) },
                         that[1].split(",").map {  val2 -> val2.split(",", ", ")}.map { j -> Integer.valueOf(j[0]) })}}
                .map { Pair(Pair(it.first[0], it.first[1]), Pair(it.second[0], it.second[1])) }

        }

        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        private fun getInput(): String {
           return Puzzle5::class.java.getResource("/day5_input.txt").readText()
        }

        private fun drawToGrid(pos1: Pair<Int,Int>, pos2: Pair<Int,Int>, grid: Array<Array<Cell>>) {
            val isHorizontal = pos1.first == pos2.first
            val isVertical = pos1.second == pos2.second

            if (isVertical) {
                if (pos1.first > pos2.first) {
                    for (i in pos1.first downTo pos2.first) {
                        grid[i][pos2.second].increase()
                    }
                } else {
                    for (i in pos1.first..pos2.first) {
                        grid[i][pos2.second].increase()
                    }
                }
            } else if (isHorizontal) {
                if (pos1.second > pos2.second) {
                    for (i in pos1.second downTo pos2.second) {
                        grid[pos2.first][i].increase()
                    }
                } else {
                    for (i in pos1.second..pos2.second) {
                        grid[pos2.first][i].increase()
                    }
                }
            }
        }


        private fun printGrid(grid: Array<Array<Cell>>) {
            for (i in grid.indices) {
                for (j in grid[i].indices) {
                    print(" ${grid[i][j].value} ")
                }
                println("")
            }
        }

    }



}
class Cell {
    var value = 0
    fun increase() {
        value += 1
    }
}