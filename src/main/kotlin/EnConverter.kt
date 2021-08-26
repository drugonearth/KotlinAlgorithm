import java.lang.StringBuilder

class EnConverter : Converter(){

    private val digitMap:Map<Int, Array<String>> = mapOf(
        101 to arrayOf("", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"),
        111 to arrayOf("ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
            "seventeen", "eighteen", "nineteen"),
        120 to arrayOf("","", "twenty", "thirty", "forty", "fifty",
            "sixty", "seventy", "eighty", "ninety"),
        1 to arrayOf("thousand","thousands"),
        2 to arrayOf("million", "millions"),
        3 to arrayOf("milliard", "milliards" ),
    )

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



}