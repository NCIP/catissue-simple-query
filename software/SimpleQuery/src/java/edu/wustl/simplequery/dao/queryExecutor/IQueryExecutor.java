/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-simple-query/LICENSE.txt for details.
 */

package edu.wustl.simplequery.dao.queryExecutor;

import java.sql.Connection;
import java.util.Map;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.PagenatedResultData;
import edu.wustl.dao.exception.DAOException;


public interface IQueryExecutor 
{
	
	/**
	 * Constants required for forming/changing SQL.
	 */
	public static final String SELECT_CLAUSE = "SELECT";
	public static final String FROM_CLAUSE = "FROM";
	public IQueryExecutor getInstance();
	public PagenatedResultData getQueryResultList(String query,
			Connection connection, SessionDataBean sessionDataBean,
			boolean isSecureExecute, boolean hasConditionOnIdentifiedField,
			Map queryResultObjectDataMap, int startIndex, int noOfRecords) throws DAOException;
}
