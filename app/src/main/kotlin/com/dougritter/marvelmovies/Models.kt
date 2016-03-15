package com.dougritter.marvelmovies

object Model {
    data class CharacterResponse(val code: Int, val status: String, val copyright: String,
                                 val attributionText: String, val attributionHTML: String,
                                 val etag: String, val data: CharacterData)

    data class CharacterData(val offset: Int, val limit: Int, val total: Int, val count: Int,
                             var results: MutableList<Character>)

    data class Character(val id: Int, val name: String, val description: String,
                         val modified: String, val thumbnail: Thumbnail,
                         val comics: CollectionItem,
                         val series: CollectionItem,
                         val stories: CollectionItem,
                         val events: CollectionItem,
                         val urls: MutableList<ItemUrl>)

    data class Thumbnail(val path: String, val extension: String)

    data class CollectionItem(val available: Int, val collectionURI: String,
                              val items: MutableList<Item>, val returned: Int)

    data class Item(val resourceURI: String, val name: String, val type: String)

    data class ItemUrl(val type: String, val url: String)

}

