
public class HtmlToken {

    private String literalValue;
    private HtmlTokenType tokenType;

    public HtmlToken() {
    }

    public String getLiteralValue() {
        return literalValue;
    }

    public HtmlTokenType getTokenType() {
        return tokenType;
    }

    public void setLiteralValue(String literalValue) {
        this.literalValue = literalValue;
    }

    public void setTokenType(HtmlTokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "HtmlToken{"
                + "literalValue='" + literalValue + '\''
                + ", tokenType=" + tokenType
                + '}';
    }
}
