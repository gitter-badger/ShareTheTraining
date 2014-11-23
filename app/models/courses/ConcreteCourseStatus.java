package models.courses;

public enum ConcreteCourseStatus {
	VERIFYING,APPROVED,STARTED,FINISHED;
	
	public static ConcreteCourseStatus fromInteger(int x) {
        switch(x) {
        case 0:
            return VERIFYING;
        case 1:
            return APPROVED;
        case 2:
            return FINISHED;
        }
        return null;
    }
}