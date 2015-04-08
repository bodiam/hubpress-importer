package nl.jworks.wordpress.importer

/**
 * @author Erik Pragt
 */
class WordpressImporter {
    int postCount

    def importFile(File file) {

        def rss = new XmlParser().parse(file)

        def items = rss.channel.item.'**'.findAll { item ->
            item.'wp:post_type'.text() == 'post'
        }

        def posts = items.collect { item ->
            def htmlContent = item.'content:encoded'

            new Post(
                    title:item.title.text(),
                    content:new HtmlToAsciiDocConverter().convert(htmlContent.text())
            )
        }

        postCount = posts.size()

        new File("posts").mkdir()

        posts.each { post ->
            new File("posts/"+post.title+".adoc") << post.content
        }

    }
}

class Post {
    String title, content
}
