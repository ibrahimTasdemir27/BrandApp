package com.mtasdemir.brandapp.Base.Base.Helper

class Helper {


    companion object {
        fun lowercase(text: String): String {
            var newText = text
            text.forEachIndexed { index, char ->
                if (char == 'İ') {
                    val strBuilder = StringBuilder(newText)
                    strBuilder.setCharAt(index, 'i')
                    newText = strBuilder.toString()
                }
            }

            return newText.lowercase()
        }


    }

}