package fi.alekstu.adventofcode2021.day4

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Puzzle1 {

    companion object {
        @JvmStatic
        fun main(args : Array<String>) {
            val input = Puzzle1::class.java.getResource("/day4_1_input.txt").readText()
            val numbers = readNumbers(input)
            val boards = splitToBoards(input)
            val bingo = Bingo(boards, numbers)
            val winnerBoardAndWinningNumber = bingo.play()
            val unlitSum = winnerBoardAndWinningNumber.first?.getSum { !it.lit }
            println(winnerBoardAndWinningNumber.second * unlitSum!!)
        }


        private fun readNumbers(input : String) : List<Int> {
           return input.split("\n")
               .first()
               .split(",")
               .map { Integer.parseInt(it) }
               .toList()
        }

        private fun splitToBoards(inputStr : String) : List<BingoBoard> {
            return inputStr.split("\n", "\n", " ", "  ")
                .asSequence()
                .drop(1)
                .filter { it.isNotBlank() }
                .chunked(5) { it.map { str -> Integer.parseInt(str) } }
                .chunked(5)
                .map { BingoBoard(5, it) }
                .toList()
        }

    }


    class Bingo constructor(
        private val bingoBoards: List<BingoBoard>,
        private val bingoNumbers : List<Int>) {

        fun play() : Pair<BingoBoard?, Int> {
            for (i in bingoNumbers) {
                bingoBoards.forEach{ it.onNumber(i) }
                val winner = getWinner()
                if (winner != null) {
                    return winner to i
                }
            }
            throw NoWinnerFoundException("No winner found for BINGO")
        }

        private fun getWinner() : BingoBoard? {
            return bingoBoards.firstOrNull { it.isBingo() }
        }
    }

    class BingoBoard constructor(
        private val gridSize: Int = 5,
        private val values: List<List<Int>>) {

        private val grid = Array(gridSize) { i ->
            Array(gridSize) { j ->
                BoardCell(values[i][j])
            }
        }

        fun onNumber(number: Int) {
            grid.asSequence()
                .flatMap { it.iterator().asSequence() }
                .find { it.number == number && !it.lit}?.apply { lit = true }
        }

        fun isBingo() : Boolean {
            for (x in 0 until gridSize) {
                var horizontalBingo = true
                var verticalBingo = true
                for (i in 0 until gridSize) {
                    for (j in 0 until gridSize) {
                        horizontalBingo = horizontalBingo && grid[x][i].lit
                        verticalBingo = verticalBingo && grid[j][x].lit
                    }
                }
                if (horizontalBingo || verticalBingo) {
                    return true
                }
            }
            return false
        }

        fun getSum(predicate : (BoardCell) -> Boolean) : Int {
            return grid.flatten().filter { predicate(it) }.sumOf { it.number }
        }

    }

    class BoardCell constructor(val number : Int) {
        var lit = false
        override fun toString(): String {
            return "$number ${if (lit) "*" else ""}"
        }
    }

    class NoWinnerFoundException(msg : String) : RuntimeException(msg)
}