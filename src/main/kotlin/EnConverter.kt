import java.lang.StringBuilder

class EnConverter : Converter(){

    private val digitMap:Map<Int, Array<String>> = mapOf(
        101 to arrayOf("", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"),
        102 to arrayOf("ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
            "seventeen", "eighteen", "nineteen"),
        103 to arrayOf("","", "twenty", "thirty", "forty", "fifty",
            "sixty", "seventy", "eighty", "ninety"),
    )

    override fun numToString(input: String, minus: Boolean): String {
        val arr: Array<String> = input.split(",").toTypedArray()

        if(arr.size == 1 && arr[0].length == 1 && arr[0].toInt() == 0) return "zero"

        var output: StringBuilder = StringBuilder("")
        if(minus) output.append("minus ")

        for(i in arr.indices) {
            output.append(subStringBuilder(arr[i], arr.size-i-1))
        }

        return output.toString().replace("  ", " ").replace("  ", " ");
    }

    private fun subStringBuilder(input: String, order: Int ): String {
        var output: StringBuilder = StringBuilder()

        when(input.length){
            1 -> {
                output.append(digitMap[101]?.get(input[0].toString().toInt()) + " ")
                when(order) {
                    1 -> { // thousand
                        if(input[0].toString().toInt()!=1)
                            output.append("thousands" + " ")
                        else
                            output.append("thousand" + " ")
                    }
                    2 -> { // million
                        if(input[0].toString().toInt()!=1)
                            output.append("millions" + " ")
                        else
                            output.append("million" + " ")
                    }
                    3 -> { // milliard
                        if(input[0].toString().toInt()!=1)
                            output.append("milliards" + " ")
                        else
                            output.append("milliard" + " ")
                    }
                }
            }

            2-> {
                if (input[0].toString().toInt() == 1)
                    output.append((digitMap[102]?.get(input[1].toString().toInt())) + " ")
                else {
                    output.append(digitMap[103]?.get(input[0].toString().toInt()) + " ")
                    output.append(digitMap[100]?.get(input[1].toString().toInt()) + " ")
                }
                when(order) {
                    1 -> {
                        output.append("thousands ")
                    }
                    2 -> {
                        output.append("millions ")
                    }
                    3 -> {
                        output.append("milliards ")
                    }
                }
            }

            3 ->
            {
                if(input[0]!='0') {
                    output.append(digitMap[101]?.get(input[0].toString().toInt()) + " ")

                    if(input[0]=='1')output.append("hundred ")

                    else output.append("hundreds ")
                }
                if (input[1].toString().toInt() == 1)
                    output.append((digitMap[102]?.get(input[2].toString().toInt())) + " ")
                else {
                    output.append(digitMap[103]?.get(input[1].toString().toInt()) + " ")
                    output.append(digitMap[101]?.get(input[2].toString().toInt()) + " ")
                    when (order) {
                        1 -> {
                            output.append("thousands ")
                        }
                        2 -> {
                            output.append("millions ")
                        }
                        3 -> {
                            output.append("milliards ")
                        }
                    }
                }
            }
        }

        return output.toString()
    }

    override fun stringToNum(input: String): String {
        return ""
    }

}