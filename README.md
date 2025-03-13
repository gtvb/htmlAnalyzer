EASTER_EGG_URLS

**This project also handles the optional veriication of malformed HTML, correctly throwing "malformed HTML" errors** 

### HtmlAnalyzer
This project contains all the .java files necessary to correctly find the deepest
text node inside an HTML structure. It follows all the functional requirements of
the project description, and provides an organized file structure to deal with 
the problem.

![How does it work?](/assets/htmlAnalyzer.png)

### Code structure

#### URL Extraction
- `HtmlExtractor.java`: Extracts URLs from the HTML content.

#### Tokenizer
- `HtmlTokenizer.java`: Tokenizes the HTML content into manageable pieces.
- `HtmlToken.java`: Represents a single token in the HTML content.
- `HtmlTokenType.java`: Contains all the possible token types.

#### Parser and max depth search 
- `HtmlParser.java`: Parses the tokenized HTML content to find the deepest text node.
- `HtmlNode.java`: Represents a node in the HTML  structure.
- `HtmlData.java`: Wrapper interface that allows all
the existant types of nodes (supported by the project description).
- `HtmlTagNode.java`: One of the classes that implement `HtmlData`. Represents an open/close tag pair.
- `HtmlTextContentNode.java`: The other class that 
implements `HtmlData`. Represents plain text content. 

#### Executing

To execute the project, use `javac` to generate the
necessary `.class` files. After that, run:

```
java HtmlAnalyzer.java desired-url
```

10 minutos:

30 segundos: Apresentação pessoal e enuciação do objetivo proposto!
1:30: Descrever funcionamento da parte 1
4:30: Descrever funcionamento da parte 2
3:30: Descrever funcionamento da parte 3 e apresentar possíveis melhorias
