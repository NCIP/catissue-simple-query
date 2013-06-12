/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-simple-query/LICENSE.txt for details.
 */

package edu.wustl.simplequery.querysuite.factory;

import edu.wustl.common.util.Utility;

public class CommonObjectFactory
{

	/**
	 * Reference of common factory.
	 */
	private static CommonObjectFactory clientFactory;

	/**
	 * Default private constructor.
	 * Required for singleton implementation. 
	 */
	private CommonObjectFactory()
	{
	}

	/**
	 * Returns an instance of caB2BFactory.
	 * @return an instance of caB2BFactory.
	 */
	public static CommonObjectFactory getInstance()
	{
		if (clientFactory == null)
			clientFactory = new CommonObjectFactory();

		return clientFactory;
	}

	/**
	 * Returns the object of the class whose name is passed.
	 * @param className The class name.
	 * @return the object of the class whose name is passed.
	 */
	public Object getObjectofClass(String className)
	{
		return Utility.getObject(className);
	}
}
