import java.lang.StringBuilder

class EnConverter : Converter(){

    private val digitMap:Map<Int, Array<String>> = mapOf(
        101 to arrayOf("", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"),
        111 to arrayOf("ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
            "seventeen", "eighteen", "nineteen"),
        120 to arrayOf("","", "twenty", "thirty", "forty", "fifty",
            "sixty", "seventy", "eighty", "ninety"),
        0 to arrayOf("hundred","hundreds"),
        1 to arrayOf("thousand","thousands"),
        2 to arrayOf("million", "millions"),
        3 to arrayOf("milliard", "milliards" ),
    )

    override fun stringToNum(input: String): String {
        val arr = ArrayList(input.toLowerCase().split(" "))

        var output: StringBuilder = StringBuilder()

        var minus = false

        if (arr[0] == "minus")
        {
            minus = true
            arr.removeFirst()
        }

        return output.toString()
    }

    private fun numSubstringBuilder(steps: Int, arr: ArrayList<String>, output: StringBuilder) //steps максимальная степень 1000
    {
        search1(arr, output)
        for(i in 1..steps)
        {
            var searched = searchWords(arr, i)

            if(searched == 0 && arr.isNotEmpty() && i!=steps) output.insert(0,"000")
            else {
                if (i == 1) search1for1000(arr, output, searched, 1000)
                else search1for1000(arr, output, searched, 101)
            }
        }
    }

    private fun subSearch(arr: ArrayList<String>, sb: StringBuilder, from: Int, to: Int, dmPos: Int) : Boolean //поиск слова в мапе, dmpos = ключ в мапе
    {
        if(arr.isEmpty()) return false
        val temp: String = arr.last();

        for(i in from..to)
        {
            if(temp==digitMap[dmPos]?.get(i))
            {
                arr.removeLast();
                sb.insert(0, i.toString())
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
            if (temp == digitMap[111]?.get(i)) {
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

        if(!subSearch(arr, sb, 1, 9, 101))
        {
            if(!elevenSubSearch(arr, sb))
            {
                search10(arr, sb)
                sb.append("0")
            }
        }
        else if(arr.isNotEmpty()) search10(arr, sb)
    }

    private fun search10(arr: ArrayList<String>, sb:StringBuilder)//20-90
    {
        if(arr.isEmpty()) return

        if(!subSearch(arr, sb, 2, 9, 120))sb.insert(0,"0")

        search100(arr, sb)
    }

    private fun search100(arr: ArrayList<String>, sb:StringBuilder)//100-900
    {
        if(arr.isEmpty()) return

        val position = searchWords(arr, sb, 0)
        if(position < 2)
        {
            when(position) {
                0 ->
                {
                    if(arr.last()=="one") arr.removeLast()
                    sb.insert(0, "1")
                }
                1 -> {
                    subSearch(arr, sb, )
                }
            }
        }
    }

    private fun searchWords(arr: ArrayList<String>, sb:StringBuilder, nulls: Int): Int//nulls= 0 сто, 1 тысяча,2 миллион, 3 миллиард
    {
        if(arr.isEmpty()) return 2

        var temp: String = arr.last();

        for(i in 0..1)
        {
            if(temp==digitMap[nulls]?.get(i))
            {
                arr.removeLast();
                if(arr.isEmpty())
                {
                    if(i == 0) {
                        sb.insert(0, "1")
                    }
                    else throw Exception("input error")
                }
                return i
            }
        }

        return 2
    }

    private fun search1for1000(arr: ArrayList<String>, sb:StringBuilder, pos: Int, mapPos: Int) //1-19 для тысяч, миллионов, миллиардов
    {
        var temp: String = arr.last();

        when(pos) {
            1 -> {
                if (temp == digitMap[mapPos]?.get(1)) {
                    arr.removeLast();
                    search10(arr, sb)
                } else sb.insert(0, "00")

            }
            2-> {
                if (subSearch(arr, sb, 2, 4, mapPos)) {
                    if (arr.isNotEmpty()) search10(arr, sb)
                }
            }
            5 -> {
                if (!subSearch(arr, sb, 5, 9, mapPos)) {
                    if (!elevenSubSearch(arr, sb)) {
                        search10(arr, sb)
                        sb.append("0")
                    }
                } else if (arr.isNotEmpty()) search10(arr, sb)
            }
        }
    }

    override fun numToString(input: String, minus: Boolean): String {
        val arr: Array<String> = input.split(",").toTypedArray()

        if(arr.size == 1 && arr[0].length == 1 && arr[0].toInt() == 0) return "zero"

        var output: StringBuilder = StringBuilder("")
        if(minus) output.append("minus ")

        for(i in arr.indices) {
            wordsSubStringBuilder(arr[i], arr.size-i-1, output)
        }

        return output.toString().replace("  ", " ").replace("  ", " ");
    }

    private fun wordsSubStringBuilder(input: String, order: Int , output: StringBuilder): String //order степень тысячи
    {
        when(input.length){
            1 -> {
                output.append(digitMap[101]?.get(input[0].toString().toInt()) + " ")

                if(order > 0) {
                    if (input[0].toString().toInt() != 1) output.append(digitMap[order]?.get(1) + " ")
                    else output.append(digitMap[order]?.get(0) + " ")
                }
            }

            2-> {
                buildDozen(output, input, order)
            }

            3 ->
            {
                if(input[0]!='0') {
                    output.append(digitMap[101]?.get(input[0].toString().toInt()) + " ")

                    if(input[0]=='1')output.append("hundred ")
                    else output.append("hundreds ")
                }
                buildDozen(output, input, order)
            }
        }

        return output.toString()
    }

    private fun buildDozen(output: StringBuilder,input: String, order: Int)
    {
        if (input[input.length-2].toString().toInt() == 1)
            output.append((digitMap[111]?.get(input[input.length-1].toString().toInt())) + " ")
        else {
            output.append(digitMap[120]?.get(input[input.length-2].toString().toInt()) + " ")
            output.append(digitMap[101]?.get(input[input.length-1].toString().toInt()) + " ")
        }
        if(order > 0) output.append(digitMap[order]?.get(1) + " ")
    }

}