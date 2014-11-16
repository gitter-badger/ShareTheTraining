package models.courses;

public enum CourseStatus {
	VERIFYING, APPROVED, COMPLETED, CANCELLED;
	
	public static CourseStatus fromInteger(int x) {
        switch(x) {
        case 0:
            return VERIFYING;
        case 1:
            return APPROVED;
        case 2:
        	return COMPLETED;
        case 3:
        	return CANCELLED;
        }
        return null;
    }
}
