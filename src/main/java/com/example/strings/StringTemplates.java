import java.io.*;

void main() {
    String filePath = "tmp.dat";
    File file     = new File(filePath);
    System.out.println(STR."The file \{filePath} \{file.exists() ? "does" : "does not"} exist");

    String title = "My Web Page";
    String text  = "Hello, world";

    String html = STR."""
        <html>
          <head>
            <title>\{title}</title>
          </head>
          <body>
            <p>\{text}</p>
          </body>
        </html>
        """;
    System.out.println(html);
}
