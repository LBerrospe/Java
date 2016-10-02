
public class MyDate {
	
	private final int JANUARY = 1;
	private final int FEBRUARY = 2;
	private final int MARCH = 3;
	private final int APRIL = 4;
	private final int MAY = 5;
	private final int JUNE = 6;
	private final int JULY = 7;
	private final int AUGUST = 8;
	private final int SEPTEMBER = 9;
	private final int OCTOBER = 10;
	private final int NOVEMBER = 11;
	private final int DECEMBER = 12;
	
	private final short HUNDRED=100,FOUR_HUNDRED=400;
	
	private int day;
	private int month;
	private int year;
	
	
	public MyDate() { //Constructor. Initialize variables
		day = 1;
		month = 1;
		year = 0;
	}//MiFecha
	
	private boolean checkDate(int day, int month, int year) {
		switch(month) {
	
		case JANUARY:case MARCH:case MAY:case JULY:case AUGUST:case OCTOBER:case DECEMBER: //monthes con 31 days
			return daysMonth(day,1,31);
				
		case APRIL:case JUNE:case SEPTEMBER:case NOVEMBER: //months with 30 days
			return daysMonth(day,1,30);
		
		case FEBRUARY: //FEBRUARY 28/29 days
			if(!(leapYear(year)) )
				return daysMonth(day,1,28);
			else 
				return daysMonth(day,1,29);
			
		default: return false;
		}//case(month)
	}//checkDate()
	
	private boolean leapYear(int year) {
		if( (year % 4 == 0) && ((year % HUNDRED != 0) || (year % FOUR_HUNDRED == 0)) )
			return true;
		else
			return false;
	}//leapYear
	
	private boolean daysMonth(int day, int initialDay, int dayFinal) {
		if( (day >= initialDay) && (day <= dayFinal) )
			return true;
		else
			return false;
	}//daysMonth
	
	
	public int getDay() {
		return day;
	}//getDay()
	
	public int getMonth() {
		return month;
	}//getMonth
	
	public int getYear() {
		return year;
	}//getYear
	
	public boolean setDay(int day) {
		if( checkDate(day, getMonth(), getYear()) ) {
			this.day = day;
			return true;
		}else
			return false;
	}//setDay()
	
	public boolean setMonth(int month) {
		if( checkDate(getDay(), month, getYear()) ) {
			this.month = month;
			return true;
		}else
			return false;
	}//setMonth()
	
	public boolean setYear(int year) {
		if( checkDate(getDay(), getMonth(), year) ){
			this.year = year;
			return true;
		}else
			return false;
	}//setYear
}//class MyDate
