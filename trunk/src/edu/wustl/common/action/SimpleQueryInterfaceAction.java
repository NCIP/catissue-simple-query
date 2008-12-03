/**
* <p>Title: SimpleQueryInterfaceAction Class>
 * <p>Description:	SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.common.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.dbmanager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;

/**
 * SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.
 * @author gautam_shetty
 */
public class SimpleQueryInterfaceAction extends SecureAction
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SimpleQueryInterfaceAction.class);

	/**
	 * get the query related data from request and set it in session.
	 * Overrides the execute method of Action class.
	 * set the query related data in session.
	 * @param mapping	ActionMapping
	 * @param form	ActionForm
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 * @return page to be processed next.
	 * @exception Exception Generic exception
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.info("in executeSecureAction method.");
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
		QueryBizLogic queryBizLogic = new QueryBizLogic();
		HttpSession session = request.getSession();
		String operation = (String) request.getParameter(Constants.OPERATION);
		if (Constants.REDEFINE.equals(operation))
		{
			Map simpleQueryMap = (Map) session.getAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT);
			String originalCounter = (String) session
					.getAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER);
			simpleQueryInterfaceForm.setValues(simpleQueryMap);
			simpleQueryInterfaceForm.setCounter(originalCounter);
		}
		session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT, null);
		session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER, null);
		setAttributeNameList(request, simpleQueryInterfaceForm, queryBizLogic);
		request.setAttribute(Constants.ATTRIBUTE_CONDITION_LIST,
				Constants.ATTRIBUTE_CONDITION_ARRAY);
		session.setAttribute(Constants.SIMPLE_QUERY_MAP, null);
		session.setAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST, null);
		String pageOf = request.getParameter(Constants.PAGEOF);
		request.setAttribute(Constants.PAGEOF, pageOf);
		return mapping.findForward(pageOf);
	}

	/**
	 * set the fields name required for processing the query is in request.
	 * @param request HttpServletRequest
	 * @param simpleQueryInterfaceForm SimpleQueryInterfaceForm
	 * @param queryBizLogic QueryBizLogic
	 * @throws DAOException database exception
	 * @throws ClassNotFoundException Generic exception
	 */
	private void setAttributeNameList(HttpServletRequest request,
			SimpleQueryInterfaceForm simpleQueryInterfaceForm, QueryBizLogic queryBizLogic)
			throws DAOException, ClassNotFoundException
	{
		int counter = Integer.parseInt(simpleQueryInterfaceForm.getCounter());
		Validator validator = new Validator();
		NameValueBean nameValueBean;
		List<NameValueBean> objectList;
		for (int i = Constants.ONE; i <= counter; i++)
		{
			// Key of present object.
			String key = "SimpleConditionsNode:" + i + "_Condition_DataElement_table";
			String value = (String) simpleQueryInterfaceForm.getValue(key);
			if (validator.isValidOption(value))
			{
				List<NameValueBean> beanList = queryBizLogic.getColumnNames(value);
				if (!beanList.isEmpty())
				{
					request.setAttribute("attributeNameList" + i, beanList);
				}
			}

			// For the last condition row.
			if (i == counter)
			{
				setLastAttributeNameList(request, simpleQueryInterfaceForm, queryBizLogic, i);
			}
			else
			// For rows other than last, show only the object selected.
			{
				//Key of the next operator (AND/OR).
				String nextOperatorKey = "SimpleConditionsNode:" + i + "_Operator_operator";
				String nextOperatorValue = (String) simpleQueryInterfaceForm
						.getValue(nextOperatorKey);

				if (nextOperatorValue != null && !"".equals(nextOperatorValue))
				{
					String beanList = "objectList" + i;
					String prevValueDisplayName = queryBizLogic.getDisplayName(value);
					nameValueBean = new NameValueBean();
					nameValueBean.setName(prevValueDisplayName);
					nameValueBean.setValue(value);
					objectList = new ArrayList<NameValueBean>();
					objectList.add(nameValueBean);
					request.setAttribute(beanList, objectList);
				}
			}
		}
	}

	/**
	 * set the fields name required for processing the query, is in request for last counter.
	 * @param request HttpServletRequest
	 * @param simpleQueryInterfaceForm SimpleQueryInterfaceForm
	 * @param queryBizLogic QueryBizLogic
	 * @param index int
	 * @throws DAOException database exception
	 * @throws ClassNotFoundException Generic exception
	 */
	private void setLastAttributeNameList(HttpServletRequest request,
			SimpleQueryInterfaceForm simpleQueryInterfaceForm, QueryBizLogic queryBizLogic,
			int index) throws DAOException, ClassNotFoundException
	{
		//Key of previous object.
		String prevKey = "SimpleConditionsNode:" + (index - Constants.ONE)
				+ "_Condition_DataElement_table";
		String prevValue = (String) simpleQueryInterfaceForm.getValue(prevKey);
		String calKey = "SimpleConditionsNode:" + index + "_showCalendar";
		simpleQueryInterfaceForm.setShowCalendar(calKey, "");
		if (prevValue == null)
		{
			String aliasName = request.getParameter(Constants.TABLE_ALIAS_NAME);
			// Get all the table names.
			Set<NameValueBean> tableNameList = queryBizLogic.getAllTableNames(aliasName,
					Constants.SIMPLE_QUERY_TABLES);
			if (!tableNameList.isEmpty())
			{
				request.setAttribute(Constants.OBJECT_NAME_LIST, tableNameList);
				request.setAttribute(Constants.ATTRIBUTE_NAME_LIST,Constants.ATTRIBUTE_NAME_ARRAY);
			}

			if ((aliasName != null) && (!"".equals(aliasName)))
			{
				request.setAttribute(Constants.TABLE_ALIAS_NAME, aliasName);
				List<NameValueBean> columnNameList = queryBizLogic.getColumnNames(aliasName);
				if (!columnNameList.isEmpty())
				{
					String attributeNameList = "attributeNameList1";
					request.setAttribute(attributeNameList, columnNameList);
				}
			}

		}
		else
		//If there is only one condition row.
		{
			Set<NameValueBean> nextTableNameList = queryBizLogic.getNextTableNames(prevValue);
			if (!nextTableNameList.isEmpty())
			{
				String objectNameList = "objectList" + index;
				request.setAttribute(objectNameList, nextTableNameList);
				request.setAttribute(Constants.ATTRIBUTE_NAME_LIST,Constants.ATTRIBUTE_NAME_ARRAY);
			}
		}
	}

	/**
	 * Returns the object id of the protection element that represents
	 * the Action that is being requested for invocation.
	 * @param request HttpServletRequest
	 * @return String
	 */
	protected String getObjectIdForSecureMethodAccess(HttpServletRequest request)
	{
		String objectId = "";
		String aliasName = request.getParameter("aliasName");
		if (aliasName != null && !aliasName.equals(""))
		{
			objectId = this.getClass().getName() + "_" + aliasName;
		}
		else
		{
			objectId = super.getObjectIdForSecureMethodAccess(request);
		}
		return objectId;
	}

	protected String getObjectId(AbstractActionForm form)
	{
	
		return null;
	}

	
	protected boolean isAuthorizedToExecute(HttpServletRequest request)
	{
		return true;
	}
}