package com.android.testapp;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public Object parse(InputStream in) throws XmlPullParserException, IOException {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }

    }

    private Object readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        List<Item> entries = new ArrayList<Item>();

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG && parser.getName().equals("user"))
                entries.add(readUser(parser));
            if(eventType == XmlPullParser.START_TAG && parser.getName().equals("balance"))
                entries.add(readBalance(parser));
            if(eventType == XmlPullParser.START_TAG && parser.getName().equals("result-code")) {
                Error error = this.readError(parser);
                if (error.getId() != 0)
                    return error;
            }

            eventType = parser.next();
        }

        return entries;

    }

    private Error readError(XmlPullParser parser) throws IOException, XmlPullParserException {

        String message = parser.getAttributeValue(null, "message");
        parser.next();
        int id = Integer.valueOf(parser.getText());

        return new Error(id, message);

    }

    private User readUser(XmlPullParser parser) throws IOException, XmlPullParserException {

        int id = Integer.valueOf(parser.getAttributeValue(null, "id"));
        String name = parser.getAttributeValue(null, "name");
        String secondName = parser.getAttributeValue(null, "second-name");

        return new User(id, name, secondName);

    }

    private Balance readBalance(XmlPullParser parser) throws IOException, XmlPullParserException {

        String currency = parser.getAttributeValue(null, "currency");
        BigDecimal amount = new BigDecimal(parser.getAttributeValue(null, "amount"));

        return new Balance(0, currency, amount);

    }
}
