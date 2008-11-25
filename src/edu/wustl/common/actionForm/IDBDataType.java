/**
 *
 */
package edu.wustl.common.actionForm;

import org.apache.struts.action.ActionErrors;


/**
 * @author prashant_bandal
 *
 */
public interface IDBDataType
{
	/**
	 * This method validate various data types.
	 * @param enteredValue entered Value
	 * @param errors errors
	 * @return conditionError.
	 */
	boolean validate(String enteredValue, ActionErrors errors);
}
