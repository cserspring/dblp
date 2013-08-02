package edu.whu.wang.setup;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class IndexEnvironment {

	private final String envPath;
	private Environment dbEnv;

	public IndexEnvironment(String envPath) {
		this.envPath = envPath;
	}
	
	public boolean openEnv() {
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setCachePercent(90);
		try {
			this.dbEnv = new Environment(new File(envPath), envConfig);
			return true;
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
			System.err.println("Error: opening index database environment");
			return false;
		}
	}

	public void closeEnv() {
		try {
			if (dbEnv != null) {
				dbEnv.close();
			}
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
			System.err.println("Error: closing index database environment");
		}
	}
	
	public Environment getEnvironment() {
		return dbEnv;
	}
}
