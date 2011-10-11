/**
 * <p>Title: SimpleQueryInterfaceForm Class>
 * <p>Description:  SimpleQueryInterfaceForm Class is used to encapsulate all the request parameters passed
 * from simple query interface webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.simplequery.actionForm;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.datatypes.DataTypeConfigFactory;
import edu.wustl.common.datatypes.IDBDataType;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ParseException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.simplequery.global.Constants;
import edu.wustl.simplequery.query.Operator;

/**
 * SimpleQueryInterfaceForm Class is used to encapsulate all the request parameters passed.
 * from simple query interface webpage.
 * @author gautam_shetty
 */
public class SimpleQueryInterfaceForm extends ActionForm
{

	/**
	 * Serial Version Unique Identifier.
	 */
	private static final long serialVersionUID = 9041513259017953749L;

	/**
	 * Specifies whether the id is mutable or not.
	 */
	private boolean mutable = true;

	/**
	 * Specifies INDEX FOR DATA TYPE.
	 */
	private static final int INDEX_FOR_DATA_TYPE = 3;
	/**
	 * Specifies Tree Map.
	 */
	private Map<Object, Object> values = new TreeMap<Object, Object>();

	/**
	 * Specifies counter.
	 */
	private String counter;

	/**
	 * Specifies the page associated with this form bean.
	 */
	private String pageOf;

	/**
	 * Specifies alias Name.
	 */
	private String aliasName;

	/**
	 * Specifies andOr Operation.
	 */
	private boolean andOrOperation = false;

	//Variables neccessary for Configuration of SImple Search.

	/**
	 * Specifies table Name neccessary for Configuration of Simple Search.
	 */
	private String tableName;

	/**
	 * Specifies selected Column Names neccessary for Configuration of Simple Search.
	 */
	private String[] selectedColumnNames;

	private String []columnNames;
	
	/**
	 * @return Returns the mutable.
	 */
	public boolean isMutable()
	{
		return mutable;
	}

	/**
	 * @param mutable The mutable to set.
	 */
	public void setMutable(boolean mutable)
	{
		this.mutable = mutable;
	}

	/**
	 * Default constructor.
	 */
	public SimpleQueryInterfaceForm()
	{
		super();
		this.counter = "1";
	}

	/**
	 * @return Returns the counter.
	 */
	public String getCounter()
	{
		return counter;
	}

	/**
	 * @param counter The counter to set.
	 */
	public void setCounter(String counter)
	{
		if (isMutable())
		{
			this.counter = counter;
		}
	}

	/**
	 * @return Returns the pageOf.
	 */
	public String getPageOf()
	{
		return pageOf;
	}

	/**
	 * @param pageOf The pageOf to set.
	 */
	public void setPageOf(String pageOf)
	{
		this.pageOf = pageOf;
	}

	/**
	 * Put key and value pair in Map.
	 * @param key key
	 * @param value value
	 */
	public void setValue(String key, Object value)
	{
		if (isMutable())
		{
			values.put(key, value);
		}
	}

	/**
	 * Return key from Map.
	 * @param key key.
	 * @return key.
	 */
	public Object getValue(String key)
	{
		return values.get(key);
	}

	/**
	 * Gets all values from Map.
	 * @return all values from Map.
	 */
	public Collection<Object> getAllValues()
	{
		return values.values();
	}

	/**
	 * @return Returns the aliasName.
	 */
	public String getAliasName()
	{
		return aliasName;
	}

	/**
	 * @param aliasName The aliasName to set.
	 */
	public void setAliasName(String aliasName)
	{
		this.aliasName = aliasName;
	}

	/**
	 * Gets Values Map.
	 * @return values Map.
	 */
	public Map<Object, Object> getValuesMap()
	{
		return values;
	}

	/**
	 * @return Returns the andOrOperation.
	 */
	public boolean isAndOrOperation()
	{
		return andOrOperation;
	}

	/**
	 * @param andOrOperation The andOrOperation to set.
	 */
	public void setAndOrOperation(boolean andOrOperation)
	{
		if (isMutable())
		{
			this.andOrOperation = andOrOperation;
		}
	}

