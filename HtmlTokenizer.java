
import java.util.ArrayList;

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

    public void tokenize() {
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

    private HtmlToken addTagToken(String line) {
        HtmlToken newToken = new HtmlToken();

        if (line.charAt(1) == '/') {
            newToken.setTokenType(HtmlTokenType.CloseTag);
            newToken.setLiteralValue(extractLiteralValue(line, 2, '>'));
            return newToken;
        }

        newToken.setTokenType(HtmlTokenType.OpenTag);
        newToken.setLiteralValue(extractLiteralValue(line, 1, '>'));

        return newToken;
    }

    private HtmlToken addTextContentToken(String line) {
        HtmlToken newToken = new HtmlToken();

        newToken.setTokenType(HtmlTokenType.TextContent);
        newToken.setLiteralValue(extractLiteralValue(line, 0, '\n'));

        return newToken;
    }

    private String extractLiteralValue(String line, int startIndex, char stoppingChar) {
        StringBuilder literalValue = new StringBuilder();

        int currentIndex = startIndex;
        while (currentIndex < line.length() && line.charAt(currentIndex) != stoppingChar) {
            literalValue.append(line.charAt(currentIndex));
            currentIndex++;
        }

        return literalValue.toString();
    }
}
