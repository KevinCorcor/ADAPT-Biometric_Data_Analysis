package com.csvreader;

///Interface for the DataObject that are filled with a CSV file (Sensor, GPS, etc)
public interface CSVParsable {

	///Function that fill the data of the Object with a CSV file line
	public abstract void fillWithCSV(String[] oneLine);
}