	/**
	 * Override validate method fo ActionForm class.
	 * @param mapping ActionMapping object.
	 * @param request HttpServletRequest object.
	 * @return ActionErrors.
	 * @throws ParseException 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		//if the operation is AND / OR.
		try
		{

			if (isAndOrOperation())
			{
				validateAndOrOperationTrue(errors);
			}
			else
			{
				boolean tableError = false, attrError = false, conditionError = false;
				String errorKeyForTable = "simpleQuery.object.required";
				String errorKeyForField = "simpleQuery.attribute.required";
				for (int i = 1; i <= Integer.parseInt(counter); i++)
				{
					String condDataElement = i + "_Condition_DataElement_table";
					tableError = isError(errors, tableError, errorKeyForTable, condDataElement);

					condDataElement = i + "_Condition_DataElement_field";
					attrError = isError(errors, attrError, errorKeyForField, condDataElement);

					if (!conditionError)
					{
						conditionError = validateOperatorValue(errors, i);

					}
				}
			}
			setAndOrOperation(false);
			setMutable(false);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
		}

		return errors;
	}

	/**
	 * @param errors ActionErrors object to update if error present.
	 * @param error error true or false.
	 * @param actionErrorKey actionError Key
	 * @param conditionDataElement
	 * @return errorValue true or false.
	 * @param condDataElement condition Data Element.
	 */
	private boolean isError(ActionErrors errors, boolean error, String actionErrorKey,
			String condDataElement)
	{
		boolean errorValue = error;
		if (!error)
		{
			Validator validator = new Validator();
			StringBuffer key = new StringBuffer();
			key.append("SimpleConditionsNode:").append(condDataElement);
			String enteredValue = (String) getValue(key.toString());
			if (!validator.isValidOption(enteredValue))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(actionErrorKey));
				errorValue = true;
			}
		}
		return errorValue;
	}

	/**
	 * @param errors errors.
	 */
	public void validateAndOrOperationTrue(ActionErrors errors)
	{
		Validator validator = new Validator();
		String key = "SimpleConditionsNode:" + (Integer.parseInt(counter) - 1)
				+ "_Condition_DataElement_table";
		String selectedTable = (String) getValue(key);

		//if the table is not selected, show an error message.
		if (!(validator.isValidOption(selectedTable)))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.object.required"));

			this.counter = String.valueOf(Integer.parseInt(this.counter) - 1);

			//remove the key for the join condition of last condition object from the map.
			key = "SimpleConditionsNode:" + this.counter + "_Operator_operator";
			values.remove(key);
		}
	}

	/**
	 * @param errors errors.
	 * @param integerValue integer Value.
	 * @return conditionError.
	 * @throws ParseException 
	 * @throws ApplicationException 
	 */
	private boolean validateOperatorValue(ActionErrors errors, int integerValue)
			throws ParseException, ApplicationException
	{
		boolean conditionError = false;
		String key = "SimpleConditionsNode:#_Condition_Operator_operator";
		String newKey = replaceAll(key, "#", Integer.toString(integerValue));
		StringBuffer operatorKey = new StringBuffer(newKey);
		String operatorValue = (String) getValue(operatorKey.toString());
		if (!Validator.isEmpty(operatorValue)
				&& !(operatorValue.equals(Operator.IS_NULL) || operatorValue
						.equals(Operator.IS_NOT_NULL)))
		{
			conditionError = validateCondition(errors, integerValue);
		}
		return conditionError;
	}

	/**
	 * This method updates ActionErrors object if errors present.
	 * @param errors ActionErrors object.
	 * @param integerValue integer Value for key generation.
	 * @return conditionError
	 * @throws ParseException Parse Exception.
	 * @throws ApplicationException Application Exception.
	 */
	private boolean validateCondition(ActionErrors errors, int integerValue) throws ParseException,
			ApplicationException
	{
		boolean conditionError;
		String keyString = "SimpleConditionsNode:#_Condition_value";
		String newKey = replaceAll(keyString, "#", Integer.toString(integerValue));
		String enteredValue = (String) getValue(newKey);
		String dataElement = "SimpleConditionsNode:#_Condition_DataElement_field";
		String dataElementKey = replaceAll(dataElement, "#", Integer.toString(integerValue));
		String selectedField = (String) getValue(dataElementKey);
		StringTokenizer strTok = new StringTokenizer(selectedField, ".");
		int tokenCnt = 1;
		String dataType = "";
		while (strTok.hasMoreTokens())
		{
			if (tokenCnt == INDEX_FOR_DATA_TYPE)
			{
				dataType = strTok.nextToken();
			}
			else
			{
				strTok.nextToken();
			}
			tokenCnt++;
		}
		conditionError = validateDataType(dataType, enteredValue, errors);
		return conditionError;
	}

	/**
	 * This method replace string.
	 * @param source source String
	 * @param toReplace toReplace String
	 * @param replacement replacement String
	 * @return String.
	 */
	public static String replaceAll(String source, String toReplace, String replacement)
	{
		String sourceString = source;
		if (sourceString.contains(toReplace))
		{
			sourceString = sourceString.replace(toReplace, replacement);
		}
		return sourceString;
	}

	/**
	 * validate Data Type.
	 * @param dataType data Type to validate.
	 * @param enteredValue entered Value.
	 * @param errors ActionErrors.
	 * @return returns true if valid data type else false.
	 * @throws ParseException 
	 * @throws ApplicationException 
	 */
	private boolean validateDataType(String dataType, String enteredValue, ActionErrors errors)
			throws ParseException, ApplicationException
	{
		IDBDataType dbDataType = DataTypeConfigFactory.getInstance().getDataType(dataType);
		return dbDataType.validate(enteredValue, errors);
	}

	/**
	 * @return Returns the selectedColumnNames.
	 */
	public String[] getSelectedColumnNames()
	{
		return selectedColumnNames;
	}

	/**
	 * @param selectedColNames The selectedColumnNames to set.
	 */
	public void setSelectedColumnNames(String[] selectedColNames)
	{
		this.selectedColumnNames = selectedColNames;
	}


	/**
	 * @return Returns the tableName.
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * @param tableName The tableName to set.
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * @return Returns the values.
	 */
	public Map<Object, Object> getValues()
	{
		return values;
	}

	/**
	 * @param values The values to set.
	 */
	public void setValues(Map<Object, Object> values)
	{
		this.values = values;
	}

	/**
	 * variable to store the menu selected.
	 */
	private String menuSelected = "";

	/**
	 * @return Returns the menuSelected.
	 */
	public String getMenuSelected()
	{
		return menuSelected;
	}

	/**
	 * @param menuSelected The menuSelected to set.
	 */
	public void setMenuSelected(String menuSelected)
	{
		this.menuSelected = menuSelected;
	}

	/**
	* Returns the id assigned to form bean.
	* @return the id assigned to form bean
	*/
	public int getFormId()
	{
		return Constants.SIMPLE_QUERY_INTERFACE_ID;
	}

	// Mandar: -- Map added to maintain values to display the Calendar icon
	/**
	 * Map to hold values for rows to display calendar icon.
	 */
	private Map<Object, Object> showCalendarValues = new HashMap<Object, Object>();

	/**
	 * @return Returns the showCalendarValues.
	 */
	public Map<Object, Object> getShowCalendarValues()
	{
		return showCalendarValues;
	}

	/**
	 * @param showCalValues The showCalendarValues to set.
	 */
	public void setShowCalendarValues(Map<Object, Object> showCalValues)
	{
		this.showCalendarValues = showCalValues;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setShowCalendar(String key, Object value)
	{
		showCalendarValues.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key
	 *            the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getShowCalendar(String key)
	{
		return showCalendarValues.get(key);
	}

	/**
	 * @return Returns the columnNames.
	 */
	public String[] getColumnNames() {
		return columnNames;
	}
	/**
	 * @param columnNames The columnNames to set.
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
}