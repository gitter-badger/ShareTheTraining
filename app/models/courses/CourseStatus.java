package models.courses;

public enum CourseStatus {
	VERIFYING, APPROVED, OPEN, CANCELLED, COMPLETED;
	
	public static CourseStatus fromInteger(int x) {
        switch(x) {
        case 0:
            return VERIFYING;
        case 1:
            return APPROVED;
        case 2:
        	return OPEN;
        case 3:
        	return CANCELLED;
        case 4:
        	return COMPLETED;
        }
        return null;
    }
}
