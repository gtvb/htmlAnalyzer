
import java.util.ArrayList;

public class HtmlAnalyzer {

    public static void main(String[] args) {
        String requestedUrlString = args[0];

        try {
            // First step: Extracting the data
            HtmlExtractor extractor = new HtmlExtractor(requestedUrlString);
            extractor.extractHtml();

            ArrayList<String> rawLines = extractor.getRawLines();

            // Second step: Parsing the HTML lines according to the instructions.
            HtmlTokenizer tokenizer = new HtmlTokenizer(rawLines);
            tokenizer.tokenize();

            HtmlParser parser = new HtmlParser(tokenizer.getTokens());
            parser.parse();

            // Third step: Finding the deepest text node indside this HTML tree.
            parser.printDeepestTextNode();
        } catch (HtmlExtractorException | HtmlParserException e) {
            System.out.println(e.getMessage());
        }
    }
}
