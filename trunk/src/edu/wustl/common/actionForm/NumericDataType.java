/**
 *
 */

package edu.wustl.common.actionForm;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author prashant_bandal
 *
 */
public class NumericDataType implements IDBDataType
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(NumericDataType.class);

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.ValidatorDataTypeInterface#
	 * validate(java.lang.String, java.lang.String, org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate numeric values.
	 * @param enteredValue entered Value.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		Validator validator = new Validator();
		boolean conditionError = false;
		logger.debug(" Check for integer");
		if (validator.convertToLong(enteredValue) == null)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.intvalue.required"));
			conditionError = true;
			logger.debug(enteredValue + " is not a valid integer");
		}
		else if (!validator.isPositiveNumeric(enteredValue, 0))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"simpleQuery.intvalue.poisitive.required"));
			conditionError = true;
			logger.debug(enteredValue + " is not a positive integer");
		}
		return conditionError;
	}

}
