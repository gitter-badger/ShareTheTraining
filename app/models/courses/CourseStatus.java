package models.courses;

public enum CourseStatus {
	VERIFYING, APPROVED, OPEN, COMPLETED, CANCELLED;
	
	public static CourseStatus fromInteger(int x) {
        switch(x) {
        case 0:
            return VERIFYING;
        case 1:
            return APPROVED;
        case 2:
        	return OPEN;
        case 3:
        	return COMPLETED;
        case 4:
        	return CANCELLED;
        }
        return null;
    }
}
