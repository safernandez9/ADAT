package persistenciaSAX;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SimpleErrorHandler implements ErrorHandler {

    @Override
    public void warning(SAXParseException e) throws SAXException {
        // No detiene el proceso de parseo
        System.out.println("WARNING SAX: " + e.getMessage());
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        // No detiene el proceso de parseo
        System.out.println("ERROR SAX: " + e.getMessage());
        // Si quieres detener el parseo:
        // throw e;
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        // Siempre detiene el proceso de parseo
        System.out.println("FATAL ERROR SAX: " + e.getMessage());
        throw e;
    }
}
