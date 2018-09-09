# CSV4180 - CSV parser based on RFC 4180

Java implementation of a comma separated values, CSV, reader and writer based on RFC 4180. This format is exactly as Excel handles CSV files. CSV4180 is simple to use and understand with a commercial friendly license.

CSV4180 is a Java implementation of a comma separated values (CSV) reader and writer based on RFC 4180. This format is exactly as Excel handles CSV files. CSV4180 is simple to use and understand with a commercial friendly license.

This library is all business with no frills. There are tons of powerful alternatives to this implementation with tons of configuration options, but beware that many do not actually support the format used by Excel even though they may say it does. The main objective of CSV4180 is Excel compatibility, so there is no configuration required. And best of all, it is [Apache 2.0](LICENSE.md) licensed making it truly free, unlike GPL.

This library was created because other libraries had bad licenses, invalid implementations or were unnecessarily complicated. If someone is interested in CSV then they are most likely interested in Excel. "Make it simple stupid" is the motto for CSV4180. CSV is not hard, so there is no need to go overboard when implementing it.

Build status: [![build_status](https://travis-ci.org/phillip-kruger/apiee.svg?branch=master)](https://travis-ci.org/phillip-kruger/apiee)


## Quick Start: Reading Files
The most intuitive way to to read the CSV file one row at a time. This is done using the convenience method CSVReader::readFields() as shown below.

```java
CSVReader csvReader = new CSVReader(new FileReader(fileName));
ArrayList<String> fields = new ArrayList<String>();
while (!csvReader.isEOF()) {
	csvReader.readFields(fields);
	// ... process row 
}
```

However, the most direct method reads a field at a time eliminating the need for an ArrayList.

```java
CSVReader csvReader = new CSVReader(new FileReader(fileName));
while (!csvReader.isEOF()) {
	do {
		String field = csvReader.readField();
		// ... process field 
	} while (csvReader.hasMoreFieldsOnLine());
	// ... process row 
}
```

## Quick Start: Writing Files
The most intuitive way is to write the CSV file one row at a time. This is done using the convenience method CSVReader::writeFields() as shown below. This method accepts ArrayList<String> or String[] arguments.

```java
CSVReader csvWriter = new CSVWriter(new FileReader(fileName));
ArrayList<String> fields = new ArrayList<String>();
// ...  
csvWriter.writeFields(fields);
// ...  
csvWriter.close();
```

However, the most direct method is to write a field at a time calling CSVWriter::newLine() when a row is complete.

```java
CSVReader csvWriter = new CSVWriter(new FileReader(fileName));
String field;
// ...  
csvWriter.writeField(field);
// write more fields  
csvWriter.newLine();
// ...  
csvWriter.writeField(field);
// write more fields  
csvWriter.close();
```