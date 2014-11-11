package models.courses;

public enum ConcreteCourseStatus {
	UNSTARTED,FINISHED,STARTED;
	
	public static ConcreteCourseStatus fromInteger(int x) {
        switch(x) {
        case 0:
            return UNSTARTED;
        case 1:
            return FINISHED;
        case 2:
            return STARTED;
        }
        return null;
    }
}