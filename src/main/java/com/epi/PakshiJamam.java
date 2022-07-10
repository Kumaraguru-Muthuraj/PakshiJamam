package com.epi;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Period;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DailyJamams {
	public DailyJamams(List<Date> jamams, RuleOrDeath dayType) {
		this.jamams = jamams;
		this.dayType = dayType;
	}
	public List<Date> getJamams() {
		return this.jamams;
	}
	public RuleOrDeath getDayType() {
		return this.dayType;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(dayType.getValue()).append(" ");
		
		for (Date j : jamams) {
			sb.append(getDateStr(j)).append(", ");
		}
		return sb.toString();
	}
	
	private String getDateStr(Date d) {
		/*long millis = end.getTimeInMillis() - begin.getTimeInMillis();
		long seconds = millis / 1000 % 60;
		long minutes = millis / (60 * 1000) % 60;
		long hours = millis / (60 * 60 * 1000);*/
		
		SimpleDateFormat sdf = new SimpleDateFormat(CalendarUtil.displayDateFormat);		
		return sdf.format(d);
	}
	private List<Date> jamams;
	private RuleOrDeath dayType;
}

enum RuleOrDeath {
	RULE("***RULING DAY***"), DEATH ("***BAD DAY***"), NEUTRAL("");
	
	private RuleOrDeath(String value) {
		this.value = value;
	}
	public String getValue() {
		return this.value;
	}
	private String value;
	
}
enum HinduDayNight {
	DAY, NIGHT;
}
enum Paksh {
	SHUKLA, KRISHNA;
}

enum Pakshi {
	FALCON, PEACOCK, COCK, CROW, OWL;
}

enum Months {
	JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC;
}

class PoornimaAmavasyaGMT {
	public PoornimaAmavasyaGMT(String thithi, String date) {
		try {
			this.thithi = thithi;
			this.date = new SimpleDateFormat(CalendarUtil.dateAndTimeFormat).parse(date);
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}
	public String thithi;
	public Date date;
}

class CalendarUtil {

	public static final String dateFmtSunrisesunsetOrg = "dd/MMM/yyyy hh:mm:ss aa";
	public static final String dateAndTimeFormat = "dd/MM/yyyy HH:mm";
	public static final String dateFormat = "dd/MM/yyyy";
	public static final String timeFormat = "HH:mm";
	public static final String displayDateFormat = "EEEEE dd/MMM/yyyy HH:mm";
	public static final String sunRiseSetTimes = "F:\\FromD\\Astrology\\PakshiJamam\\FileDump\\SunriseTimes\\";
	public static final String filePath = "F:\\FromD\\Astrology\\PakshiJamam\\FileDump\\";
	public static final int numDaysOfWeek = 7;
	public static final int numJamams = 5;
	public static final String currentYear = "2022";

	public static int getMonthNumber(String month) {
		int d = -1;
		switch(month.toUpperCase()) {
			case "JAN":
				d = 1;
				break;
			case "FEB":
				d = 2;
				break;
			case "MAR":
				d = 3;
				break;
			case "APR":
				d = 4;
				break;
			case "MAY":
				d = 5;
				break;
			case "JUN":
				d = 6;
				break;
			case "JUL":
				d = 7;
				break;
			case "AUG":
				d = 8;
				break;
			case "SEP":
				d = 9;
				break;
			case "OCT":
				d = 10;
				break;
			case "NOV":
				d = 11;
				break;
			case "DEC":
				d = 12;
				break;
		}
		return d;
	}
	
	public static String getNameOfDay(int day) {
		String d = "INVALID_DAY";
		switch(day) {
			case Calendar.SUNDAY:
				d = "SUNDAY";
				break;
			case Calendar.MONDAY:
				d = "MONDAY";
				break;
			case Calendar.TUESDAY:
				d = "TUESDAY";
				break;
			case Calendar.WEDNESDAY:
				d = "WEDNESDAY";
				break;
			case Calendar.THURSDAY:
				d = "THURSDAY";
				break;
			case Calendar.FRIDAY:
				d = "FRIDAY";
				break;
			case Calendar.SATURDAY:
				d = "SATURDAY";
				break;
		}
		return d;
	}

	public static boolean isLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return (cal.getActualMaximum(Calendar.DATE) == cal.get(Calendar.DATE));
	}

