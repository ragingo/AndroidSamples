package com.ragingo.sample.tcpclient

data class GetFlvInfo(
    val done: Boolean,
    val threadId: String,
    val msHost: String,
    val msPort: Int,
    val channelNo: Int,
    val channelName: String
) {
    companion object {
        fun parse(data: String): GetFlvInfo {
            val pairs = data.split('&').map {
                val nameValue = it.split('=')
                if (nameValue.size == 2) {
                    return@map Pair(nameValue[0], nameValue[1])
                }
                return@map Pair("", "")
            }
            return GetFlvInfo(
                pairs.firstOrNull { it.first == "done" }?.second?.toBoolean() ?: false,
                pairs.firstOrNull { it.first == "thread_id" }?.second ?: "",
                pairs.firstOrNull { it.first == "ms" }?.second ?: "",
                pairs.firstOrNull { it.first == "ms_port" }?.second?.toInt() ?: 0,
                pairs.firstOrNull { it.first == "channel_no" }?.second?.toInt() ?: 0,
                pairs.firstOrNull { it.first == "channel_name" }?.second ?: ""
            )
        }
    }
}
