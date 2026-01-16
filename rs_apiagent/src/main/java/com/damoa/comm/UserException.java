package com.damoa.comm;

/**
 * <p>Title: AegisAtmClient  </p>
 * <p>Description: Aegis Cash Management System</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: AegisHyosung </p>
 * @author KSS
 * @version 1.0
 */

public class UserException extends Exception {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7622216498406053610L;

	public UserException() {
    	super();
    }

    public UserException(String s) {
        super(s);
    }
}
