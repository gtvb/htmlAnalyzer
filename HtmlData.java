
/**
 * Represents a node in the HTML document tree. It acts as the wrapper
 * for the two types of nodes that can exist in the tree: {@link HtmlTagNode}
 * and {@link HtmlTextContentNode}.
 */
public sealed interface HtmlData permits HtmlTagNode, HtmlTextContentNode {
}
