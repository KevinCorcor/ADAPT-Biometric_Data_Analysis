package detection;

import java.util.ArrayList;
import java.util.List;


/**
 * !PEAKDET Detect peaks in a vector ! ! call PEAKDET(MAXTAB, MINTAB, N, V, DELTA) finds the local ! maxima and minima ("peaks") in the vector V of size N. ! MAXTAB and MINTAB
 * consists of two columns. Column 1 ! contains indices in V, and column 2 the found values. ! ! call PEAKDET(MAXTAB, MINTAB, N, V, DELTA, X) replaces the ! indices in MAXTAB
 * and MINTAB with the corresponding X-values. ! ! A point is considered a maximum peak if it has the maximal ! value, and was preceded (to the left) by a value lower by !
 * DELTA. ! ! Eli Billauer, 3.4.05 (http://billauer.co.il) ! Translated into Fortran by Brian McNoldy (http://andrew.rsmas.miami.edu/bmcnoldy) ! This function is released to
 * the public domain; Any use is allowed.
 * 
 * @author kevin corcoran
 * @based_on https://github.com/jivesoftware/miru/blob/master/miru-reco-plugins/src/main/java/com/jivesoftware/os/miru/reco/plugins/trending/PeakDet.java
 */
public class Detection
{
	static double avg;
	static double sum_sd;
	static double sum_avg;
	
	/**
	 * initial method call which redirects to the full call
	 * 
	 * @param vector - List<Float> - one of the metrics( e.g; temperature or eda) 
	 * @param triggerDelta - double - unsure of purpose
	 * @return	maxtab_tmp - ArrayList<Peak>
	 */
	public static List<Peak> PeakDetection(List<Double> vector, List<Long> creationDates, double triggerDelta)
	{
		return PeakDetection(vector, creationDates, 0, vector.size(), triggerDelta);
	}
	
	/**
	 * determines the actual peaks in the data
	 * 
	 * @param vector - List<Float> - one of the metrics( e.g; temperature or eda) 
	 * @param length - int - simply the length of the list
	 * @param triggerDelta - double -  unsure of purpose
	 * @return max_tmp - List<Peak> 
	 */
	public static List<Peak> PeakDetection(List<Double> vector, List<Long> creationDates, int offset, int length, double triggerDelta)
	{
		double mn = Float.POSITIVE_INFINITY;
		double mx = Float.NEGATIVE_INFINITY;
		//Long mnpos = 0L;
		Long mxpos = 0L;
		int lookformax = 1;

		List<Peak> maxtab_tmp = new ArrayList<>();
		//List<Valley> mintab_tmp = new ArrayList<>();

		for (int i = offset; i < length; i++)
		{
			double a = vector.get(i);
			if (a > mx)
			{
				mx = a;
				mxpos = creationDates.get(i);
			}
			if (a < mn)
			{
				mn = a;
		//		mnpos = creationDates.get(i);
			}
			if (lookformax == 1)
			{
				if (a < mx - triggerDelta)
				{
					maxtab_tmp.add(new Peak(mx, mxpos));
					mn = a;
				//	mnpos = creationDates.get(i);
					lookformax = 0;
				}
			} else
			{
				if (a > mn + triggerDelta)
				{
			//		mintab_tmp.add(new Valley(mn, mnpos));
					mx = a;
					mxpos = creationDates.get(i);
					lookformax = 1;
				}
			}
		}

		return maxtab_tmp;
	}
	
	/**
	 * initial method call which redirects to the full call
	 * 
	 * @param vector - List<Float> - one of the metrics( e.g; temperature or eda) 
	 * @param triggerDelta - double - unsure of purpose
	 * @return	maxtab_tmp - ArrayList<Valley>
	 */
	public static List<Valley> ValleyDetection(List<Double> vector, List<Long> creationDates, double triggerDelta)
	{
		return ValleyDetection(vector, creationDates, 0, vector.size(), triggerDelta);
	}
	
