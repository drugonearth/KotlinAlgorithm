import java.lang.StringBuilder
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


class RuConverter: Converter() {

    private val digitMap:Map<Int, Array<String>> = mapOf(
        1000 to arrayOf("", "одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"),
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

    override fun stringToNum(input: String): String {
        val arr = ArrayList(input.toLowerCase().split(" "))

        var output: StringBuilder = StringBuilder()

        var minus = arr[0] == "минус"
        if (minus) arr.removeFirst()

        numSubstringBuilder(3, arr, output)

        if(minus) output.insert(0,"-")

        if(arr.isNotEmpty()) throw Exception("Input error")

        return output.toString()
    }

    private fun numSubstringBuilder(steps: Int, arr: ArrayList<String>, output: StringBuilder)
    {
        search1(arr, output)
        for(i in 1..steps)
        {
            var searched = searchWords(arr, i)

            if(searched == 0 && arr.isNotEmpty() && i!=steps) output.insert(0,"000")
            else {
                if (i == 1) search1for1000(arr, output, searched)
                else search1for1000in2(arr, output, searched)
            }
        }
    }

    private fun oneSubSearch(arr: ArrayList<String> , sb: StringBuilder, from: Int, to: Int, dmPos: Int) : Boolean //1-9 dmpos = ключ в мапе
    {
        if(arr.isEmpty()) return false
        val temp: String = arr.last();

        for(i in 1..9)
        {
            if(temp==digitMap[dmPos]?.get(i))
            {
                arr.removeLast();
                sb.insert(0, i.toString())
                search10(arr, sb)
                return true
            }
        }

        return false
    }

    private fun elevenSubSearch(arr: ArrayList<String> , sb: StringBuilder) : Boolean //10-19
    {
        if(arr.isEmpty()) return false
        val temp: String = arr.last();

        for (i in 0..9) {
            if (temp == digitMap[102]?.get(i)) {
                arr.removeLast();
                sb.insert(0, ("1$i"))
                search100(arr, sb)
                return true
            }
        }
        return false
    }



    private fun search1(arr: ArrayList<String>, sb:StringBuilder) //1-19
    {
        if(arr.isEmpty()) return
        val temp: String = arr.last();
        var find: Boolean = false

        if(!oneSubSearch(arr, sb, 1, 9, 101))
        {
            if(!elevenSubSearch(arr, sb))
            {
                search10(arr, sb)
                sb.append("0")
            }
        }
    }

    private fun search10(arr: ArrayList<String>, sb:StringBuilder)//20-90
    {
        if(arr.isEmpty()) return
        var find: Boolean = false
        val temp: String = arr.last();

        for(i in 2..9)
        {
            if(temp==digitMap[103]?.get(i))
            {
                arr.removeLast();
                sb.insert(0, i.toString())
                find = true
                break
            }
        }

        if(!find)sb.insert(0, "0")
        search100(arr, sb)
    }

    private fun search100(arr: ArrayList<String>, sb:StringBuilder)//100-900
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

    private fun search1for1000(arr: ArrayList<String>, sb:StringBuilder, pos: Int) //1-19 для тысяч
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
                sb.insert(0,"00")
                return
            }
        }
        if(pos == 2) {
            for (i in 2..4) {
                if (temp == digitMap[1000]?.get(i)) {
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
                if (temp == digitMap[1000]?.get(i)) {
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
                sb.insert(0,"0")
            }
        }

    }

    private fun searchWords(arr: ArrayList<String>, nulls: Int): Int//nulls=1 тысяча,2 миллион, 3 миллиард
    {
        if(arr.isEmpty()) return 0
        var find: Boolean = false
        var temp: String = arr.last();

        for(i in 1..9)
        {
            if(temp==digitMap[nulls]?.get(i))
            {
                arr.removeLast();
                if(arr.isEmpty() && i > 1) throw Exception("input error")
                return i
            }
        }

        return 0
    }

    private fun search1for1000in2(arr: ArrayList<String>, sb:StringBuilder, pos: Int)//1-19 для миллиона+, pos номер в мапе
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
                sb.insert(0,"00")
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
                sb.insert(0,"0")
                search10(arr, sb)
            }
        }

    }

    private fun wordsSubStringBuilder(output: StringBuilder, input: String, order: Int ) //input подстрока из 1-3 цифр, order количество нулей x3
    {
        when(input.length) {
            1-> {
                when {
                    order == 1 -> { //тысячи
                        if (input[0].toString().toInt() != 1)
                            output.append(digitMap[1000]?.get(input[0].toString().toInt()) + " ")
                        output.append(digitMap[order]?.get(input[0].toString().toInt()) + " ")
                    }
                    order > 1 -> { //миллионы+
                        if (input[0].toString().toInt() != 1)
                            output.append(digitMap[101]?.get(input[0].toString().toInt()) + " ")
                        output.append(digitMap[order]?.get(input[0].toString().toInt()) + " ")
                    }
                    else -> output.append(digitMap[101]?.get(input[0].toString().toInt()) + " ")
                }
            }

            2-> buildDozen(output, input, order)

            3-> {
                if (input[0] != '0')
                    output.append(digitMap[104]?.get(input[0].toString().toInt()) + " ")
                buildDozen(output, input, order)
            }
        }
    }

    private fun buildDozen(output: StringBuilder,input: String, order: Int)
    {
        if (input[input.length-2].toString().toInt() == 1) {
            output.append((digitMap[102]?.get(input[input.length - 1].toString().toInt())) + " ")
            if (order > 0) output.append(digitMap[order]?.get(5) + " ")
        }
        else
        {
            output.append(digitMap[103]?.get(input[input.length-2].toString().toInt()) + " ")

            if(order==1) output.append(digitMap[1000]?.get(input[input.length-1].toString().toInt()) + " ")
            else output.append(digitMap[101]?.get(input[input.length-1].toString().toInt()) + " ")

            if(order>0) output.append(digitMap[order]?.get(input[input.length-1].toString().toInt()) + " ")
        }
    }

    override fun numToString(input: String, minus: Boolean): String {
        val arr: Array<String> = input.split(",").toTypedArray()

        if(arr.size == 1 && arr[0].length == 1 && arr[0].toInt() == 0) return "ноль"

        var output: StringBuilder = StringBuilder("")
        if(minus) output.append("минус ")

        for(i in arr.indices) {
            wordsSubStringBuilder(output, arr[i], arr.size-i-1)
        }

        return output.toString().replace("  ", " ").replace("  ", " ");
    }


}