package models.courses;

public enum CourseStatus {
	VERIFYING, APPROVED;
	
	public static CourseStatus fromInteger(int x) {
        switch(x) {
        case 0:
            return VERIFYING;
        case 1:
            return APPROVED;
        }
        return null;
    }
}
