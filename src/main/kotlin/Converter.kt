import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.abs

abstract class Converter {
    fun gettingData(input: String): String {
        var data = input

        return try {
            data = data.replace(" ", "")
            val long:Long = data.toLong()
            val big = BigInteger(abs(long).toString())
            numToString(DecimalFormat(",###.##", DecimalFormatSymbols(Locale.UK)).format(big), long<0)
        } catch (e: NumberFormatException) {
            stringToNum(input)
        }
    }

    abstract fun numToString(input: String, minus: Boolean): String

    abstract fun stringToNum(input: String): String
}