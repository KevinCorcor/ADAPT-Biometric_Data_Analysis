package DataSorting;

import java.util.ArrayList;
import java.util.List;

import Data.QSensorData;

/// A Range is a range of data (SensorData)
/// I believe Kevin Corcoran already rework these
/// This code was written in a rush and is really imprecise

/**
 * range object of qsensordata objects which are within a certain deviation from one another, currently 0.003
 * 
 * public ArrayList<QSensorData> list = new ArrayList<QSensorData>(); private float tempAvg=0; private float eda=0; private float accelerationAvg=0; private float accBound[]=new
 * float[2], tempBound[]=new float[2]; public boolean isNoise=false;
 *
 */
public class Range implements Comparable<Range>
{
	/**
	 * list contains all csv data
	 */
	public ArrayList<QSensorData> list = new ArrayList<QSensorData>();
	private float tempAvg = 0;
	private float eda = 0;
	private float accelerationAvg = 0;

	private float accBound[] = new float[2], tempBound[] = new float[2];

	public boolean isNoise = false;

	/**
	 * overriding constructor
	 * 
	 * @param d QSensor object
	 */
	public Range(QSensorData d)
	{
		list.add(d);
	}

	/**
	 * default constructor which does nothing
	 *
	 */
	public Range()
	{

	}

	/**
	 * making the ranges
	 * 
	 * @param d QSensor data
	 * @return boolean as to whether or not the value was successfully added
	 */
	public boolean add(QSensorData d)
	{
		if (d == null)
			return false;
		if (list.isEmpty())
		{
			list.add(d);
			return true;
		}
		if (d.getEDA() <= list.get(0).getEDA() + 0.03 && d.getEDA() >= list.get(0).getEDA() - 0.03)			// kcor - 0.3 is a value specified by Thibault  
		{
			d.setEDA(list.get(0).getEDA());
			list.add(d);
			return true;
		}
		return false;
	}
	/**
	 * 	
	 */
	public void calcul()
	{
		// System.out.println("starting calcul");

		if (list.size() == 0) 																// kcor - if the range is empty just return
			return;

		this.eda = list.get(0).getEDA(); 													// kcor - list is csv data associated with this particular range
		float acceleration = 0, temp = 0;													// kcor - retrieve initial eda value
		float avgAcc = 0, avgTemp = 0;

		this.accBound[0] = this.accBound[1] = (float) Math
				.sqrt((this.list.get(0).getxAxis() * this.list.get(0).getxAxis() + 			// kcor - both acc bounds are initially equal
						this.list.get(0).getyAxis() * this.list.get(0).getyAxis()
						+ this.list.get(0).getzAxis() * this.list.get(0).getzAxis()));
		this.tempBound[0] = this.tempBound[1] = 											// kcor - both temp bounds are initially equal
				this.list.get(0).getTemperature();

		for (QSensorData data : list)
		{ 																					// kcor - cycles through all qsensor data associated with this particular range
			temp = data.getTemperature(); 													// kcor - gets the temperature for that row
			acceleration = (float) Math.sqrt((data.getxAxis() * data.getxAxis() + 			// kcor - records ass as magnitude
					data.getyAxis() * data.getyAxis() + data.getzAxis() * data.getzAxis()));
			avgAcc += acceleration; 														// kcor - sum up all acc for this range
			avgTemp += temp; 																// kcor - sum up all temp for this range

			if (temp < tempBound[0]) 														// kcor - set lower temp bound for the entire range
				tempBound[0] = temp;
			else if (temp > tempBound[1]) 													// kcor - set upper temp bound for the entire range
				tempBound[1] = temp;
			
			if (acceleration < accBound[0]) 												// kcor - set lower acc bound for the entire range
				accBound[0] = acceleration;
			else if (acceleration > accBound[1])											// kcor - set upper acc bound for the entire range
				accBound[1] = acceleration;
		}

		this.tempAvg = avgTemp / list.size();
		this.accelerationAvg = avgAcc / list.size();

		if (this.eda < 0.300) 																// kcor - if this average eda of a range is less than 0.3 it is clearly noise**was 0.2 but changed to 0.3
			this.isNoise = true; 															// kcor - mark for removal
		// System.out.println("calcul done");
	}

	/**
	 * groups all the data into ranges based on eda deviations
	 * 
	 * @param l contains QSensor objects with essentially every line of the csv file
	 * @return L - array of all the different ranges
	 */
	public static ArrayList<Range> getRangesEDA(ArrayList<QSensorData> l)
	{
		// System.out.println("Getting Range EDA");

		Range p = new Range(); 																// kcor - empty range to be filled
		ArrayList<Range> L = new ArrayList<Range>(); 										// kcor - array of ranges to be sent back

		for (QSensorData d : l)
		{ 																					// kcor - cycle through all csv rows
			if (!p.add(d))
			{ 																				// kcor - if this eda value deviates to much and is no0t added
				L.add(p); 																	// kcor - add the current range to the array since we are finished with it
				p = new Range(d); 															// kcor - create a new range beginning with deviated row
			}
		}
		L.add(p); 																			// kcor - finally add the last range being dekt with to the array
		for (Range p1 : L)
		{ 																					// kcor - cycle through all the Ranges - this could probably be incorporated into the previous loop
			p1.calcul(); 																	// kcor - inialises each Ranges fields
		}

		// System.out.println("got Range eda");
		return L;
	}