	public static String getOppositePaksh(String paksh) {
		if (paksh.equalsIgnoreCase("K")) {
			return "S";
		} else if (paksh.equalsIgnoreCase("S")) {
			return "K";
		} else {
			return "ERROR";
		}
	}
}

class RuleDeathDetail {
	public RuleDeathDetail(Paksh paksh, int dayOfWeek, HinduDayNight dayOrNight, RuleOrDeath ruleOrDeath) {
		this.paksh = paksh;
		this.dayOfWeek = dayOfWeek;
		this.dayOrNight = dayOrNight;
		this.ruleOrDeath = ruleOrDeath;
	}
	public Paksh getPaksh() {return paksh;}
	public int getDayOfWeek() {return dayOfWeek;}
	public HinduDayNight getHinduDayNight() {return dayOrNight;}
	public RuleOrDeath getRuleOrDeath() {return ruleOrDeath;}
	public String toString() {
		return this.paksh + " " + CalendarUtil.getNameOfDay(this.dayOfWeek) + " " + this.dayOrNight + " " + this.ruleOrDeath;
	}
	
	private final Paksh paksh;
	private final int dayOfWeek;
	private final HinduDayNight dayOrNight;
	private final RuleOrDeath ruleOrDeath;
}

class PakshiBehavior {
	public PakshiBehavior(List<RuleDeathDetail> rulingDeathDays, Boolean[][] jamams) {
		this.rulingDeathDays = rulingDeathDays;
		this.jamams = jamams;
	}
	
