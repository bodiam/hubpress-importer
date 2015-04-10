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
        AsciiDocTagVisitor tagVisitor = new AsciiDocTagVisitor();
        lagartoParser.parse(tagVisitor);

        return tagVisitor.getResult();
    }
}


class AsciiDocTagVisitor implements TagVisitor {

    private StringBuilder buffer = new StringBuilder();

    private boolean processTokens = true;
    private String listToken = "";

    public String getResult() {
        return buffer.toString();
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

        if (tag.getType().isStartingTag() && processTokens) {
            switch (tag.getName().toString().toLowerCase()) {
                case "h1":
                    buffer.append("= ");
                    break;
                case "h2":
                    buffer.append("== ");
                    break;
                case "h3":
                    buffer.append("=== ");
                    break;
                case "h4":
                    buffer.append("==== ");
                    break;
                case "h5":
                    buffer.append("===== ");
                    break;
                case "h6":
                    buffer.append("====== ");
                    break;
                case "b":
                case "strong":
                    buffer.append("*");
                    break;
                case "i":
                case "em":
                    buffer.append("_");
                    break;
                case "del":
                    buffer.append("[line-through]#");
                    break;
                case "blockquote":
                    buffer.append("____\n");
                    break;
                case "p":
                    buffer.append("\n");
                    break;
                case "img":
                    buffer.append("\nimage::").append(tag.getAttributeValue("src")).append("[]");
                    break;
                case "code":
                    buffer.append("`");
                    break;
                case "pre":
                    String source = "";

                    CharSequence attributeValue = tag.getAttributeValue("class");
                    if (attributeValue != null) {
                        Pattern p = Pattern.compile("brush:\\s?([a-z]+)");
                        Matcher m = p.matcher(attributeValue);

                        if (m.find()) {
                            source = "," + m.group(1);
                        }
                    }

                    processTokens = false;


                    buffer.append("[source").append(source).append("]\n----\n");
                    break;
                case "a":
                    buffer.append(tag.getAttributeValue("href")).append("[");
                    break;
                case "ol":
                    listToken = "1.";
                    break;
                case "ul":
                    listToken = "*";
                    break;
                case "li":
                    buffer.append(listToken).append(" ");
                    break;
                default:
                    System.err.println("DONT KNOW HOW TO HANDLE " + tag);
            }
        } else {
            if (processTokens) {
                switch (tag.getName().toString().toLowerCase()) {
                    case "p":
                        buffer.append("\n");
                        break;
                    case "code":
                        buffer.append("`");
                        break;
                    case "b":
                    case "strong":
                        buffer.append("*");
                        break;
                    case "i":
                    case "em":
                        buffer.append("_");
                        break;
                    case "del":
                        buffer.append("#");
                        break;
                    case "blockquote":
                        buffer.append("\n____\n");
                        break;
                    case "a":
                        buffer.append("]");
                        break;
                    case "h1":
                    case "h2":
                    case "h3":
                    case "h4":
                    case "h5":
                    case "h6":
                        buffer.append("\n");
                        break;
                    case "ol":
                    case "ul":
                        listToken = "";
                        break;
                    case "li":
                        buffer.append("\n");
                        break;

                }
            } else {
                switch (tag.getName().toString().toLowerCase()) {
                    case "pre":
                        buffer.append("\n----\n");
                        processTokens = true;
                        break;
                    default:
                        buffer.append(tag);
                }
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

        buffer.append(text);
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