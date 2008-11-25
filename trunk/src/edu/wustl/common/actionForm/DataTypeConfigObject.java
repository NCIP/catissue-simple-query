/**
 *
 */

package edu.wustl.common.actionForm;

/**
 * @author prashant_bandal
 *
 */
public class DataTypeConfigObject
{

	/**
	 * Specifies dataType Name.
	 */
	private String dataTypeName;

	/**
	 * Specifies dataType Class Name.
	 */
	private String dataTypeClassName;

	/**
	 * @param dataTypeName the dataTypeName to set
	 */
	public void setDataTypeName(String dataTypeName)
	{
		this.dataTypeName = dataTypeName;
	}

	/**
	 * @return the dataTypeName
	 */
	public String getDataTypeName()
	{
		return dataTypeName;
	}

	/**
	 * @param dataTypeClassName the dataTypeClassName to set
	 */
	public void setDataTypeClassName(String dataTypeClassName)
	{
		this.dataTypeClassName = dataTypeClassName;
	}

	/**
	 * @return the dataTypeClassName
	 */
	public String getClassName()
	{
		return dataTypeClassName;
	}

}