	public List<DailyJamams> getGoodJamams(Paksh paksh, final Date sunrise, final Date sunset, final Date pakshToggler) {
		List<DailyJamams> goodJamams = new LinkedList<DailyJamams>();
		
		Calendar sunRiseCal = Calendar.getInstance();
		sunRiseCal.setTime(sunrise);
		
		Calendar sunSetCal = Calendar.getInstance();
		sunSetCal.setTime(sunset);
		
		Calendar approxNextDaySunrise = Calendar.getInstance();
		approxNextDaySunrise.setTime(sunrise);
		approxNextDaySunrise.add(Calendar.DATE, 1);
		
		final long dayLength = sunSetCal.getTimeInMillis() - sunRiseCal.getTimeInMillis();
		final long dayJamam = dayLength / CalendarUtil.numJamams;
		
		final long nightLength = approxNextDaySunrise.getTimeInMillis() - sunSetCal.getTimeInMillis();
		final long nightJamam = nightLength / CalendarUtil.numJamams;
		
		final int dayOfWeek = sunRiseCal.get(Calendar.DAY_OF_WEEK);
		int dayOfWeekIdx = paksh.equals(Paksh.SHUKLA) ? dayOfWeek - 1 : dayOfWeek + CalendarUtil.numDaysOfWeek - 1 ;
		
		
		//CALCULATE THE JAMAMS FOR THE DAY
		Boolean[] jamamsForDay = this.jamams[dayOfWeekIdx];
		
		List<Date> jamams = new LinkedList();
		for (int j = 0 ; j < CalendarUtil.numJamams; j++) {
			long jamamBegin = sunRiseCal.getTimeInMillis();
			int jamamIdx = (jamamsForDay[j] == true) ? j : -1;
			if (jamamIdx >= 0) {
				jamamBegin += (jamamIdx * dayJamam);
				
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(jamamBegin);
				Date d = c.getTime();
				
				if (pakshToggler != null && (sunrise.equals(pakshToggler) || sunrise.before(pakshToggler)) && 
						pakshToggler.before(sunset) && (d.equals(pakshToggler) || d.after(pakshToggler))) {
					break;/*Rest of the jamams are invalid*/
				}
				jamams.add(d);
			}
		}
		
		if (!jamams.isEmpty()) {
			RuleOrDeath dayType = RuleOrDeath.NEUTRAL;
			for (RuleDeathDetail ruleDeathDetail : this.rulingDeathDays) {
				if (ruleDeathDetail.getPaksh().equals(paksh) && 
					ruleDeathDetail.getDayOfWeek() == dayOfWeek && 
					ruleDeathDetail.getHinduDayNight().equals(HinduDayNight.DAY)) {
					
					dayType = ruleDeathDetail.getRuleOrDeath();
				}
			}
			goodJamams.add(new DailyJamams(jamams, dayType));
		}
		
		//Toggle the Paksh because it changed during the day.
		if (pakshToggler != null && pakshToggler.before(sunset)) {
			paksh = paksh.equals(Paksh.SHUKLA) ? Paksh.KRISHNA : Paksh.SHUKLA;
			dayOfWeekIdx = paksh.equals(Paksh.SHUKLA) ? dayOfWeek - 1 : dayOfWeek + CalendarUtil.numDaysOfWeek - 1 ;
			jamamsForDay = this.jamams[dayOfWeekIdx];
		}
		
		//CALCULATE THE JAMAMS FOR THE NIGHT
		jamams = new LinkedList();
		for  (int j = CalendarUtil.numJamams; j < CalendarUtil.numJamams * 2; j++) {
			long jamamBegin = sunSetCal.getTimeInMillis();
			int jamamIdx = (jamamsForDay[j] == true) ? j % CalendarUtil.numJamams : -1;
			if (jamamIdx >= 0) {
				jamamBegin += (jamamIdx * nightJamam);
				
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(jamamBegin);
				Date d = c.getTime();
				
				if (pakshToggler != null && (sunset.equals(pakshToggler) || sunset.before(pakshToggler)) 
						&& pakshToggler.before(approxNextDaySunrise.getTime()) && (d.equals(pakshToggler) || d.after(pakshToggler))) {
					break;/*Rest of the jamams are invalid*/
				}
				
				jamams.add(d);
			}
		}
		
		if (!jamams.isEmpty()) {
			RuleOrDeath nightType = RuleOrDeath.NEUTRAL;
			for (RuleDeathDetail ruleDeathDetail : this.rulingDeathDays) {
				if (ruleDeathDetail.getPaksh().equals(paksh) && 
					ruleDeathDetail.getDayOfWeek() == dayOfWeek && 
					ruleDeathDetail.getHinduDayNight().equals(HinduDayNight.NIGHT)) {
					
					nightType = ruleDeathDetail.getRuleOrDeath();
				}
			}
			goodJamams.add(new DailyJamams(jamams, nightType));
		}
		
		return goodJamams;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("RULING AND DEATH DAYS\n\n");
		for (RuleDeathDetail ruleDeathDetail : this.rulingDeathDays) {
			sb.append(ruleDeathDetail.toString()).append("\n");
		}
		sb.append("\n");
	
		sb.append("FAVORABLE JAMAMs\n");
		Paksh paksh = Paksh.SHUKLA;
		String dayOfWeek = null;
		for (int i = 0 ; i < this.jamams.length; i++) {
			if (i >= CalendarUtil.numDaysOfWeek) {
				 paksh = Paksh.KRISHNA;
			}
			sb.append(paksh).append(" - ");
			
			int weekdayIdx = (i % CalendarUtil.numDaysOfWeek) + 1;
			dayOfWeek = CalendarUtil.getNameOfDay(weekdayIdx);
			
			sb.append(HinduDayNight.DAY).append(" ");
			for (int j = 0 ; j < CalendarUtil.numJamams; j++) {
				String jamamIdx = (this.jamams[i][j] == true) ? new Integer(j+1).toString() : "";
				sb.append(jamamIdx).append(" ");
			}
			
			sb.append(" ").append(HinduDayNight.NIGHT).append(" ");
			for  (int j = CalendarUtil.numJamams; j < CalendarUtil.numJamams * 2; j++) {
				String jamamIdx = (this.jamams[i][j] == true) ? new Integer(j % CalendarUtil.numJamams + 1).toString() : "";
				sb.append(jamamIdx).append(" ");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	private final List<RuleDeathDetail> rulingDeathDays;
	private final Boolean[][] jamams;
}

class GMTDiff {
    public GMTDiff(String plusMinus, String diff) {
        this.plusMinus = plusMinus;
        this.diff = diff;
    }
    String plusMinus;
    String diff;
}

class DayWithSunRiseSet {
	public DayWithSunRiseSet(String date, String sunRise, String sunSet, String shuklaKrishna, String pakshToggleTime) {
		this.date = date;
		this.sunRise = sunRise;
		this.sunSet = sunSet;
		this.shuklaKrishna = shuklaKrishna;
		this.pakshToggleTime = pakshToggleTime;
	}
	@Override
	public String toString() {
		return this.date + " " + this.sunRise + " " + this.sunSet + " " + this.shuklaKrishna + " " + this.pakshToggleTime;
	}

	String date;
	String sunRise;
	String sunSet;
	String shuklaKrishna;
	String pakshToggleTime;
}

public class PakshiJamam {
	
	public PakshiJamam() {

	    this.timeZones.put("IST", new GMTDiff("+", "05:30"));
	    this.timeZones.put("PDT", new GMTDiff("-", "07:00"));
        this.timeZones.put("PST", new GMTDiff("-", "08:00"));
        this.timeZones.put("AEST", new GMTDiff("+", "09:00"));
        this.timeZones.put("AEDT", new GMTDiff("+", "10:00"));

		
		//FALCON
		List<RuleDeathDetail> FalconRuleDeathList = new LinkedList<RuleDeathDetail>();	
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.THURSDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.THURSDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.SATURDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.SATURDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.SUNDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.TUESDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.FRIDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));

		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.TUESDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.FRIDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.SUNDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		FalconRuleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.TUESDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		
		Boolean[][] FalconJamams = new Boolean[][] {
			//Shukla Paksha
			{/*Day*/true, false, true, false, false,/*Night*/false, false, false, true, true},
			{/*Day*/false, true, false, true, false,/*Night*/false, false, true, true, false},
			{/*Day*/true, false, true, false, false,/*Night*/false, false, false, true, true},
			{/*Day*/false, true, false, true, false,/*Night*/false, false, true, true, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, true, true, false, false},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			{/*Day*/false, true, false, false, true,/*Night*/true, false, false, false, true},
			
			//Krishna Paksha
			{/*Day*/false, true, false, false, true,/*Night*/true, false, false, false, true},
			{/*Day*/false, true, false, true, false,/*Night*/false, true, true, false, false},
			{/*Day*/false, true, false, false, true,/*Night*/true, false, false, false, true},
			{/*Day*/false, false, true, false, true,/*Night*/false, false, false, true, true},
			{/*Day*/true, false, true, false, false,/*Night*/false, false, true, true, false},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			{/*Day*/false, true, false, true, false,/*Night*/false, true, true, false, false},
			}; 
		
		this.pakshiBehavior.put(Pakshi.FALCON, new PakshiBehavior(FalconRuleDeathList, FalconJamams));
		
		//OWL
		List<RuleDeathDetail> ruleDeathList = new LinkedList<RuleDeathDetail>();
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.SUNDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.SUNDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.FRIDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.FRIDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));

		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.MONDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.WEDNESDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.SATURDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));

		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.MONDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.MONDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.THURSDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.WEDNESDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		
		Boolean[][] jamams = new Boolean[][] {
			//Shukla Paksha
			{/*Day*/false, true, false, false, true,/*Night*/true, false, false, false, true},
			{/*Day*/true, false, true, false, false,/*Night*/false, false, false, true, true},
			{/*Day*/false, true, false, false, true,/*Night*/true, false, false, false, true},
			{/*Day*/true, false, true, false, false,/*Night*/false, false, false, true, true},
			{/*Day*/false, true, false, true, false,/*Night*/false, false, true, true, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, true, true, false, false},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			
			//Krishna Paksha
			{/*Day*/false, false, true, false, true,/*Night*/true,  true, false, false, false},
			{/*Day*/false, true, false, false, true,/*Night*/false, false, true, true, false},
			{/*Day*/false, false, true, false, true,/*Night*/true, true, false, false, false},
			{/*Day*/true, false, true, false, false,/*Night*/true, false, false, false, true},
			{/*Day*/true, false, false, true, false,/*Night*/false, false, false, true, true},
			{/*Day*/false, true, false, true, false,/*Night*/false, true, true, false, false},
			{/*Day*/false, true, false, false, true,/*Night*/false, false, true, true, false},
			};
			
		this.pakshiBehavior.put(Pakshi.OWL, new PakshiBehavior(ruleDeathList, jamams));

		
		//CROW
		List<RuleDeathDetail> CrowRuleDeathList = new LinkedList<RuleDeathDetail>();
		CrowRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.MONDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		CrowRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.MONDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		CrowRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.THURSDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		CrowRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.TUESDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		CrowRuleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.SUNDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		
		CrowRuleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.SUNDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		CrowRuleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.SUNDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		CrowRuleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.WEDNESDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		CrowRuleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.THURSDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		
		Boolean[][] CrowJamams = new Boolean[][] {
			//Shukla Paksha
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			{/*Day*/false, true, false, false, true,/*Night*/true, false, false, false, true},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			{/*Day*/false, true, false, false, true,/*Night*/true, false, false, false, true},
			{/*Day*/true, false, true, false, false,/*Night*/false, false, false, true, true},
			{/*Day*/false, true, false, true, false,/*Night*/false, false, true, true, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, true, true, false, false},
			
			//Krishna Paksha
			{/*Day*/true, false, true, false, false,/*Night*/false, true, true, false, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, false, false, true, true},
			{/*Day*/true, false, true, false, false,/*Night*/false, true, true, false, false},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			{/*Day*/false, true, false, true, false,/*Night*/true, false, false, false, true},
			{/*Day*/false, true, false, false, true,/*Night*/false, false, true, true, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, false, false, true, true},
									}; 
		
		this.pakshiBehavior.put(Pakshi.CROW, new PakshiBehavior(CrowRuleDeathList, CrowJamams));
		
		//COCK
		ruleDeathList = new LinkedList<RuleDeathDetail>();
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.TUESDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.TUESDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.FRIDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.MONDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.WEDNESDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.THURSDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.THURSDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.SATURDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.SUNDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.TUESDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.MONDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.SATURDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		
		
		jamams = new Boolean[][] {
			//Shukla Paksha
			{/*Day*/false, false, true, false, true,/*Night*/false, true, true, false, false},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, true, true, false, false},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			{/*Day*/false, true, false, false, true,/*Night*/true, false, false, false, true},
			{/*Day*/true, false, true, false, false,/*Night*/false, false, false, true, true},
			{/*Day*/false, true, false, true, false,/*Night*/false, false, true, true, false},
			
			//Krishna Paksha
			{/*Day*/true, false, false, true, false,/*Night*/false, false, true, true, false},
			{/*Day*/true, false, true, false, false,/*Night*/true, false, false, false, true},
			{/*Day*/true, false, false, true, false,/*Night*/false, false, true, true, false},
			{/*Day*/false, true, false, true, false,/*Night*/false, true, true, false, false},
			{/*Day*/false, true, false, false, true,/*Night*/true, true, false, false, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, false, false, true, true},
			{/*Day*/true, false, true, false, false,/*Night*/true, false, false, false, true},
			};
			
		this.pakshiBehavior.put(Pakshi.COCK, new PakshiBehavior(ruleDeathList, jamams));

		//PEACOCK
		ruleDeathList = new LinkedList<RuleDeathDetail>();
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.WEDNESDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.WEDNESDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.SATURDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.SHUKLA, Calendar.THURSDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.WEDNESDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.WEDNESDAY, HinduDayNight.NIGHT, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.FRIDAY, HinduDayNight.DAY, RuleOrDeath.DEATH));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.MONDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.SATURDAY, HinduDayNight.DAY, RuleOrDeath.RULE));
		ruleDeathList.add(new RuleDeathDetail(Paksh.KRISHNA, Calendar.FRIDAY, HinduDayNight.NIGHT, RuleOrDeath.RULE));
		
		jamams = new Boolean[][] {
			//Shukla Paksha
			{/*Day*/false, true, false, true, false,/*Night*/false, false, true, true, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, true, true, false, false},
			{/*Day*/false, true, false, true, false,/*Night*/false, false, true, true, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, true, true, false, false},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			{/*Day*/false, true, false, false, true,/*Night*/true, false, false, false, true},
			{/*Day*/true, false, true, false, false,/*Night*/false, false, false, true, true},
			
			//Krishna Paksha
			{/*Day*/false, true, false, true, false,/*Night*/false, false, false, true, true},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			{/*Day*/false, true, false, true, false,/*Night*/false, false, false, true, true},
			{/*Day*/false, true, false, false, true,/*Night*/false, false, true, true, false},
			{/*Day*/false, false, true, false, true,/*Night*/false, true, true, false, false},
			{/*Day*/true, false, true, false, false,/*Night*/true, false, false, false, true},
			{/*Day*/true, false, false, true, false,/*Night*/true, true, false, false, false},
			};
			
		this.pakshiBehavior.put(Pakshi.PEACOCK, new PakshiBehavior(ruleDeathList, jamams));
	}
	
	public List<DailyJamams> calculateGoodJamams(Pakshi pakshiName) {
		List<DailyJamams> allDJ = new LinkedList();
		SimpleDateFormat sdf = new SimpleDateFormat(CalendarUtil.dateAndTimeFormat);
		StringBuilder sb = new StringBuilder();
				
		try {
			for (String[] dateSunRiseSetPaksh : this.datesSunRiseSetPaksh) {
				String sunriseDateTimeStr = dateSunRiseSetPaksh[0] + " " + dateSunRiseSetPaksh[1];
				String sunsetDateTimeStr = dateSunRiseSetPaksh[0] + " " + dateSunRiseSetPaksh[2];
				
				Paksh paksh = dateSunRiseSetPaksh[3].equalsIgnoreCase("S") ? Paksh.SHUKLA : Paksh.KRISHNA;
				
				Date pakshToggler = dateSunRiseSetPaksh[4].equalsIgnoreCase("") ? null : sdf.parse(dateSunRiseSetPaksh[4]); 
				
				Date sunriseDateTime = sdf.parse(sunriseDateTimeStr);	
				Date sunsetDateTime = sdf.parse(sunsetDateTimeStr);
				
				PakshiBehavior pakshiBehavior = this.pakshiBehavior.get(pakshiName);
				allDJ.addAll(pakshiBehavior.getGoodJamams(paksh, sunriseDateTime, sunsetDateTime, pakshToggler));
			}
			writeToFile(pakshiName.toString(), allDJ);
		} catch (Exception e) {
			System.out.println("Error parsing date.");
		    e.printStackTrace();
		}
		return allDJ;
	}
	
	private void writeToFile(String pakshiName, List<DailyJamams> djList) {
		StringBuilder sb = new StringBuilder();
		sb.append("************Pakshi Jamam for auspicious activity - " + pakshiName).append(" ************").append("\n");
		sb.append("****RULING DAY - Most auspicious to start****").append("\n");
		sb.append("****BAD DAY - Avoid starting any NEW work this day****").append("\n");
		sb.append("****You must start the activity within 5 minutes of the time mentioned****").append("\n\n");
		sb.append("Note that we don't consider Rahu Kala, Yamaganda for Pakshi calculations. ").append("\n")
		.append("If you want to exclude these muhuraths by applying Rahu Kala, Yamaganda, use a standard panchang ").append("\n")
		.append(" like https://www.drikpanchang.com/panchang/month-panchang.html")
		.append("\n\n");
		
		for (DailyJamams dj : djList) {
			sb.append(dj.toString()).append("\n");
		}
		createFile(pakshiName, sb.toString());
	}
	
	private final HashMap<Pakshi, PakshiBehavior> pakshiBehavior = new HashMap<Pakshi, PakshiBehavior>();
    private final Map<String, GMTDiff> timeZones = new LinkedHashMap<String, GMTDiff>();


    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<Pakshi> keys = pakshiBehavior.keySet();
		for (Pakshi key : keys) {
			PakshiBehavior behavior = pakshiBehavior.get(key);
			sb.append("\n\n           ").append(key).append("\n");
			sb.append(behavior.toString());
		}
		return sb.toString();
	}
	
	private void createFile(String fileName, String content) {
		try {		
	        FileWriter myWriter = new FileWriter(CalendarUtil.filePath + "\\" + fileName + ".txt");			  
			myWriter.write(content);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (Exception e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}
	
	public void testFunction() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(CalendarUtil.dateAndTimeFormat);
			Date dt = sdf.parse("09/06/2020 06:34");
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			
			long millis = c.getTimeInMillis();
			millis += (1000 * 60 * 5);
			
			c.setTimeInMillis(millis);
			Date nd = c.getTime();
			
			String dtsrt = sdf.format(nd);
			
			System.out.println(nd.toString());
			System.out.println(dtsrt);
			
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		return;
	}

	//private LinkedList
	private String[][] datesSunRiseSetPaksh = {
			//Date, Sunrise, Sunset, Paksham, Amavasya/Poornima toggles
			//{"dd/MM/yyyy", "hh:mm", "hh:mm", "(K|S)", "dd/MM/yyyy HH:mm"}

			/* Bengalore */
			{"1/05/2022", "5:9", "18:35", "S", ""},
			{"2/05/2022", "5:59", "18:35", "S", ""},
			{"03/05/2022", "05:58", "18:35", "S", ""},
			{"04/05/2022", "05:58", "18:35", "S", ""},
			{"05/05/2022", "05:57", "18:35", "S", ""},
			{"06/05/2022", "05:57", "18:36", "S", ""},
			{"07/05/2022", "05:57", "18:36", "S", ""},
			{"08/05/2022", "05:56", "18:36", "S", ""},
			{"09/05/2022", "05:56", "18:36", "S", ""},
			{"10/05/2022", "05:56", "18:37", "S", ""},
			{"11/05/2022", "05:55", "18:37", "S", ""},
			{"12/05/2022", "05:55", "18:37", "S", ""},
			{"13/05/2022", "05:55", "18:37", "S", ""},
			{"14/05/2022", "05:55", "18:37", "S", ""},
			{"15/05/2022", "05:54", "18:38", "S", ""},
			{"16/05/2022", "05:54", "18:38", "S", "16/05/2022 10:00"},
			{"17/05/2022", "05:54", "18:38", "K", ""},
			{"18/05/2022", "05:54", "18:39", "K", ""},
			{"19/05/2022", "05:54", "18:39", "K", ""},
			{"20/05/2022", "05:53", "18:39", "K", ""},
			{"21/05/2022", "05:53", "18:39", "K", ""},
			{"22/05/2022", "05:53", "18:40", "K", ""},
			{"23/05/2022", "05:53", "18:40", "K", ""},
			{"24/05/2022", "05:53", "18:40", "K", ""},
			{"25/05/2022", "05:53", "18:41", "K", ""},
			{"26/05/2022", "05:53", "18:41", "K", ""},
			{"27/05/2022", "05:53", "18:41", "K", ""},
			{"28/05/2022", "05:52", "18:41", "K", ""},
            {"29/05/2022", "05:52", "18:42", "K", ""},
            {"30/05/2022", "05:52", "18:42", "K", "30/05/2022 15:47"},
            {"31/05/2022", "05:52", "18:42", "S", ""}

	};

	private void generateSunRiseSetTimes() {
		try {

			List<PoornimaAmavasyaGMT> poornimaAndAmavasyaGMTList = new LinkedList<>();
			// MODIFY THIS FOR POORNIMA AND AMAVASYA END TIMES IN CURRENT MONTH
			poornimaAndAmavasyaGMTList.add(new PoornimaAmavasyaGMT("P", "01/05/2022 10:00"));
			poornimaAndAmavasyaGMTList.add(new PoornimaAmavasyaGMT("A", "15/05/2022 10:00"));
			poornimaAndAmavasyaGMTList.add(new PoornimaAmavasyaGMT("P", "30/05/2022 10:00"));
			// ENDS

			List<DayWithSunRiseSet> currentMonthSunRiseSet = new LinkedList<DayWithSunRiseSet>();

			// pass the path to the file as a parameter
			File file = new File(CalendarUtil.sunRiseSetTimes + "\\" + "sunriseset.txt");
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				currentMonthSunRiseSet.add(this.getDateAndTimes(line));
			}

			SimpleDateFormat dF = new SimpleDateFormat(CalendarUtil.dateFormat);
			SimpleDateFormat datTimeF = new SimpleDateFormat(CalendarUtil.dateAndTimeFormat);
			PoornimaAmavasyaGMT poornimaAmavasyaGMT = poornimaAndAmavasyaGMTList.remove(poornimaAndAmavasyaGMTList.size() - 1);


			ListIterator<DayWithSunRiseSet> currentMonth = currentMonthSunRiseSet.listIterator(currentMonthSunRiseSet.size());

			while (currentMonth.hasPrevious()) {
				DayWithSunRiseSet dayWithSunRiseSet = currentMonth.previous();

				//poornimaAmavasyaGMT.date
				//poornimaAmavasyaGMT.thithi

				Date sunRiseTime = datTimeF.parse(dayWithSunRiseSet.date + " " + dayWithSunRiseSet.sunRise);
				if (poornimaAmavasyaGMT.date.after(sunRiseTime)) {
					dayWithSunRiseSet.pakshToggleTime = datTimeF.format(poornimaAmavasyaGMT.date);
				}
				dayWithSunRiseSet.shuklaKrishna = CalendarUtil.getOppositePaksh(poornimaAmavasyaGMT.thithi);

				/*
				dayWithSunRiseSet.date;
				dayWithSunRiseSet.sunRise;
				dayWithSunRiseSet.sunSet;
				dayWithSunRiseSet.shuklaKrishna;
				dayWithSunRiseSet.pakshToggleTime;
				*/

			}

			//ListIterator<PoornimaAmavasyaGMT> itr = CalendarUtil.poornimaAndAmavasyaGMTList.listIterator(CalendarUtil.poornimaAndAmavasyaGMTList.size());
			//itr.hasPrevious()

			/*
			for (PoornimaAmavasyaGMT pa : CalendarUtil.poornimaAndAmavasyaGMTList) {
				String paksham = pa.thithi;
				Date date = pa.date;

				for (DayWithSunRiseSet dayWithSunRiseSet : currentMonthSunRiseSet) {
					dayWithSunRiseSet.date;
					dayWithSunRiseSet.pakshToggleTime;
					dayWithSunRiseSet.sunRise;
					dayWithSunRiseSet.sunSet;
					dayWithSunRiseSet.shuklaKrishna;


				}

			}*/




		} catch (Exception ex) {
			System.out.println(ex);
		}

    }

    private DayWithSunRiseSet getDateAndTimes(String row) {
		try {
			String date = "\\w{3},\\s\\w{3}\\s\\d{1,2}";
			String sunrise = "\\d{1,2}:\\d{1,2}:\\d{1,2}\\sam";
			String sunset = "\\d{1,2}:\\d{1,2}:\\d{1,2}\\spm";

			String dateStr = null;
			String riseTime = null;
			String setTime = null;

			StringBuilder sb = new StringBuilder();

			Pattern pattern = Pattern.compile(date);
			Matcher matcher = pattern.matcher(row);
			if (matcher.find()) {
				dateStr = matcher.group();
			}
			if (dateStr != null) {
				String[] sl = dateStr.split(" ");
				sb.append(sl[2]).append("/").append(sl[1]).append("/").append(CalendarUtil.currentYear);
				dateStr = sb.toString();
			}

			pattern = Pattern.compile(sunrise);
			matcher = pattern.matcher(row);
			if (matcher.find()) {
				if (matcher.find()) {
					riseTime = matcher.group();
				}
			}

			pattern = Pattern.compile(sunset);
			matcher = pattern.matcher(row);
			if (matcher.find()) {
				setTime = matcher.group();
			}
			riseTime = dateStr + " " + riseTime;
			setTime = dateStr + " " + setTime;

			SimpleDateFormat ssorgDf = new SimpleDateFormat(CalendarUtil.dateFmtSunrisesunsetOrg);
			Date sunRise = ssorgDf.parse(riseTime);
			Date sunSet = ssorgDf.parse(setTime);

			SimpleDateFormat dF = new SimpleDateFormat(CalendarUtil.dateFormat);
			SimpleDateFormat tF = new SimpleDateFormat(CalendarUtil.timeFormat);

			return new DayWithSunRiseSet(dF.format(sunRise).toString(), tF.format(sunRise).toString(), tF.format(sunSet), "", "");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void test() {
		try {
			String d1 = "21/Jul/2022 3:56:00 pm";
			SimpleDateFormat sdf = new SimpleDateFormat(CalendarUtil.dateFmtSunrisesunsetOrg);
			Date d = sdf.parse(d1);

			SimpleDateFormat sdf2 = new SimpleDateFormat(CalendarUtil.displayDateFormat);
			System.out.println("HERE --- " + sdf2.format(d));


			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			System.out.println(cal.getActualMaximum(Calendar.DATE));
			System.out.println(cal.get(Calendar.DATE));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args){
		try {

		} catch(Exception ex) {
			System.out.println();
		}

		PakshiJamam pj = new PakshiJamam();
		pj.test();
		//pj.generateSunRiseSetTimes();
		pj.calculateGoodJamams(Pakshi.FALCON);
		pj.calculateGoodJamams(Pakshi.CROW);
		pj.calculateGoodJamams(Pakshi.COCK);
		pj.calculateGoodJamams(Pakshi.PEACOCK);
		pj.calculateGoodJamams(Pakshi.OWL);
	}

}

