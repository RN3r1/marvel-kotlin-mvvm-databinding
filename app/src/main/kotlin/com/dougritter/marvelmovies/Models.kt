
object Model {
    data class Character(val id: Int, val name: String, val description: String,
                         val modified: String, val thumbnail: Thumbnail,
                         val comics: MutableList<CollectionItem>,
                         val series: MutableList<CollectionItem>,
                         val stories: MutableList<CollectionItem>,
                         val events: MutableList<CollectionItem>,
                         val urls: MutableList<ItemUrl>)

    data class Thumbnail(val path: String, val extension: String)

    data class CollectionItem(val available: Int, val collectionURI: String,
                              val items: MutableList<Item>, val returned: Int)

    data class Item(val resourceURI: String, val name: String, val type: String)

    data class ItemUrl(val type: String, val url: String)

}
