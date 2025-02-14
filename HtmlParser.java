
import java.util.ArrayList;

/**
 * This class is responsible for parsing the HTML content of a web page. It
 * receives a list of tokens obtained from the HtmlTokenizer class and creates
 * an abstract syntax tree that represents the structure of the HTML content.
 *
 * The grammar can be defined as follows (respecting the functional requirements
 * of the project):
 *
 * <pre>
 * {@code
 * <html> ::= <tag>
 * <tag> ::= <openingTag> <content> <closingTag>
 * <openingTag> ::= "<" STRING ">"
 * <closingTag> ::= "</" STRING ">"
 * <content> ::= <text> <content> | <tag> <content>
 * <text> ::= STRING
 * }
 * </pre>
 *
 * @see HtmlTokenizer
 * @see HtmlParserException
 */
public class HtmlParser {

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
    // "non-terminal" rules, and walks its way down to the "terminal" rules. In the case 
    // of an error in the parsing process, a HtmlParserException is thrown.

    /**
     * This function is used to get the abstract syntax tree of the HTML content.
     *
     * @return The abstract syntax tree of the HTML content.
     */
    public HtmlNode getAbstractSyntaxTree() {
        return abstractSyntaxTree;
    }

    /**
     * This function is responsible for parsing the HTML content. It starts by
     * calling the `html` rule and sets the abstract syntax tree to the result of
     * the parsing process.
     *
     * @throws HtmlParserException If an error occurs during the parsing process.
     */
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

        // Following the tag rule, we should process the nodes in the following order:
        // 1. OpenTag
        HtmlToken openTag = tokens.get(currentIndex);
        currentIndex++;

        // 2. Content
        ArrayList<HtmlNode> inner = content();

        if (currentIndex >= tokens.size() || tokens.get(currentIndex).getTokenType() != HtmlTokenType.CloseTag) {
            throw new HtmlParserException();
        }

        // 3. CloseTag
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

    /**
     * This function is responsible for parsing the content of a tag. It is a
     * recursive function that keeps parsing the content, and takes the decision
     * on what to do next based on the next token in the list. If the next token
     * is a text content, we create a new HtmlTextContentNode and add it to the
     * list of children. If the next token is an OpenTag, we call the `tag`
     * function to parse the tag and add it to the list of children. If the next
     * token is a CloseTag, we return from the function.
     *
     * @param children The list of children nodes of the current tag.
     */
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

    /**
     * This function is responsible for printing the deepest text content node
     * in the abstract syntax tree. It uses a DepthNodeWrapper to keep track of
     * the deepest node found so far, and a recursive function to traverse the
     * tree and find the deepest node. This function is responsible for solving
     * the main functional requirement of the project.
     *
     * @see DepthNodeWrapper
     * @see HtmlNode
     */
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
            System.out.println("Text: \"" + node.getData().toString() + "\"");
        } else {
            System.out.println("Node(tag=" + node.getData().toString() + ")");
            for (HtmlNode child : node.getChildren()) {
                printTree(child, indent + 1);
            }
        }
    }
}
