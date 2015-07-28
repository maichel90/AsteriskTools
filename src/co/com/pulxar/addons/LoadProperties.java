package co.com.pulxar.addons;

import java.util.Properties;

public  class  LoadProperties {
	
	private static String asteriskdirgsm;
	private static String asteriskdirwav;
	private static String asteriskuser;
    private static String asteriskpass;
    private static String asteriskhost;
    private static int asteriskport;
	
	public LoadProperties() {
		try {
			Properties properties = new Properties();
			properties.load(getClass().getResourceAsStream("/co/com/pulxar/addons/general.properties"));
			asteriskdirgsm = properties.getProperty("ASTERISKDIRGSM");
			asteriskdirwav = properties.getProperty("ASTERISKDIRWAV");
			asteriskuser = properties.getProperty("ASTERISKMANAGER");
            asteriskpass = properties.getProperty("ASTERISKPASSWORD");
            asteriskhost = properties.getProperty("ASTERISKHOST");
            asteriskport = Integer.parseInt(properties.getProperty("ASTERISKPORT"));
		}catch(Exception e) {
			System.out.println("-------> ERROR: LOADING THE FILE PROPERTIES -------");
		}
	}

	public String getAsteriskdirgsm() {
		return asteriskdirgsm;
	}

	public String getAsteriskdirwav() {
		return asteriskdirwav;
	}

	public static String getAsteriskuser() {
		return asteriskuser;
	}

	public static String getAsteriskpass() {
		return asteriskpass;
	}

	public static String getAsteriskhost() {
		return asteriskhost;
	}

	public static int getAsteriskport() {
		return asteriskport;
	}
}
