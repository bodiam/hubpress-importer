package nl.jworks.wordpress.importer

/**
 * @author Erik Pragt
 */
class WordpressImporter {

    static void main(args) {

        new File("posts").deleteDir()
        new File("posts").mkdir()

        def rss = new XmlParser().parse(new File("src/test/resources/jworks-blog-export.xml"))

        def items = rss.channel.item.'**'.findAll { item ->
            item.'wp:post_type'.text() == 'post'
        }

        List<Post> posts = convertNodesToPosts(items)

        writeHtmlFiles(items)
        writeAsciidocFiles(posts)

    }

    private static List<Post> convertNodesToPosts(ArrayList items) {
        items.collect { item ->
            new Post(
                    title: item.title.text(),
                    content: new HtmlToAsciiDocConverter().convert(item.'content:encoded'.text())
            )
        }
    }

    private static List<Post> writeAsciidocFiles(List<Post> posts) {
        posts.each { post ->
            new File("posts/" + post.title + ".adoc") << post.content
        }
    }

    private static List writeHtmlFiles(ArrayList items) {
        items.each { item ->
            new File("posts/" + item.title.text() + ".html") << item.'content:encoded'.text()
        }
    }
}

class Post {
    String title, content
}
