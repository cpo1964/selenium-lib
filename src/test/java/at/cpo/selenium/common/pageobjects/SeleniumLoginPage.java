package at.cpo.selenium.common.pageobjects;

import at.cpo.selenium.SeleniumHelper;

/**
 * The Class SeleniumLoginPage.
 */
public class SeleniumLoginPage extends SeleniumHelper {

    /** The page. */
    protected static String PAGE = "SeleniumLoginPage.";

    /**
     * The constant USERNAME.
     */
    public static final String USERNAME = PAGE + "UsernameIN";

    /**
     * The constant PASSWORD.
     */
    public static final String PASSWORD = PAGE + "PasswordIN";
    /**
     * The constant SMSTAN.
     */

    /**
     * The constant Login.
     */
    public static final String LOGIN = PAGE + "LoginBT";

    /**
     * The constant Login.
     */
    public static final String NOTICE = PAGE + "NoticeBT";

    /**
     * The constant HOME.
     */
    public static final String HOME = PAGE + "HomeLN";

    /**
     * The constant SIGNININFO.
     */
    public static final String SIGNININFO = PAGE + "SigInInfoTXT";
    
    {
        PAGE = "DCBSeleniumLoginPage.";
    }

}
