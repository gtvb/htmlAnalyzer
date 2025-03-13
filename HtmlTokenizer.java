
import java.util.ArrayList;

/**
 * The `HtmlTokenizer` class is responsible for tokenizing lines of HTML
 * content. Tokenizing essentially means breaking down the content into smaller
 * parts, which help us understand our data in a more structured way, rather
 * than dealing with raw text lines. This is usually the first step that happens
 * in programming languages when processing code. It processes each line and
 * generates a list of HtmlToken objects representing different parts of the
 * content, in our case, text content, open tags, and close tags.
 */
public class HtmlTokenizer {

    private final ArrayList<String> lines;
    private final ArrayList<HtmlToken> tokens;

    public HtmlTokenizer(ArrayList<String> lines) {
        this.lines = lines;
        this.tokens = new ArrayList<>();
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public ArrayList<HtmlToken> getTokens() {
        return new ArrayList<>(tokens);
    }

    /**
     * Tokenizes HTML lines. Trims each line, skips empty lines, and creates a
     * token based on the first character. If the first character is a '<', an
     * OpenTag token is created. If we start with a '<' , and the next character
     * is a '/', a CloseTag token is created. otherwise, creates a TextContent
     * token. Adds tokens to the tokens list.
     */
    public void tokenize() throws HtmlTokenizerException {
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) {
                continue;
            }

            char firstChar = trimmedLine.charAt(0);

            HtmlToken newToken;
            newToken = switch (firstChar) {
                case '<' ->
                    addTagToken(trimmedLine);
                default ->
                    addTextContentToken(trimmedLine);
            };

            tokens.add(newToken);
        }
    }

    /**
     * Creates an HtmlToken based on the provided line.
     * If the line starts with "</", sets token type to CloseTag.
     * Otherwise, sets token type to OpenTag.
     *
     * @param line the HTML line to tokenize
     * @return a new HtmlToken representing the tag
     */
    private HtmlToken addTagToken(String line) throws HtmlTokenizerException {
        HtmlToken newToken = new HtmlToken();

        try {
            // Start at the second character to skip the '<', which was already checked
            int currentIndex = 1;
            char currentChar = line.charAt(currentIndex);
            if (currentChar == '/') {
                currentIndex++;
                currentChar = line.charAt(currentIndex);

                newToken.setTokenType(HtmlTokenType.CloseTag);
                newToken.setLiteralValue(extractLiteralValue(line, currentIndex));
                return newToken;
            }

            newToken.setTokenType(HtmlTokenType.OpenTag);
            newToken.setLiteralValue(extractLiteralValue(line, currentIndex));

            return newToken;
        } catch (StringIndexOutOfBoundsException e) {
            throw new HtmlTokenizerException();
        }
    }

    /**
     * Creates an HtmlToken with TextContent type and the provided line as the
     * literal value.
     *
     * @param line the HTML line to tokenize
     * @return a new HtmlToken representing the text content
     */
    private HtmlToken addTextContentToken(String line) {
        HtmlToken newToken = new HtmlToken();

        newToken.setTokenType(HtmlTokenType.TextContent);
        newToken.setLiteralValue(line);

        return newToken;
    }

    /**
     * Extracts the literal value from the provided line starting from the given
     * index and stopping at the default stopping character.
     *
     * @param line         the line to extract the literal value from
     * @param startIndex   the index to start extracting from
     * @return the extracted literal value
     */
    private String extractLiteralValue(String line, int startIndex) throws HtmlTokenizerException {
        final char defaultStoppingChar = '>';
        StringBuilder literalValue = new StringBuilder();

        int currentIndex = startIndex;
        while (currentIndex < line.length()) {
            literalValue.append(line.charAt(currentIndex));
            currentIndex++;
        }

        if (line.charAt(currentIndex - 1) != defaultStoppingChar) {
            throw new HtmlTokenizerException();
        }

        return literalValue.toString();
    }
}
