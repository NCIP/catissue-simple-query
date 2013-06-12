/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-simple-query/LICENSE.txt for details.
 */

/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.simplequery.query;

import java.io.Serializable;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author gautam_shetty
 * @hibernate.class table="CATISSUE_INTERFACE_COLUMN_DATA"
 */
public class QueryColumnData extends AbstractDomainObject implements Serializable
{

	/**
	 * serialVersionUID serial version UID for serialization.
	 */
	private static final long serialVersionUID = -4180953341688220631L;
	/**
	 * identifier to identify the query.
	 */
	private long identifier;
	/**
	 * table data object for query.
	 */
	private QueryTableData tableData = new QueryTableData();
	/**
	 * Name of the column.
	 */
	private String columnName;
	/**
	 * Name of the display.
	 */
	private String displayName;
	/**
	 * Type of the column.
	 */
	private String columnType;

	/**
	 * get identifier.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_INTF_COLUMN_DATA_SEQ"
	 * @return Returns the identifier.
	 */
	public long getIdentifier()
	{
		return identifier;
	}

	/**
	 * set the identifier.
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(long identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * get the object for table data.
	 * @hibernate.many-to-one column="TABLE_ID" class="edu.wustl.simplequery.query.QueryTableData"
	 * constrained="true"
	 * @return Returns the tableData.
	 */
	public QueryTableData getTableData()
	{
		return tableData;
	}

	/**
	 * @return Returns the columnType.
	 */
	public String getColumnType()
	{
		return columnType;
	}

	/**
	 * @param columnType The columnType to set.
	 */
	public void setColumnType(String columnType)
	{
		this.columnType = columnType;
	}

	/**
	 * @param tableData The tableData to set.
	 */
	public void setTableData(QueryTableData tableData)
	{
		this.tableData = tableData;
	}

	/**
	 * @hibernate.property name="columnName" type="string" column="COLUMN_NAME" length="50"
	 * @return Returns the name of the column.
	 */
	public String getColumnName()
	{
		return columnName;
	}

	/**
	 * This method set column Name.
	 * @param columnName The column Name to set.
	 */
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	/**
	 * @hibernate.property name="displayName" type="string" column="DISPLAY_NAME" length="50"
	 * @return Returns the displayName.
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	/**
	 * set the values from the ui form and set it into object.
	 * @param abstractForm from where the data has to be retrieved.
	 * @throws AssignDataException Assign Data Exception.
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		// TODO Auto-generated method stub
	}
	/**
	 * @return Returns the Id.
	 */
	public Long getId()
	{
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * set the identifier.
	 * @param identifier identifier to be set.
	 */
	public void setId(Long identifier)
	{
	}
}