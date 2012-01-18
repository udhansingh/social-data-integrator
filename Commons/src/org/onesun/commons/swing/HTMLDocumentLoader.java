/*
   Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
package org.onesun.commons.swing;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.swing.text.BadLocationException;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.Parser;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

public class HTMLDocumentLoader {
	public HTMLDocument loadDocument(HTMLDocument htmlDocument, URL url, String charSet) throws IOException {
		htmlDocument.putProperty(Document.StreamDescriptionProperty, url);

		InputStream in = null;
		boolean ignoreCharSet = false;

		for (;;) {
			try {
				htmlDocument.remove(0, htmlDocument.getLength());

				URLConnection urlc = url.openConnection();
				in = urlc.getInputStream();
				Reader reader = (charSet == null) ? new InputStreamReader(in) : new InputStreamReader(in, charSet);

				Parser parser = getParser();
				ParserCallback htmlReader = getParserCallback(htmlDocument);
				parser.parse(reader, htmlReader, ignoreCharSet);
				htmlReader.flush();

				break;
			} catch (BadLocationException ex) {
				throw new IOException(ex.getMessage());
			} catch (ChangedCharSetException e) {
				charSet = getNewCharSet(e);
				ignoreCharSet = true;
				in.close();
			}
		}

		return htmlDocument;
	}

	public HTMLDocument loadDocument(URL url, String charSet) throws IOException {
		return loadDocument((HTMLDocument) kit.createDefaultDocument(), url, charSet);
	}

	public HTMLDocument loadDocument(URL url) throws IOException {
		return loadDocument(url, null);
	}

	public synchronized Parser getParser() {
		if (parser == null) {
			try {
				Class<?> c = Class.forName("javax.swing.text.html.parser.ParserDelegator");
				parser = (HTMLEditorKit.Parser) c.newInstance();
			} catch (Throwable e) {
			}
		}
		return parser;
	}

	public synchronized ParserCallback getParserCallback(HTMLDocument doc) {
		return doc.getReader(0);
	}

	protected String getNewCharSet(ChangedCharSetException e) {
		String charsetSpec = e.getCharSetSpec();
		if (e.keyEqualsCharSet()) {
			return charsetSpec;
		}

		int index = charsetSpec.indexOf(";");
		if (index != -1) {
			charsetSpec = charsetSpec.substring(index + 1);
		}

		charsetSpec = charsetSpec.toLowerCase();

		StringTokenizer st = new StringTokenizer(charsetSpec, " \t=", true);
		boolean foundCharSet = false;
		boolean foundEquals = false;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals(" ") || token.equals("\t")) {
				continue;
			}
			if (foundCharSet == false && foundEquals == false
					&& token.equals("charset")) {
				foundCharSet = true;
				continue;
			} else if (foundEquals == false && token.equals("=")) {
				foundEquals = true;
				continue;
			} else if (foundEquals == true && foundCharSet == true) {
				return token;
			}

			foundCharSet = false;
			foundEquals = false;
		}

		return "8859_1";
	}

	protected static HTMLEditorKit kit;

	protected static HTMLEditorKit.Parser parser;

	static {
		kit = new HTMLEditorKit();
	}
}