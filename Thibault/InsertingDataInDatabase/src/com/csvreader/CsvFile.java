package com.csvreader;
import java.io.File;
import java.util.List;

///Class used to read a generic CSV File, there is no need to change it
public interface CsvFile {

    File getFile();

    List<String[] > getData();
}