	/**
	 * determines the actual valleys in the data
	 * 
	 * @param vector - List<Float> - one of the metrics( e.g; temperature or eda) 
	 * @param length - int - simply the length of the list
	 * @param triggerDelta - double -  unsure of purpose
	 * @return max_tmp - List<Valley> 
	 */
	public static List<Valley> ValleyDetection(List<Double> vector, List<Long> creationDates, int offset, int length, double triggerDelta)
	{
		double mn = Float.POSITIVE_INFINITY;
		double mx = Float.NEGATIVE_INFINITY;
		Long mnpos = 0L;
		//Long mxpos = 0L;
		int lookformax = 1;

		//List<Peak> maxtab_tmp = new ArrayList<>();
		List<Valley> mintab_tmp = new ArrayList<>();

		for (int i = offset; i < length; i++)
		{
			double a = vector.get(i);
			if (a > mx)
			{
				mx = a;
			//	mxpos = creationDates.get(i);
			}
			if (a < mn)
			{
				mn = a;
				mnpos = creationDates.get(i);
			}
			if (lookformax == 1)
			{
				if (a < mx - triggerDelta)
				{
				//	maxtab_tmp.add(new Peak(mx, mxpos));
					mn = a;
					mnpos = creationDates.get(i);
					lookformax = 0;
				}
			} else
			{
				if (a > mn + triggerDelta)
				{
					mintab_tmp.add(new Valley(mn, mnpos));
					mx = a;
				//	mxpos = creationDates.get(i);
					lookformax = 1;
				}
			}
		}

		return mintab_tmp;
	}

	//***************************************************************************************************************************************************
	/**
	 * initial method call which redirects to the full call
	 * 
	 * @param vector - List<Float> - one of the metrics( e.g; temperature or eda) 
	 * @param triggerDelta - double - unsure of purpose
	 * @return	maxtab_tmp - ArrayList<Peak>
	 */
	public static List<Peak> PeakDetectionStDev(List<Double> vector, List<Long> creationDates, Double triggerDelta)
	{
		return PeakDetectionStDev(vector, creationDates, 0, vector.size(), triggerDelta);
	}
	
	/**
	 * determines the actual peaks in the data
	 * 
	 * @param vector - List<Float> - one of the metrics( e.g; temperature or eda) 
	 * @param length - int - simply the length of the list
	 * @param triggerDelta - double -  unsure of purpose
	 * @return max_tmp - List<Peak> 
	 */
	public static List<Peak> PeakDetectionStDev(List<Double> vector, List<Long> creationDates, int offset, int length, Double triggerDelta)
	{
		//DescriptiveStatistics stats = new DescriptiveStatistics();
		double mn = Float.POSITIVE_INFINITY;
		double mx = Float.NEGATIVE_INFINITY;
	//	Long mnpos = 0L;
		Long mxpos = 0L;
		int lookformax = 1;

		List<Peak> maxtab_tmp = new ArrayList<>();
		//List<Valley> mintab_tmp = new ArrayList<>();
		 List<Double> temp = new ArrayList<>();
		 
		for (int i = offset; i < length; i++)
		{
			if(i>1){
				triggerDelta = standardDeviation(temp);						//update standard deviation
			}
			temp.add(vector.get(i));
			
			double a = vector.get(i);
			if (a > mx)
			{
				mx = a;
				mxpos = creationDates.get(i);
			}
			if (a < mn)
			{
				mn = a;
		//		mnpos = creationDates.get(i);
			}
			if (lookformax == 1)
			{
				if (a < mx - triggerDelta)
				{
					maxtab_tmp.add(new Peak(mx, mxpos));
					mn = a;
		//			mnpos = creationDates.get(i);
					lookformax = 0;
				}
			} else
			{
				if (a > mn + triggerDelta)
				{
			//		mintab_tmp.add(new Valley(mn, mnpos));
					mx = a;
					mxpos = creationDates.get(i);
					lookformax = 1;
				}
			}
		}

		return maxtab_tmp;
	}
	
	/**
	 * initial method call which redirects to the full call
	 * 
	 * @param vector - List<Float> - one of the metrics( e.g; temperature or eda) 
	 * @param triggerDelta - double - unsure of purpose
	 * @return	maxtab_tmp - ArrayList<Valley>
	 */
	public static List<Valley> ValleyDetectionStDev(List<Double> vector, List<Long> creationDates, double triggerDelta)
	{
		return ValleyDetectionStDev(vector, creationDates, 0, vector.size(), triggerDelta);
	}
	
