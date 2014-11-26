package models.forms;

import play.data.validation.Constraints;

public class NewPswForm {
	@Constraints.Required
	private String oldpsw;
	@Constraints.Required
	private String newpsw;
	@Constraints.Required
	private String renewpass;
	
	public String validate() {

        if (isBlank(newpsw)) 
            return "Password is required";
        if (isBlank(renewpass)) 
            return "Reentering Password is required";
        if(!newpsw.equals(renewpass))
        	return "The two inputs should be the same";
        return null;
    }

    private boolean isBlank(String input) {
        return input == null || input.isEmpty() || input.trim().isEmpty();
    }
	
	public String getRenewpass() {
		return renewpass;
	}
	public void setRenewpass(String renewpass) {
		this.renewpass = renewpass;
	}
	public String getOldpsw() {
		return oldpsw;
	}
	public void setOldpsw(String oldpsw) {
		this.oldpsw = oldpsw;
	}
	public String getNewpsw() {
		return newpsw;
	}
	public void setNewpsw(String newpsw) {
		this.newpsw = newpsw;
	}
	
	public NewPswForm() {
		
	}
	
	
	
}
