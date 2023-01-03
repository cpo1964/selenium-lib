package at.cpo.utils;

import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * The Class ResourceUtils.
 */
public class ResourceUtils {

	/** The Constant LOGGER. */
//	private static final Logger LOGGER = Logger.getLogger(ResourceUtils.class);

	/**
	 * Instantiates a new resource utils.
	 */
	private ResourceUtils() {
	}
	/**
	 * Gets the class location.
	 *
	 * @param c the c
	 * @return the class location
	 */
	public static URL getClassLocation(Class<?> c) {
		URL url = c.getResource(c.getSimpleName() + ".class");
		if (url == null) {
			return null;
		}
		String s = url.toExternalForm();
		// s most likely ends with a /, then the full class name with . replaced
		// with /, and .class. Cut that part off if present. If not also check
		// for backslashes instead. If that's also not present just return null
	
		String end = "/" + c.getName().replaceAll("\\.", "/") + ".class";
		if (s.endsWith(end)) {
			s = s.substring(0, s.length() - end.length());
		} else {
			end = end.replaceAll("/", "\\");
			if (s.endsWith(end)) {
				s = s.substring(0, s.length() - end.length());
			} else {
				return null;
			}
		}
		// s is now the URL of the location, but possibly with jar: in front and
		// a trailing !
		if (StringUtils.startsWith(s, "jar:") && s.endsWith("!")) {
			s = s.substring(4, s.length() - 1);
		}
		try {
			return new URL(s);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * Gets the resource property value by key.
	 *
	 * @param key
	 *            the key
	 * @return the resource property value by key
	 */
	public static String getResourcePropertyValueByKey(final Class<?> propHolder, String key) {
		String propertiesFileDestination = propHolder.getSimpleName() + ".properties";
		InputStream stream = propHolder.getResourceAsStream(propertiesFileDestination);
		if (stream == null) {
//			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//			InputStream stream = classloader.getResourceAsStream("test-data/myExcel.xlsx");
			URL url = propHolder.getClassLoader().getResource(propertiesFileDestination);
			if (url != null) {
				try {
					stream = url.openStream();
				} catch (IOException e1) {
					throw new UnsupportedOperationException("Can not find property file: " + propertiesFileDestination);
				}
			} else {
				throw new UnsupportedOperationException("Can not find property file: " + propertiesFileDestination);
			}
		}
		Properties prop = new Properties();
		try {
			prop.load(stream);
		} catch (IOException e) {
			throw new UnsupportedOperationException("Can not open property file: " + propertiesFileDestination);
		}

		String value = prop.getProperty(key);
		if (value == null || value.isEmpty()) {
//			LOGGER.error(
//					"No value found by key '" + key + "' from '" + propertiesFileDestination + "'");
			throw new IllegalArgumentException("Property \"" + key + "\" from file " + propertiesFileDestination
					+ " does not exists or is empty!");
		}
//		LOGGER.debug(
//				"Found value '" + (!key.equals("password") ? value : "*****") + "' by key '" + key
//						+ "' from file '" + propertiesFileDestination + "'");
		return value;
	}

	/**
	 * Gets the description from property.
	 *
	 * @param pageName the page name
	 * @param key the key
	 * @param isDesktop the is desktop
	 * @param clazz the clazz
	 * @return the description from property
	 */
	public static String getDescriptionFromProperty(String pageName, String key, boolean isDesktop , Class<?> clazz)  {
			if (!isDesktop) {
				key = key + ".mobile";
			}
			return ResourceUtils.getResourcePropertyValueByKey(clazz, key);
	}
}
