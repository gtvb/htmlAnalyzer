
import java.util.ArrayList;

/**
 * This class is responsible for parsing the HTML content of a web page. It
 * receives a list of tokens obtained from the HtmlTokenizer class and creates
 * an abstract syntax tree that represents the structure of the HTML content.
 *
 * The grammar is defined as follows (respecting the functional requirement of
 * the project:
 *
 * <html> -> <tag>
 * <tag> -> <openingTag> <content> <closingTag>
 * <openingTag> -> "<" STRING ">" = OPEN_TAG_TOKEN
 * <closingTag> -> "</" STRING ">" = CLOSE_TAG_TOKEN
 * <content> -> <text> <content> | <tag> <content> | ε
 * <text> -> STRING
 *
 * @see HtmlTokenizer
 * @see HtmlParserException
 */
public class HtmlParser {

    sealed interface HtmlData permits HtmlTagNode, HtmlTextContentNode {
    }

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

    private int currentIndex;
    private final ArrayList<HtmlToken> tokens;
    private HtmlNode abstractSyntaxTree;

    public HtmlParser(ArrayList<HtmlToken> tokens) {
        this.tokens = tokens;
        this.currentIndex = 0;
    }

    // The following functions are used to implement the grammar described above.
    // As the function names suggest, each function represents a rule on the grammar.
    // Some rules don't need a function, but the core of the grammar (which allows us)
    // to give meaning to the HTML tokens, is present in one of these functions. Essentially,
    // we are implementing a Recursive Descent Parser, which starts from the so-called
    // "non-terminal" 

    public HtmlNode getAbstractSyntaxTree() {
        return abstractSyntaxTree;
    }

    public void parse() throws HtmlParserException {
        abstractSyntaxTree = html();
    }

    private HtmlNode html() throws HtmlParserException {
        return tag();
    }

    private HtmlNode tag() throws HtmlParserException {
        if (currentIndex >= tokens.size() || tokens.get(currentIndex).getTokenType() != HtmlTokenType.OpenTag) {
            throw new HtmlParserException();
        }

        HtmlToken openTag = tokens.get(currentIndex);
        currentIndex++;

        ArrayList<HtmlNode> inner = content();

        if (currentIndex >= tokens.size() || tokens.get(currentIndex).getTokenType() != HtmlTokenType.CloseTag) {
            throw new HtmlParserException();
        }

        HtmlToken closeTag = tokens.get(currentIndex);
        currentIndex++;

        if (!closeTag.getLiteralValue().equals(openTag.getLiteralValue())) {
            throw new HtmlParserException();
        }

        HtmlNode newNode = new HtmlNode(new HtmlTagNode(openTag, closeTag));
        newNode.setChildren(inner);

        return newNode;
    }

    // This function acts as a wrapper to the recursive function `parseContent`.
    private ArrayList<HtmlNode> content() throws HtmlParserException {
        ArrayList<HtmlNode> children = new ArrayList<>();
        parseContent(children);

        return children;
    }

    private void parseContent(ArrayList<HtmlNode> children) {
        if (currentIndex >= tokens.size()) {
            return;
        }

        HtmlToken next = tokens.get(currentIndex);
        switch (next.getTokenType()) {
            case TextContent -> {
                children.add(new HtmlNode(new HtmlTextContentNode(next)));
                currentIndex++;
                parseContent(children);
            }
            case OpenTag -> {
                children.add(tag());
                parseContent(children);
            }
            case CloseTag -> {
                return;
            }
        }
    }

    public void printDeepestTextNode() {
        DepthNodeWrapper wrapper = new DepthNodeWrapper();
        findDeepestTextContentNode(abstractSyntaxTree, 1, wrapper);

        if (wrapper.deepestNode != null) {
            System.out.println(wrapper.deepestNode.getData().toString());
        }
    } 

    private void findDeepestTextContentNode(HtmlNode node, int depth, DepthNodeWrapper wrapper) {
        if (node.getData() instanceof HtmlTextContentNode && depth > wrapper.maxDepth) {
            wrapper.maxDepth = depth;
            wrapper.deepestNode = node;
        }

        if (node.getChildren() == null) {
            return;
        }

        for (HtmlNode child : node.getChildren()) {
            findDeepestTextContentNode(child, depth + 1, wrapper);
        }
    }

    private static class DepthNodeWrapper {
        private int maxDepth = -1;
        private HtmlNode deepestNode = null;
    }

    // Helper methods, used for debugging purposes.
    public void printHtmlTree() {
        printTree(abstractSyntaxTree, 0);
    }

    private void printTree(HtmlNode node, int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }

        if (node.getData() instanceof HtmlTextContentNode) {
            System.out.println("Text: \"" + node.data.toString() + "\"");
        } else {
            System.out.println("Node(tag=" + node.data.toString() + ")");
            for (HtmlNode child : node.getChildren()) {
                printTree(child, indent + 1);
            }
        }
    }
}
