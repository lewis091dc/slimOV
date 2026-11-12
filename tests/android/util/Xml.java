package android.util;
import org.xmlpull.v1.XmlPullParser;
import org.kxml2.io.KXmlParser;
// JVM-only adapter: production uses Android's own Xml.newPullParser().
public final class Xml { public static XmlPullParser newPullParser(){return new KXmlParser();} }
