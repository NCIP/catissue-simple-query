/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-simple-query/LICENSE.txt for details.
 */

/**
 * <p>Title: QueryDataCache Class>
 * <p>Description:  This Class is used to cache all the data (table names, table display names,
 *  column names, column display name etc.) required for query modules.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Nov 22, 2005
 */

package edu.wustl.simplequery.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * This Class is used to cache all the data (table names, table display names,
 * column names, column display name etc.) required for query modules.
 * @author aniruddha_phadnis
 */
public class QueryDataCache
{

	/**
	 * Returns a map of all query data.
	 * @return a map of all query data.
	 */
	public static Map getQueryData() throws DAOException
	{
		String appName=CommonServiceLocator.getInstance().getAppName();
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
		DAO dao = daofactory.getDAO();
		

		dao.openSession(null);
		List list = dao.retrieve(QueryTableData.class.getName());
		dao.closeSession();

		HashMap tableMap = new HashMap();

		for (int i = 0; i < list.size(); i++)
		{
			QueryTableData tableData = (QueryTableData) list.get(i);
			tableMap.put(tableData.getAliasName(), tableData);
		}
		return tableMap;
	}
}