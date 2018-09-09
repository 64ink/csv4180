/*
 * Copyright 2010,2018 No Face Press, LLC
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
package com.nofacepress.test.csv4180;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.nofacepress.csv4180.CSVReader;
import com.nofacepress.csv4180.CSVWriter;

public class CSVTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	final static String FILE_NAME = "test.csv";

	final static String[][] data = { { "ONE", "TWO", "THREE" }, { "\"ONE\"", "\"TWO\"", "\"THREE\"" },
			{ "THIS ONE", "TESTS", "Multi-\nLine" }, { "A", "com,ma", "can exist" }, { "" }, { "That was blank!" } };

	@Test
	public void testIOByField() throws IOException {
		File file = folder.newFile(FILE_NAME);

		// Write the file a FIELD at a time
		final CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
		for (int row = 0; row < data.length; row++) {
			if (row > 0) {
				csvWriter.newLine();
			}
			for (int field = 0; field < data[row].length; field++) {
				csvWriter.writeField(data[row][field]);
			}
		}
		csvWriter.close();

		// Read the file a FIELD at a time
		final ArrayList<String[]> rows = new ArrayList<String[]>();
		final ArrayList<String> fields = new ArrayList<String>();
		final CSVReader csvReader = new CSVReader(new FileReader(file));
		while (!csvReader.isEOF()) {
			fields.clear();
			do {
				fields.add(csvReader.readField());
			} while (csvReader.hasMoreFieldsOnLine());
			rows.add(fields.toArray(new String[fields.size()]));
		}
		csvReader.close();

		String[][] result = rows.toArray(new String[rows.size()][1]);

		verifyData(data, result);
	}

	@Test
	public void testIOByLine() throws IOException {
		File file = folder.newFile(FILE_NAME);

		// Write the file a LINE at a time
		final CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
		for (int row = 0; row < data.length; row++) {
			csvWriter.writeFields(data[row]);
		}
		csvWriter.close();

		// Read the file a LINE at a time
		final ArrayList<String[]> rows = new ArrayList<String[]>();
		final ArrayList<String> fields = new ArrayList<String>();
		final CSVReader csvReader = new CSVReader(new FileReader(file));
		while (!csvReader.isEOF()) {
			csvReader.readFields(fields);
			rows.add(fields.toArray(new String[fields.size()]));
		}
		csvReader.close();

		String[][] result = rows.toArray(new String[rows.size()][1]);
		verifyData(data, result);
	}

	public static boolean verifyData(String[][] data1, String[][] data2) {
		assertEquals(data1.length, data2.length);
		for (int i = 0; i < data1.length; i++) {
			assertEquals(data1[i].length, data2[i].length);
			for (int j = 0; j < data1[i].length; j++) {
				assertEquals(data1[i][j], data2[i][j]);
			}
		}
		return true;
	}

}
