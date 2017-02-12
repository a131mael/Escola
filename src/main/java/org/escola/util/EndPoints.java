package org.escola.util;

public class EndPoints {

	private static StringBuilder urlBase = new StringBuilder("http://localhost");
	//private static StringBuilder urlBase = new StringBuilder("http://ec2-52-67-36-232.sa-east-1.compute.amazonaws.com");
	
 	
	public static final String QUESTIONS_BY_DISCIPLINE = new StringBuilder(urlBase).append("/UI/rest/questions/discipline?discipline=*1&&year=*2").toString();
	
}
