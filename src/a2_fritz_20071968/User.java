package a2_fritz_20071968;

public class User {
	private String firstName;
	private String lastName;
	private String studentNo;
	public User(String firstName, String lastName, String studentNo)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.studentNo = studentNo;
	}
	
	public String getFullName()
	{
		return this.firstName + " " + this.lastName;
	}
	
	public String getStudetNo()
	{
		return this.studentNo;
	}
	

}
