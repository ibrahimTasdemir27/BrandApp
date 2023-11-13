package com.mtasdemir.brandapp.Service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


interface GoogleImageRequestDelegate {
    fun imageUrlListReady(urlList: Array<String>)
}


class GoogleImageRequestService {


    var delegate: GoogleImageRequestDelegate? = null

    fun load(queryItem: String) {
        val urlString = "https://www.google.com.tr/search?q=${queryItem.lowercase()}+marka+amblem&tbm=isch&ved=2ahUKEwjljZX84cCCAxW2pP0HHQs_AyMQ2-cCegQIABAA&oq=${queryItem.lowercase()}+marka+amblem&gs_lcp=CgNpbWcQAzoFCAAQgARQywNYnwxgjg1oAHAAeACAAaYBiAHuB5IBAzAuOJgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=K_pRZaXMCrbJ9u8Pi_6MmAI&bih=835&biw=1680"
            //"https://www.google.com.tr/search?q=${queryItem.lowercase() + "marka amblem"}&sca_esv=580252979&tbm=isch&sxsrf=AM9HkKlG8qSs3bNtP6K2tEJWLRaitvMXLQ:1699395262155&source=lnms&sa=X&ved=2ahUKEwjm2bvX9LKCAxWQSPEDHUP0CHkQ_AUoAXoECAEQAw&biw=1680&bih=835&dpr=2"


        runBlocking(Dispatchers.IO) {
            try {
                val doc: Document = Jsoup.connect(urlString).header("Content-Type", "text/html; charset=utf-8")
                    //.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")
                    .userAgent("Mozilla/5.0 (Linux; U; Android 3.0; en-us; Xoom Build/HRI39) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13")
                    .get()

                delegate?.imageUrlListReady(parse(doc.body().html()))
            } catch (e: Exception) {
                println("herrrDefined: $e")
            }
        }
    }


    fun parse(html: String): Array<String> {
        val urlList = ArrayList<String>()


        var counter = 0
        var finder = ""
        var state: Boolean = false


        var httpState: Boolean = false
        var httpFind = ""
        var lastHttpIndex = 0

        html.forEachIndexed { index, char ->
            if (char == '.') {
                state = true
            }


            if (char == 'h') {
                httpState = true
            }

            if (httpState == true) {
                httpFind += char
                if (httpFind.count() == 5) {
                    if (httpFind == "https") {
                        //print("httpsBulundu")
                        lastHttpIndex = index - 4
                    }

                    httpFind = ""
                    httpState = false
                }
            }


            if (state == true) {
                counter += 1
                finder += char
                if (counter == 4) {
                    if (finder == ".jpg") {
                        val url = html.substring(lastHttpIndex, index + 1)

                        urlList.add(url)
                    }

                    state = false
                    finder = ""
                    counter = 0
                }
            }
        }


        return urlList.toTypedArray()


    }
}

