
public final class HtmlTextContentNode implements HtmlData {

    private final HtmlToken textContent;

    public HtmlTextContentNode(HtmlToken textContent) {
        this.textContent = textContent;
    }

    @Override
    public String toString() {
        return textContent.getLiteralValue();
    }
}