	/**
	 * This function takes all ranges and determines which is the best baseline to work from
	 *
	 * @param l - array of Ranges
	 * @return p - single Range object
	 */
	public static Range getBestRange(List<Range> l)
	{
		Range p = null; 																	// kcor - create null Range

		if (l.isEmpty())
			return null;
		// Collections.sort(l);

		// int cpt=0, maxCpt=(int)(l.size()*0.4); 											// kcor - WHY 0.4

		for (Range pt : l)
		{ 																					// kcor - cycle through all Ranges in "l"
			// cpt++;
			// if(cpt>=maxCpt)
			// break;
			if (p == null)
			{
				p = pt; 																	// kcor - if its the first iteration let p equal the first element
				continue; 																	// kcor - continue cause it to jump to the end
			}
			if (pt.isNoise) 																// kcor - if noise is detected, ignore the range
				continue;

			/// Taille
			/*
			 * if(pt.list.size()<p.list.size()*0.7) 										// kcor - if the current ranges list is smaller than 70% of the currently recorder best, skip continue;
			 * /*if(pt.list.size()>p.list.size()*1.5){ 										// kcor - if the current ranges list size is greater than 150% of the currently recorded best p=pt; // kcor - update as the
			 * new best range continue; }
			 */																				//perhaps some outlier detection here?
			
			/// EDA
			/*
			 * if(pt.eda>p.eda) 															// kcor - THIS IS A NEW LINE continue; ///Température if(pt.getTempGap()>p.getTempGap()) // kcor - if the current ranges tempGap is greater than the
			 * currently recorder best continue; ///Accélération if(pt.getAccGap()>p.getAccGap()) // kcor - if the current ranges AccGap is greater than the currently recorder best
			 * continue; continue;
			 */
			if (p.list.size() < pt.list.size()) 											// kcor - THIS IS A NEW IF STATEMENT
				p = pt; 																	// kcor - if the above two 'if's hold update best range
		}

		return p;
	}

	/**
	 * This function is performed on the single Range we regards as our baseline range. It cycles through all Range objects and relative to the baseline detects which ranges
	 * contain noise
	 * 
	 * @param l - array of Range objects
	 */
	public void eraseNoise(List<Range> l)
	{
		for (int i = 0; i < l.size(); i++)
		{
			if (l.get(i).isNoise)
			{
				l.remove(i);
				i--;
				continue;
			}
			if (l.get(i).eda < this.eda - 0.05)
			{
				l.remove(i);
				i--;
				continue;
			}
		}
	}

	/**
	 * given an array of Range objects, we cycle through then and for each one we generate SQL queries for them and then output all the SQL command in a string array
	 * 
	 * @param l - Range object containing list(an array of QSensorData objects), float tempAvg, float eda, float accelerationAvg float accBound[], float tempBound[], boolean
	 *            isNoise
	 * @return lQ
	 */
	public static List<String> getAllQueries(List<Range> l)
	{
		ArrayList<String> lQ = new ArrayList<String>();
		for (Range p : l)
		{
			lQ.addAll(p.getQueries());
		}
		return lQ;
	}

	/**
	 * cycles through the list(a QSensor array) and creates an String array of SQL statements
	 * 
	 * @return LQ a String array of SQL statements
	 */
	public List<String> getQueries()
	{
		ArrayList<String> lQ = new ArrayList<String>();
		for (QSensorData q : this.list)
		{
			lQ.add(q.getInsertQuery("Sensor"));
		}
		return lQ;
	}

	/**
	 * 
	 * 
	 * @return the difference between both acc bounds
	 */
	public float getAccGap()
	{
		return this.accBound[1] - this.accBound[0];
	}

	/**
	 * 
	 * 
	 * @return the difference between both temp bounds
	 */
	public float getTempGap()
	{
		return this.tempBound[1] - this.tempBound[0];
	}

	/**
	 * overriding method for converting the Range object to a string incluced tempavg, accavg, eda
	 * 
	 * @return retour
	 */
	public String toString()
	{
		String retour = "";

		retour += "Temp: " + this.tempAvg + '\n' + "Acc: " + this.accelerationAvg + '\n' + "eda: " + this.eda;

		return retour;
	}

	/**
	 * returns the Range avgtemp
	 * 
	 * @return tempavg
	 */
	public float getTempAvg()
	{
		return tempAvg;
	}

	/**
	 * sets tempavg to input value
	 * 
	 * @param tempAvg
	 */
	public void setTempAvg(float tempAvg)
	{
		this.tempAvg = tempAvg;
	}

	/**
	 * returns the Range eda value
	 * 
	 * @return eda
	 */
	public float getEda()
	{
		return eda;
	}

	/**
	 * sets the Range eda value
	 * 
	 * @param eda
	 */
	public void setEda(float eda)
	{
		this.eda = eda;
	}

	/**
	 * simple getter
	 * 
	 * @return accelerationAvg - float acceleration average
	 */
	public float getAccelerationAvg()
	{
		return accelerationAvg;
	}

	/**
	 * simple setter
	 * 
	 * @param accelerationAvg float
	 */
	public void setAccelerationAvg(float accelerationAvg)
	{
		this.accelerationAvg = accelerationAvg;
	}

	/**
	 * 
	 */
	@Override
	public int compareTo(Range p)
	{
		// TODO Auto-generated method stub
		return (int) (this.getAccelerationAvg() * 1000 - p.getAccelerationAvg() * 1000);
	}
}
