package nl.jworks.wordpress.importer

import spock.lang.Ignore

/**
 * @author Erik Pragt
 */
class HtmlToAsciiDocConverterSpec extends spock.lang.Specification {

    def converter = new HtmlToAsciiDocConverter()

    def "headings"() {
        expect:
        converter.convert(html) == asciidoc

        where:
        html             | asciidoc
        "<h1>Title</h1>" | "= Title\n"
        "<h2>Title</h2>" | "== Title\n"
        "<h3>Title</h3>" | "=== Title\n"
        "<h4>Title</h4>" | "==== Title\n"
        "<h5>Title</h5>" | "===== Title\n"
        "<h6>Title</h6>" | "====== Title\n"
    }

    def "markup"() {
        expect:
        converter.convert(html) == asciidoc

        where:
        html                                        | asciidoc
        "<b>Strong</b>"                             | "*Strong*"
        "<strong>Strong</strong>"                   | "*Strong*"
        "<i>Italic</i>"                             | "_Italic_"
        "<em>Italic</em>"                           | "_Italic_"
        "<del>striked</del>"                        | "[line-through]#striked#"
        "<blockquote>my awesome quote</blockquote>" | "____\nmy awesome quote\n____\n"
    }

    def "lists"() {
        expect:
        converter.convert(html) == asciidoc

        where:
        html                                      | asciidoc
        "<ol><li>Item 1</li><li>Item 2</li></ol>" | "1. Item 1\n1. Item 2\n"
        "<ul><li>Item 1</li><li>Item 2</li></ul>" | "* Item 1\n* Item 2\n"
    }


    @Ignore
    def "test bugs"() {
        expect:
        converter.convert(html) == asciidoc

        where:
        html                                                                    | asciidoc
        "'<a href=\"http://docs.python-requests.org/en/latest/\">requests</a>'" | "" // doesn't render the link because of ' around the link
    }

    def "spacing"() {
        expect:
        converter.convert(html) == asciidoc

        where:
        html                                                                                   | asciidoc
        "Some time ago, I bought a <a href=\"http://www.leapmotion.com\">Leapmotion</a> and a" | "Some time ago, I bought a http://www.leapmotion.com[Leapmotion] and a" // doesn't render the link because of ' around the link
    }

    def "xml conversion"() {
        when:
        def html = """<pre class="brush: xml">
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.6.3.201306030806</version>
</plugin>
</pre>""".stripMargin()

        def asciidoc = converter.convert(html)

        then:
        asciidoc == """[source,xml]
----
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.6.3.201306030806</version>
</plugin>
----""".stripMargin()
    }

    @Ignore
    def "basic scenario"() {
        when:
        def html = """<html>
                     |<body>
                     |    <h1>hello world</h1>
                     |    <p>some text</p>
                     |    <img src="http://myimage.com/pic.gif">
                     |</body>
                     |</html>""".stripMargin()

        def asciidoc = converter.convert(html)

        then:
        asciidoc == """= hello world
                      |
                      |some text
                      |
                      |image::http://myimage.com/pic.gif[]""".stripMargin()
    }

    @Ignore
    def "complex scenario"() {
        when:
        def html = """As a developer, you might be familiar with Hamcrest, a declarative rule based object matcher framework. In other words, a testing library which will make your test easy to read. While Hamcrest is a great library, there are some limitations to it's expressiveness, so looked for a better alternative. Meet AssertJ.

AssertJ is a library which provides a rich set of asserts and a fluent interface to help developers write maintainable tests. Besides that, most of the AssertJ error messages in the case of failing tests are very expressive and helpful, and provide great feedback to the developers in finding out why a test failure occurred.

To setup AssertJ in your project, the only library you'll need is the <code>assertj-core</code> library. I've set up a <a href="https://github.com/bodiam/assertj-blog">sample project</a> on Github to help you get started.

To make sure you're importing the right assertThat method, we'll be excluding the JUnit method:

<img class="aligncenter size-full wp-image-889" alt="exclude-auto-import" src="http://www.jworks.nl/wp-content/uploads/2014/02/exclude-auto-import.png" width="600" />

To make our lives even easier, we'll setup an IntelliJ Live Template, which will map the 'at' abbreviation to expand to assertThat's assertion method. This way, we only need to type 'at', press , and the right assertThat method will be used, including imports.

<img class="aligncenter size-full wp-image-886" alt="live-template" src="http://www.jworks.nl/wp-content/uploads/2014/02/live-template.png" width="500" />

In this example we'll be testing a small movie catalogue, in which most logic is located in finding the movies, and, in our case, only thrillers:
<pre class="&quot;brush:java">public List findThrillers() {
    return from(movies).filter(thrillerFilter()).toList();
}</pre>
The code above uses the Guava FluentIterator to filter movies based on certain criteria. In this case a filter will be applied to find only Thrillers. To test this, we write the following AssertJ test:
<pre class="&quot;brush:java">List thrillers = movieDao.findThrillers();

assertThat(thrillers).extracting("title").containsOnly(
        "The Dark Knight", "Pulp Fiction", "Se7en", "Gunday"
);</pre>
What happens here, is that for each element in the list of Thrillers, the 'title' property is extracted, and an exact match is done on the result of the thrillers result, ie. it should have exactly those 4 elements.

One of the great features of AssertJ is that assertThat is a generic method. This means that your IDE will help you a great deal to know which methods are available to help in testing, since it depends on the type of the object passed to assertThat. To illustrate this, see the following example:

<img class="aligncenter size-full wp-image-887" alt="code-completion-list" src="http://www.jworks.nl/wp-content/uploads/2014/02/code-completion-list.png" width="600" />

As you can see here, only the methods which can be applied to a List are shown. When we replace the List with a String

<img class="aligncenter size-full wp-image-888" alt="code-completion-error" src="http://www.jworks.nl/wp-content/uploads/2014/02/code-completion-error.png" width="600" />

IntelliJ immediately responds with a compilation error, and when using code completion again, you'll get the list of methods which are compatible with a String type:

<img class="aligncenter size-full wp-image-885" alt="code-completion-string" src="http://www.jworks.nl/wp-content/uploads/2014/02/code-completion-string.png" width="600" />

Besides providing a hints to developers and giving early feedback, AssertJ also enables you to have very readable tests. An example can be seen below:
<pre class="&quot;brush:java">assertThat(thrillers)
        .extracting("title", "year")
        .contains(
                tuple("The Dark Knight", 2008),
                tuple("Pulp Fiction", 1994),
        );</pre>
Here, we extract multiple attributes of a Movie, and we'll compare them with our actual result. Should a comparison fail, an error is shown:
<pre>java.lang.AssertionError:
Expecting:
&lt;[('The Dark Knight', 2008), ('Pulp Fiction', 1994), ('Se7en', 1995), ('Gunday', 2014)]&gt;
to contain:
&lt;[('The Dark Knight', 2008), ('Pulp Fiction', 1994), ('Se8en', 1995)]&gt;
but could not find:
&lt;[('Se8en', 1995)]&gt;</pre>
In this, we expected the movie 'Se8en' to be there, but we made an obvious typo, which could be corrected quickly.

I hope this short introduction gave you a impression of what is possible with AssertJ. More examples can be found in the same project or on the AssertJ website. AssertJ provides many more features, like powerful test DSL's, auto generated asserts for your own types, support for Joda, Guava and Neo4j, and much more.

To find out more, checkout <a href="http://www.assertj.org">AssertJ</a> and start improving your test code!"""

        def asciidoc = converter.convert(html)

        then:
        asciidoc == html
    }
}
