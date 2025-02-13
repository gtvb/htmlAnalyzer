
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

            System.out.println(tokenizer.getTokens());
        } catch (HtmlExtractorException e) {
            System.out.println(e.getMessage());
        }
    }
}
