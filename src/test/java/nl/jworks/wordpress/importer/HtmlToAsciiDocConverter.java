package nl.jworks.wordpress.importer;

import jodd.lagarto.Doctype;
import jodd.lagarto.LagartoParser;
import jodd.lagarto.Tag;
import jodd.lagarto.TagVisitor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Erik Pragt
 */
class HtmlToAsciiDocConverter {
    public String convert(String htmlContent) {

        LagartoParser lagartoParser = new LagartoParser(htmlContent, false);
        FooTagVisitor tagVisitor = new FooTagVisitor();
        lagartoParser.parse(tagVisitor);

        return tagVisitor.getBuffer();
    }
}


class FooTagVisitor implements TagVisitor {

    private String buffer = "";

    public String getBuffer() {
        return buffer;
    }

    @Override
    public void start() {
        System.out.println("start");
    }

    @Override
    public void end() {
        System.out.println("end");

    }

    @Override
    public void doctype(Doctype doctype) {

    }

    @Override
    public void tag(Tag tag) {
        System.out.println("tag: " + tag);

        if (tag.getType().isStartingTag()) {
            switch (tag.getName().toString().toLowerCase()) {
                case "h1":
                    buffer += "= ";
                    break;
                case "p":
                    buffer += "\n";
                    break;
                case "img":
                    buffer += "\nimage::" + tag.getAttributeValue("src") + "[]";
                    break;
                case "code":
                    buffer += "`";
                    break;
                case "pre":
                    String source = "";

                    Pattern p = Pattern.compile("\"brush:([a-z]*)");
                    Matcher m = p.matcher("\"brush:java");

                    if(m.find()) {
                        source = "," +m.group(1);
                    }

                    buffer += "[source"+source+"]\n----\n";
                    break;
                case "a":
                    buffer += tag.getAttributeValue("href") + "[";
                    break;
                default:
                    System.err.println("DONT KNOW HOW TO HANDLE " + tag);
            }
        } else {
            switch (tag.getName().toString().toLowerCase()) {
                case "p":
                    buffer += "\n";
                    break;
                case "code":
                    buffer += "`";
                    break;
                case "a":
                    buffer += "]";
                    break;
                case "pre":
                    buffer += "\n----\n";
                    break;
                case "h1":
                    buffer += "\n";
                    break;
            }
        }
    }


    @Override
    public void script(Tag tag, CharSequence body) {

    }

    @Override
    public void comment(CharSequence comment) {

    }

    @Override
    public void text(CharSequence text) {
        System.out.println("text: " + text);

        buffer += text;
    }

    @Override
    public void condComment(CharSequence expression, boolean isStartingTag, boolean isHidden, boolean isHiddenEndTag) {

    }

    @Override
    public void xml(CharSequence version, CharSequence encoding, CharSequence standalone) {

    }

    @Override
    public void cdata(CharSequence cdata) {

    }

    @Override
    public void error(String message) {

    }
}