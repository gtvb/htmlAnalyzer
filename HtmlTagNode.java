
public final class HtmlTagNode implements HtmlData {

    private final HtmlToken openTag;
    private final HtmlToken closeTag;

    public HtmlTagNode(HtmlToken openTag, HtmlToken closeTag) {
        this.openTag = openTag;
        this.closeTag = closeTag;
    }

    @Override
    public String toString() {
        return openTag.getLiteralValue() + ", " + closeTag.getLiteralValue();
    }
}
