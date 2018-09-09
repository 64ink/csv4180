/*
 * Copyright 2010,2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nofacepress.csv4180;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * CSV4180Reader provides a simple way to import CSV values from a file. The
 * reader extends BufferedReader for improved performance.
 * 
 * @author Thomas Davis
 */
public class CSVReader extends BufferedReader {

	/**
	 * Create a buffering character-input stream that uses a default-sized input
	 * buffer.
	 * 
	 * @param in
	 *            A Reader
	 */
	public CSVReader(Reader in) {
		super(in);
	}

	/**
	 * Create a buffering character-input stream that uses an input buffer of
	 * the specified size.
	 * 
	 * @param in
	 *            A Reader
	 * @param sz
	 *            Input-buffer size
	 */
	public CSVReader(Reader in, int sz) {
		super(in, sz);
	}

	/**
	 * Indicates if the last field read was at the end of the line.
	 * 
	 * @return true if last field read was at the end of the line, false
	 *         otherwise or if no fields have been read.
	 */
	public boolean hasMoreFieldsOnLine() {
		return this.moreFieldsOnLine;
	}

	/**
	 * Indicates if all the data from the reader has been read.
	 * 
	 * @return true if all the data from the reader has been read.
	 */
	public boolean isEOF() {
		return this.eof;
	}

	/**
	 * Reads the current line's fields into an ArrayList. This is a convenience
	 * method.
	 * 
	 * @param fields
	 *            container for the fields in the current row. It will be
	 *            cleared.
	 * @throws IOException
	 *             on general I/O error
	 * @throws EOFException
	 *             on end of file
	 */
	public void readFields(ArrayList<String> fields) throws IOException {
		fields.clear();
		if (this.eof) {
			throw new EOFException();
		}
		do {
			fields.add(readField());
		} while (this.moreFieldsOnLine);
	}

	/**
	 * Reads the next field from the input removing quotes as necessary.
	 * 
	 * @return next field
	 * @throws IOException
	 *             If an I/O error occurs, may be an EOFException on end of
	 *             input.
	 */
	public String readField() throws IOException {
		int c;
		final int UNQUOTED = 0;
		final int QUOTED = 1;
		final int QUOTEDPLUS = 2;
		int state = UNQUOTED;

		if (this.eof) {
			throw new EOFException();
		}

		this.buffer.setLength(0);
		while ((c = this.read()) >= 0) {
			if (state == QUOTEDPLUS) {
				switch (c) {
				case '"':
					this.buffer.append('"');
					state = QUOTED;
					continue;
				default:
					state = UNQUOTED;
					break;
				}
			}
			if (state == QUOTED) {
				switch (c) {
				default:
					this.buffer.append((char) c);
					continue;
				case '"':
					state = QUOTEDPLUS;
					continue;
				}
			}

			// (state == UNQUOTED)
			switch (c) {
			case '"':
				state = QUOTED;
				continue;
			case '\r':
				continue;
			case '\n':
			case ',':
				this.moreFieldsOnLine = (c != '\n');
				return this.buffer.toString();
			default:
				this.buffer.append((char) c);
				continue;
			}

		}
		this.eof = true;
		this.moreFieldsOnLine = false;
		return this.buffer.toString();
	}

	/** @ignore */
	private boolean moreFieldsOnLine = true;

	/** @ignore */
	private boolean eof = false;

	/** @ignore */
	private final StringBuffer buffer = new StringBuffer();

}
