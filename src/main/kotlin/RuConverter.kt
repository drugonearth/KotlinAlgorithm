import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.abs


class RuConverter {

    private val digitMap:Map<Int, Array<String>> = mapOf(
        100 to arrayOf("", "одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"),
        101 to arrayOf("", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"),
        102 to arrayOf("десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать",
            "семнадцать", "восемнадцать", "девятнадцать"),
        103 to arrayOf("","", "двадцать", "тридцать", "сорок", "пятьдесят",
            "шестьдесят", "семьдесят", "восемьдесят", "девяносто"),
        104 to arrayOf("","сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"),
        1 to arrayOf("тысяч","тысяча", "тысячи", "тысячи", "тысячи", "тысяч", "тысяч", "тысяч", "тысяч", "тысяч"),
        2 to arrayOf("миллионов","миллион", "миллиона", "миллиона", "миллиона", "миллионов",
            "миллионов", "миллионов", "миллионов", "миллионов"),
        3 to arrayOf("миллиардов","миллиард", "миллиарда", "миллиарда", "миллиарда", "миллиардов",
            "миллиардов", "миллиардов", "миллиардов", "миллиардов"),
    )

    private fun stringToNum(input: String): String? {
        var arr = ArrayList(input.split(" "))

        var output: StringBuilder = StringBuilder()

        search1(arr, output)

        search1for1000(arr, output, search1000(arr))

        search1for1000in2(arr, output, search1000in2(arr, 2))

        search1for1000in2(arr, output, search1000in2(arr, 3))

        return output.toString()
    }

    private fun search1(arr: ArrayList<String>, sb:StringBuilder)
    {
        if(arr.isEmpty()) return
        var temp: String = arr.last();
        var find: Boolean = false

        for(i in 1..9)
        {
            if(temp==digitMap[101]?.get(i))
            {
                arr.removeLast();
                sb.insert(0, i.toString())
                search10(arr, sb)
                find = true
                break
            }
        }

        if(!find) {
            for (i in 0..9) {
                if (temp == digitMap[102]?.get(i)) {
                    arr.removeLast();
                    sb.insert(0, ("1$i"))
                    search100(arr, sb)
                    find = true
                    break
                }
            }
        }

        if(!find) {
            search10(arr, sb)
            sb.append("0")
        }
    }

    private fun search10(arr: ArrayList<String>, sb:StringBuilder)
    {
        if(arr.isEmpty()) return
        var find: Boolean = false
        var temp: String? = arr?.last();

        for(i in 2..9)
        {
            if(temp==digitMap[103]?.get(i))
            {
                arr?.removeLast();
                sb.insert(0, i.toString())
                find = true
                break
            }
        }

        if(!find)sb.insert(0, "0")
        search100(arr, sb)
    }

    private fun search100(arr: ArrayList<String>, sb:StringBuilder)
    {
        if(arr.isEmpty()) return
        var find: Boolean = false
        var temp: String? = arr?.last();

        for(i in 1..9)
        {
            if(temp==digitMap[104]?.get(i))
            {
                sb.insert(0, i.toString())
                arr?.removeLast();
                find = true;
                break
            }
        }

        if(!find)sb.insert(0,"0")
    }

    private fun search1000(arr: ArrayList<String>): Int
    {
        if(arr.isEmpty()) return 0
        var find: Boolean = false
        var temp: String? = arr?.last();

        for(i in 1..9)
        {
            if(temp==digitMap[1]?.get(i))
            {
                arr?.removeLast();
                return i
            }
        }

        return 0
    }

    private fun search1for1000(arr: ArrayList<String>, sb:StringBuilder, pos: Int)
    {
        if (pos==1) sb.insert(0, "1")

        if(arr.isEmpty()) return
        var temp: String = arr.last();
        var find: Boolean = false

        if(pos == 1)
        {
            if(temp == "одна")
            {
                arr.removeLast();
                search10(arr, sb)
                find = true
            }
            else
            {
                return
            }
        }
        if(pos == 2) {
            for (i in 2..4) {
                if (temp == digitMap[100]?.get(i)) {
                    arr.removeLast();
                    sb.insert(0, i.toString())
                    search10(arr, sb)
                    find = true
                    break
                }
            }
        }
        if(pos == 5){
            for (i in 5..9) {
                if (temp == digitMap[100]?.get(i)) {
                    arr.removeLast();
                    sb.insert(0, i.toString())
                    search10(arr, sb)
                    find = true
                    break
                }
            }

            if(!find) {
                for (i in 0..9) {
                    if (temp == digitMap[102]?.get(i)) {
                        arr.removeLast();
                        sb.insert(0, ("1$i"))
                        search100(arr, sb)
                        find = true
                        break
                    }
                }
            }

            if(!find) {
                search10(arr, sb)
                sb.append("0")
            }
        }
    }

    private fun search1000in2(arr: ArrayList<String>, nulls: Int): Int
    {
        if(arr.isEmpty()) return 0
        var find: Boolean = false
        var temp: String? = arr?.last();

        for(i in 1..9)
        {
            if(temp==digitMap[nulls]?.get(i))
            {
                arr?.removeLast();
                return i
            }
        }

        return 0
    }

    private fun search1for1000in2(arr: ArrayList<String>, sb:StringBuilder, pos: Int)
    {
        if (pos == 1) sb.insert(0,"1")

        if(arr.isEmpty()) return
        var temp: String = arr.last();
        var find: Boolean = false

        if(pos == 1)
        {
            if(temp=="один")
            {
                arr.removeLast();
                search10(arr, sb)
                find = true
            }
            else
            {
                return
            }
        }
        if(pos == 2) {
            for (i in 2..4) {
                if (temp == digitMap[101]?.get(i)) {
                    arr.removeLast();
                    sb.insert(0, i.toString())
                    search10(arr, sb)
                    find = true
                    break
                }
            }
        }
        if(pos == 5){
            for (i in 5..9) {
                if (temp == digitMap[101]?.get(i)) {
                    arr.removeLast();
                    sb.insert(0, i.toString())
                    search10(arr, sb)
                    find = true
                    break
                }
            }

            if(!find) {
                for (i in 0..9) {
                    if (temp == digitMap[102]?.get(i)) {
                        arr.removeLast();
                        sb.insert(0, ("1$i"))
                        search100(arr, sb)
                        find = true
                        break
                    }
                }
            }

            if(!find) {
                search10(arr, sb)
                sb.append("0")
            }
        }
    }


    fun gettingData(input: String): String? {
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

    private fun subStringBuilder(input: String, order: Int ): String?{
        var output: StringBuilder = StringBuilder()

        if (input.length == 1)
        {
            when {
                order == 1 -> { //тысячи
                    if(input[0].toString().toInt()!=1)
                        output.append(digitMap[100]?.get(input[0].toString().toInt()) + " ")
                    output.append(digitMap[order]?.get(input[0].toString().toInt()) + " ")
                }
                order > 1 -> { //миллионы+
                    if(input[0].toString().toInt()!=1)
                        output.append(digitMap[101]?.get(input[0].toString().toInt()) + " ")
                    output.append(digitMap[order]?.get(input[0].toString().toInt()) + " ")
                }
                else -> output.append(digitMap[101]?.get(input[0].toString().toInt()) + " ")
            }
        }

        else if (input.length == 2) {
            when {
                order == 1 -> {
                    if (input[0].toString().toInt() == 1)
                        output.append((digitMap[102]?.get(input[1].toString().toInt())) + " " + "тысяч ")
                    else {
                        output.append(digitMap[103]?.get(input[0].toString().toInt()) + " ")
                        output.append(digitMap[100]?.get(input[1].toString().toInt()) + " ")
                        output.append(digitMap[order]?.get(input[1].toString().toInt()) + " ")
                    }
                }
                order > 1 -> {
                    if (input[0].toString().toInt() == 1)
                        output.append((digitMap[102]?.get(input[1].toString().toInt())) + " " + digitMap[order]?.get(5))
                    else {
                        output.append(digitMap[103]?.get(input[0].toString().toInt()) + " ")
                        output.append(digitMap[101]?.get(input[1].toString().toInt()) + " ")
                        output.append(digitMap[order]?.get(input[1].toString().toInt()) + " ")
                    }
                }
                else -> {
                    output.append(digitMap[103]?.get(input[0].toString().toInt()) + " ")
                    output.append(digitMap[101]?.get(input[1].toString().toInt()) + " ")
                }
            }
        }

        else
        {
            if(input[0]!='0')
                output.append(digitMap[104]?.get(input[0].toString().toInt()) + " ")
            when {
                order == 1 -> {
                    if (input[1].toString().toInt() == 1)
                        output.append((digitMap[102]?.get(input[2].toString().toInt())) + " " + "тысяч ")
                    else {
                        output.append(digitMap[103]?.get(input[1].toString().toInt()) + " ")
                        output.append(digitMap[100]?.get(input[2].toString().toInt()) + " ")
                        output.append(digitMap[order]?.get(input[2].toString().toInt()) + " ")
                    }
                }
                order > 1 -> {
                    if (input[1].toString().toInt() == 1)
                        output.append((digitMap[102]?.get(input[2].toString().toInt())) + " " + digitMap[order]?.get(5))
                    else {
                        output.append(digitMap[103]?.get(input[1].toString().toInt()) + " ")
                        output.append(digitMap[101]?.get(input[2].toString().toInt()) + " ")
                        output.append(digitMap[order]?.get(input[2].toString().toInt()) + " ")
                    }
                }
                else -> {
                    output.append(digitMap[103]?.get(input[1].toString().toInt()) + " ")
                    output.append(digitMap[101]?.get(input[2].toString().toInt()) + " ")
                }
            }
        }

        return output.toString()
    }

    private fun numToString(input: String, minus: Boolean): String? {
        val arr: Array<String> = input.split(",").toTypedArray()

        if(arr.size == 1 && arr[0].length == 1 && arr[0].toInt() == 0) return "ноль"

        var output: StringBuilder = StringBuilder("")
        if(minus) output.append("минус ")

        for(i in arr.indices) {
            output.append(subStringBuilder(arr[i], arr.size-i-1))
        }

        return output.toString().replace("  ", " ").replace("  ", " ");
    }


}