
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * The class HtmlExtractor is responsible for extracting the HTML content from a
 * given URL. Any errors that occur during the extraction process are thrown as
 * a HtmlExtractorException, which is a simple wrapper around a RuntimeException
 *
 * @see HtmlExtractorException
 * @see HtmlAnalyzer
 */
public class HtmlExtractor {

    private final String urlRequestString;
    private final ArrayList<String> rawLines;

    public HtmlExtractor(String requestedUrl) {
        this.urlRequestString = requestedUrl;
        this.rawLines = new ArrayList<>();
    }

    public String getUrlRequestString() {
        return urlRequestString;
    }

    public ArrayList<String> getRawLines() {
        return new ArrayList<>(rawLines);
    }

    /**
     * Extracts HTML content from the specified URL and stores each line in the rawLines list.
     * 
     * @throws HtmlExtractorException if an I/O error occurs while reading from the URL.
     */
    public void extractHtml() throws HtmlExtractorException {
        try {
            @SuppressWarnings("deprecation")
            URL parsedUrl = new URL(urlRequestString);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(parsedUrl.openStream()));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                rawLines.add(currentLine);
            }
        } catch (IOException e) {
            throw new HtmlExtractorException();
        }
    }
}
