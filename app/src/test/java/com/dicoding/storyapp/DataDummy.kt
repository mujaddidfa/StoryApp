package com.dicoding.storyapp

import com.dicoding.storyapp.data.api.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                photoUrl = "https://picsum.photos/200/300",
                createdAt = "2021-08-25T10:00:00Z",
                name = "Story $i",
                description = "Description $i",
                lon = 0.0,
                id = "$i",
                lat = 0.0
            )
            items.add(story)
        }
        return items
    }
}