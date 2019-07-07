package com.ragingo.sample.tcpclient

object NiconicoJikkyo {
    data class ChannelInfo(val id: String, val name: String) {
    }

    val TvChannels: Array<ChannelInfo> = arrayOf(
        ChannelInfo("jk1", "NHK総合"),
        ChannelInfo("jk2", "Eテレ"),
        ChannelInfo("jk4", "日テレ"),
        ChannelInfo("jk5", "テレ朝日"),
        ChannelInfo("jk6", "TBS"),
        ChannelInfo("jk7", "テレ東"),
        ChannelInfo("jk8", "フジテレビ"),
        ChannelInfo("jk9", "MX"),
        ChannelInfo("jk10", "テレ玉"),
        ChannelInfo("jk11", "tvk"),
        ChannelInfo("jk12", "チバテレビ")
    )
}
