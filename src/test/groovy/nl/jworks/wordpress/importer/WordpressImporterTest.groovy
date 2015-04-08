package nl.jworks.wordpress.importer

import org.junit.Ignore
import org.junit.Test

/**
 * @author Erik Pragt
 */
class WordpressImporterTest {

    @Ignore
    public void countPosts() {
        def wordpressImporter = new WordpressImporter()

        wordpressImporter.importFile(new File("/Users/erikp/Userfiles/projects/groovy/hubpress-importer/src/test/resources/jworks-blog-export.xml"));

        assert wordpressImporter.postCount == 84
    }
}
