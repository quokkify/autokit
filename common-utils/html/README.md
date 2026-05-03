# common-utils/html

Parse raw HTML strings and extract elements by XPath — useful for validating HTML in API responses.

## Dependency

```gradle
testImplementation project(":common-utils:html")
```

## Usage

Extract an element from an HTML string using an XPath locator:

```java
Node node = HtmlParser.getHtmlNode(response.getBody(), "//table[@id='summary']//td[1]");
assertThat(node.getTextContent(), equalTo("Expected Value"));
```

Escape and unescape HTML entities:

```java
String escaped = HtmlParser.escapeHtml("<b>Hello & World</b>");
String original = HtmlParser.unescapeHtml(escaped);
```