	/**
	 * determines the actual valleys in the data
	 * 
	 * @param vector - List<Float> - one of the metrics( e.g; temperature or eda) 
	 * @param length - int - simply the length of the list
	 * @param triggerDelta - double -  unsure of purpose
	 * @return max_tmp - List<Valley> 
	 */
	public static List<Valley> ValleyDetectionStDev(List<Double> vector, List<Long> creationDates, int offset, int length, double triggerDelta)
	{
		double mn = Float.POSITIVE_INFINITY;
		double mx = Float.NEGATIVE_INFINITY;
		Long mnpos = 0L;
		//Long mxpos = 0L;
		int lookformax = 1;

		//List<Peak> maxtab_tmp = new ArrayList<>();
		List<Valley> mintab_tmp = new ArrayList<>();
		
		 List<Double> temp = new ArrayList<>();

		for (int i = offset; i < length; i++)
		{
			if(i>1){
				triggerDelta = standardDeviation(temp);						//update standard deviation
			}
			temp.add(vector.get(i));
			double a = vector.get(i);
			if (a > mx)
			{
				mx = a;
	//			mxpos = creationDates.get(i);
			}
			if (a < mn)
			{
				mn = a;
				mnpos = creationDates.get(i);
			}
			if (lookformax == 1)
			{
				if (a < mx - triggerDelta)
				{
				//	maxtab_tmp.add(new Peak(mx, mxpos));
					mn = a;
					mnpos = creationDates.get(i);
					lookformax = 0;
				}
			} else
			{
				if (a > mn + triggerDelta)
				{
					mintab_tmp.add(new Valley(mn, mnpos));
					mx = a;
			//		mxpos = creationDates.get(i);
					lookformax = 1;
				}
			}
		}

		return mintab_tmp;
	}
	
	 /**
     * standardDeviation() - designed to calculate the standard deviation of a data set incrementally by taking the last entered value and the previous variance recorded.
     * (i.e; upon adding a value to the data set this function should immediately be called)
     * 
     * NOTE: do not call this function if the data set size it less than 2 since standard deviation cannot be calculated on a single value
     * NOTE: sum_avg, sum_sd and avg are all static variables
     * NOTE: on attempting to use this on another set following previous use, the static values will have to be reset**
     * 
     * @param vector - List<Double> - data with only one additional value from previous method call
     * @return updated value for the Standard deviation
     */
    public static double standardDeviation(List<Double> vector)
    {   
    	double N = (double) vector.size();								//size of the data set
    	double oldavg = avg;											//save the old average
		avg = updateAverage(vector);									//update the new average
		
    	if(N==2.0)														//if there are only two, we calculate the standard deviation using the standard formula	
    	{																
    		for(double d:vector)										//cycle through the 2 elements of the data set - there is no need to use a loop here, the set is quite small to just do manually
        	{
        		sum_sd += Math.pow((Math.abs(d)-avg), 2);				//sum the following according to the formula
        	}
    	}
    	else if(N>2)													//once we have calculated the base sum_std 	
    	{	
    		double newel = (vector.get(vector.size()-1));				//get the latest addition to the data set
    		
    		sum_sd = sum_sd + (newel - oldavg)*(newel-avg); 			//https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm
    	}
    	return Math.sqrt((sum_sd)/(N));								//N or N-1 depends on your choice of sample of population standard deviation
    	
    }
    
    /**
     * simplistic method for incrementally calculating the mean of a data set
     * 
     * @param vector - List<Double> - data with only one additional value from previous method call
     * @return updated value for the mean of the given data set
     */
    public static double updateAverage(List<Double> vector)
    {
    	if(vector.size()==2){
    		sum_avg = vector.get(vector.size()-1) + vector.get(vector.size()-2);
    	}
    	else{
    		sum_avg += vector.get(vector.size()-1);
    	}
    	
    	    	
    	return sum_avg/(double)vector.size();
    	
    }
    
    /**
     * defines a peak object
     * 
     * NOTE: the P1 & P2 variables have not been used but were include for future use
     *
     */
	static class Peak
	{

		public final double value;
		public final Long index;
		public double V1;
		public double V2;

		/**
		 * @param height
		 * @param index
		 */
		private Peak(double value, Long index)
		{
			this.value = value;
			this.index = index;
		}

		public void setP1(Double val)
		{
			V1=val;
		}
		public void setP2(Double val)
		{
			V2=val;
		}
		@Override
		public String toString()
		{
			return "Peak{" + "value = " + value + ", creationDate = " + index + '}';
		}

	}
	
	/**
	 * 
	 * defines a valley object
	 * 
	 * NOTE: the P1 & P2 variables have not been used but were include for future use
	 *
	 */
	static class Valley
	{

		public final double value;
		public final Long index;
		public double P1; //beginning 
		public double P2;//end
		
		/**
		 * @param value - double - metric value
		 * @param index Long - creation date
		 */
		private Valley(double value, Long index)
		{
			this.value = value;
			this.index = index;
		}

		public void setP1(Double val)
		{
			P1=val;
		}
		public void setP2(Double val)
		{
			P2=val;
		}
		@Override
		public String toString()
		{
			return "Valley{" + "value = " + value + ", creationDate = " + index + '}';
		}

	}
}
