/**
 * <p>Title: ResultData Class>
 * <p>Description:	ResultData is used to generate the data required for the result view of query interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.simplequery.query;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.simplequery.global.Constants;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * ResultData is used to generate the data required for the result view of query interface.
 * @author gautam_shetty
 */
public class ResultData
{

	/**
	 * Query results temporary table.
	 */
	private String tmpResultsTableName;

	public List getSpreadsheetViewData(String[] whereColName, String[] whereColValue,
			String[] whereColCondition, String[] columnList, SessionDataBean sessionDataBean,
			int securityParam)
	{

		String[] whereColumnName = whereColName;
		String[] whereColumnCondition = whereColCondition;
		 String[] whereColumnValue = whereColValue;
		
		
		tmpResultsTableName = Constants.QUERY_RESULTS_TABLE + "_" + sessionDataBean.getUserId();

		if (whereColumnName[0].equals(Constants.ROOT))
		{
		//	Logger.out.debug("inside root condition........." + whereColumnName[0]);
			//columnList = null;
			whereColumnName = null;
			whereColumnCondition = null;
			whereColumnValue = null;
		}
		/*if(whereColumnName.length==2)
			Logger.out.debug("arrayvalues:"+whereColumnName[0]+":"+whereColumnName[1]+":"+whereColumnValue[0]+":"+whereColumnValue[1]);*/
		List dataList = new ArrayList();
		//Bug#2003: For having unique records in result view
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory();
		JDBCDAO dao = null;
		try
		{
			dao = daofactory.getJDBCDAO();
			dao.openSession(sessionDataBean);
			boolean onlyDistinctRows = true;
		//	Logger.out.debug("Get only distinct rows:" + onlyDistinctRows);
			
			QueryWhereClause queryWhereClause = new QueryWhereClause(tmpResultsTableName);
			queryWhereClause.getWhereCondition(whereColumnName, whereColumnCondition,
					whereColumnValue,null);
			
			
			dataList = dao.retrieve(tmpResultsTableName, columnList, queryWhereClause, onlyDistinctRows);
			
		//	Logger.out.debug("List of spreadsheet data for advance search:" + dataList);

			//End of Bug#2003: For having unique records in result view
		}
		catch (DAOException sqlExp)
		{
		//	Logger.out.error(sqlExp.getMessage(), sqlExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				//Logger.out.error(e.getMessage(), e);
			}
		}

		return dataList;
	}
}