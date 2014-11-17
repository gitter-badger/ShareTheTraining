package models.users;

public enum UserStatus {
	INACTIVE, ACTIVE;
	public static UserStatus fromInteger(int x) {
		switch (x) {
		case 0:
			return INACTIVE;
		case 1:
			return ACTIVE;
		}
		return null;
	}
}
