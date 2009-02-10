/**
 * <p>Description:	SimpleSearchAction takes the conditions
 * from the user, prepares,
 * executes the query and shows the result in a spreadsheet view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.common.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.bizlogic.IQueryBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.PagenatedResultData;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.QuerySessionData;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.simplequery.bizlogic.SimpleQueryBizLogic;
import edu.wustl.simplequery.global.Constants;
import edu.wustl.simplequery.query.DataElement;
import edu.wustl.simplequery.query.Query;
import edu.wustl.simplequery.query.QueryFactory;
import edu.wustl.simplequery.query.SimpleConditionsNode;
import edu.wustl.simplequery.query.SimpleQuery;
import edu.wustl.simplequery.query.Table;

/**
 * SimpleSearchAction takes the conditions from the user, prepares,
 * executes the query and shows the result in a spreadsheet view.
 * @author gautam_shetty
 */
public class SimpleSearchAction extends BaseAction
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SimpleSearchAction.class);

	/**
	 * @param mapping	ActionMapping
	 * @param form	ActionForm
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 * @return ActionForward
	 * @exception Exception Generic exception
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
		// -------- set the selected menu ------- start
		String strMenu = simpleQueryInterfaceForm.getMenuSelected();
		request.setAttribute(edu.wustl.common.util.global.Constants.MENU_SELECTED, strMenu);
		Logger.out.debug(edu.wustl.common.util.global.Constants.MENU_SELECTED
				+ " set in SimpleSearch Action : -- " + strMenu);
		// -------- set the selected menu ------- end
		HttpSession session = request.getSession();

		String target = edu.wustl.common.util.global.Constants.SUCCESS;
		Map map = simpleQueryInterfaceForm.getValuesMap();
		String counter = simpleQueryInterfaceForm.getCounter();
		//Bug 1096:Creating a copy of the orginal query object for redefining the query object
		Map originalQueryObject = (Map) session
				.getAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT);
		String originalCounter = (String) session
				.getAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER);
		//If map from form is null get the map values from session.
		if (map.size() == 0)
		{
			map = (Map) session.getAttribute(Constants.SIMPLE_QUERY_MAP);
			//Get the counter from the simple query map
			//if set during configure action in the session object
			counter = (String) map.get("counter");
			//After retrieving the value of counter, remove from the map
			map.remove("counter");
		}
		//Bug 1096:Set the simple query map attributes in a temp session object for redefining the query
		//Set the counter(number of rows in the UI of simple query formed) also in session,
		//used for redefining the query
		if (originalQueryObject == null && originalCounter == null)
		{
			if (map != null && counter != null)
			{
				session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT, new HashMap(map));
				session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER, new String(counter));
			}
		}

		session.setAttribute(Constants.SIMPLE_QUERY_MAP, map);
		Logger.out.debug("map after setting in session" + map);

		MapDataParser parser = new MapDataParser("edu.wustl.simplequery.query");
		Collection simpleConditionNodeCollection = parser.generateData(map, true);

		/**
		 * Bug-2778: Results should return at least the attribute that was queried.
		 *
		 */
		List fieldList = new ArrayList();
		if (simpleConditionNodeCollection != null && !simpleConditionNodeCollection.isEmpty())
		{
			Iterator itr = simpleConditionNodeCollection.iterator();
			while (itr.hasNext())
			{
				SimpleConditionsNode simpleConditionsNode = (SimpleConditionsNode) itr.next();
				Table table = simpleConditionsNode.getCondition().getDataElement().getTable();
				DataElement dataElement = simpleConditionsNode.getCondition().getDataElement();
				String field = table.getTableName() + "." + dataElement.getField();
				fieldList.add(field);
			}
		}

		Map queryResultObjectDataMap = new HashMap();

		SimpleQueryBizLogic simpleQueryBizLogic = new SimpleQueryBizLogic();

		// Get the alias name of the first object in the condition.
		String viewAliasName = (String) map
				.get("SimpleConditionsNode:1_Condition_DataElement_table");

		// Instantiating the query object.
		Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, viewAliasName);
		 // List containing the Alias name of the Table name of the first condition element.
		List aliasList = new ArrayList();
		aliasList.add(viewAliasName);

		// Puts the single quotes for attributes of type string and date and
		// returns the Set of objects to which the attributes belong.
		List fromTablesList = new ArrayList();
		simpleQueryBizLogic.handleStringAndDateConditions(simpleConditionNodeCollection,
				fromTablesList);

		// Get the configured result view columns else is null.
		String[] selectedColumns = simpleQueryInterfaceForm.getSelectedColumnNames();
		if (selectedColumns != null)
		{
			session.setAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST, selectedColumns);
		}

		// Set the result view for the query.
		List columnNames = new ArrayList();
		Vector selectDataElements = null;

		/**
		 * Bug-2778: Results should return at least the attribute that was queried.
		 * If selectedColumns is null, then we are showing all the default view attribute
		 * plus the attibute that was queried. And if selectedColumns is not null, then we
		 * are showing only those attribute that are selected in Define View.
		 */
		if (selectedColumns != null)
		{
			selectDataElements = simpleQueryBizLogic.getSelectDataElements(selectedColumns,
					aliasList, columnNames, true, null);
		}
		else
		{
			selectDataElements = simpleQueryBizLogic.getSelectDataElements(selectedColumns,
					aliasList, columnNames, true, fieldList);
		}
		query.setResultView(selectDataElements);
		/**
		 *  To fix bug 3449, scientist seeing PHI data
		 */
		Set fromTables = new HashSet();

		Set tableSet = query.getTableSet();
		Iterator itr = tableSet.iterator();
		while (itr.hasNext())
		{
			Table table = (Table) itr.next();
			fromTables.add(table.getTableName());
		}

		//fromTables = new HashSet();
		fromTables.addAll(fromTablesList);
		fromTables.addAll(aliasList);

		// Set the from tables in the query.
		query.setTableSet(fromTables);

		// Checks and gets the activity status conditions for all the objects in
		// the from clause
		// and adds it in the simple conditions node collection.
		simpleQueryBizLogic.addActivityStatusConditions(simpleConditionNodeCollection, fromTables);

		simpleQueryBizLogic.createOrderByListInQuery(fromTables, query);

		// Sets the condition objects from user in the query object.
		((SimpleQuery) query).addConditions(simpleConditionNodeCollection);

		/*QueryBizLogic queryBizLogic = (QueryBizLogic)AbstractBizLogicFactory.getBizLogic(
		    	ApplicationProperties.getValue("app.bizLogicFactory"),
				"getBizLogic", Constants.QUERY_INTERFACE_ID);*/

		IQueryBizLogic queryBizLogic;
		try
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			queryBizLogic = (IQueryBizLogic) factory
					.getBizLogic(edu.wustl.common.util.global.Constants.QUERY_INTERFACE_ID);
		}
		catch (BizLogicException exception)
		{
			logger.error("Failed to get QueryBizLogic object from BizLogic Factory");
			throw new ApplicationException(ErrorKey.getErrorKey("errors.item"), exception,
					"Failed to get QueryBizLogic in base Add/Edit.");
		}
		int identifierIndex = 0;

		/**
		 * Name: Prafull
		 * Description: Query performance issue.
		 * Instead of saving complete query results in session,
		 * resultd will be fetched for each result page navigation.
		 * object of class QuerySessionData will be saved in session,
		 * which will contain the required information for query execution
		 * while navigating through query result pages.
		 * Added PagenatedResultData object, to transfer the results from the query/jdbcDao execute method.
		 */

		int recordsPerPage;
		String recordsPerPageSessionValue = (String) session
				.getAttribute(edu.wustl.common.util.global.Constants.RESULTS_PER_PAGE);
		if (recordsPerPageSessionValue == null)
		{
			recordsPerPage = Integer
					.parseInt(XMLPropertyHandler
							.getValue(edu.wustl.common.util.global.Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
			session.setAttribute(edu.wustl.common.util.global.Constants.RESULTS_PER_PAGE,
					recordsPerPage + "");
		}
		else
		{
			recordsPerPage = new Integer(recordsPerPageSessionValue).intValue();
		}

		PagenatedResultData pagenatedResultData = null;
		boolean isSecureExecute = getSessionData(request).isSecurityRequired();
		boolean hasConditionOnIdentifiedField;
		/**
		 * Constants.SWITCHSECURIRY is removed from if statement
		 */

		simpleQueryBizLogic
				.createQueryResultObjectData(fromTables, queryResultObjectDataMap, query);

		List identifierColumnNames = new ArrayList();
		identifierColumnNames = simpleQueryBizLogic.addObjectIdentifierColumnsToQuery(
				queryResultObjectDataMap, query);
		simpleQueryBizLogic.setDependentIdentifiedColumnIds(queryResultObjectDataMap, query);

		//Aarti: adding other columns to the result view
		//			for (int i = 0; i < columnNames.size(); i++) {
		//				identifierColumnNames.add((String) columnNames.get(i));
		//			}

		for (int i = 0; i < identifierColumnNames.size(); i++)
		{
			columnNames.add((String) identifierColumnNames.get(i));
		}
		if (isSecureExecute)
		{
			queryBizLogic.insertQuery(query.getString(), getSessionData(request));
			hasConditionOnIdentifiedField = query.hasConditionOnIdentifiedField();
			/**
			 * Name: Prafull
			 * Description: Query performance issue.
			 * Instead of saving complete query results in session,
			 * resultd will be fetched for each result page navigation.
			 * object of class QuerySessionData will be saved session,
			 * which will contain the required information for query execution
			 * while navigating through query result pages.
			 *
			 *  PagenatedResultData object will contain the query results
			 */
			pagenatedResultData = query.execute(getSessionData(request), isSecureExecute,
					queryResultObjectDataMap, hasConditionOnIdentifiedField, 0, recordsPerPage);
		}
		else
		{
			isSecureExecute = false;
			hasConditionOnIdentifiedField = false;
			pagenatedResultData = query.execute(getSessionData(request), false, null, false, 0,
					recordsPerPage);
		}
		if (simpleQueryInterfaceForm.getPageOf().equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
		{
			/**
			 * Added by Vijay. Check is added to decide hyperlink should be displayed or not,
			 * based on the variable isSecurityRequired of session dataBean
			 */
			//if(!isSecureExecute)
			{
				/**
				 * Name : Prafull_kadam
				 * Reviewer: Aarti_Sharma
				 * Patch ID: SimpleSearchEdit_1
				 * Description: User should be able to Edit
				 * the Objects searched from Simple search.
				 * For this Selected Colunms in the Query Results are shown as Heypelink,
				 * on clicking it user can edit that object.
				 */
				// Creating & setting Hyperlink column map in request,
				//which contains the information required for the Columns to be hyperlinked.
				Map<Integer, QueryResultObjectData> hyperlinkColumnMap = simpleQueryBizLogic
						.getHyperlinkMap(queryResultObjectDataMap, query.getResultView());
				/**
				 * Name : Prafull_kadam
				 * Patch ID: 4270_1
				 * Description: edit mode through simple search fails
				 * if records per page dropdown is changed.
				 * Setting hyperlinkColumnMap in session instead of request,
				 * so that it will persiste when the records per page drop
				 * down changed or page number changed.
				 * In jsp same is retrived from session.
				 */
				session.setAttribute(Constants.HYPERLINK_COLUMN_MAP, hyperlinkColumnMap);
			}
		}
		else
		{
			// Get the index of Identifier field of main object.
			Vector tableAliasNames = new Vector();
			tableAliasNames.add(viewAliasName);
			Map tableMap = query.getIdentifierColumnIds(tableAliasNames);
			if (tableMap != null)
			{
				identifierIndex = Integer.parseInt(tableMap.get(viewAliasName).toString()) - 1;
				request
						.setAttribute(Constants.IDENTIFIER_FIELD_INDEX,
								new Integer(identifierIndex));
			}
			queryBizLogic.insertQuery(query.getString(), getSessionData(request));
			/**
			 * Name: Prafull
			 * Description: Query performance issue.
			 * Instead of saving complete query results in session,
			 * resultd will be fetched for each result page navigation.
			 * object of class QuerySessionData will be saved session,
			 * which will contain the required information for query
			 * execution while navigating through query result pages.
			 *
			 *  PagenatedResultData object will contain the query results
			 */

		}

		// List of results the query will return on execution.
		List list = pagenatedResultData.getResult();
		// If the result contains no data, show error message.
		if (list.isEmpty())
		{
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.noRecordsFound"));
			saveErrors(request, errors);

			String alias = (String) session.getAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME);
			if (alias == null)
			{
				alias = simpleQueryInterfaceForm.getAliasName();
			}
			simpleQueryInterfaceForm.setValues(map);
			//remove the original session attributes for the query if the list is empty
			session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT, null);
			session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER, null);

			String path = Constants.SIMPLE_QUERY_INTERFACE_ACTION + "?" + Constants.PAGEOF + "="
					+ simpleQueryInterfaceForm.getPageOf() + "&";
			if (alias != null)
			{
				path = path + Constants.TABLE_ALIAS_NAME + "=" + alias;
			}

			return getActionForward(Constants.SIMPLE_QUERY_NO_RESULTS, path);
		}
		else
		{
			// If the result contains only one row and the page is of edit
			// then show the result in the edit page.
			if ((list.size() == 1)
					&& (Constants.PAGEOF_SIMPLE_QUERY_INTERFACE.equals(simpleQueryInterfaceForm
							.getPageOf()) == false))
			{
				List rowList = (List) list.get(0);

				String path = Constants.SEARCH_OBJECT_ACTION + "?" + Constants.PAGEOF + "="
						+ simpleQueryInterfaceForm.getPageOf() + "&" + Constants.OPERATION + "="
						+ Constants.SEARCH + "&"
						+ edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER + "="
						+ rowList.get(identifierIndex);

				if (simpleQueryInterfaceForm.getPageOf().equals("pageOfCollectionProtocol"))
				{
					path = "/RetrieveCollectionProtocol.do?"
							+ edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER + "="
							+ rowList.get(identifierIndex);
				}
				return getActionForward(Constants.SIMPLE_QUERY_SINGLE_RESULT, path);
			}
			else
			{
				/**
				 * Name: Prafull
				 * Description: Query performance issue.
				 * Instead of saving complete query results in session,
				 * resultd will be fetched for each result page navigation.
				 * object of class QuerySessionData will be saved session,
				 * which will contain the required information for query execution
				 * while navigating through query result pages.
				 *
				 *  saving required query data in Session so that can be used later on
				 *  while navigating through result pages using pagination.
				 */
				QuerySessionData querySessionData = new QuerySessionData();
				querySessionData.setSql(query.getString());
				querySessionData.setQueryResultObjectDataMap(queryResultObjectDataMap);
				querySessionData.setSecureExecute(isSecureExecute);
				querySessionData.setHasConditionOnIdentifiedField(hasConditionOnIdentifiedField);
				querySessionData.setRecordsPerPage(recordsPerPage);
				querySessionData.setTotalNumberOfRecords(pagenatedResultData.getTotalRecords());

				session.setAttribute(edu.wustl.common.util.global.Constants.QUERY_SESSION_DATA,
						querySessionData);

				// If results contain more than one result, show the spreadsheet
				// view.
				request.setAttribute(Constants.PAGEOF, simpleQueryInterfaceForm.getPageOf());
				request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
				request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
			}
		}
		session.setAttribute(Constants.IS_SIMPLE_SEARCH, Boolean.TRUE.toString());
		return mapping.findForward(target);
	}

	/**
	 * gets ActionForward.
	 * @param name name
	 * @param path path
	 * @return ActionForward
	 */
	private ActionForward getActionForward(String name, String path)
	{
		ActionForward actionForward = new ActionForward();
		actionForward.setName(name);
		actionForward.setPath(path);

		return actionForward;
	}
}