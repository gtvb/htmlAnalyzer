
import java.util.ArrayList;

public class HtmlNode {

    private HtmlNode parent = null;
    private final HtmlData data;
    private ArrayList<HtmlNode> children;

    public HtmlNode(HtmlData data) {
        this.data = data;
    }

    public HtmlNode getParent() {
        return parent;
    }

    public HtmlData getData() {
        return data;
    }

    public ArrayList<HtmlNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<HtmlNode> parsedChildren) {
        children = parsedChildren;
    }

    public void setParent(HtmlNode p) {
        parent = p;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
