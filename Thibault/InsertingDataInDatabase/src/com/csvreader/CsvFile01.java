package com.csvreader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

///Class used to read a generic CSV File, there is no need to change it
public class CsvFile01 implements CsvFile {

	public final static char SEPARATOR = ',';

    private File file;
    private List<String> lines;
    private List<String[] > data;

    @SuppressWarnings("unused")
	private CsvFile01() {
    }

    public CsvFile01(File file) {
        this.file = file;

        // Init
        init();
    }

    private void init() {
        lines = CsvFileHelper.readFile(file);

        data = new ArrayList<String[] >(lines.size());
        String sep = new Character(SEPARATOR).toString();
        for (String line : lines) {
            String[] oneData = line.split(sep);
            data.add(oneData);
        }
    }

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return file;
	}

	@Override
	public List<String[]> getData() {
		// TODO Auto-generated method stub
		return data;
	}

}
