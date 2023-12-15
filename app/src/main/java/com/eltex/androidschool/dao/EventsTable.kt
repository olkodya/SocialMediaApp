package com.eltex.androidschool.dao

object EventsTable {
    const val TABLE_NAME = "Events"
    const val ID = "id"
    const val CONTENT = "content"
    const val PUBLISHED = "published"
    const val AUTHOR = "author"
    const val LIKED_BY_ME = "likedByMe"
    const val TYPE = "type"
    const val DATETIME = "datetime"
    const val PARTICIPATED_BY_ME = "participatedByMe"
    const val LINK = "link"
    val allColumns = arrayOf(
        ID,
        CONTENT,
        PUBLISHED,
        AUTHOR,
        LIKED_BY_ME,
        TYPE,
        DATETIME,
        PARTICIPATED_BY_ME,
        LINK
    )
}