/*
   Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
package org.onesun.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class TimeUtils {
	private static Logger logger = Logger.getLogger(TimeUtils.class);
	
	private static List<SimpleDateFormat> formats = Collections.synchronizedList(new ArrayList<SimpleDateFormat>());
	
	private static List<String> dateFormats = null;
	
	public static long getTimeInMillis() {
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public static long diffTimeInMillis(long start, long end){
		return end - start;
	}
	
	public static String millisToTime(final long time) {
		String format = String.format("%%0%dd", 2);
		long elapsedTime = time / 1000;
		String seconds = String.format(format, elapsedTime % 60);
		String minutes = String.format(format, (elapsedTime % 3600) / 60);
		String hours = String.format(format, elapsedTime / 3600);
		String output =  hours + ":" + minutes + ":" + seconds;
		return output;
	}
	
	// For sprint initialization of dates
	public void init(){
		if(dateFormats != null && dateFormats.size() > 0){
			int counter = 0;
			for(String format : dateFormats){
				String cleanFormat = format.trim();
				
				if(!cleanFormat.isEmpty() && cleanFormat.length() > 0){
					counter++;
					logger.info("Adding dateFormat parsing support for [" + cleanFormat + "]");
					formats.add(new SimpleDateFormat(cleanFormat));
				}
			}
			
			logger.info("Total date formats loaded : " + counter + " : Make sure you optimize this list based on your sources");
		}
		else {
			logger.info("Date formats list not defined; loading a pre-defined set of date formats internally");
			// A sample set of date formats using which the framework was tested.
			formats.add(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"));
			formats.add(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z"));
			formats.add(new SimpleDateFormat("yyyy/MM/dd HH:mm"));
			formats.add(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));
			formats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
			formats.add(new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ssZ"));
			formats.add(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a z"));
		}
	}
	
	public enum Day {
    	SUNDAY		(1, "SUNDAY"),
    	MONDAY		(2, "MONDAY"),
    	TUESDAY		(3, "TUESDAY"),
    	WEDNESDAY	(4, "WEDNESDAY"),
    	THURSDAY	(5, "THURSDAY"),
    	FRIDAY		(6, "FRIDAY"),
    	SATURDAY	(7, "SATURDAY");

		private int _number = -1;
		private String _name = null;
		
		Day(int number, String name){
			this._number = number;
			this._name = name;
		}
		
		public static String name(int number){
			for(Day day : Day.values()){
				if(day._number == number){
					return day._name;
				}
			}
			
			return null;
		}
	}
	
	public enum Month {
		JANUARY		(0, 	"JANUARY", 		"Q1",		"H1"),
		FEBURARY	(1, 	"FEBRUARY", 	"Q1",		"H1"),
		MARCH		(2, 	"MARCH", 		"Q1",		"H1"),
		APRIL		(3, 	"APRIL",	 	"Q2",		"H1"),
		MAY			(4, 	"MAY", 			"Q2",		"H1"),
		JUNE		(5, 	"JUNE",	 		"Q2",		"H1"),
		JULY		(6, 	"JULY", 		"Q3",		"H2"),
		AUGUST		(7, 	"AUGUST", 		"Q3",		"H2"),
		SEPTEMBER	(8, 	"SEPTEMBER", 	"Q3",		"H2"),
		OCTOBER		(9, 	"OCTOBER", 		"Q4",		"H2"),
		NOVEMBER	(10, 	"NOVEMBER", 	"Q4",		"H2"),
		DECEMBER	(11, 	"DECEMBER", 	"Q4",		"H2");

		private int _number = -1;
		private String _name = null;
		private String _quarter = null;
		private String _halfYear = null;
		
		Month(int number, String name, String quarter, String halfYear){
			this._number = number;
			this._name = name;
			this._quarter = quarter;
			this._halfYear = halfYear;
		}
		
		public static String name(int number){
			for(Month month : Month.values()){
				if(month._number == number){
					return month._name;
				}
			}
			
			return null;
		}
		
		public static String quarter(int number){
			for(Month month : Month.values()){
				if(month._number == number){
					return month._quarter;
				}
			}
			
			return null;
		}
		
		public static String halfYear(int number){
			for(Month month : Month.values()){
				if(month._number == number){
					return month._halfYear;
				}
			}
			
			return null;
		}
	}
	
	public int toMinutes(int hour, int minutes){
		hour = (hour < 0) ? 0 : hour;
		minutes = (minutes < 0) ? 0 : minutes;
		
		return ((hour * 60) + minutes);
	}
	
	private static String PARSE_STATUS_PASSED = "passed";
	private static String PARSE_STATUS_FAILED = "failed";
	private static String PARSE_STATUS_DEFAULT = "processing";
	private static String PARSE_STATUS_EMPTY = "empty";
	
	public Date toDate(final String dateString, SimpleDateFormat format){
		if(format != null){
			try {
				return format.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public Date toDate(final String dateString){
		 SimpleDateFormat format = getDateFormat(dateString);
		 
		 return toDate(dateString, format);
	}
	
	public SimpleDateFormat getDateFormat(final String dateString){
		String status = PARSE_STATUS_DEFAULT;
		
		Date date = null;
		SimpleDateFormat format = null;
		
		do {
			if(dateString == null || dateString.isEmpty() || (dateString.compareTo("") == 0) || dateString.compareToIgnoreCase("null") == 0) {
				status = PARSE_STATUS_EMPTY;
				break;
			}
			
			for(SimpleDateFormat sdf : formats){
				try {
					date = sdf.parse(dateString);
					
					if(date != null){
						format = sdf;
						status = PARSE_STATUS_PASSED;

						logger.debug("Date parsing passed for " + sdf.toPattern());
					}
				}catch (ParseException e) {
					status = PARSE_STATUS_FAILED;
				}catch (NumberFormatException e){
					status = PARSE_STATUS_FAILED;
				}catch(ArrayIndexOutOfBoundsException e){
					status = PARSE_STATUS_FAILED;
				}finally{
					if(status.compareTo(PARSE_STATUS_FAILED) == 0){
						logger.debug("[" + date + "] parsing failed for " + sdf.toPattern() + " will try with next available format");
					}
				}
			}
		}while(status.compareTo(PARSE_STATUS_PASSED) == 0);
		
		if(date == null || (status.compareTo(PARSE_STATUS_EMPTY) == 0)){
			logger.error("Date could not be parsed for " + dateString + " report this to developer and have it fixed in next version!");
			return null;
		}
		else {
			return format;
		}
	}

	public void setDateFormats(List<String> dateFormats) {
		TimeUtils.dateFormats = dateFormats;
	}

	public static List<String> getDateFormats() {
		return dateFormats;
	}
}