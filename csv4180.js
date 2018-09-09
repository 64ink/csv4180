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

/**
 * Class that outputs CSV style data according to RFC 4180.
 *
 * Usage: 
 * writer = new CSVWriter(); writer.writeFields([1,2,3]); console.log(writer.data);
 */
function CSVWriter() {

    /**
     * Writes a line of fields to the writer. A new line is automatically added
     * if beyond the first call to this method. This is a convenience method.
     */
    this.writeFields = function (fields /*[]*/ ) {

        if (this._newWriter) {
            this._newWriter = false;
        } else {
            this.writeLine();
        }
        for (var i = 0; i < fields.length; i++) {
            this.writeField(fields[i]);
        }
    };

    /**
     * Write a line separator. The line separator string is defined by the
     * system property line.separator, and is not necessarily a single newline
     * ('\n') character.
     */
    this.writeLine = function () {
        this._newLine = true;
        this.write('\n');
    };

    /**
     * Write a field to the output quoting as necessary and adding comma
     * separators between fields.
     */
    this.writeField = function (field /*string*/ ) {
        if (this._newLine) {
            this._newLine = false;
        } else {
            this.write(',');
        }

        // case 0: empty string, simple :)
        if (field == null) {
            return;
        }
        field = String(field);
        if (field.length == 0) {
            return;
        }

        // case 1: field has quotes in it, if so convert to, quote field and
        // double all quotes
        if (field.indexOf('"') > -1) {
            this.write('"');
            this.write(field.replace(/"/g, "\"\""));
            this.write('"');
            return;
        }

        // case 2: field has a comma, carriage return or new line in it, if so
        // quote field and double all quotes
        if (/[,\r\n]/.test(field)) {
            this.write('"');
            this.write(field);
            this.write('"');
            return;
        }

        // case 3: safe string to just add
        this.write(field);
    };

    /**
     * Simple output method useful for overwriting.
     */
    this.write = function (s /*string*/ ) {
        this.data += s;
    };

    /**
     * string containing the output data if the default writer is used.
     */
    this.data = "";

    // private data
    this._newLine = true,
    this._newWriter = true

}