package com.ragingo.sample.tcpclient

import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

data class ChatInfo(
    val no: String,
    val vpos: Int,
    val mail: String,
    val premium: Boolean,
    val anonymity: Boolean,
    val comment: String
) {
    companion object {

        private val xmlDocBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

        fun isValid(data: String): Boolean {
            return data.startsWith("<chat")
        }

        fun parse(data: String): ChatInfo {
            val doc = xmlDocBuilder.parse(InputSource(StringReader(data)))
            val elems = doc.getElementsByTagName("chat")
            if (elems.length != 1) {
                return ChatInfo()
            }
            val elem = elems.item(0)
            return ChatInfo(
                elem.attributes.getNamedItem("no")?.nodeValue ?: "",
                elem.attributes.getNamedItem("vpos")?.nodeValue?.toInt() ?: -1,
                elem.attributes.getNamedItem("mail")?.nodeValue ?: "",
                elem.attributes.getNamedItem("premium")?.nodeValue?.toInt() == 1,
                elem.attributes.getNamedItem("anonymity")?.nodeValue?.toInt() == 1,
                elem.textContent
            )
        }
    }

    constructor() : this("", -1, "", false, false, "")
}
