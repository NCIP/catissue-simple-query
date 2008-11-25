/**
 *
 */

package edu.wustl.common.actionForm;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.util.global.Constants;

/**
 * @author prashant_bandal
 *
 */
public class TinyIntDataType implements IDBDataType
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.ValidatorDataTypeInterface#
	 * validate(java.lang.String, java.lang.String, org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate TinyInt values.
	 * @param enteredValue entered Value.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{

		boolean conditionError = false;
		if (!Constants.BOOLEAN_YES.equals(enteredValue.trim())
				&& !Constants.BOOLEAN_NO.equals(enteredValue.trim()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.tinyint.format"));
			conditionError = true;
		}
		return conditionError;
	}

}
