# Information Retrieval #

## Overview ##

The developed program uses the following steps:
### 1. ensures the creation of the folder structure required in ../Documents/MyLifeLogging/ ###
### 2. Biometric Analysis ###
	1. The data for a given metric is stored in List<Double>(for values) & List<Long>(for creation dates)
	2. These Lists are then input into the extremity detection algorithm.
		1. Here a current minimum and maximum are kept track of until a new value exceeds either by a set or running 	standard deviation.
	3. The results are incrementally appended to a file as a single SQL statement.
	4. This SQL statement is executed and its results are logged to a text file whilst also copying said results to the appropriate folders.
	5. Finally, given the average result set size of the previous detection algorithm result set, a set of randomly selected lifelog items are copied to the appropriate folders.
### 3. The participant browses the result set of mixed random and algorithmic selected lifelof items and moves them to ../Documents/MylifeLogging/Important/ ###
### 4. Statistical Analysis of results relative to the randomly selected set of lifelog items ###
	1. Participant selected items are check to see which result sets they belong to and the statistical correctness of each result set is output.

## Future Work ##

* Implementation of some form of low pass filter for data rather than signals. (ie. a function for smoothening the biometric data before it is analysed). for example see [Butterworth](https://en.wikipedia.org/wiki/Butterworth_filter)
* Perform a Monte Carlo similation on comparing results to different randomly selected data sets..
* Implement Normalisation of the biometric data.
* Explore other forms of biometric data and Lifelog media.
* Improve the extent of noise reduction in biometric data by recording energy expenditure.
* To implement the approach of noise detection outlined in the .ppt file in the parent repository  

## Notes ##
* In order to run a new trial the folders containing the lifelog items must be archived to an alternative location or deleted. Otherwise data from previous trials will merge with that of newer trials and produce incorrect statistical results results.
* In order for Alex’s application to present the lifelog photos you must specify the name of the “app” file you want to read in from located in the folder “appInput”.
